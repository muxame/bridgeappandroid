package net.bridgeint.app.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import net.bridgeint.app.R;
import net.bridgeint.app.adapters.LiveMessagesAdapter;
import net.bridgeint.app.fragments.BaseFragment;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.utils.AnimationUtility;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Response;

public class LiveActivity extends BaseFragment implements Session.SessionListener,
        Session.SignalListener,
        PublisherKit.PublisherListener,
        SubscriberKit.SubscriberListener, ResponceCallback {


    private static String API_KEY = "";
    private static String SESSION_ID = "";
    private static String TOKEN = "";
    private Context context;
    private VideoView vvSession;

    private static final String LOG_TAG = LiveActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session mSession;
    private FrameLayout mSubscriberViewContainer;
    private Publisher mPublisher;

    private Subscriber mSubscriber;
    private TextView noStream;

    private LinearLayout camera_change, publisher_view, ic_logout;
    private ImageView camera, go_live, show_opts, ic_back, ic_mic;
    int i = 0;
    private int mic = 0;
    private ProgressDialog dialog;
    private Handler handler;

    private RecyclerView msg_recycler;
    private TextView tv_send;
    private EditText et_msg;
    private RelativeLayout msg_layout;

    private LiveMessagesAdapter adapter;
    private List<String> msgList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_live, container, false);
        initialize();
