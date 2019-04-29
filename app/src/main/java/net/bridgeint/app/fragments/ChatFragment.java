package net.bridgeint.app.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Dash;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kcode.bottomlib.BottomDialog;

import net.bridgeint.app.Animation.ViewProxy;
import net.bridgeint.app.R;
import net.bridgeint.app.activities.DashboardUpdateActivity;
import net.bridgeint.app.adapters.ChatAdapter;
import net.bridgeint.app.application.Application;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.interfaces.SocketMessageListerner;
import net.bridgeint.app.models.ChatModel.Message;
import net.bridgeint.app.models.ChatModel.messageModel;
import net.bridgeint.app.models.MessageModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.ChatResponce;
import net.bridgeint.app.responces.UploadFileResponce;
import net.bridgeint.app.sockets.SocketManager;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.ImageUtils;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener, ResponceCallback, SocketMessageListerner {

    public RecyclerView rvChat;
    ArrayList<MessageModel> chatlist = new ArrayList<>();
    ChatAdapter adapter = null;

    private ImageView cam_btn, send_btn;
    private EditText textbox;
    private int _xDeltaButton;
    private int _xDeltaText;
    private int textStartPosition;
    private int audioStartXPosition;
    private RelativeLayout audioLayout;
    private ImageView audioBtn;
    private TextView timer, swipeText;
    private float distCanMove;
    Handler handler;
    Runnable runnable;
    int min, sec;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    boolean hasPermission = false;
    private boolean deleted = false;
    private SocketManager mSocketManager;
    private int pageNumber;
    private Uri cameraImageUri;
    private View view;
    private Animation topBottom, bottomTop;
    LinearLayoutManager layoutManager;
    public HashMap<String, String> dates = new HashMap<>();
    public HashMap<String, String> MessageIds = new HashMap<>();
    String recentDate;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        pageNumber = 0;
        topBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.top_to_bottom);
        bottomTop = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_to_top);
        ((DashboardUpdateActivity) getActivity()).updateAdminStatus(Constants.ADMIN_OFFLINE);
        FetchPreviousChat();
        InitSocketManager();
        MeasureMicMargin();

        Application.setShowChat(true);

        rvChat = view.findViewById(R.id.rvChat);

        textbox = view.findViewById(R.id.textbox);

        cam_btn = view.findViewById(R.id.cam_btn);
        send_btn = view.findViewById(R.id.send_btn);
        send_btn.setOnClickListener(this);
        cam_btn.setOnClickListener(this);

        initAdapter();
        AccessPermissions();

        audioBtn = view.findViewById(R.id.audioBtn);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) audioBtn.getLayoutParams();
        audioStartXPosition = layoutParams.leftMargin;
        audioLayout = view.findViewById(R.id.audioLayout);
        timer = view.findViewById(R.id.timer);
        swipeText = view.findViewById(R.id.swipeText);
        RelativeLayout.LayoutParams textlayoutParams = (RelativeLayout.LayoutParams) swipeText.getLayoutParams();
        textStartPosition = textlayoutParams.leftMargin;
        Log.d("Start X Position", audioStartXPosition + "");
        RecordingViewAnimation();

        runnable = new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                sec++;
                if (sec > 9) {
                    sec = 0;
                    min++;
                }
                timer.setText(String.format("%02d", sec) + ":" + String.format("%02d", min));
                handler.postDelayed(this, 1000);
            }
        };
        textbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSocketManager.startTyping(SharedClass.userModel.getAccessKey(), Constants.TO_USER_ID, SharedClass.userModel.getId().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        rvChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pastVisibleItems = layoutManager.findLastCompletelyVisibleItemPosition();
                if (pastVisibleItems == chatlist.size() - 1) {
                    pageNumber++;
                    FetchPreviousChat();
                }
            }
        });

        return view;
    }

    private void FetchPreviousChat() {
        if (pageNumber == 0) {
            view.findViewById(R.id.loadingview).setAnimation(topBottom);
        }
        ServerCall.FetchPreviousChat(getActivity(), pageNumber + "", this);
    }

    private void InitSocketManager() {
        mSocketManager = new SocketManager(getContext(), ChatFragment.this);
        mSocketManager.connectSocketManager();
    }

    private void RecordingViewAnimation() {
        audioBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int X_button = (int) motionEvent.getRawX();
                final int X_text = ((int) motionEvent.getRawX()) + 50;
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (hasPermission) {
                        audioLayout.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams audioBtnParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) swipeText.getLayoutParams();
                        _xDeltaButton = X_button - audioBtnParams.leftMargin;
                        _xDeltaText = X_text - textParams.leftMargin;
                        handler.postDelayed(runnable, 1000);
                        StartRecording();
                        audioBtn.getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        AccessPermissions();
                    }
                } else if (hasPermission && (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL)) {
                    layoutParams.leftMargin = audioStartXPosition;
                    audioBtn.setLayoutParams(layoutParams);
                    Log.d("END X Position", layoutParams.leftMargin + "");
                    RelativeLayout.LayoutParams textlayoutParams = (RelativeLayout.LayoutParams) swipeText.getLayoutParams();
                    textlayoutParams.leftMargin = textStartPosition;
                    handler.removeCallbacks(runnable);
                    swipeText.setLayoutParams(textlayoutParams);
                    timer.setText("00:00");
                    audioLayout.setVisibility(View.GONE);
                    ViewProxy.setAlpha(swipeText, 1.0f);
                    if (sec >= 3 && min >= 0) {
                        Stop_and_SaveRecroding();
                    } else {
                        DeleteRecording();
                    }
                    sec = 0;
                    min = 0;
                } else if (hasPermission && layoutParams.leftMargin >= 0 && motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d("Button X Position", layoutParams.leftMargin + "");
                    layoutParams.leftMargin = X_button - _xDeltaButton;
                    audioBtn.setLayoutParams(layoutParams);
                    RelativeLayout.LayoutParams textlayoutParams = (RelativeLayout.LayoutParams) swipeText.getLayoutParams();
                    textlayoutParams.leftMargin = X_text - _xDeltaText;
                    swipeText.setLayoutParams(textlayoutParams);
                    float alpha = 1.0f - (layoutParams.leftMargin / distCanMove);
                    if (alpha > 1) {
                        alpha = 1;
                    } else if (alpha < 0) {
                        alpha = 0;
                    }
                    ViewProxy.setAlpha(swipeText, alpha);
                    if (layoutParams.leftMargin >= distCanMove) {
                        layoutParams.leftMargin = audioStartXPosition;
                        audioBtn.setLayoutParams(layoutParams);
                        Log.d("END X Position", layoutParams.leftMargin + "");
                        audioLayout.setVisibility(View.GONE);
                        DeleteRecording();
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:

                if (Utility.isNetConnected(getActivity())) {
                    if (textbox.getText().toString().trim().length() > 0) {
                        try {
                            sendMessage(textbox.getText().toString().trim());
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                    }
                } else {
                    Utility.showNetworkDailogSingle(getActivity(), Constants.NET_CONNECTION_LOST, new ResponceCallback() {
                        @Override
                        public void onSuccessResponce(Object obj) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }

                        @Override
                        public void onFailureResponce(Object obj) {

                        }
                    });
                }
                break;
            case R.id.cam_btn:
                ImageSourceChooser();
                break;
        }
    }

    private void ImageSourceChooser() {
        BottomDialog dialog = BottomDialog.newInstance("Pick Image Source", "Cancle", new String[]{"Take Photo From Camera", "Choose Image From Gallery"});
        dialog.show(getChildFragmentManager(), "dialog");
        dialog.setListener(new BottomDialog.OnClickListener() {
            @Override
            public void click(int position) {
                switch (position) {
                    case 0:
                        OpenCamera();
                        break;
                    case 1:
                        OpenImagePicker();
                        break;
                }
            }
        });
    }

    private void sendMessage(String message) {
        final String msg = message;
        final String tempId = System.currentTimeMillis() + "";
        textbox.setText("");
        //Utility.hideKeyPad(getActivity());
        Date dt = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = dateFormat.format(dt);
        if (!dates.containsKey(time)) {
            dates.put(time, "found");
            time = Utility.getFormattedDate(getContext(), time);
            chatlist.add(0, new MessageModel(-1, msg, time, Constants.MESSAGE_STATE_PENDING, Constants.TEXT_TYPE, null, tempId + "", true, 0));
        }
        chatlist.add(0, new MessageModel(0, msg, "", Constants.MESSAGE_STATE_PENDING, Constants.TEXT_TYPE, null, tempId, true, 0));
        adapter.notifyDataSetChanged();
        SenndMessageToServer(msg, tempId, Constants.TEXT_TYPE + "");
    }

    private void SenndMessageToServer(final String msg, final String tempId, final String type) {
        final Handler msgHandler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                mSocketManager.SendMessage(SharedClass.userModel.getAccessKey(), msg, Constants.TO_USER_ID, SharedClass.userModel.getId().toString(), tempId, type, ChatFragment.this);
            }
        };
        msgHandler.postDelayed(r, 1000);
    }

    @Override
    public void onGetMessageAck(final Message message, int type) {
        if (type == Constants.MESSAGE_UPDATE) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.updateMessage(message);
                }
            });
        } else if (type == Constants.RECIEVE_NEW_MESSAG) {
            sendAckOnMessageRecieve(message.getId() + "");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String time = message.getCreated();
                    if (time.contains("-")) {
                        String[] split = time.split(" ");
                        time = split[0];
                    } else {
                        time = message.getCreated();
                    }
                    if (!dates.containsKey(time)) {
                        dates.put(time, "found");
                        time = Utility.getFormattedDate(getContext(), time);
                        chatlist.add(new MessageModel(-1, message.getMessage(), time, message.getStatus(), Constants.TEXT_TYPE, null, message.getTempId() + "", false, message.getIsRead()));
                    }
                    if (message.getType().equals(Constants.AUDIO_TYPE + "")) {
                        chatlist.add(0, new MessageModel(0, Constants.IMAGE_URL + message.getMessage(), message.getCreated(), Constants.MESSAGE_STATE_PENDING, Constants.AUDIO_TYPE, null, message.getTempId(), false, message.getIsRead()));
                    } else if (message.getType().equals(Constants.IMAGE_TYPE + "")) {
                        chatlist.add(0, new MessageModel(0, Constants.IMAGE_URL + message.getMessage(), message.getCreated(), Constants.MESSAGE_STATE_PENDING, Constants.IMAGE_TYPE, null, message.getTempId(), false, message.getIsRead()));
                    } else {
                        chatlist.add(0, new MessageModel(0, message.getMessage(), message.getCreated(), Constants.MESSAGE_STATE_PENDING, Constants.TEXT_TYPE, null, message.getTempId(), false, message.getIsRead()));
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (type == Constants.ADMIN_TYPING) {
            if (getActivity() != null && !getActivity().isFinishing() && getActivity() instanceof DashboardUpdateActivity) {
                ((DashboardUpdateActivity) getActivity()).updateTitleInChat(" is typing...");
            }
        } else if (type == Constants.ADMIN_ONLINE) {
            if (getActivity() != null && !getActivity().isFinishing() && getActivity() instanceof DashboardUpdateActivity) {
                ((DashboardUpdateActivity) getActivity()).updateAdminStatus(Constants.ADMIN_ONLINE);
            }
        } else if (type == Constants.ADMIN_OFFLINE) {
            if (getActivity() != null && !getActivity().isFinishing() && getActivity() instanceof DashboardUpdateActivity) {
                ((DashboardUpdateActivity) getActivity()).updateAdminStatus(Constants.ADMIN_OFFLINE);
            }
        }
    }

    private void AccessPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            }
        } else {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    hasPermission = true;
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            TedPermission.with(getActivity())
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();
        }
    }

    private void initAdapter() {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        rvChat.setItemAnimator(new DefaultItemAnimator());
        rvChat.setLayoutManager(layoutManager);
        rvChat.setHasFixedSize(true);
        rvChat.setItemViewCacheSize(20);
        rvChat.setDrawingCacheEnabled(true);
        rvChat.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //adapter = new ChatAdapter(ChatFragment.this,getContext(), chatlist);
        rvChat.setAdapter(adapter);
    }

    private void MeasureMicMargin() {
        min = 0;
        sec = 0;
        handler = new Handler();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        distCanMove = (width / 2) - 60;
    }

    private void OpenCamera() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                SharedClass.openImagePicker = true;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File cameraImageOutputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), createCameraImageFileName());
                cameraImageUri = Uri.fromFile(cameraImageOutputFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                startActivityForResult(Intent.createChooser(intent, "Image Source"), Constants.REQUEST_IMAGE_CAPTURE);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

    }

    private String createCameraImageFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return timeStamp + ".jpg";
    }

    public void OpenImagePicker() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                SharedClass.openImagePicker = true;
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, Constants.REQUEST_IMAGE_GALLERY);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri uri = data.getData();
                String tempId = System.currentTimeMillis() + "";
                File file = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(), uri)));
                chatlist.add(0, new MessageModel(13, textbox.getText().toString().trim(), "", Constants.MESSAGE_STATE_PENDING, Constants.IMAGE_TYPE, file.getAbsolutePath(), tempId, true, 0));
                adapter.notifyItemChanged(chatlist.size());
                UploadAudio_Image(file, tempId, Constants.IMAGE_TYPE + "");
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        } else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            String tempId = System.currentTimeMillis() + "";
            chatlist.add(0, new MessageModel(13, textbox.getText().toString().trim(), "", Constants.MESSAGE_STATE_PENDING, Constants.IMAGE_TYPE, cameraImageUri.getPath(), tempId, true, 0));
            adapter.notifyItemChanged(chatlist.size());
            Uri uri = cameraImageUri;
            File file = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(), uri)));
            UploadAudio_Image(file, tempId, Constants.IMAGE_TYPE + "");
        }
    }

    private void StartRecording() {
        try {
            AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + "_AudioRecording.3gp";
            MediaRecorderReady();
            mediaRecorder.prepare();
            mediaRecorder.start();
            deleted = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void DeleteRecording() {
        try {
            if (!deleted) {
                deleted = true;
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                }
                File file = new File(AudioSavePathInDevice);
                if (file.exists()) {
                    file.delete();
                    Toast.makeText(getActivity(), "Recording Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ext) {
            // TODO Auto-generated catch block
            ext.printStackTrace();
        }
    }

    private void Stop_and_SaveRecroding() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.release();
                sendAudio();
            }
        } catch (Exception ext) {
            // TODO Auto-generated catch block
            ext.printStackTrace();
        }
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private void sendAudio() {
        String tempId = System.currentTimeMillis() + "";
        chatlist.add(0, new MessageModel(13, null, "", Constants.MESSAGE_STATE_PENDING, Constants.AUDIO_TYPE, AudioSavePathInDevice, tempId, true, 0));
        adapter.notifyItemChanged(chatlist.size());
        File file = new File(AudioSavePathInDevice);
        UploadAudio_Image(file, tempId, Constants.AUDIO_TYPE + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Application.setShowChat(false);
        ((DashboardUpdateActivity) getActivity()).updateAdminStatus(Constants.ADMIN_OFFLINE);
        mSocketManager.socketDisconnect();
    }

    @Override
    public void onSuccessResponce(Object obj) {
        ChatResponce chatResponce = ((Response<ChatResponce>) obj).body();
        try {
            if (pageNumber == 0) {
                hideChatLoading();
            }
            if (!chatResponce.getError()) {
                ArrayList<messageModel> messageArray = chatResponce.getmessageModel();
                messageModel model = null;
                for (int i = 0; i < messageArray.size(); i++) {
                    model = messageArray.get(i);
                    String time;
                    if (model.getMessage().getCreated().contains("-")) {
                        String[] split = model.getMessage().getCreated().split(" ");
                        time = split[0];
                    } else {
                        time = model.getMessage().getCreated();
                    }
                    recentDate = time;
                    boolean isIamSender;
                    isIamSender = model.getMessage().getFromUser() == SharedClass.userModel.getId();
                    if (!MessageIds.containsKey(model.getMessage().getId() + "")) {
                        MessageIds.put(model.getMessage().getId() + "", "exist");
                        if (model.getMessage().getType().equals(Constants.AUDIO_TYPE + "")) {
                            chatlist.add(new MessageModel(model.getMessage().getId(), Constants.IMAGE_URL + model.getMessage().getMessage(), model.getMessage().getCreated(), model.getMessage().getStatus(), Constants.AUDIO_TYPE, null, model.getMessage().getTempId() + "", isIamSender, model.getMessage().getIsRead()));
                        } else if (model.getMessage().getType().equals(Constants.IMAGE_TYPE + "")) {
                            chatlist.add(new MessageModel(model.getMessage().getId(), Constants.IMAGE_URL + model.getMessage().getMessage(), model.getMessage().getCreated(), model.getMessage().getStatus(), Constants.IMAGE_TYPE, null, model.getMessage().getTempId() + "", isIamSender, model.getMessage().getIsRead()));
                        } else {
                            chatlist.add(new MessageModel(model.getMessage().getId(), model.getMessage().getMessage(), model.getMessage().getCreated(), model.getMessage().getStatus(), Constants.TEXT_TYPE, null, model.getMessage().getTempId() + "", isIamSender, model.getMessage().getIsRead()));
                        }
                    }
                }
                if (!dates.containsKey(recentDate)) {
                    dates.put(recentDate, "found");
                    recentDate = Utility.getFormattedDate(getContext(), recentDate);
                    chatlist.add(new MessageModel(-1, model.getMessage().getMessage(), recentDate, model.getMessage().getStatus(), Constants.TEXT_TYPE, null, model.getMessage().getTempId() + "", false, model.getMessage().getIsRead()));
                }
                adapter.notifyDataSetChanged();
            } else {
                if (chatResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(getActivity());
                } else {
                    Utility.showToast(getActivity(), chatResponce.getMessage());
                }
            }
        } catch (Exception ex) {
            //Utility.showToast(getActivity(), chatResponce.getMessage());
        }
    }

    @Override
    public void onFailureResponce(Object obj) {
        try {
            if (pageNumber == 0) {
                hideChatLoading();
            }
            Response<ChatResponce> response = (Response<ChatResponce>) obj;
            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                SharedClass.logout(getActivity());
            } else {
                ErrorUtils.responseError(getActivity(), response);
            }
        } catch (Exception exp) {
        }
    }

    private void hideChatLoading() {
        view.findViewById(R.id.loadingview).setAnimation(bottomTop);
        bottomTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.findViewById(R.id.loadingview).setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void sendAckOnMessageRecieve(String id) {
        mSocketManager.sendAckOnRecievingMessage(SharedClass.userModel.getAccessKey(), Constants.TO_USER_ID, SharedClass.userModel.getId().toString(), id);
    }

    private void UploadAudio_Image(File file, final String tempId, final String type) {
        ServerCall.uploadImage(getActivity(), file, new ResponceCallback() {
            @Override
            public void onSuccessResponce(Object obj) {
                final UploadFileResponce response = ((Response<UploadFileResponce>) obj).body();
                try {
                    if (!response.getError()) {
                        SenndMessageToServer(response.getName(), tempId, type);
                    } else {
                        if (response.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                            SharedClass.logout(getActivity());
                        } else {
                            Utility.showToast(getActivity(), response.getMessage());
                        }
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void onFailureResponce(Object obj) {
                try {
                    Response<UploadFileResponce> response = (Response<UploadFileResponce>) obj;
                    if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(getActivity());
                    } else {
                        ErrorUtils.responseError(getActivity(), response);
                    }
                } catch (Exception exp) {
                }
            }
        });
    }

}
