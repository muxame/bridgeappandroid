package net.bridgeint.app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.adapters.CountryAdapterUpdate;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.CountryResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Response;

import static net.bridgeint.app.utils.Constants.EVENT_COUNTRY_SELECT_SCREEN;

public class StepThirdCountrySelectionFragment extends BaseFragment {
    View view;
    @BindView(R.id.rvCountry)
    RecyclerView rvCountry;
    CountryAdapterUpdate mAdapter;
    Unbinder unbinder;
    @BindView(R.id.ic_backbtn)
    ImageView icBackbtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_third_country, container, false);
        unbinder = ButterKnife.bind(this, view);
        Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_country));
        ButterKnife.bind(this, view);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            icBackbtn.setRotation(180);
        }
        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_COUNTRY_SELECT_SCREEN.replace(" ", "_").toLowerCase());
        ServerCall.getCountries(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), ApplyModel.degreeId/*, ""*/, new ResponceCallback() {
            @Override
            public void onSuccessResponce(Object obj) {
                Utility.hideLoadingDialog();
                CountryResponce countryResponce = ((Response<CountryResponce>) obj).body();
                if (!countryResponce.getError()) {
                    SharedClass.countryModels = countryResponce.getCountryModels();
                    initAdapter();

                } else {

                    if (countryResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(getActivity());
                    } else {
                        Utility.showToast(getActivity(), countryResponce.getMessage());
                    }
                }
            }

            @Override
            public void onFailureResponce(Object obj) {
                Utility.hideLoadingDialog();
                try {
                    Response<CountryResponce> response = (Response<CountryResponce>) obj;
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

    private void initAdapter() {
       /* rvCountry.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new Country_Adapter(getContext(), SharedClass.countryModels);
        rvCountry.setAdapter(adapter);*/
        mAdapter = new CountryAdapterUpdate(getContext(), SharedClass.countryModels) {
            @Override
            protected void onItemClick(int position) {
                super.onItemClick(position);
                ApplyModel.countryId = SharedClass.countryModels.get(position).getId();
                replaceFragment(new StepFourFeesFragment(), "fees");
//                    ((DegreeActivity) getActivity()).onNextClick();
            }
        };
        rvCountry.setNestedScrollingEnabled(false);

        rvCountry.setAdapter(mAdapter);


        if (SharedClass.countryModels.size() == 1) {
            ApplyModel.countryId = SharedClass.countryModels.get(0).getId();
        }
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


    @OnClick(R.id.ic_backbtn)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }
}
