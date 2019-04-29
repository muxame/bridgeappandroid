package net.bridgeint.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.adapters.myApplcationAdapterUpdate;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.ApplicationModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.ApplicationResponce;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Response;

import static net.bridgeint.app.utils.Constants.EVENT_CHECK_STA_SCREEN;

public class CheckStatusFragment extends BaseFragment {
    View view;
    RecyclerView rvRecent;
    myApplcationAdapterUpdate adapter;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    Unbinder unbinder;
    @BindView(R.id.tv_empty_applications)
    TextView tvEmptyApplications;

    private ArrayList<ApplicationModel> applicationModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check_status, container, false);
        init();
        getCheckStatusDate();
        unbinder = ButterKnife.bind(this, view);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            btnBack.setRotation(180);
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_CHECK_STA_SCREEN.replace(" ", "_").toLowerCase());
        return view;
    }

    private void getCheckStatusDate() {
        Utility.showLoadingDialog(getActivity(), "" + getResources().getString(R.string.fatching_application));
        getAllMyApplications();
    }

    private void getAllMyApplications() {
        ServerCall.getMyApplication(getActivity(), new ResponceCallback() {
            @Override
            public void onSuccessResponce(Object obj) {
                Utility.hideLoadingDialog();
                applicationModels.clear();
                final ApplicationResponce response = ((Response<ApplicationResponce>) obj).body();
                try {
                    if (!response.getError()) {
                        if (response.getApplicationModels().size() > 0) {
//                            no_data.setVisibility(View.GONE);
                            if (response.getApplicationModels().size() == 0) {
                                rvRecent.setVisibility(View.GONE);

                                tvEmptyApplications.setVisibility(View.VISIBLE);
                            } else {
                                rvRecent.setVisibility(View.VISIBLE);
                                tvEmptyApplications.setVisibility(View.GONE);
                            }
                            applicationModels.addAll(response.getApplicationModels());
                            setRecentData();
                        } else {
                            Utility.showConfirmDialogSingle(getActivity(), getString(R.string.we_can_help_you_popup_message), new ResponceCallback() {
                                @Override
                                public void onSuccessResponce(Object obj) {
                                    Utility.hideConfirmDialog();
                                }

                                @Override
                                public void onFailureResponce(Object obj) {

                                }
                            });

                            tvEmptyApplications.setVisibility(View.VISIBLE);
//                            no_data.setVisibility(View.GONE);
                            setRecentData();
                        }
                    }
                } catch (Exception exp) {

                }
            }

            @Override
            public void onFailureResponce(Object obj) {
                try {
                    Utility.hideLoadingDialog();
                    Response<ApplicationResponce> response = (Response<ApplicationResponce>) obj;
                    Utility.showToast(getActivity(), response.message());

                } catch (Exception exp) {
                }
            }
        });
    }


    private void init() {
        rvRecent = view.findViewById(R.id.rvRecent);
        setRecentData();

    }

    private void setRecentData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvRecent.setLayoutManager(layoutManager);
        adapter = new myApplcationAdapterUpdate(getActivity(), applicationModels, false) {
            @Override
            protected void onCloseClick(ApplicationModel applicationModel) {
                super.onCloseClick(applicationModel);
                Utility.showConfirmDialog(getActivity(), "Are you sure you want to remove this Application?", new ResponceCallback() {
                    @Override
                    public void onSuccessResponce(Object obj) {
                        callApiforRemoveData(applicationModel.getApplyId());
                        Utility.hideConfirmDialog();
                    }

                    @Override
                    public void onFailureResponce(Object obj) {
                        Utility.hideConfirmDialog();
                    }
                });

            }
        };
        rvRecent.setAdapter(adapter);
    }

    private void callApiforRemoveData(String applyId) {
        Utility.showLoadingDialog(getActivity(), "Removing  Application");
        ServerCall.removeapplication(getActivity(), applyId, new ResponceCallback() {
            @Override
            public void onSuccessResponce(Object obj) {
                Utility.hideLoadingDialog();
                GenericResponce degreeResponce = ((Response<GenericResponce>) obj).body();
                if (degreeResponce.getError()) {
                    if (degreeResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(getActivity());
                    } else {
                        Utility.showToast(getActivity(), degreeResponce.getMessage());
                    }
                } else {
//                    SharedClass.degreeModels = degreeResponce.getDegreeModels();
                    //adapter.UpdateList(SharedClass.degreeModels);
                    getCheckStatusDate();
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
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
