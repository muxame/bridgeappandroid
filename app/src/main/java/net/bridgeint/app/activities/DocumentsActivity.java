package net.bridgeint.app.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.adapters.Document_Adapter;
import net.bridgeint.app.adapters.Upload_Adapter;
import net.bridgeint.app.fragments.BaseFragment;
import net.bridgeint.app.fragments.UploadFragment;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.ApplicationModel;
import net.bridgeint.app.models.UploadModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.models.successResponse.SavePaymentResponse;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class DocumentsActivity extends BaseFragment implements View.OnClickListener, ResponceCallback {

    private Context context;
    private RecyclerView rvDocument;
    private Button btnNext;
    private Document_Adapter adapter;
    private Upload_Adapter uploadAdapter;
    private ProgressDialog dialog;
    private TextView tvStudentId;
    private CircleImageView ivUser;
    private ApplicationModel applicationModel;
    String action = "";
    private ImageView ibBack;
    private ArrayList<UploadModel> list = new ArrayList<>();

    public ArrayList<UploadModel> getList() {
        return list;
    }

    View view;

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_documents, container, false);
        initialize();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        ((BaseActivity) getActivity()).logFirebaseEvent(Constants.EVENT_DOCUMENT_LISTING_SCREEN.replace(" ", "_").toLowerCase());
//        if (getArguments().get("application") != null) {
//            btnNext.setVisibility(View.VISIBLE);
//            action = "false";
//            applicationModel = getArguments().getParcelableArray("application");
//            list = new ArrayList<>();
//            list.add(new UploadModel((getString(R.string.school_transcript))));
//            list.add(new UploadModel((getString(R.string.recom_letters))));
//            list.add(new UploadModel((getString(R.string.pass_copy))));
//            list.add(new UploadModel((getString(R.string.lang_score))));
//            list.add(new UploadModel((getString(R.string.personal_statement))));
//            list.add(new UploadModel((getString(R.string.extra_documents))));
//            rvDocument.setLayoutManager(new LinearLayoutManager(context));
//            for (UploadModel uploadModel : applicationModel.getApplication()) {
//                if (uploadModel.getType().equals("high_school_transcripts")) {
//                    list.get(0).setType(uploadModel.getType());
//                    list.get(0).setImage(uploadModel.getImage());
//                    list.get(0).setApplyId(uploadModel.getApplyId());
//                } else if (uploadModel.getType().equals("passport_copies")) {
//                    list.get(2).setType(uploadModel.getType());
//                    list.get(2).setImage(uploadModel.getImage());
//                    list.get(2).setApplyId(uploadModel.getApplyId());
//                } else if (uploadModel.getType().equals("recommendation_letters")) {
//                    list.get(1).setType(uploadModel.getType());
//                    list.get(1).setImage(uploadModel.getImage());
//                    list.get(1).setApplyId(uploadModel.getApplyId());
//                } else if (uploadModel.getType().equals("eng_proficiency_tests")) {
//                    list.get(3).setType(uploadModel.getType());
//                    list.get(3).setImage(uploadModel.getImage());
//                    list.get(3).setApplyId(uploadModel.getApplyId());
//                } else if (uploadModel.getType().equals("personal_statements")) {
//                    list.get(4).setType(uploadModel.getType());
//                    list.get(4).setImage(uploadModel.getImage());
//                    list.get(4).setApplyId(uploadModel.getApplyId());
//                } else if (uploadModel.getType().equals("extra_documents")) {
//                    list.get(5).setType(uploadModel.getType());
//                    list.get(5).setImage(uploadModel.getImage());
//                    list.get(5).setApplyId(uploadModel.getApplyId());
//                }
//            }
//            uploadAdapter = new Upload_Adapter(context, list);
//            rvDocument.setAdapter(uploadAdapter);
//            uploadAdapter.setOnItemClickListener(new Upload_Adapter.MyClickListener() {
//                @Override
//                public void onItemClick(int position) {
//                    FragmentManager fragmentManager = getChildFragmentManager();
//                    UploadFragment dialog = new UploadFragment();
//                    Bundle bundle = new Bundle();
//                    String type = "";
//                    switch (position) {
//                        case 0:
//                            type = "high_school_transcripts";
//                            bundle.putString("SelectionType", Constants.High_School_Transcript);
//                            break;
//                        case 1:
//                            type = "recommendation_letters";
//                            bundle.putString("SelectionType", Constants.Recommendation_Letters);
//                            break;
//                        case 2:
//                            type = "passport_copies";
//                            bundle.putString("SelectionType", Constants.Passport_Copy);
//                            break;
//                        case 3:
//                            type = "eng_proficiency_tests";
//                            bundle.putString("SelectionType", Constants.English_Proficiency_Test);
//                            break;
//                        case 4:
//                            type = "personal_statements";
//                            bundle.putString("SelectionType", Constants.Personal_Statement);
//                            break;
//                        case 5:
//                            type = "extra_documents";
//                            bundle.putString("SelectionType", Constants.Extra_Documents);
//                            break;
//                    }
//                    bundle.putBoolean("isInEditMode", true);
//                    bundle.putString("type", type);
//                    bundle.putString("applyId", applicationModel.getApplyId());
//                    dialog.setArguments(bundle);
//                    dialog.show(fragmentManager, "");
//                }
//            });
//        } else {
        action = "true";
        setData();
