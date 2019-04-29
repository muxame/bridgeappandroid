package net.bridgeint.app.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.activities.DashboardUpdateActivity;
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
import butterknife.Unbinder;
import retrofit2.Response;

import static net.bridgeint.app.utils.Constants.EVENT_CONTACT_US_SCREEN;

public class ContactUsFragment extends BaseFragment {

    View view;
    @BindView(R.id.backBtn)
    ImageView backBtn;
    @BindView(R.id.appbar)
    LinearLayout appbar;
    @BindView(R.id.firstName)
    EditText firstName;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.edt_your_msg)
    EditText edtYourMsg;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    Unbinder unbinder;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        unbinder = ButterKnife.bind(this, view);
        dialog = new ProgressDialog(getActivity());
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            backBtn.setRotation(180);
        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callContactUsApi();
            }
        });
        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_CONTACT_US_SCREEN.replace(" ", "_").toLowerCase());
        return view;
    }

    private void callContactUsApi() {
        if (isValid()) {
            ServerCall.contactUsCall(getActivity(), dialog, firstName.getText().toString(), email.getText().toString(), edtYourMsg.getText().toString(), new ResponceCallback() {
                @Override
                public void onSuccessResponce(Object obj) {
                    Utility.hideConfirmDialog();
                    GenericResponce registerResponse = ((Response<GenericResponce>) obj).body();
                    try {
                        Utility.dismissProgressDialog(dialog);
                        if (!registerResponse.getError()) {
                            Utility.showConfirmDialogSingle(getActivity(), getResources().getString(R.string.thanks_msg), new ResponceCallback() {
                                @Override
                                public void onSuccessResponce(Object obj) {
                                    startActivity(new Intent(getActivity(), DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                }

                                @Override
                                public void onFailureResponce(Object obj) {

                                }
                            });

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
                    Utility.hideConfirmDialog();
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

    }

    private boolean isValid() {
        if (firstName.getText().toString().equalsIgnoreCase("")) {
            Utility.showToast(getActivity(), getString(R.string.validation_first_name_contact_us));
            return false;
        }
        if (!TextValidation.isValidEmail(email.getText().toString())) {
            Utility.showToast(getActivity(), getString(R.string.validation_empty_email_address));
            return false;
        }
        if (!TextValidation.isValidtText(edtYourMsg.getText().toString())) {
            Utility.showToast(getActivity(), getString(R.string.validation_message));
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
