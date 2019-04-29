package net.bridgeint.app.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.activities.DashboardUpdateActivity;
import net.bridgeint.app.activities.DocumentsActivity;
import net.bridgeint.app.activities.DocumentsFromFreeActivity;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.UploadModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.UploadFileResponce;
import net.bridgeint.app.utils.AnimationUtility;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.ImageUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static net.bridgeint.app.utils.Constants.EVENT_UPLOAD_DOCUMENT_SCREEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends DialogFragment implements ResponceCallback {


    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;

    public UploadFragment() {
        // Required empty public constructor
    }

    private Handler handler;
    private Context ctx;
    private Activity activity;
    View view;

    private AppCompatImageButton scanBtn;
    private AppCompatImageButton addPhotoBtn;
    private ImageView btnBack;
    int selectedIvId;
    public static final int REQ_SCAN = 1003;
    int totalAttachedImage;
    File file1, file2, file3;
    private AlertDialog alertDialog;
    private ArrayList<File> selectedImages = new ArrayList<>();
    private int uploadingImagePosition = 0;
    private String documentType;
    private Uri cameraImageUri;

    private boolean isInEditMode;
    private boolean isFromFreeMode;
    private String type;
    private String applyId;

    // update
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload, container, false);
        unbinder = ButterKnife.bind(this, view);
        documentType = getArguments().getString("SelectionType");
        getIds();
        handler = new Handler();
        ctx = getActivity();

        setUpDialog();
        onClickListeners();
        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_UPLOAD_DOCUMENT_SCREEN.replace(" ", "_").toLowerCase());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        isInEditMode = getArguments().getBoolean("isInEditMode", false);
        isFromFreeMode = getArguments().getBoolean("isFromFreeMode", false);

        if (isInEditMode) {
            type = getArguments().getString("type");
            applyId = getArguments().getString("applyId");
        }


        return view;
    }

    private void setUpDialog() {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.findViewById(R.id.root).setVisibility(View.VISIBLE);
                AnimationUtility.slideInDown(view.findViewById(R.id.root));
            }
        }, 300);
    }

    private void getIds() {
        totalAttachedImage = 0;
        file1 = null;
        file2 = null;
        file3 = null;
        scanBtn = view.findViewById(R.id.scanBtn);
        btnBack = view.findViewById(R.id.back_button);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            btnBack.setRotation(180);
        }

        addPhotoBtn = view.findViewById(R.id.addPhotoBtn);
    }

    private void setInitialData() {

    }

    private void onClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissWithAnimation();
            }
        });
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanDocument();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file1 != null) {


                    ((DashboardUpdateActivity) getActivity()).replaceFragment(new DocumentsActivity(), "DocumentUpload");
                    dismissWithAnimation();
                } else {
                    Utility.showToast(getActivity(), getString(R.string.select_validation_image));
                }

            }
        });

        /*firstIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file1 == null) {
                    selectedIvId = R.id.firstIv;
                    PickImageFromGallary();
                } else {
                    Utility.showToast(getActivity(), "Please Deleted Selected Image");
                }
            }
        });
        secondtIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file2 == null) {
                    selectedIvId = R.id.secondtIv;
                    PickImageFromGallary();
                } else {
                    Utility.showToast(getActivity(), "Please Deleted Selected Image");
                }
            }
        });
        thirdIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file3 == null) {
                    selectedIvId = R.id.thirdIv;
                    PickImageFromGallary();
                } else {
                    Utility.showToast(getActivity(), "Please Deleted Selected Image");
                }
            }
        });

        firstCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file1 = null;
                firstIv.setImageResource(R.drawable.img_add);
                scanBtn.setVisibility(View.VISIBLE);
                seprator.setVisibility(View.VISIBLE);
                firstCross.setVisibility(View.GONE);
                selectedImages.remove(file1);
            }
        });
        secondCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file2 = null;
                secondtIv.setImageResource(R.drawable.img_add);
                scanBtn.setVisibility(View.VISIBLE);
                seprator.setVisibility(View.VISIBLE);
                secondCross.setVisibility(View.GONE);
                selectedImages.remove(file2);
            }
        });
        thirdCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file3 = null;
                thirdIv.setImageResource(R.drawable.img_add);
                scanBtn.setVisibility(View.VISIBLE);
                seprator.setVisibility(View.VISIBLE);
                thirdCross.setVisibility(View.GONE);
                selectedImages.remove(file3);
            }
        });*/

        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String allImages = GenerateCommaSeperatedString();

                //selectedIvId = R.id.firstIv;
                PickImageFromGallary();

