package net.bridgeint.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.bridgeint.app.R;
import net.bridgeint.app.adapters.Document_Adapter;
import net.bridgeint.app.adapters.Upload_Adapter;
import net.bridgeint.app.application.Application;
import net.bridgeint.app.fragments.UploadFragment;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.ApplicationModel;
import net.bridgeint.app.models.UploadModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.network.WebServiceHelper;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.responces.packages.ApplyResponse;
import net.bridgeint.app.utils.Config;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentsFromFreeActivity extends BaseActivity implements View.OnClickListener, ResponceCallback {

    private Context context;
    private RecyclerView rvDocument;
    private Button btnNext;
    private Document_Adapter adapter;
    private Upload_Adapter uploadAdapter;
    private ProgressDialog dialog;
    private TextView tvStudentId;
    private CircleImageView ivUser;
    private ApplicationModel applicationModel;
    private ImageView ibBack;
    private ArrayList<UploadModel> list = new ArrayList<>();
    private String countryId = "",countryName = "",universityId = "",universityName = "",comment = "";

    public ArrayList<UploadModel> getList() {
        return list;
    }

    public void updateNewList() {
        uploadAdapter.updateList(list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Utility.changeStatusBarColor(this, R.color.dashboard_color);
            setContentView(R.layout.activity_documents);
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        if(getIntent().hasExtra("fromFreeFragment")){
            btnNext.setVisibility(View.VISIBLE);
            countryId = getIntent().getStringExtra("countryId");
            countryName = getIntent().getStringExtra("countryName");
            universityId = getIntent().getStringExtra("universityId");
            universityName = getIntent().getStringExtra("universityName");
            comment = getIntent().getStringExtra("comment");
            applicationModel = getIntent().getParcelableExtra("application");
            list = new ArrayList<>();
            list.add(new UploadModel((getString(R.string.school_transcript))));
            list.add(new UploadModel((getString(R.string.recom_letters))));
            list.add(new UploadModel((getString(R.string.pass_copy))));
            list.add(new UploadModel((getString(R.string.lang_score))));
            list.add(new UploadModel((getString(R.string.personal_statement))));
            list.add(new UploadModel((getString(R.string.extra_documents))));
            rvDocument.setLayoutManager(new LinearLayoutManager(context));
            uploadAdapter = new Upload_Adapter(context, list);
            rvDocument.setAdapter(uploadAdapter);
            uploadAdapter.setOnItemClickListener(new Upload_Adapter.MyClickListener() {
                @Override
                public void onItemClick(int position) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    UploadFragment dialog = new UploadFragment();
                    Bundle bundle = new Bundle();
                    String type = "";
                    switch (position) {
                        case 0:
                            type = "high_school_transcripts";
                            bundle.putString("SelectionType", Constants.High_School_Transcript);
                            break;
                        case 1:
                            type = "recommendation_letters";
                            bundle.putString("SelectionType", Constants.Recommendation_Letters);
                            break;
                        case 2:
                            type = "passport_copies";
                            bundle.putString("SelectionType", Constants.Passport_Copy);
                            break;
                        case 3:
                            type = "eng_proficiency_tests";
                            bundle.putString("SelectionType", Constants.English_Proficiency_Test);
                            break;
                        case 4:
                            type = "personal_statements";
                            bundle.putString("SelectionType", Constants.Personal_Statement);
                            break;
                        case 5:
                            type = "extra_documents";
                            bundle.putString("SelectionType", Constants.Extra_Documents);
                            break;
                    }
                    bundle.putString("type", type);
                    bundle.putBoolean("isFromFreeMode", true);
                    dialog.setArguments(bundle);
                    dialog.show(fragmentManager, "");
                }
            });
        }

    }

    private void initialize() {
        dialog = new ProgressDialog(DocumentsFromFreeActivity.this);
        rvDocument = findViewById(R.id.rvDocument);
        btnNext = findViewById(R.id.btnNext);
        ibBack = findViewById(R.id.ibBack);
        context = DocumentsFromFreeActivity.this;
        btnNext.setOnClickListener(this);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            ibBack.setRotation(180);
        }
    }

    private void setData() {
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.school_transcript));
        list.add(getString(R.string.recom_letters));
        list.add(getString(R.string.pass_copy));
        list.add(getString(R.string.lang_score));
        list.add(getString(R.string.personal_statement));
        list.add(getString(R.string.extra_documents));
        rvDocument.setLayoutManager(new LinearLayoutManager(context));
        adapter = new Document_Adapter(context, list);
        rvDocument.setAdapter(adapter);
        adapter.setOnItemClickListener(new Document_Adapter.MyClickListener() {
            @Override
            public void onItemClick(int position) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                UploadFragment dialog = new UploadFragment();
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0:
                        bundle.putString("SelectionType", Constants.High_School_Transcript);
                        break;
                    case 1:
                        bundle.putString("SelectionType", Constants.Recommendation_Letters);
                        break;
                    case 2:
                        bundle.putString("SelectionType", Constants.Passport_Copy);
                        break;
                    case 3:
                        bundle.putString("SelectionType", Constants.English_Proficiency_Test);
                        break;
                    case 4:
                        bundle.putString("SelectionType", Constants.Personal_Statement);
                        break;
                    case 5:
                        bundle.putString("SelectionType", Constants.Extra_Documents);
                        break;
                }
                dialog.setArguments(bundle);
                dialog.show(fragmentManager, "");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                StringBuilder docTypeStr = new StringBuilder();
                StringBuilder imageNameStr = new StringBuilder();
                for (UploadModel uploadModel : getList()) {
                    if(uploadModel.getImage().size()>0) {
                        for (UploadModel.ImageBean imageBean : uploadModel.getImage()) {
                            if(imageNameStr.toString().length() > 0){
                                imageNameStr.append("@");
                            }
                            imageNameStr.append(imageBean.getName());
                            if (docTypeStr.toString().length() > 0) {
                                docTypeStr.append("@");
                            }
                            docTypeStr.append(uploadModel.getName());
                        }
                    }
                }

                applyFreePackges(countryId,countryName,universityId,universityName,imageNameStr.toString(),docTypeStr.toString(),comment);

/*
                if (Utility.isNetConnected(DocumentsFromFreeActivity.this)) {
                    int addedCounter = 0;
                    if (ApplyModel.highSchoolTranscript == null) {
                        ++addedCounter;
                    }
                    if (ApplyModel.recommendationLetters == null) {
                        ++addedCounter;
                    }
                    if (ApplyModel.passportCopies == null) {
                        ++addedCounter;
                    }
                    if (ApplyModel.englishProfeciencyTest == null) {
                        ++addedCounter;
                    }
                    if (ApplyModel.personalStatment == null) {
                        ++addedCounter;
                    }
                    if (ApplyModel.extraDocuments == null) {
                        ++addedCounter;
                    }
                    Utility.showFreeAddCommentDialog(DocumentsFromFreeActivity.this, new ResponceCallback() {
                        @Override
                        public void onSuccessResponce(Object obj) {
                            Utility.hideConfirmDialog();


                        }

                        @Override
                        public void onFailureResponce(Object obj) {


                        }
                    });

*//*                    if (addedCounter == 6) {
                        Utility.showConfirmDialog(DocumentsFromFreeActivity.this, getString(R.string.empty_doc_text), new ResponceCallback() {
                            @Override
                            public void onSuccessResponce(Object obj) {
                                Intent intent = new Intent(DocumentsFromFreeActivity.this, ServiceActivity.class);
                                intent.putExtra("action", action);
                                if (action.equalsIgnoreCase("false")) {
                                    intent.putExtra("packageList", applicationModel.getPackages());
                                    intent.putExtra("appliedId", applicationModel.getApplyId());
                                }
                                startActivity(intent);
                                Utility.hideConfirmDialog();
                                *//**//*SaveAllDataToServer();*//**//*
                            }

                            @Override
                            public void onFailureResponce(Object obj) {
                                Utility.hideConfirmDialog();
                            }
                        });
                    } else {
                        Intent intent = new Intent(DocumentsFromFreeActivity.this, ServiceActivity.class);
                        intent.putExtra("action", action);
                        if (action.equalsIgnoreCase("false")) {
                            intent.putExtra("packageList", applicationModel.getPackages());
                            intent.putExtra("appliedId", applicationModel.getApplyId());
                        }
                        startActivity(intent);
                        *//**//* SaveAllDataToServer();*//**//*
                    }*//*
                } else {
                    Utility.showToast(DocumentsFromFreeActivity.this, Constants.NET_CONNECTION_LOST);
                }
                break;*/
        }
    }

    private void applyFreePackges(String countyId, String countryName, String universityId, String universityName,
                                  String doument, String doumentType, String comment) {
        Callback<ApplyResponse> callback = new Callback<ApplyResponse>() {
            @Override
            public void onResponse(Call<ApplyResponse> call, Response<ApplyResponse> response) {
                Utility.hideLoadingDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null && !response.body().getError()) {
                        Toast.makeText(DocumentsFromFreeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Application.mSelection = 0;
                        startMainActivity();
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ApplyResponse> call, Throwable t) {
                Utility.hideLoadingDialog();
            }
        };
        Utility.showLoadingDialog(DocumentsFromFreeActivity.this, "Please wait...");
        String user_id = SharedClass.userModel.getId().toString();
        String accessToken = SharedClass.userModel.getAccessKey();

        WebServiceHelper.applyfreepackageCall(user_id, accessToken, Config.API_KEY, countyId, countryName, universityId, universityName, doument, doumentType, comment).enqueue(callback);

    }

    private void startMainActivity() {
        startActivity(new Intent(DocumentsFromFreeActivity.this, DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    public void updateList() {
        if (adapter != null) {
            adapter.updateList();
        } else {
            uploadAdapter.updateList();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSuccessResponce(Object obj) {
        final GenericResponce response = ((Response<GenericResponce>) obj).body();
        try {
            if (!response.getError()) {
                Utility.showToast(DocumentsFromFreeActivity.this, response.getMessage());
                ApplyModel.clearAll();
                startActivity(new Intent(DocumentsFromFreeActivity.this, DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                if (response.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(DocumentsFromFreeActivity.this);
                } else {
                    Utility.showToast(DocumentsFromFreeActivity.this, response.getMessage());
                }
            }
        } catch (Exception exp) {

        }
    }

    @Override
    public void onFailureResponce(Object obj) {
        try {
            Response<GenericResponce> response = (Response<GenericResponce>) obj;
            Utility.showToast(DocumentsFromFreeActivity.this, response.message());
            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                SharedClass.logout(DocumentsFromFreeActivity.this);
            } else {
                Utility.showToast(DocumentsFromFreeActivity.this, response.message());
            }
        } catch (Exception exp) {
        }
    }
}
