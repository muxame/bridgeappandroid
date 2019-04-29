package net.bridgeint.app.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.adapters.FeesAdapterUpdate;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.FeesModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.CourseResponce;
import net.bridgeint.app.responces.FeesResponse;
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

import static net.bridgeint.app.utils.Constants.EVENT_FEESS_SELECT_SCREEN;

public class StepFourFeesFragment extends BaseFragment {
    View view;
    @BindView(R.id.ic_backbtn)
    ImageView icBackbtn;
    @BindView(R.id.rvCourse)
    RecyclerView rvCourse;
    Unbinder unbinder;
    LinearLayoutManager linearManager;
    FeesAdapterUpdate adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fess, container, false);
        unbinder = ButterKnife.bind(this, view);
        SharedClass.feesModels.clear();
        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_FEESS_SELECT_SCREEN.replace(" ", "_").toLowerCase());
        getIds(view);
        firstCall();
        return view;
    }

    private void initAdapter() {
        linearManager = new LinearLayoutManager(getActivity());
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvCourse.setLayoutManager(linearManager);

        adapter = new FeesAdapterUpdate(getActivity(), SharedClass.feesModels) {
            @Override
            protected void onItemClick(int position) {
                super.onItemClick(position);
                replaceFragment(new UniversityListFragment(), "universityList");
//                ((DegreeActivity) getActivity()).onNextClick();
            }
        };
        rvCourse.setAdapter(adapter);


    }

    private void firstCall() {
        if (ApplyModel.degreeId != null) {
            Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_courses));
            /*ServerCall.getCourses(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), ApplyModel.degreeId.toString(), 1 + "", "", this);*/
            ServerCall.getTuitionfeesoption(getActivity(), SharedClass.userModel.getId().toString(), ApplyModel.degreeId/*, ApplyModel.streamId*/, ApplyModel.countryId, SharedClass.userModel.getAccessKey(), new ResponceCallback() {
                @Override
                public void onSuccessResponce(Object obj) {
                    Utility.hideLoadingDialog();
                    FeesResponse feesResponse = ((Response<FeesResponse>) obj).body();
                    if (feesResponse.getError()) {
                        if (feesResponse.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                            SharedClass.logout(getActivity());
                        } else {
                            Utility.showToast(getActivity(), feesResponse.getMessage());
                        }
                    } else {
                        ArrayList<FeesModel> feesModels = feesResponse.getCourseModels();
                        SharedClass.feesModels.addAll(feesModels);
                        initAdapter();
//                            if (adapter != null)
//
//                                adapter.notifyDataSetChanged();
//                            else


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

    private void getIds(View view) {
        rvCourse = view.findViewById(R.id.rvCourse);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            icBackbtn.setRotation(180);
        }
    }

//    public void replaceFragment(android.support.v4.app.Fragment fragment, String transactionTag) {
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