//                if(selectedImages.size()>0) {
//                    showUploadingDialog();
//                    UploadgImagesToServer(uploadingImagePosition);
//                }
//                else
//                {
//                    Utility.showToast(getActivity(),"Select Atleast 1 Image");
//                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                dismissWithAnimation();
            }
        };
    }

    private void dismissWithAnimation() {
        AnimationUtility.slideOutUp(view.findViewById(R.id.root));
        handler.postDelayed(() -> {
            try {
                getDialog().dismiss();
                DocumentsActivity documentsActivity = new DocumentsActivity();
                documentsActivity.updateList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 500);
    }

    private void PickImageFromGallary() {
        BaseActivity.logEvent(Constants.EVENT_UPLOAD_DOCUMENT_FROM_GALLARY, BaseActivity.getUserDetails());
        BaseActivity.logFirebaseEvent(Constants.EVENT_UPLOAD_DOCUMENT_FROM_GALLARY.replace(" ", "_").toLowerCase());
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

        if (requestCode == Constants.REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (file1 == null) {

                Picasso.with(getActivity()).load(uri).into(addPhotoBtn);
                file1 = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(), uri)));
//                firstCross.setVisibility(View.VISIBLE);
                selectedImages.add(file1);

                if (selectedImages.size() > 0) {
                    showUploadingDialog();
                    UploadgImagesToServer(uploadingImagePosition);
                } else {
                    Utility.showToast(getActivity(), "Select Atleast 1 Image");
                }
            }
//            else if(file2==null)
//            {
//                Picasso.with(getActivity()).load(uri).into(secondtIv);
//                file2 = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(),uri)));
//                secondCross.setVisibility(View.VISIBLE);
//                selectedImages.add(file2);
//            }
//            else if(file3==null)
//            {
//                Picasso.with(getActivity()).load(uri).into(thirdIv);
//                file3 = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(),uri)));
//                thirdCross.setVisibility(View.VISIBLE);
//                selectedImages.add(file3);
//            }
//            if(file1!=null && file2!=null && file3!=null)
//            {
//                scanBtn.setVisibility(View.INVISIBLE);
//                seprator.setVisibility(View.INVISIBLE);
//            }
        } else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri uri = cameraImageUri;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                if (file1 == null) {
//                        firstIv.setImageBitmap(bitmap);
//                        file1 = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(), resultUri)));
//                        firstCross.setVisibility(View.VISIBLE);
//                        selectedImages.add(file1);

//                        Picasso.with(getActivity()).load(resultUri).into(scanBtn);
                    scanBtn.setImageBitmap(bitmap);
                    file1 = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(), uri)));
//                        firstCross.setVisibility(View.VISIBLE);
                    selectedImages.add(file1);

                    if (selectedImages.size() > 0) {
                        showUploadingDialog();
                        UploadgImagesToServer(uploadingImagePosition);
                    } else {
                        Utility.showToast(getActivity(), "Select Atleast 1 Image");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
//            CropImage.activity(uri).start(getActivity(), UploadFragment.this)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    if (file1 == null) {
//                        firstIv.setImageBitmap(bitmap);
//                        file1 = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(), resultUri)));
//                        firstCross.setVisibility(View.VISIBLE);
//                        selectedImages.add(file1);

//                        Picasso.with(getActivity()).load(resultUri).into(scanBtn);
                        scanBtn.setImageBitmap(bitmap);
                        file1 = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(), resultUri)));
