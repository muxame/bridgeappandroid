package net.bridgeint.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.activities.DashboardUpdateActivity;
import net.bridgeint.app.adapters.DegreeAdapterUpdate;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.DegreeModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.DegreeResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import butterknife.ButterKnife;
import retrofit2.Response;

import static net.bridgeint.app.utils.Constants.EVENT_LEVEL_OF_STUDY_SCREEN;

public class StepOneLevelsOfstudy extends BaseFragment {
    private RecyclerView rvDegree;
    private DegreeAdapterUpdate adapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ((DashboardUpdateActivity) getActivity()).backbuttonHide();
        } else {
            ((DashboardUpdateActivity) getActivity()).backButtonshow();
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            ((DashboardUpdateActivity) getActivity()).backbuttonHide();
        } else {
            ((DashboardUpdateActivity) getActivity()).backButtonshow();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_levels_of_study, container, false);
        Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_degree));
        getIds(view);
        ButterKnife.bind(this, view);
        ((DashboardUpdateActivity) getActivity()).backbuttonHide();
//        new Handler().postDelayed(new Runnable() {

//            @Override
//            public void run() {
//                showPopup();
//            }
//        }, 3000);
//        showPopup();
//        ((DashboardUpdateActivity) getActivity()).backbuttonHide();

        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_LEVEL_OF_STUDY_SCREEN.replace(" ", "_").toLowerCase());
        ServerCall.getDegrees(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), new ResponceCallback() {
            @Override
            public void onSuccessResponce(Object obj) {
                Utility.hideLoadingDialog();
                DegreeResponce degreeResponce = ((Response<DegreeResponce>) obj).body();
                if (degreeResponce.getError()) {
                    if (degreeResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(getActivity());
                    } else {
                        Utility.showToast(getActivity(), degreeResponce.getMessage());
                    }
                } else {
                    SharedClass.degreeModels = degreeResponce.getDegreeModels();
                    //adapter.UpdateList(SharedClass.degreeModels);
                    initAdapter();
                }
            }

            @Override
            public void onFailureResponce(Object obj) {
                Utility.hideLoadingDialog();
                try {
                    Response<DegreeResponce> response = (Response<DegreeResponce>) obj;
                    if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(getActivity());
                    } else {
                        ErrorUtils.responseError(getActivity(), response);
                    }
                } catch (Exception exp) {
                }
            }
        });

        return view;

    }


    private void showPopup() {
//       if(SessionManager.getInt("isShowPopup")){
        if (SessionManager.getInt("isShowPopup") == 1) {
            return;
        }
//       }
        try {


            Utility.hideConfirmDialog();
            Utility.showConfirmwithCheckDialog(getActivity(), getString(R.string.cant_find__what_), new ResponceCallback() {
                @Override
                public void onSuccessResponce(Object obj) {
                    Utility.hideConfirmDialog();

                    ((DashboardUpdateActivity) getActivity()).openchatfragment();

                }

                @Override
                public void onFailureResponce(Object obj) {
                    Utility.hideConfirmDialog();
                }
            });
        } catch (Exception e) {

        }
    }

    private void getIds(View view) {
        rvDegree = view.findViewById(R.id.rvDegree);
    }

    private void initAdapter() {
        rvDegree.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DegreeAdapterUpdate(getActivity(), SharedClass.degreeModels) {
            @Override
            protected void onItemClick(int pos, DegreeModel degreeModel) {
                super.onItemClick(pos, degreeModel);
                ApplyModel.degreeId = degreeModel.getId() + "";

                replaceFragment(new StepThirdCountrySelectionFragment(), "country");

            }
        };
        rvDegree.setAdapter(adapter);
        rvDegree.getItemAnimator().endAnimations();
    }


}

