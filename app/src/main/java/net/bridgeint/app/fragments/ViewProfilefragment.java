package net.bridgeint.app.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kcode.bottomlib.BottomDialog;
import com.squareup.picasso.Picasso;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.activities.DashboardUpdateActivity;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.UpdateResponse;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.RegisterResponse;
import net.bridgeint.app.responces.UploadFileResponce;
import net.bridgeint.app.utils.Config;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.ImageUtils;
import net.bridgeint.app.utils.ProgressRequestBody;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static net.bridgeint.app.utils.Constants.EVENT_EDIT_PROFILE_SCREEN;
import static net.bridgeint.app.utils.Constants.EVENT_VIEW_PROFILE_SCREEN;

public class ViewProfilefragment extends BaseFragment implements ResponceCallback, ProgressRequestBody.UploadCallbacks {

    @BindView(R.id.backBtn)
    ImageView backBtn;
    @BindView(R.id.appbar)
    LinearLayout appbar;
    @BindView(R.id.user_pic)
    CircleImageView userPic;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.loStudentName)
    LinearLayout loStudentName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.cbNotification)
    CheckBox cbNotification;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.ll_profile)
    LinearLayout llProfile;
    private SessionManager sessionManager;
    //    private ProgressDialog dialog;
    HashMap<String, String> paramMap = new HashMap<String, String>();
    String notificationStatus;
    private Uri cameraImageUri;
    private AlertDialog alertDialog;
    private ProgressDialog dialog;
    File file = null;
    boolean isCameraSelect;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile_update, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Picasso.with(getActivity()).load(Constants.IMAGE_URL + SharedClass.userModel.getImage()).into(userPic);
        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_VIEW_PROFILE_SCREEN.replace(" ", "_").toLowerCase());
        /*cbNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notificationStatus = Constants.ON;
                } else {
                    notificationStatus = Constants.OFF;
                }
                callNotificationUpdate();
            }
        });*/
        setData();
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            backBtn.setRotation(180);
        }
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSourceChooser();
            }
        });
    }

    private void ImageSourceChooser() {
        BottomDialog dialog = BottomDialog.newInstance("Pick Image Source", "Cancel", new String[]{"Take Photo From Camera", "Choose Image From Gallery"});
        dialog.show(getActivity().getSupportFragmentManager(), "dialog");
        dialog.setListener(new BottomDialog.OnClickListener() {
            @Override
            public void click(int position) {
                switch (position) {
                    case 0:
                        isCameraSelect = true;
                        PickImage();
                        break;
                    case 1:
                        isCameraSelect = false;
                        PickImage();
                        break;
                }
            }
        });
    }

    private void PickImage() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (isCameraSelect) {
                    OpenCamera();
                } else {
                    OpenGallery();
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void OpenCamera() {
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File cameraImageOutputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Utility.createCameraImageFileName());
        cameraImageUri = Uri.fromFile(cameraImageOutputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(Intent.createChooser(intent, "Image Source"), Constants.REQUEST_IMAGE_CAPTURE);
*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(getActivity().getFilesDir(), "profile.jpg");
        cameraImageUri = FileProvider.getUriForFile(getActivity(),
                getActivity().getPackageName() + ".provider",
                file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(Intent.createChooser(intent, "Image Source"), Constants.REQUEST_IMAGE_CAPTURE);


    }

    private void OpenGallery() {
//        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(pickPhoto, Constants.REQUEST_IMAGE_GALLERY);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        //intent.putExtra(Intent.EXTRA_MIME_TYPES, StringA("image/jpg", "image/png", "image/jpeg"));
        startActivityForResult(intent, Constants.REQUEST_IMAGE_GALLERY);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                switch (requestCode) {
                    case Constants.REQUEST_IMAGE_GALLERY:
                        if (resultCode == RESULT_OK) {
                            try {
                                Uri uri = imageReturnedIntent.getData();
                                Picasso.with(getActivity()).load(uri).into(userPic);
                                final InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
                              //  file = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(), uri)));
                                file = new File(ImageUtils.compressImage(file.getAbsolutePath()));
                                showUploadingDialog();
                                ServerCall.uploadImage(getActivity(), file, new ResponceCallback() {
                                    @Override
                                    public void onSuccessResponce(Object obj) {
                                        final UploadFileResponce response = ((Response<UploadFileResponce>) obj).body();
                                        try {
                                            alertDialog.dismiss();
                                            Utility.showToast(getActivity(), getString(R.string.image_upload_success));
                                            if (!response.getError()) {
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put(Constants.USER_ID, SharedClass.userModel.getId().toString());
                                                map.put(Constants.ACCESS_KEY, SharedClass.userModel.getAccessKey());
                                                map.put(Constants.API_KEY, Config.API_KEY);
                                                map.put(Constants.IMAGE, response.getName());
                                                ServerCall.updateProfile(getActivity(), dialog, "Updating Profile...", map, new ResponceCallback() {
                                                    @Override
                                                    public void onSuccessResponce(Object obj) {
                                                        UpdateResponse registerResponse = ((Response<UpdateResponse>) obj).body();
                                                        try {
                                                            Utility.showToast(getActivity(), registerResponse.getMessage());
                                                            Utility.dismissProgressDialog(dialog);
                                                            if (!registerResponse.getError()) {
                                                                SharedClass.userModel.setImage(response.getName());
//                                                                finish();
                                                            } else {
                                                                if (registerResponse.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                                                                    SharedClass.logout(getActivity());
                                                                } else {
                                                                    Utility.showToast(getActivity(), registerResponse.getMessage());
                                                                }
                                                            }
                                                        } catch (Exception ex) {
                                                            Utility.dismissProgressDialog(dialog);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailureResponce(Object obj) {
                                                        Utility.dismissProgressDialog(dialog);
                                                        try {
                                                            Response<RegisterResponse> response = (Response<RegisterResponse>) obj;
                                                            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                                                                SharedClass.logout(getActivity());
                                                            } else {
                                                                ErrorUtils.responseError(getActivity(), response);
                                                            }
                                                        } catch (Exception exp) {
                                                        }
                                                    }
                                                });
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
                                        alertDialog.dismiss();
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
                            } catch (Exception exp) {
                                exp.printStackTrace();

                            }
                        }

                        break;
                    case Constants.REQUEST_IMAGE_CAPTURE:
                        if (resultCode == RESULT_OK) {
                            try {
                                String path = file.getAbsolutePath();
                                Uri uri = cameraImageUri;
                                 Picasso.with(getActivity()).load(new File(path)).into(userPic);

//                                Picasso.with(getActivity())
//                                        .load(file)
//                                        .resize(Utility.getDeviceWidthInPercentage(getActivity(), 20), 0).into(userPic);
                                file = new File(ImageUtils.compressImage(file.getAbsolutePath()));
                                showUploadingDialog();
                                ServerCall.uploadImage(getActivity(), file, new ResponceCallback() {
                                    @Override
                                    public void onSuccessResponce(Object obj) {
                                        final UploadFileResponce response = ((Response<UploadFileResponce>) obj).body();
                                        try {
                                            alertDialog.dismiss();
                                            Utility.showToast(getActivity(), "Image uploaded successfully");
                                            if (!response.getError()) {
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put(Constants.USER_ID, SharedClass.userModel.getId().toString());
                                                map.put(Constants.ACCESS_KEY, SharedClass.userModel.getAccessKey());
                                                map.put(Constants.API_KEY, Config.API_KEY);
                                                map.put(Constants.IMAGE, response.getName());
                                                ServerCall.updateProfile(getActivity(), dialog, "Updating Profile...", map, new ResponceCallback() {
                                                    @Override
                                                    public void onSuccessResponce(Object obj) {
                                                        UpdateResponse registerResponse = ((Response<UpdateResponse>) obj).body();
                                                        try {
                                                            Utility.showToast(getActivity(), registerResponse.getMessage());
                                                            Utility.dismissProgressDialog(dialog);
                                                            if (!registerResponse.getError()) {
                                                                SharedClass.userModel.setImage(response.getName());
//                                                                finish();
                                                            } else {
                                                                if (registerResponse.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                                                                    SharedClass.logout(getActivity());
                                                                } else {
                                                                    Utility.showToast(getActivity(), registerResponse.getMessage());
                                                                }
                                                            }
                                                        } catch (Exception ex) {
                                                            Utility.dismissProgressDialog(dialog);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailureResponce(Object obj) {
                                                        Utility.dismissProgressDialog(dialog);
                                                        try {
                                                            Response<RegisterResponse> response = (Response<RegisterResponse>) obj;
                                                            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                                                                SharedClass.logout(getActivity());
                                                            } else {
                                                                ErrorUtils.responseError(getActivity(), response);
                                                            }
                                                        } catch (Exception exp) {
                                                        }
                                                    }
                                                });
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
                                        alertDialog.dismiss();
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
                            } catch (Exception exp) {
                                exp.printStackTrace();
                                Log.e("TAG", "" + exp.getMessage());
                            }
                        }
                        break;
                }
            }
        }, 1000);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        float progress = percentage / 100.0f;
        Log.d("Uploading", progress + "");
    }

    @Override
    public void onError() {
    }

    @Override
    public void onFinish() {
    }

    private void init() {
        dialog = new ProgressDialog(getActivity());
        isCameraSelect = false;

    }


    private void showUploadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.CustomDialog);
        final FrameLayout frameView = new FrameLayout(getActivity());
        builder.setView(frameView);
        alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.uploading_dialog, frameView);
        alertDialog.setView(dialoglayout);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void callNotificationUpdate() {

        paramMap.clear();
        paramMap.put(Constants.USER_ID, SharedClass.userModel.getId().toString());
        paramMap.put(Constants.ACCESS_KEY, SharedClass.userModel.getAccessKey());
        paramMap.put(Constants.API_KEY, Config.API_KEY);
        paramMap.put(Constants.NOTIFICATION, notificationStatus);
        ServerCall.changeNotificationStatus(getActivity(), dialog, "Updating", paramMap, new ResponceCallback() {
            @Override
            public void onSuccessResponce(Object obj) {
                UpdateResponse updateResponse = ((Response<UpdateResponse>) obj).body();
                if (!updateResponse.getError()) {
                    if (updateResponse.getUser() != null) {
                        SharedClass.userModel = updateResponse.getUser();
                    }
                }

            }

            @Override
            public void onFailureResponce(Object obj) {

            }
        });

    }


    public void setData() {
        sessionManager = new SessionManager(getActivity());
        etFirstName.setText(SharedClass.userModel.getFirstName());
        etLastName.setText(SharedClass.userModel.getLastName());
        etEmail.setText(SharedClass.userModel.getEmail());
        etPhone.setText(SharedClass.userModel.getPhone());
        dialog = new ProgressDialog(getActivity());
        if (SharedClass.userModel.getNotifStatus() != null) {
            notificationStatus = SharedClass.userModel.getNotifStatus().toString();
            if (notificationStatus.equals(Constants.ON)) {
                cbNotification.setChecked(true);
            } else {
                cbNotification.setChecked(false);
            }
        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
//                finish(); popup fragment


            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etFirstName.getText().toString().trim().equals(SharedClass.userModel.getFirstName())) {
                    paramMap.put(Constants.FIRST_NAME, etFirstName.getText().toString().trim());
                }
                if (!etLastName.getText().toString().trim().equals(SharedClass.userModel.getLastName())) {
                    paramMap.put(Constants.LAST_NAME, etLastName.getText().toString().trim());
                }
                if (!etEmail.getText().toString().trim().equals(SharedClass.userModel.getEmail())) {
                    paramMap.put(Constants.EMAIL, etEmail.getText().toString().trim());
                }
                if (!etPhone.getText().toString().trim().equals(SharedClass.userModel.getPhone())) {
                    paramMap.put(Constants.PHONE, etPhone.getText().toString().trim());
                }
                if (!notificationStatus.equals(SharedClass.userModel.getNotifStatus().toString())) {
                    paramMap.put(Constants.NOTIFICATION, notificationStatus);
                }
                paramMap.put(Constants.USER_ID, SharedClass.userModel.getId().toString());
                paramMap.put(Constants.ACCESS_KEY, SharedClass.userModel.getAccessKey());
                paramMap.put(Constants.API_KEY, Config.API_KEY);
                paramMap.put(Constants.NOTIFICATION, notificationStatus);
                ServerCall.updateProfile(getActivity(), dialog, "Updating Profile", paramMap, new ResponceCallback() {
                    @Override
                    public void onSuccessResponce(Object obj) {
                        UpdateResponse registerResponse = ((Response<UpdateResponse>) obj).body();
                        try {
                            Utility.dismissProgressDialog(dialog);
                            if (!registerResponse.getError()) {
                                SharedClass.userModel = registerResponse.getUser();
                                Utility.setUserLogin(getActivity(), etEmail.getText().toString().trim(), Utility.getUserPassword(getActivity()));
//                                getActivity().finish();
                                Utility.showToast(getActivity(), registerResponse.getMessage());
                                startActivity(new Intent(getActivity(), DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            } else {
                                if (registerResponse.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                                    SharedClass.logout(getActivity());
                                } else {
                                    Utility.showToast(getActivity(), registerResponse.getMessage());
                                }
                            }
                        } catch (Exception ex) {
                            Utility.dismissProgressDialog(dialog);
                        }
                    }

                    @Override
                    public void onFailureResponce(Object obj) {
                        try {
                            Utility.dismissProgressDialog(dialog);
                            Response<RegisterResponse> response = (Response<RegisterResponse>) obj;
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
        });
        BaseActivity.logEvent(EVENT_EDIT_PROFILE_SCREEN, BaseActivity.getUserDetails());
        BaseActivity.logFirebaseEvent(EVENT_EDIT_PROFILE_SCREEN.replace(" ", "_").toLowerCase());
    }

    @Override
    public void onSuccessResponce(Object obj) {

    }

    @Override
    public void onFailureResponce(Object obj) {

    }

}