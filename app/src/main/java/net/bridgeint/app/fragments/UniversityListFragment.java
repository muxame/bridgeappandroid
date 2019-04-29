package net.bridgeint.app.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.activities.DashboardUpdateActivity;
import net.bridgeint.app.adapters.UniversityListAdapter;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.Paging;
import net.bridgeint.app.models.UniversityModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.UniversitiesResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Response;

import static net.bridgeint.app.utils.Constants.EVENT_UNIVERSITY_MEDIA_SCREEN;

public class UniversityListFragment extends BaseFragment {
    View view;
    @BindView(R.id.rvUniversity)
    RecyclerView rvUniversity;
    Unbinder unbinder;
    @BindView(R.id.ic_backbtn)
    ImageView icBackbtn;
    boolean getAllUniversities;
    private UniversityListAdapter adapter;
    //    TextView tvFound;
    private int totalPages, currentPage, nextPage;
    private int visibleItemCount, pastVisiblesItems, totalItemCount;
    private LinearLayoutManager linearManager;

    List<UniversityModel> universityModel = new ArrayList<>();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {

        }
    }

    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            ((DashboardUpdateActivity) getActivity()).hideHelpPopup();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_university_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            icBackbtn.setRotation(180);
        }
        ((DashboardUpdateActivity) getActivity()).showHelpPopup();
        rvUniversity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardUpdateActivity) getActivity()).hideHelpPopup();
            }
        });

        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_UNIVERSITY_MEDIA_SCREEN.replace(" ", "_").toLowerCase());
        linearManager = new LinearLayoutManager(getActivity());
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvUniversity.setLayoutManager(linearManager);
        adapter = new UniversityListAdapter(getActivity(), universityModel) {
            @Override
            protected void onRootClick(int position, UniversityModel item) {
                super.onRootClick(position, item);
                ((DashboardUpdateActivity) getActivity()).hideHelpPopup();
            }
        };
        rvUniversity.setAdapter(adapter);

        if (ApplyModel.degreeId != null) {
            getAllUniversities = false;
            String degreeId = ApplyModel.degreeId;
//            String courseId = ApplyModel.streamId;
            String fees = ApplyModel.fees;
            String countryId = ApplyModel.countryId;
//            etSearch.setVisibility(View.GONE);
            icBackbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
            Utility.hideLoadingDialog();
            Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_university));
            ServerCall.getUniversities(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), degreeId/*, courseId*/, countryId, fees, new ResponceCallback() {
                @Override
                public void onSuccessResponce(Object obj) {
                    Utility.hideLoadingDialog();
                    UniversitiesResponce universitiesResponce = ((Response<UniversitiesResponce>) obj).body();
                    try {
                        if (!universitiesResponce.getError()) {
//                tvFound.setText(universitiesResponce.getUniversityModel().size()+" universities found");
                            if (universitiesResponce.getUniversityModel().size() == 0) {
//                    tv_noUniversity.setVisibility(View.VISIBLE);
                                universityModel.clear();
                                adapter.notifyDataSetChanged();
                            } else {
//                    tv_noUniversity.setVisibility(View.GONE);
                                universityModel.clear();
                                universityModel.addAll(universitiesResponce.getUniversityModel());
                                adapter.notifyDataSetChanged();
                            }
//                SharedClass.allUniversitiesList.clear();
                            if (universitiesResponce.getPaging() != null) {
                                Paging paging = universitiesResponce.getPaging();
                                totalPages = paging.getNumberofpage();
                                currentPage = paging.getCurrentpage();
                                nextPage = paging.getCurrentpage() + 1;
                            }
                            if (getAllUniversities) {
                                Log.e("Universities", new Gson().toJson(universitiesResponce.getUniversityModel()));
                                SharedClass.ParseUniversity(universitiesResponce.getUniversityModel());
                                SharedClass.allUniversitiesList.addAll(universitiesResponce.getUniversityModel());
                                universityModel.addAll(SharedClass.allUniversitiesList);
                                adapter.notifyDataSetChanged();
                            } else {
                                universityModel.clear();
                                linearManager = new LinearLayoutManager(getActivity());
                                linearManager.setOrientation(LinearLayoutManager.VERTICAL);
                                rvUniversity.setLayoutManager(linearManager);
                                adapter = new UniversityListAdapter(getActivity(), universityModel);
                                rvUniversity.setAdapter(adapter);
                                universityModel.addAll(universitiesResponce.getUniversityModel());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            if (universitiesResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                                SharedClass.logout(getActivity());
                            } else {
                                if (universitiesResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                                    SharedClass.logout(getActivity());
                                } else {
                                    Utility.showToast(getActivity(), universitiesResponce.getMessage());
                                }
                            }
                        }

                    } catch (Exception ex) {
                        Utility.showToast(getActivity(), ex.getMessage());
                    }
                }

                @Override
                public void onFailureResponce(Object obj) {
                    Utility.hideLoadingDialog();
                    try {
                        Response<UniversitiesResponce> response = (Response<UniversitiesResponce>) obj;
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
            getAllUniversities = true;
            Utility.hideLoadingDialog();
            Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_university));
            ServerCall.getAllUniversities(getActivity(), new ResponceCallback() {
                @Override
                public void onSuccessResponce(Object obj) {
                    Utility.hideLoadingDialog();
                }

                @Override
                public void onFailureResponce(Object obj) {
                    Utility.hideLoadingDialog();
                }
            }, 1 + "");
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