//        }

        return view;
    }

    public void updateNewList() {
        uploadAdapter.updateList(list);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void initialize() {
        dialog = new ProgressDialog(getActivity());
//        logEvent(EVENT_UPLOAD_DOCUMENT_SCREEN, getUserDetails());
//        logFirebaseEvent(EVENT_UPLOAD_DOCUMENT_SCREEN.replace(" ", "_").toLowerCase());
        rvDocument = view.findViewById(R.id.rvDocument);
        btnNext = view.findViewById(R.id.btnNext);
        ibBack = view.findViewById(R.id.ibBack);
        context = getActivity();
        btnNext.setOnClickListener(this);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
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
        adapter = new Document_Adapter(context, list, this);
        rvDocument.setAdapter(adapter);
        adapter.setOnItemClickListener(new Document_Adapter.MyClickListener() {
            @Override
            public void onItemClick(int position) {
                FragmentManager fragmentManager = getChildFragmentManager();
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
//    public void replaceFragment(Fragment fragment, String transactionTag) {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
////        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
//        transaction.addToBackStack(transactionTag)
//                .replace(R.id.container, fragment)
//                .commit();
//
////        updateToolbar(transactionTag);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (Utility.isNetConnected(getActivity())) {
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
                    if (addedCounter == 6) {
                        Utility.showConfirmDialog(getActivity(), getString(R.string.empty_doc_text), new ResponceCallback() {
                            @Override
                            public void onSuccessResponce(Object obj) {
                                if (ApplyModel.universityModel.getIs_partner().equalsIgnoreCase("Yes")) {
                                    Utility.showLoadingDialog(getActivity(), "Please wait...");
                                    ServerCall.saveCODPayment(getActivity(), "Partner", "-", "0", "0",
                                            new ResponceCallback() {
                                                @Override
                                                public void onSuccessResponce(Object obj) {
                                                    Utility.hideLoadingDialog();
                                                    SavePaymentResponse savePaymentResponse = ((Response<SavePaymentResponse>) obj).body();
                                                    callAppyRequestUniversity(savePaymentResponse.getResult().getSuccessPayment().getId() + "",savePaymentResponse.getResult().getSuccessPayment().getPayment_status());
                                                    //Utility.showToast(ServiceActivity.this,savePaymentResponse.getMessage());
                                                    //startMainActivity();
                                                }

                                                @Override
                                                public void onFailureResponce(Object obj) {
                                                    Utility.hideLoadingDialog();

                                                }
                                            });
                                } else {
                                    Intent intent = new Intent(getActivity(), ServiceActivity.class);
                                    intent.putExtra("action", action);
                                    if (action.equalsIgnoreCase("false")) {
                                        intent.putExtra("packageList", applicationModel.getPackages());
                                        intent.putExtra("appliedId", applicationModel.getApplyId());
                                    }
                                    startActivity(intent);
                                }

                                Utility.hideConfirmDialog();
                                /*SaveAllDataToServer();*/
                            }

                            @Override
                            public void onFailureResponce(Object obj) {
                                Utility.hideConfirmDialog();
                            }
                        });
                    } else {
                        if (ApplyModel.universityModel.getIs_partner().equalsIgnoreCase("Yes")) {
                            Utility.showLoadingDialog(getActivity(), "Please wait...");
                            ServerCall.saveCODPayment(getActivity(), "Partner", "-", "0", "0",
                                    new ResponceCallback() {
                                        @Override
                                        public void onSuccessResponce(Object obj) {
                                            Utility.hideLoadingDialog();
                                            SavePaymentResponse savePaymentResponse = ((Response<SavePaymentResponse>) obj).body();
                                            callAppyRequestUniversity(savePaymentResponse.getResult().getSuccessPayment().getId() + "",savePaymentResponse.getResult().getSuccessPayment().getPayment_status());
                                            //Utility.showToast(ServiceActivity.this,savePaymentResponse.getMessage());
                                            //startMainActivity();
                                        }

                                        @Override
                                        public void onFailureResponce(Object obj) {
                                            Utility.hideLoadingDialog();

                                        }
                                    });
                        } else {
                            Intent intent = new Intent(getActivity(), ServiceActivity.class);
                            intent.putExtra("action", action);
                            if (action.equalsIgnoreCase("false")) {
                                intent.putExtra("packageList", applicationModel.getPackages());
                                intent.putExtra("appliedId", applicationModel.getApplyId());
                            }
                            startActivity(intent);
                        }
                        /* SaveAllDataToServer();*/
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
        }
    }

    String commaSeperatedIDs = "";
    String commaSeperatedName = "";
    String commaSeperatedPrice = "";
    String qty = "";

    private void callAppyRequestUniversity(String successPaymentId,String paymentStatus) {

//        ArrayList<Package> packagesData =[];
//        ArrayList<String> packageIds = new ArrayList<>();
//        ArrayList<String> packageName = new ArrayList<>();
//        ArrayList<String> packagePrice = new ArrayList<>();
//        ArrayList<String> qtyArr = new ArrayList<>();
//        for (int i = 0; i < packagesData.size(); i++) {
//            packageIds.add(packagesData.get(i).getPackage().getId() + "");
//            packageName.add(packagesData.get(i).getPackage().getName());
//            packagePrice.add(packagesData.get(i).getPackage().getPrice() + "");
//            if (packagesData.get(i).getPackage().getFlag().equalsIgnoreCase("1")) {
////                qtyArr.add(String.valueOf(this.adapter.getMinteger()));
//            } else {
//                qtyArr.add("1");
//            }
//        }
//        commaSeperatedIDs = TextUtils.join(",", packageIds);
//        commaSeperatedName = TextUtils.join(",", packageName);
//        commaSeperatedPrice = TextUtils.join(",", packagePrice);
//        qty = TextUtils.join(",", qtyArr);

        Log.e("====PackageID===", commaSeperatedIDs);
        Log.e("====PackageName===", commaSeperatedName);
        Log.e("====PackagePrice===", commaSeperatedPrice);
        Log.e("====qty===", qty);


        if (Utility.isNetConnected(getActivity())) {
            SaveAllDataToServer("0", "-", "0", "0", successPaymentId,"PT");

//            FLAG_NUMBER = 2;
        } else {
            /* Utility.showToast(ServiceActivity.this, Constants.NET_CONNECTION_LOST);*/
        }
    }

    public void updateList() {
        if (adapter != null) {
            adapter.updateList();
        } else {
            uploadAdapter.updateList();
        }
    }

    private void SaveAllDataToServer(String commaSeperatedIDs, String commaSeperatedName, String commaSeperatedPrice, String qty, String successPaymentId,String paymentstatus) {
//        if (action.equalsIgnoreCase("true")) {
            ServerCall.ApplyApplication(getActivity(), dialog, new ResponceCallback() {
                @Override
                public void onSuccessResponce(Object obj) {
                    final GenericResponce response = ((Response<GenericResponce>) obj).body();
                    try {
                        if (!response.getError()) {
                            Utility.showToast(getActivity(), response.getMessage());
                            ApplyModel.clearAll();
                            startActivity(new Intent(getActivity(), DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        } else {
                            if (response.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                                SharedClass.logout(getActivity());
                            } else {
                                 Utility.showToast(getActivity(), response.getMessage());
                            }
                        }
                    } catch (Exception exp) {

                    }
                }

                @Override
                public void onFailureResponce(Object obj) {
                    Utility.hideLoadingDialog();
                }
            }, commaSeperatedIDs, commaSeperatedName, commaSeperatedPrice, qty, successPaymentId,paymentstatus);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                getActivity().onBackPressed();
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

    @Override
    public void onSuccessResponce(Object obj) {
        final GenericResponce response = ((Response<GenericResponce>) obj).body();
        try {
            if (!response.getError()) {
                Utility.showToast(getActivity(), response.getMessage());
                ApplyModel.clearAll();
                startActivity(new Intent(getActivity(), DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                if (response.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(getActivity());
                } else {
                    Utility.showToast(getActivity(), response.getMessage());
                }
            }
        } catch (Exception exp) {

        }
    }

    @Override
    public void onFailureResponce(Object obj) {
        try {
            Response<GenericResponce> response = (Response<GenericResponce>) obj;
            Utility.showToast(getActivity(), response.message());
            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                SharedClass.logout(getActivity());
            } else {
                Utility.showToast(getActivity(), response.message());
            }
        } catch (Exception exp) {
        }
    }
}
