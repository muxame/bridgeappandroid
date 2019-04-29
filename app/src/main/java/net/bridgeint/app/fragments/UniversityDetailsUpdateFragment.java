package net.bridgeint.app.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.activities.ChatActivity;
import net.bridgeint.app.activities.DocumentsActivity;
import net.bridgeint.app.adapters.Course_AdapterUpdateForUniversity;
import net.bridgeint.app.application.Application;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.CourseModel;
import net.bridgeint.app.models.Paging;
import net.bridgeint.app.models.UniversityModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.CourseResponce;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Response;

import static net.bridgeint.app.utils.Constants.EVENT_UNIVERSITY_DETAILS_SCREEN;

public class UniversityDetailsUpdateFragment extends BaseFragment {
    View view;
    @BindView(R.id.ic_backbtn)
    ImageView icBackbtn;
    @BindView(R.id.ivUni)
    ImageView ivUni;
    @BindView(R.id.tvAdmissionOpen)
    TextView tvAdmissionOpen;
    @BindView(R.id.tvUniName)
    TextView tvUniName;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.rcv_course_list)
    RecyclerView rcvCourseList;
    Unbinder unbinder;
    @BindView(R.id.tv_request_information)
    TextView tvRequestInformation;
    @BindView(R.id.tv_average_tution_fees)
    TextView tvAverageTutionFees;
    @BindView(R.id.tv_acceptace_rate)
    TextView tvAcceptaceRate;
    @BindView(R.id.tvstudent)
    TextView tvstudent;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;

    private Course_AdapterUpdateForUniversity adapter;
    private int totalPages, currentPage, nextPage;
    private LinearLayoutManager linearManager;
    private int visibleItemCount, pastVisiblesItems, totalItemCount;
    public UniversityModel universityModel = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_university_details_screen, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            icBackbtn.setRotation(180);
        }
        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_UNIVERSITY_DETAILS_SCREEN.replace(" ", "_").toLowerCase());
        if (getArguments() != null) {
//            universityModel = (UniversityModel) getIntent().getSerializableExtra(Constants.UNIVERSITY);
            universityModel = (UniversityModel) getArguments().getSerializable(Constants.UNIVERSITY);
            SharedClass.universityModel = universityModel;
            tvUniName.setText(universityModel.getTitle());
            tvAddress.setText(universityModel.getAddress());
            tvDescription.setText(universityModel.getInformation());
            if (!universityModel.getIcon().equalsIgnoreCase("")) {
                Picasso.with(getActivity()).load(Constants.IMAGE_URL + universityModel.getIcon()).into(ivUni);

            }
            tvRequestInformation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Application.setIsBackUnable(true);
                    replaceFragment(new ChatActivity(), "chat");
                }
            });
            tvAverageTutionFees.setText(universityModel.getAverage_tuition());
            tvAcceptaceRate.setText(universityModel.getAcceptance_rate());
            tvstudent.setText(universityModel.getStudents());
        }
        firstCall();
        return view;
    }

    private void firstCall() {
        if (ApplyModel.degreeId != null) {
            Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_courses));
            /*ServerCall.getCourses(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), ApplyModel.degreeId.toString(), 1 + "", "", this);*/
            ServerCall.getCourceDetailsUniDetailsAPi(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), ApplyModel.degreeId, ApplyModel.courseType/*, ApplyModel.streamId*/, String.valueOf(ApplyModel.universityModel.getId()), 1 + "", "", new ResponceCallback() {
                @Override
                public void onSuccessResponce(Object obj) {
                    Utility.hideLoadingDialog();
                    CourseResponce courseResponce = ((Response<CourseResponce>) obj).body();
                    if (courseResponce.getError()) {
                        if (courseResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                            SharedClass.logout(getActivity());
                        } else {
                            Utility.showToast(getActivity(), courseResponce.getMessage());
                        }
                    } else {
                        Paging paging = courseResponce.getPaging();
                        totalPages = paging.getNumberofpage();
                        currentPage = paging.getCurrentpage();
                        nextPage = paging.getCurrentpage() + 1;
                        if (SharedClass.courseModels.size() == 0) {
                            SharedClass.courseModels = courseResponce.getCourseModels();
                            //adapter.UpdateList(SharedClass.courseModels);
                            initAdapter();
                        } else if (SharedClass.courseModels.size() > 0) {
                            SharedClass.courseModels.clear();
                            ArrayList<CourseModel> courses = courseResponce.getCourseModels();
                            SharedClass.courseModels.addAll(courses);
//                            if (adapter != null)
//
//                                adapter.notifyDataSetChanged();
//                            else
                            initAdapter();
                        } else {
                            SharedClass.courseModels.clear();
                            ArrayList<CourseModel> courses = courseResponce.getCourseModels();
                            SharedClass.courseModels.addAll(courses);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailureResponce(Object obj) {
                    Utility.hideLoadingDialog();
                    try {
                        Response<CourseResponce> response = (Response<CourseResponce>) obj;
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
    }

    private void initAdapter() {
        linearManager = new LinearLayoutManager(getContext());
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);

        rcvCourseList.setLayoutManager(linearManager);

        adapter = new Course_AdapterUpdateForUniversity(getContext(), SharedClass.courseModels) {
            @Override
            protected void onItemClick(int position) {
                super.onItemClick(position);
                replaceFragment(new StepThirdCountrySelectionFragment(), "country");
//                ((DegreeActivity) getActivity()).onNextClick();
            }

            @Override
            protected void onApplyClick() {
                super.onApplyClick();
                if (Utility.isNetConnected(getActivity())) {
                    callCheckApplyFor();
                }
            }
        };
        rcvCourseList.setAdapter(adapter);
//        rcvCourseList.setNestedScrollingEnabled(true);
        scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) scrollview.getChildAt(scrollview.getChildCount() - 1);

                int diff = (view.getBottom() - (scrollview.getHeight() + scrollview
                        .getScrollY()));

                if (diff == 0) {
                    if (currentPage < totalPages) {

                        visibleItemCount = linearManager.getChildCount();
                        totalItemCount = linearManager.getItemCount();
                        pastVisiblesItems = linearManager.findFirstVisibleItemPosition();
                        int lastPositon = linearManager.findLastCompletelyVisibleItemPosition();
                        Log.d("last_item", lastPositon + "");
                        if (lastPositon == SharedClass.courseModels.size() - 1) {
//                            if (searched) {
//                                searchCourse(etSearch.getText().toString(), nextPage);
//                            } else {
                            load_new_courses(nextPage);
//                            }
                        }
                    }
                }
            }
        });
        rcvCourseList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {

                }
            }
        });

    }

    public void callCheckApplyFor() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        ServerCall.checkApplyFor(getActivity(), dialog, getString(R.string.please_wait),
                new ResponceCallback() {
                    @Override
                    public void onSuccessResponce(Object obj) {
                        dialog.dismiss();
                        GenericResponce genericResponce = ((Response<GenericResponce>) obj).body();
                        try {
                            if (genericResponce.getError()) {
                                Utility.showConfirmDialogSingle(getActivity(), genericResponce.getMessage(), new ResponceCallback() {
                                    @Override
                                    public void onSuccessResponce(Object obj) {
                                        Utility.hideConfirmDialog();
                                    }

                                    @Override
                                    public void onFailureResponce(Object obj) {

                                    }
                                });
                                Utility.hideLoadingDialog();
                            } else {
                                if (ApplyModel.countryId == null) {
                                    ApplyModel.clearAll();
                                    //  ApplyModel.countryId = universityModel.getCountryId();
                                }
                                ApplyModel.universityModel = universityModel;
                                if (ApplyModel.courseId != null) {
                                    if (Utility.isNetConnected(getActivity())) {
                                        replaceFragment(new DocumentsActivity(), "DocumentUpload");
//                                        Intent intent = new Intent(getActivity(), DocumentsActivity.class);
//                                        startActivity(intent);
                                        /*   finish();*/
                                    } else {
                                        Utility.showNetworkDailogSingle(getActivity(), Constants.NET_CONNECTION_LOST, new ResponceCallback() {
                                            @Override
                                            public void onSuccessResponce(Object obj) {
                                                Process.killProcess(Process.myPid());
                                                System.exit(1);
                                            }

                                            @Override
                                            public void onFailureResponce(Object obj) {

                                            }
                                        });
                                    }
                                } else {
//                                    setResult(Activity.RESULT_OK, new Intent());
//                                    finish();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Utility.hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFailureResponce(Object obj) {
                        dialog.dismiss();
                    }

                }, universityModel.getId() + "", ApplyModel.degreeId, ApplyModel.courseId, ApplyModel.countryId);
    }

    private void load_new_courses(int va) {
        Utility.showLoadingDialog(getActivity(), getString(R.string.fetchingcourses));
        /*  ServerCall.getCourses(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), ApplyModel.degreeId.toString(), nextPage + "", "", this);*/
        ServerCall.getCourceDetailsUniDetailsAPi(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), ApplyModel.degreeId, ApplyModel.courseType, String.valueOf(ApplyModel.universityModel.getId()),nextPage + "", "", new ResponceCallback() {
            @Override
            public void onSuccessResponce(Object obj) {
                Utility.hideLoadingDialog();
                CourseResponce courseResponce = ((Response<CourseResponce>) obj).body();
                if (courseResponce.getError()) {
                    if (courseResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(getActivity());
                    } else {
                        Utility.showToast(getActivity(), courseResponce.getMessage());
                    }
                } else {
                    Paging paging = courseResponce.getPaging();
                    totalPages = paging.getNumberofpage();
                    currentPage = paging.getCurrentpage();
                    nextPage = paging.getCurrentpage() + 1;
                    if (SharedClass.courseModels.size() == 0) {
                        SharedClass.courseModels = courseResponce.getCourseModels();
                        //adapter.UpdateList(SharedClass.courseModels);
                        initAdapter();
                    } else if (SharedClass.courseModels.size() > 0) {
                        /*  SharedClass.courseModels.clear();*/
                        ArrayList<CourseModel> courses = courseResponce.getCourseModels();
                        SharedClass.courseModels.addAll(courses);
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        else
                            initAdapter();
                    } else {
                        SharedClass.courseModels.clear();
                        ArrayList<CourseModel> courses = courseResponce.getCourseModels();
                        SharedClass.courseModels.addAll(courses);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailureResponce(Object obj) {
                Utility.hideLoadingDialog();
                try {
                    Response<CourseResponce> response = (Response<CourseResponce>) obj;
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

    private void getIds(View view) {
        rcvCourseList = view.findViewById(R.id.rvCourse);
    }
//
//    public void replaceFragment(Fragment fragment, String transactionTag) {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
////        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
//        transaction.addToBackStack(transactionTag)
//                .replace(R.id.container, fragment)
//                .commit();
//
////        updateToolbar(transactionTag);
//    }
//

    @OnClick(R.id.ic_backbtn)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}

