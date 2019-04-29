package net.bridgeint.app.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import net.bridgeint.app.R;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.TextValidation;
import net.bridgeint.app.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ForgetPasswordActivity extends BaseActivity implements ResponceCallback {

    EditText email;
    @BindView(R.id.backBtn)
    ImageView backBtn;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(ForgetPasswordActivity.this);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        email = findViewById(R.id.etEmail);
        logEvent(Constants.EVENT_FORGOT_PASSWORD);
        logFirebaseEvent(Constants.EVENT_FORGOT_PASSWORD.replace(" ", "_").toLowerCase());
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

                if (email.getText().toString().isEmpty()) {
                    Utility.showToast(ForgetPasswordActivity.this, getString(R.string.validation_empty_email_address));
                } else if (!TextValidation.isValidEmail(email.getText().toString().trim())) {
                    Utility.showToast(ForgetPasswordActivity.this, getString(R.string.validation_valid_email_address));
                } else {
                    ServerCall.ForgetPassword(ForgetPasswordActivity.this, dialog, email.getText().toString().trim(), ForgetPasswordActivity.this);
                }
            }
        });
    }

    @Override
    public void onSuccessResponce(Object obj) {
        GenericResponce registerResponse = ((Response<GenericResponce>) obj).body();
        try {
            Utility.showToast(ForgetPasswordActivity.this, registerResponse.getMessage());
            if (!registerResponse.getError()) {

                Intent intent = new Intent();
                intent.putExtra(Constants.EMAIL, email.getText().toString());
                setResult(Activity.RESULT_OK, intent);

                finish();
            } else {
                if (registerResponse.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(ForgetPasswordActivity.this);
                } else {
                    Utility.showToast(ForgetPasswordActivity.this, registerResponse.getMessage());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFailureResponce(Object obj) {
        try {
            Response<GenericResponce> response = (Response<GenericResponce>) obj;
            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                SharedClass.logout(ForgetPasswordActivity.this);
            } else {
                ErrorUtils.responseError(ForgetPasswordActivity.this, response);
            }
        } catch (Exception exp) {
        }
    }
}
