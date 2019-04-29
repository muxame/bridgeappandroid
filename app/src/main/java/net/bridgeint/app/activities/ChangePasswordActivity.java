package net.bridgeint.app.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity implements ResponceCallback {

    EditText etOldPassword, etNewPassword, etConfirmNewPassword;
    @BindView(R.id.backBtn)
    ImageView backBtn;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        InitEdiTextIds();
        dialog = new ProgressDialog(ChangePasswordActivity.this);
        logEvent(Constants.EVENT_CHANGE_PASSWORD_SCREEN,getUserDetails());
        logFirebaseEvent(Constants.EVENT_CHANGE_PASSWORD_SCREEN.replace(" ","_").toLowerCase());
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            backBtn.setRotation(180);
        }
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isNetConnected(ChangePasswordActivity.this)) {
                    String oldPass = etOldPassword.getText().toString().trim();
                    String newPass = etNewPassword.getText().toString().trim();
                    String confirmNewPass = etConfirmNewPassword.getText().toString().trim();
                    if (oldPass.isEmpty()) {
                        Utility.showToast(ChangePasswordActivity.this, getString(R.string.old_password_msg));
                    } else if (newPass.isEmpty()) {
                        Utility.showToast(ChangePasswordActivity.this, getString(R.string.new_password_msg));
                    } else if (confirmNewPass.isEmpty()) {
                        Utility.showToast(ChangePasswordActivity.this, getString(R.string.confirm_password_msg));
                    } else if (!newPass.equals(confirmNewPass)) {
                        Utility.showToast(ChangePasswordActivity.this, getString(R.string.miss_matche_msg));
                    } else if (!Utility.getUserPassword(ChangePasswordActivity.this).equals(oldPass)) {
                        Utility.showToast(ChangePasswordActivity.this, getString(R.string.incorrect_msg));
                    } else {
                        HashMap<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put(Constants.OLD_PASSWORD, oldPass);
                        paramMap.put(Constants.NEW_PAWWROD, newPass);
                        paramMap.put(Constants.USER_ID, SharedClass.userModel.getId().toString());
                        paramMap.put(Constants.ACCESS_KEY, SharedClass.userModel.getAccessKey());
                        paramMap.put(Constants.API_KEY, Config.API_KEY);
                        ServerCall.updateProfile(ChangePasswordActivity.this, dialog, getString(R.string.changingpassword), paramMap, ChangePasswordActivity.this);
                    }
                } else {
                    Utility.showToast(ChangePasswordActivity.this, getString(R.string.no_internet));
                }


            }
        });
    }

    private void InitEdiTextIds() {
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
    }

    @Override
    public void onSuccessResponce(Object obj) {

        UpdateResponse registerResponse = ((Response<UpdateResponse>) obj).body();
        try {
            Utility.dismissProgressDialog(dialog);
            if (!registerResponse.getError()) {
                Utility.setUserLogin(ChangePasswordActivity.this, SharedClass.userModel.getEmail(), etNewPassword.getText().toString().trim());
                Utility.showToast(ChangePasswordActivity.this, "Password Changed Successfuly");
                finish();
            } else {
                if (registerResponse.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(ChangePasswordActivity.this);
                } else {
                    Utility.showToast(ChangePasswordActivity.this, registerResponse.getMessage());
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
                SharedClass.logout(ChangePasswordActivity.this);
            } else {
                ErrorUtils.responseError(ChangePasswordActivity.this, response);
            }
        } catch (Exception exp) {
        }
    }
}