//        logEvent(EVENT_LIVE_SCREEN, getUserDetails());
        ((BaseActivity) getActivity()).logFirebaseEvent(Constants.EVENT_LIVE_SCREEN.replace(" ", "_").toLowerCase());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        context = getActivity();

        dialog = new ProgressDialog(getActivity());

        handler = new Handler();

        mSubscriberViewContainer = view.findViewById(R.id.subscriber_container);

        noStream = view.findViewById(R.id.no_stream);
        camera_change = view.findViewById(R.id.camera_change);
        camera = view.findViewById(R.id.camera);
        go_live = view.findViewById(R.id.go_live);
        publisher_view = view.findViewById(R.id.publisher_view);
        show_opts = view.findViewById(R.id.show_opts);
        ic_back = view.findViewById(R.id.back);
        ic_mic = view.findViewById(R.id.mic);
        ic_logout = view.findViewById(R.id.logout);

        msg_recycler = view.findViewById(R.id.msg_recycler);
        msg_layout = view.findViewById(R.id.msg_layout);
        et_msg = view.findViewById(R.id.et_msg);
        tv_send = view.findViewById(R.id.tv_send);

        adapter = new LiveMessagesAdapter(getActivity(), msgList);
        msg_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        msg_recycler.setAdapter(adapter);

        show_opts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (publisher_view.getVisibility() == View.GONE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            publisher_view.setVisibility(View.VISIBLE);
                            AnimationUtility.slideInRight(publisher_view);
                        }
                    }, 300);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AnimationUtility.fadeOut(publisher_view);
                            publisher_view.setVisibility(View.GONE);

                        }
                    }, 300);

                }
            }
        });

        ic_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSession != null) {
                    if (mic == 0) {
                        mic = 1;
                        mPublisher.setPublishAudio(false);
                        ic_mic.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_new));
                    } else if (mic == 1) {
                        mic = 0;
                        mPublisher.setPublishAudio(true);
                        ic_mic.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_off));
                    } else if (mic == 3) {
                        mic = 4;
                        ic_mic.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume));
                        mSubscriber.setSubscribeToAudio(false);
                    } else {
                        mic = 3;
                        ic_mic.setImageDrawable(getResources().getDrawable(R.drawable.ic_mute));
                        mSubscriber.setSubscribeToAudio(true);
                    }
                }
            }
        });

        ic_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setTitle(getString(R.string.confirmation))
                        .setMessage(getString(R.string.logout_msg))
                        .setPositiveButton(getText(R.string.yes), null)
                        .setNegativeButton(getString(R.string.no), null)
                        .create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button yesButton = (alertDialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                        Button noButton = (alertDialog).getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                logout();
                            }
                        });

                        noButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                alertDialog.show();

            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setTitle(getString(R.string.confirmation))
                        .setMessage(getString(R.string.back_msg))
                        .setPositiveButton(getString(R.string.yes), null)
                        .setNegativeButton(getString(R.string.no), null)
                        .create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button yesButton = (alertDialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                        Button noButton = (alertDialog).getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
//                                finish();
                            }
                        });

                        noButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                alertDialog.show();

            }
        });

        if (!SharedClass.userModel.getType().equalsIgnoreCase("Student")) {
            noStream.setText(getString(R.string.go_live));
            go_live.setVisibility(View.VISIBLE);
        } else {
            noStream.setText(getString(R.string.no_university_live));
            startVideo();
            go_live.setVisibility(View.GONE);
            show_opts.setVisibility(View.GONE);
            ic_mic.setImageDrawable(getResources().getDrawable(R.drawable.speaker_icon));
            mic = 3;
        }

        go_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    i = 1;
                    startVideo();
                } else {
                    i = 0;
                    onBackPressed();
                }
            }
        });

        camera_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    i = 1;
                    camera.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_front));
                    if (mPublisher != null) {
                        mPublisher.swapCamera();
                    }
                } else {
                    i = 0;
                    camera.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_rear));
                    if (mPublisher != null) {
                        mPublisher.swapCamera();
                    }
                }
            }
        });

        if (!SharedClass.userModel.getType().equalsIgnoreCase("Student")) {
            msg_layout.setVisibility(View.GONE);
        }

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_msg.getText().toString().length() > 0) {
                    if (mSession != null) {
                        String msg = null;
                        try {
                            msg = "{\"name\":\"" + SharedClass.userModel.getFirstName() + "\"" + ",\"image\":\"" + SharedClass.userModel.getImage() + "\"" + ",\"message\":\"" + URLEncoder.encode(et_msg.getText().toString(), "UTF-8").replaceAll("\\+", "%20") + "\"" + ",\"device\":\"Android\"" + ",\"id\":\"" + SharedClass.userModel.getId() + "\"}";
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        mSession.sendSignal(Constants.CHAT_TYPE, msg);
                        et_msg.setText("");
//                        msgList.add(msg);
//                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }


    private void startVideo() {
        if (!SharedClass.userModel.getType().equalsIgnoreCase("Student")) {
            noStream.setText(getString(R.string.please_wait));
            requestPermissions();
            publisher_view.setVisibility(View.GONE);
            go_live.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop));
        } else {
            go_live.setVisibility(View.GONE);
            publisher_view.setVisibility(View.GONE);
            initializeSession();
        }
    }


    private void logout() {
        ServerCall.logout(getActivity(), dialog, getString(R.string.logout), new ResponceCallback() {


            @Override
            public void onSuccessResponce(Object obj) {
                GenericResponce genericResponce = ((Response<GenericResponce>) obj).body();
                try {
                    if (!genericResponce.getError()) {
                        SharedClass.ClearAllData();
                        Utility.logout(getActivity());
                        startActivity(new Intent(getActivity(), SigninActivity.class));
//                        LiveActivity.this.finish();
                        //Utility.showToast(LiveActivity.this, genericResponce.getMessage());
                        Utility.hideLoadingDialog();
                    } else {
                        if (genericResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                            SharedClass.logout(getActivity());
                        } else {
                            Utility.showToast(getActivity(), genericResponce.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Utility.hideLoadingDialog();
                }
            }

            @Override
            public void onFailureResponce(Object obj) {
                Utility.hideLoadingDialog();
                try {
                    Response<GenericResponce> response = (Response<GenericResponce>) obj;
                    if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(getActivity());
                    } else {
                        ErrorUtils.responseError(getActivity(), response);
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        initializeSession();

    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            initializeSession();
        } else {
            EasyPermissions.requestPermissions(getActivity(), getString(R.string.permission_need), RC_VIDEO_APP_PERM, perms);
        }
    }

    private void initializeSession() {
        ServerCall.getTokenKeys(getActivity(), this);
    }

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected " + session.getSessionId());
//        startVideo();
        noStream.setVisibility(View.GONE);
        msg_recycler.setVisibility(View.VISIBLE);
        msg_layout.setVisibility(View.VISIBLE);

        if (!SharedClass.userModel.getType().equalsIgnoreCase("Student")) {
            mPublisher = new Publisher.Builder(getActivity()).build();
            mPublisher.setPublisherListener(this);
            if (!SharedClass.userModel.getType().equalsIgnoreCase("Student")) {
                msg_layout.setVisibility(View.GONE);
            } else {
                msg_layout.setVisibility(View.VISIBLE);
            }

            mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                    BaseVideoRenderer.STYLE_VIDEO_FILL);


//            if (mPublisher.getView() instanceof GLSurfaceView) {
//                ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
//            }
            if (mSession == null) {
                initialize();
            } else {
                mSession.publish(mPublisher);
            }

            mSubscriberViewContainer.addView(mPublisher.getView());


        }

    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
        noStream.setVisibility(View.VISIBLE);
        msg_recycler.setVisibility(View.GONE);
        msg_layout.setVisibility(View.GONE);
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
        if (mSubscriber != null) {
            mSubscriber = null;
            if (mSession != null) {
                mSession.disconnect();
                mSession = null;
            }
            mSubscriberViewContainer.removeAllViews();
            noStream.setVisibility(View.VISIBLE);
        }
        noStream.setVisibility(View.VISIBLE);
        msg_recycler.setVisibility(View.GONE);
        msg_layout.setVisibility(View.GONE);

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
        noStream.setVisibility(View.VISIBLE);
        msg_recycler.setVisibility(View.GONE);
        msg_layout.setVisibility(View.GONE);
        /* noStream.setText(opentokError.getMessage());*/

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
        noStream.setVisibility(View.VISIBLE);
        msg_recycler.setVisibility(View.GONE);
        msg_layout.setVisibility(View.GONE);

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
        noStream.setVisibility(View.VISIBLE);
        msg_recycler.setVisibility(View.GONE);
        msg_layout.setVisibility(View.GONE);

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            noStream.setVisibility(View.GONE);
            msg_recycler.setVisibility(View.VISIBLE);
            msg_layout.setVisibility(View.VISIBLE);
            if (SharedClass.userModel.getType().equalsIgnoreCase("Student")) {
                mSubscriber = new Subscriber.Builder(getActivity(), stream).build();
                mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
                mSubscriber.setSubscriberListener(this);
                if (mSession != null) {
                    mSession.subscribe(mSubscriber);
                    msg_layout.setVisibility(View.VISIBLE);
                } else {
                    initialize();
                }

                mic = 3;
                ic_mic.setImageDrawable(getResources().getDrawable(R.drawable.ic_mute));
                mSubscriberViewContainer.addView(mSubscriber.getView());

            }
        } else {
            noStream.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        if (mSession != null) {
            mSession.onResume();
        }
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {
        Log.d(LOG_TAG, "onConnected: Subscriber connected. Stream: " + subscriberKit.getStream().getStreamId());
        noStream.setVisibility(View.GONE);
        msg_recycler.setVisibility(View.VISIBLE);
        msg_layout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
        Log.d(LOG_TAG, "onDisconnected: Subscriber disconnected. Stream: " + subscriberKit.getStream().getStreamId());
        noStream.setVisibility(View.VISIBLE);
        msg_recycler.setVisibility(View.GONE);
        msg_layout.setVisibility(View.GONE);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSession != null) {
            if (mPublisher != null) {
                mPublisher.destroy();
            }
            mSession.disconnect();
            mSession = null;
        }
        if (mSubscriber != null) {
            mSubscriber = null;
        }

//        finish();
    }


    public void onBackPressed() {
//        super.onBackPressed();
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.back_msg))
                .setPositiveButton(getText(R.string.yes), null)
                .setNegativeButton(getText(R.string.no), null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button yesButton = (alertDialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                Button noButton = (alertDialog).getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
//                        finish();
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
        if (mSession != null) {
            if (mPublisher != null) {
                mPublisher.destroy();
            }
            mSession.disconnect();
            mSession = null;
        }
        if (mSubscriber != null) {
            mSubscriber = null;
        }

    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());
        showOpenTokError(opentokError);
        noStream.setVisibility(View.VISIBLE);
        msg_layout.setVisibility(View.VISIBLE);

    }


    private void showOpenTokError(OpentokError opentokError) {

        Toast.makeText(getActivity(), opentokError.getErrorDomain().name() + ": " + opentokError.getMessage() + " Please, see the logcat.", Toast.LENGTH_LONG).show();
//        finish();
    }

    @Override
    public void onSuccessResponce(Object obj) {
//        TokenLiveResponse response = ((Response<TokenLiveResponse>) obj).body();
//        if (!response.getError()) {
//            API_KEY = response.getResullt().getLiveSessionToken().getApiKey();
//            SESSION_ID = response.getResullt().getLiveSessionToken().getSessionId();
//            TOKEN = response.getResullt().getLiveSessionToken().getToken();
//            mSession = new Session.Builder(getActivity(), API_KEY, SESSION_ID).build();
//            mSession.setSessionListener(this);
//            mSession.setSignalListener(this);
//            mSession.connect(TOKEN);
//        }
    }


    @Override
    public void onFailureResponce(Object obj) {

    }

    @Override
    public void onSignalReceived(Session session, String type, String data, Connection connection) {
        if (session != null) {
            if (type.equalsIgnoreCase(Constants.CHAT_TYPE)) {
                msgList.add(data);
                adapter.notifyDataSetChanged();
                msg_recycler.scrollToPosition(msgList.size() - 1);
            }
        }
    }
}