//                        firstCross.setVisibility(View.VISIBLE);
                        selectedImages.add(file1);

                        if (selectedImages.size() > 0) {
                            showUploadingDialog();
                            UploadgImagesToServer(uploadingImagePosition);
                        } else {
                            Utility.showToast(getActivity(), "Select Atleast 1 Image");
                        }
                    }
//                    else if (file2 == null) {
//                        secondtIv.setImageBitmap(bitmap);
//                        file2 = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(), resultUri)));
//                        secondCross.setVisibility(View.VISIBLE);
//                        selectedImages.add(file2);
//                    } else if (file3 == null) {
//                        thirdIv.setImageBitmap(bitmap);
//                        file3 = new File(ImageUtils.compressImage(Utility.getPathFromUri(getActivity(), resultUri)));
//                        thirdCross.setVisibility(View.VISIBLE);
//                        selectedImages.add(file3);
//                    }
//                    if (file1 != null && file2 != null && file3 != null) {
//                        scanBtn.setVisibility(View.INVISIBLE);
//                        seprator.setVisibility(View.INVISIBLE);
//                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                    getActivity().finish();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void ScanDocument() {
        BaseActivity.logEvent(Constants.EVENT_UPLOAD_DOCUMENT_FROM_SCAN, BaseActivity.getUserDetails());
        BaseActivity.logFirebaseEvent(Constants.EVENT_UPLOAD_DOCUMENT_FROM_SCAN.replace(" ", "_").toLowerCase());
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File cameraImageOutputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Utility.createCameraImageFileName());
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

    private void showUploadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        final FrameLayout frameView = new FrameLayout(getActivity());
        builder.setView(frameView);
        alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.uploading_dialog, frameView);
        alertDialog.setView(dialoglayout);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void UploadgImagesToServer(int pos) {
        if (isInEditMode) {
            ServerCall.uploadEditImage(getActivity(), selectedImages.get(pos), type, applyId, UploadFragment.this);
        } else {
            ServerCall.uploadImage(getActivity(), selectedImages.get(pos), UploadFragment.this);

        }
    }

    @Override
    public void onSuccessResponce(Object obj) {
        alertDialog.dismiss();
        final UploadFileResponce response = ((Response<UploadFileResponce>) obj).body();
        try {
            if (!response.getError()) {
                uploadingImagePosition++;
                Log.d("FileUploaded", uploadingImagePosition + "");
                if (documentType.equals(Constants.High_School_Transcript)) {
                    if (ApplyModel.highSchoolTranscript == null) {
                        ApplyModel.highSchoolTranscript = new ArrayList<>();
                    }
                    ApplyModel.highSchoolTranscript.add(response.getName());
                } else if (documentType.equals(Constants.Recommendation_Letters)) {
                    if (ApplyModel.recommendationLetters == null) {
                        ApplyModel.recommendationLetters = new ArrayList<>();
                    }
                    ApplyModel.recommendationLetters.add(response.getName());
                } else if (documentType.equals(Constants.Passport_Copy)) {
                    if (ApplyModel.passportCopies == null) {
                        ApplyModel.passportCopies = new ArrayList<>();
                    }
                    ApplyModel.passportCopies.add(response.getName());
                } else if (documentType.equals(Constants.English_Proficiency_Test)) {
                    if (ApplyModel.englishProfeciencyTest == null) {
                        ApplyModel.englishProfeciencyTest = new ArrayList<>();
                    }
                    ApplyModel.englishProfeciencyTest.add(response.getName());
                } else if (documentType.equals(Constants.Personal_Statement)) {
                    if (ApplyModel.personalStatment == null) {
                        ApplyModel.personalStatment = new ArrayList<>();
                    }
                    ApplyModel.personalStatment.add(response.getName());
                } else if (documentType.equals(Constants.Extra_Documents)) {
                    if (ApplyModel.extraDocuments == null) {
                        ApplyModel.extraDocuments = new ArrayList<>();
                    }
                    ApplyModel.extraDocuments.add(response.getName());
                }


                if (isInEditMode) {
//                    if (getActivity() instanceof DocumentsActivity) {
                    DocumentsActivity documentsActivity = new DocumentsActivity();
                    if (documentsActivity.getList() != null && documentsActivity.getList().size() > 0) {
//                        for (UploadModel uploadModel:documentsActivity.getList()) {
                        if (documentType.equals(Constants.High_School_Transcript)) {
                            documentsActivity.getList().get(0).getImage().add(new UploadModel.ImageBean(response.getName()));
                        } else if (documentType.equals(Constants.Passport_Copy)) {
                            documentsActivity.getList().get(2).getImage().add(new UploadModel.ImageBean(response.getName()));
                        } else if (documentType.equals(Constants.Recommendation_Letters)) {
                            documentsActivity.getList().get(1).getImage().add(new UploadModel.ImageBean(response.getName()));
                        } else if (documentType.equals(Constants.English_Proficiency_Test)) {
                            documentsActivity.getList().get(3).getImage().add(new UploadModel.ImageBean(response.getName()));
                        } else if (documentType.equals(Constants.Personal_Statement)) {
                            documentsActivity.getList().get(4).getImage().add(new UploadModel.ImageBean(response.getName()));
                        } else if (documentType.equals(Constants.Extra_Documents)) {
                            documentsActivity.getList().get(5).getImage().add(new UploadModel.ImageBean(response.getName()));
                        }
//                        }
//                        }

                        documentsActivity.updateNewList();
                      /*  startActivity(new Intent(getActivity(), ApplicationActivity.class));*/
                        activity.finish();
                    }
                }

                if (isFromFreeMode) {
                    if (getActivity() instanceof DocumentsFromFreeActivity) {
                        DocumentsFromFreeActivity documentsActivity = (DocumentsFromFreeActivity) getActivity();
                        if (documentsActivity.getList() != null && documentsActivity.getList().size() > 0) {
//                        for (UploadModel uploadModel:documentsActivity.getList()) {
                            if (documentType.equals(Constants.High_School_Transcript)) {
                                documentsActivity.getList().get(0).getImage().add(new UploadModel.ImageBean(response.getName()));
                            } else if (documentType.equals(Constants.Passport_Copy)) {
                                documentsActivity.getList().get(2).getImage().add(new UploadModel.ImageBean(response.getName()));
                            } else if (documentType.equals(Constants.Recommendation_Letters)) {
                                documentsActivity.getList().get(1).getImage().add(new UploadModel.ImageBean(response.getName()));
                            } else if (documentType.equals(Constants.English_Proficiency_Test)) {
                                documentsActivity.getList().get(3).getImage().add(new UploadModel.ImageBean(response.getName()));
                            } else if (documentType.equals(Constants.Personal_Statement)) {
                                documentsActivity.getList().get(4).getImage().add(new UploadModel.ImageBean(response.getName()));
                            } else if (documentType.equals(Constants.Extra_Documents)) {
                                documentsActivity.getList().get(5).getImage().add(new UploadModel.ImageBean(response.getName()));
                            }
//                        }
                        }

                        documentsActivity.updateNewList();
                        /* startActivity(new Intent(getActivity(), ApplicationActivity.class));*/
                        /*activity.finish();*/
                    }
                }


                if (uploadingImagePosition >= selectedImages.size()) {
                    alertDialog.dismiss();
//                    Utility.showToast(getActivity(), "All Images Uploaded");
                } else {
//                    UploadgImagesToServer(uploadingImagePosition);
                }

            } else {
                if (response.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(getActivity());
                } else {
                    Utility.showToast(getActivity(), response.getMessage());
                }
            }
        } catch (Exception ex) {

            Log.e("Upload Fragment ", "Exception : " + ex.getMessage());
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
        dismissWithAnimation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
