package net.bridgeint.app.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.UpdateResponse;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.RegisterResponse;
import net.bridgeint.app.utils.Config;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Response;

import static net.bridgeint.app.activities.BaseActivity.logFirebaseEvent;

public class ChangePasswordFragment extends BaseFragment implements ResponceCallback {

//    EditText etOldPassword, etNewPassword, etConfirmNewPassword;
    @BindView(R.id.backBtn)
    ImageView backBtn;
    @BindView(R.id.appbar)
    LinearLayout appbar;
    @BindView(R.id.loLogo)
    LinearLayout loLogo;
    @BindView(R.id.etOldPassword)
    EditText etOldPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etConfirmNewPassword)
    EditText etConfirmNewPassword;
    @BindView(R.id.btnSubmit)
    TextView btnSubmit;

    private ProgressDialog dialog;
    private View view;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);

//        InitEdiTextIds();
        dialog = new ProgressDialog(getActivity());
//        logEvent(EVENT_CHANGE_PASSWORD_SCREEN,getUserDetails());
        logFirebaseEvent(Constants.EVENT_CHANGE_PASSWORD_SCREEN.replace(" ","_").toLowerCase());
        view.findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                getActivity().onBackPressed();
            }
        });
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            backBtn.setRotation(180);
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isNetConnected(getActivity())) {
                    String oldPass = etOldPassword.getText().toString().trim();
                    String newPass = etNewPassword.getText().toString().trim();
                    String confirmNewPass = etConfirmNewPassword.getText().toString().trim();
                    if (oldPass.isEmpty()) {
                        Utility.showToast(getActivity(), getString(R.string.old_password_msg));
                    } else if (newPass.isEmpty()) {
                        Utility.showToast(getActivity(), getString(R.string.new_password_msg));
                    } else if (confirmNewPass.isEmpty()) {
                        Utility.showToast(getActivity(), getString(R.string.confirm_password_msg));
                    } else if (!newPass.equals(confirmNewPass)) {
                        Utility.showToast(getActivity(), getString(R.string.miss_matche_msg));
                    } else if (!Utility.getUserPassword(getActivity()).equals(oldPass)) {
                        Utility.showToast(getActivity(), getString(R.string.incorrect_msg));
                    } else {
                        HashMap<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put(Constants.OLD_PASSWORD, oldPass);
                        paramMap.put(Constants.NEW_PAWWROD, newPass);
                        paramMap.put(Constants.USER_ID, SharedClass.userModel.getId().toString());
                        paramMap.put(Constants.ACCESS_KEY, SharedClass.userModel.getAccessKey());
                        paramMap.put(Constants.API_KEY, Config.API_KEY);
                        ServerCall.updateProfile(getActivity(), dialog, getString(R.string.changingpassword), paramMap, ChangePasswordFragment.this);
                    }
                } else {
                    Utility.showToast(getActivity(), getString(R.string.no_internet));
                }


            }
        });
        return view;
    }


//    private void InitEdiTextIds() {
//        etOldPassword = view.findViewById(R.id.etOldPassword);
//        etNewPassword = view.findViewById(R.id.etNewPassword);
//        etConfirmNewPassword = view.findViewById(R.id.etConfirmNewPassword);
//    }

    @Override
    public void onSuccessResponce(Object obj) {

        UpdateResponse registerResponse = ((Response<UpdateResponse>) obj).body();
        try {
            Utility.dismissProgressDialog(dialog);
            if (!registerResponse.getError()) {
                Utility.setUserLogin(getActivity(), SharedClass.userModel.getEmail(), etNewPassword.getText().toString().trim());
                Utility.showToast(getActivity(), "Password Changed Successfuly");
                getActivity().onBackPressed();
//                finish();
            } else {
                if (registerResponse.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(getActivity());
                } else {
                    Utility.showToast(getActivity(), registerResponse.getMessage());
                }
            }
        } catch (Exception ex) {
            Utility.dismissProgressDialog(dialog);
        }
    }

    @Override
    public void onFailureResponce(Object obj) {
        Utility.dismissProgressDialog(dialog);
        try {
            Response<RegisterResponse> response = (Response<RegisterResponse>) obj;
            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                SharedClass.logout(getActivity());
            } else {
                ErrorUtils.responseError(getActivity(), response);
            }
        } catch (Exception exp) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        null.unbind();
    }
}
