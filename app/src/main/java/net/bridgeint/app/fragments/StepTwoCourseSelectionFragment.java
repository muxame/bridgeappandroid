package net.bridgeint.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.bridgeint.app.R;
import net.bridgeint.app.adapters.Course_AdapterUpdate;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.CourseModel;
import net.bridgeint.app.models.Paging;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.CourseResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

//import static net.bridgeint.app.utils.Constants.EVENT_STREAM_SELECT_SCREEN;

public class StepTwoCourseSelectionFragment extends BaseFragment {
    @BindView(R.id.ic_backbtn)
    ImageView icBackbtn;
    private RecyclerView rvCourse;
    private Course_AdapterUpdate adapter;
    private int totalPages, currentPage, nextPage;
    private LinearLayoutManager linearManager;
    private int visibleItemCount, pastVisiblesItems, totalItemCount;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_second_step_course_selection, container, false);
//        Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_degree));

        ButterKnife.bind(this, view);
//        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_STREAM_SELECT_SCREEN.replace(" ", "_").toLowerCase());
        getIds(view);
        firstCall();
        return view;
    }

    private void firstCall() {
        if (ApplyModel.degreeId != null) {
            Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_courses));
            /*ServerCall.getCourses(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), ApplyModel.degreeId.toString(), 1 + "", "", this);*/
            ServerCall.getNewCourses(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), ApplyModel.degreeId, ApplyModel.courseType, 1 + "", "", new ResponceCallback() {
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
                    }
                }
            });
        }
    }

    private void initAdapter() {
        linearManager = new LinearLayoutManager(getContext());
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvCourse.setLayoutManager(linearManager);
rvCourse.setNestedScrollingEnabled(false);
        adapter = new Course_AdapterUpdate(getContext(), SharedClass.courseModels) {
            @Override
            protected void onItemClick(int position) {
                super.onItemClick(position);
                replaceFragment(new StepThirdCountrySelectionFragment(), "country");
//                ((DegreeActivity) getActivity()).onNextClick();
            }
        };
        rvCourse.setAdapter(adapter);

        rvCourse.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    if (currentPage <= totalPages) {

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

    }

    private void load_new_courses(int va) {
        Utility.showLoadingDialog(getActivity(), getString(R.string.fetchingcourses));
        /*  ServerCall.getCourses(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), ApplyModel.degreeId.toString(), nextPage + "", "", this);*/
        ServerCall.getNewCourses(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), ApplyModel.degreeId, ApplyModel.courseType, nextPage + "", "", new ResponceCallback() {
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
        rvCourse = view.findViewById(R.id.rvCourse);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            icBackbtn.setRotation(180);
        }
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


    @OnClick(R.id.ic_backbtn)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }
}
