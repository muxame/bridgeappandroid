package net.bridgeint.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.bridgeint.app.R;
import net.bridgeint.app.adapters.SpinnerAdapter;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;
import net.mukesh.countrypicker.CountryPicker;
import net.mukesh.countrypicker.CountryPickerListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class VerificationOneActivity extends BaseActivity implements View.OnClickListener, ResponceCallback {

    @BindView(R.id.backBtn)
    ImageView backBtn;
    @BindView(R.id.appbar)
    LinearLayout appbar;
    @BindView(R.id.spCodes)
    Spinner spCodes;
    @BindView(R.id.flag)
    ImageView flag;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.codePicker)
    LinearLayout codePicker;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.btnSendCode)
    Button btnSendCode;
    @BindView(R.id.etOne)
    EditText etOne;
    @BindView(R.id.etTwo)
    EditText etTwo;
    @BindView(R.id.etThree)
    EditText etThree;
    @BindView(R.id.etFour)
    EditText etFour;
    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.ll_start)
    LinearLayout llStart;
    private Context context;

    private ProgressDialog dialog;
    private String phone;
    private String Varifycode;
    ArrayList<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_verification_one);
            ButterKnife.bind(this);
            initialize();
            findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            etListeners();
            logFirebaseEvent(Constants.EVENT_VERIFICATION.replace(" ", "_").toLowerCase());
            llStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utility.isNetConnected(VerificationOneActivity.this)) {

                        Utility.hideKeyPad(VerificationOneActivity.this);

                        if (etOne.getText().toString().trim().isEmpty() ||
                                etTwo.getText().toString().trim().isEmpty() ||
                                etThree.getText().toString().trim().isEmpty() ||
                                etFour.getText().toString().trim().isEmpty()) {
                            Toast.makeText(context, context.getResources().getString(R.string.please_enter_code), Toast.LENGTH_SHORT).show();
                        } else {
                            Varifycode = etOne.getText().toString().trim() + etTwo.getText().toString().trim() + etThree.getText().toString().trim() + etFour.getText().toString().trim();
                            ServerCall.sendVerificationCode(VerificationOneActivity.this, dialog, Varifycode, new ResponceCallback() {
                                @Override
                                public void onSuccessResponce(Object obj) {
                                    GenericResponce verify_responce = ((Response<GenericResponce>) obj).body();
                                    try {
                                        Utility.dismissProgressDialog(dialog);
                                        if (!verify_responce.getError()) {
                                            SharedClass.userModel.setIsPhoneVerified(true);
                                            SharedClass.userModel.setPhone(phone);
                                            Utility.goToHome(context,"signup");
                                            Utility.showToast(context, verify_responce.getMessage());



                                        } else {
                                            if (verify_responce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                                                SharedClass.logout(VerificationOneActivity.this);
                                            } else {
                                                Utility.showToast(context, verify_responce.getMessage());
                                            }
                                        }

                                    } catch (Exception ex) {
                                        Utility.dismissProgressDialog(dialog);

                                    }
                                }

                                @Override
                                public void onFailureResponce(Object obj) {
                                    try {
                                        Utility.dismissProgressDialog(dialog);
                                        Response<GenericResponce> response = (Response<GenericResponce>) obj;
                                        if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                                            SharedClass.logout(VerificationOneActivity.this);
                                        } else {
                                            ErrorUtils.responseError(VerificationOneActivity.this, response);
                                        }
                                    } catch (Exception exp) {
                                    }
                                }
                            });
                        }
                    } else {
                        Utility.showNetworkDailogSingle(VerificationOneActivity.this, Constants.NET_CONNECTION_LOST, new ResponceCallback() {
                            @Override
                            public void onSuccessResponce(Object obj) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }

                            @Override
                            public void onFailureResponce(Object obj) {

                            }
                        });
                    }
                }
            });
            if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
                backBtn.setRotation(180);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void etListeners() {
        etOne.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    etTwo.requestFocus();
                    // etTwo.setText("");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etTwo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    etThree.requestFocus();
                    // etThree.setText("");
                } else if (s.length() == 0) {
                    etOne.requestFocus();

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etThree.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    etFour.requestFocus();
                    // etFour.setText("");
                } else if (s.length() == 0) {
                    etTwo.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etFour.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    etThree.requestFocus();
                }
                else if (s.length() == 1){
                    if (Utility.isNetConnected(VerificationOneActivity.this)) {

                        Utility.hideKeyPad(VerificationOneActivity.this);

                        if (etOne.getText().toString().trim().isEmpty() ||
                                etTwo.getText().toString().trim().isEmpty() ||
                                etThree.getText().toString().trim().isEmpty() ||
                                etFour.getText().toString().trim().isEmpty()) {
                            Toast.makeText(context, context.getResources().getString(R.string.please_enter_code), Toast.LENGTH_SHORT).show();
                        } else {
                            Varifycode = etOne.getText().toString().trim() + etTwo.getText().toString().trim() + etThree.getText().toString().trim() + etFour.getText().toString().trim();
                            ServerCall.sendVerificationCode(VerificationOneActivity.this, dialog, Varifycode, new ResponceCallback() {
                                @Override
                                public void onSuccessResponce(Object obj) {
                                    GenericResponce verify_responce = ((Response<GenericResponce>) obj).body();
                                    try {
                                        Utility.dismissProgressDialog(dialog);
                                        if (!verify_responce.getError()) {
                                            SharedClass.userModel.setIsPhoneVerified(true);
                                            SharedClass.userModel.setPhone(phone);
                                            Utility.goToHome(context,"signup");
                                            Utility.showToast(context, verify_responce.getMessage());

                                        } else {
                                            if (verify_responce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                                                SharedClass.logout(VerificationOneActivity.this);
                                            } else {
                                                Utility.showToast(context, verify_responce.getMessage());
                                            }
                                        }

                                    } catch (Exception ex) {
                                        Utility.dismissProgressDialog(dialog);

                                    }
                                }

                                @Override
                                public void onFailureResponce(Object obj) {
                                    try {
                                        Utility.dismissProgressDialog(dialog);
                                        Response<GenericResponce> response = (Response<GenericResponce>) obj;
                                        if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                                            SharedClass.logout(VerificationOneActivity.this);
                                        } else {
                                            ErrorUtils.responseError(VerificationOneActivity.this, response);
                                        }
                                    } catch (Exception exp) {
                                    }
                                }
                            });
                        }
                    } else {
                        Utility.showNetworkDailogSingle(VerificationOneActivity.this, Constants.NET_CONNECTION_LOST, new ResponceCallback() {
                            @Override
                            public void onSuccessResponce(Object obj) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }

                            @Override
                            public void onFailureResponce(Object obj) {

                            }
                        });
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etFour.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL && etFour.getText().length() == 0) {
                    etThree.requestFocus();
                }
                return false;
            }
        });
        etThree.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL && etThree.getText().length() == 0) {
                    etTwo.requestFocus();
                }
                return false;
            }
        });
        //15
        etTwo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL && etTwo.getText().length() == 0) {
                    etOne.requestFocus();
                }
                return false;
            }
        });


//        etOne.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus || etOne.getText().length() > 0) {
//                    etOne.setBackgroundResource(R.drawable.ic_txt_box);
//                } else {
//                    etOne.setBackgroundResource(R.drawable.ic_txt_box);
//
//                }
//            }
//        });
//
//        etTwo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus || etTwo.getText().length() > 0) {
//                    etTwo.setBackgroundResource(R.drawable.ic_txt_box);
//                } else {
//                    etTwo.setBackgroundResource(R.drawable.ic_txt_box);
//
//                }
//            }
//        });
//
//
//        etThree.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus || etThree.getText().length() > 0) {
//                    etThree.setBackgroundResource(R.drawable.ic_txt_box);
//                } else {
//                    etThree.setBackgroundResource(R.drawable.ic_txt_box);
//
//                }
//            }
//        });
//
//
//        etFour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus || etFour.getText().length() > 0) {
//                    etFour.setBackgroundResource(R.drawable.ic_txt_box);
//                } else {
//                    etFour.setBackgroundResource(R.drawable.ic_txt_box);
//
//                }
//            }
//        });
    }

    private void initialize() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        btnSendCode = findViewById(R.id.btnSendCode);
        sv = findViewById(R.id.sv);
        spCodes = findViewById(R.id.spCodes);
        etPhone = findViewById(R.id.etPhone);
        dialog = new ProgressDialog(this);
        context = VerificationOneActivity.this;
        btnSendCode.setOnClickListener(this);

        list = new ArrayList<>();
        list.add(getString(R.string.uae_code));
        list.add(getString(R.string.algeria_code));
        list.add(getString(R.string.bahrain_code));
        list.add(getString(R.string.egypt_code));
        list.add(getString(R.string.iran_code));
        list.add(getString(R.string.iraq_code));
        list.add(getString(R.string.israeli_code));
        list.add(getString(R.string.jordan_code));
        list.add(getString(R.string.kuwait_code));
        list.add(getString(R.string.lebanon_code));
        list.add(getString(R.string.morocco_code));
        list.add(getString(R.string.oman_code));
        list.add(getString(R.string.qatar_code));
        list.add(getString(R.string.saudiarabia_code));
        list.add(getString(R.string.tunisia_code));

        ArrayList<Integer> listImages = new ArrayList<>();
        listImages.add(R.drawable.flag_uae);
        listImages.add(R.drawable.algeria_flag);
        listImages.add(R.drawable.bahrain_flag);
        listImages.add(R.drawable.egypt_flag);
        listImages.add(R.drawable.iran_flag);
        listImages.add(R.drawable.iraq_flag);
        listImages.add(R.drawable.israeli_flag);
        listImages.add(R.drawable.jordan_flag);
        listImages.add(R.drawable.kuwait_flag);
        listImages.add(R.drawable.lebanon_flag);
        listImages.add(R.drawable.morocco_flag);
        listImages.add(R.drawable.oman_flag);
        listImages.add(R.drawable.qatar_flag);
        listImages.add(R.drawable.saudiarabia_flag);
        listImages.add(R.drawable.tunisia_flag);

        SpinnerAdapter adapter = new SpinnerAdapter(this,
                R.layout.item_codes, R.id.txt, list, listImages) {
        };
        /*spCodes.setAdapter(adapter);
        spCodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        findViewById(R.id.codePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        // Implement your code here
                        picker.dismiss();
                        ((TextView) findViewById(R.id.code)).setText(dialCode);
                        ((ImageView) findViewById(R.id.flag)).setImageResource(flagDrawableResID);
                    }
                });
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });


        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(View.FOCUS_DOWN);
            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPhone.getText().toString().matches("^0")) {
                    // Not allowed
                    etPhone.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        logEvent(Constants.EVENT_PHONE_NUMBER_SCREEN);
        logFirebaseEvent(Constants.EVENT_PHONE_NUMBER_SCREEN.replace(" ", "_").toLowerCase());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendCode:
                phone = etPhone.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show();
                } else {

                    if (Utility.isNetConnected(VerificationOneActivity.this)) {
                        String code = ((TextView) findViewById(R.id.code)).getText().toString();
                        ServerCall.sendNumberToGetCode(VerificationOneActivity.this, dialog, code + phone, this);
                    } else {
                        Utility.showNetworkDailogSingle(VerificationOneActivity.this, Constants.NET_CONNECTION_LOST, new ResponceCallback() {
                            @Override
                            public void onSuccessResponce(Object obj) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }

                            @Override
                            public void onFailureResponce(Object obj) {

                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccessResponce(Object obj) {
        GenericResponce get_pic_code_responce = ((Response<GenericResponce>) obj).body();
        try {
            if (!get_pic_code_responce.getError()) {
//                Intent intent = new Intent(context, VerificationTwoActivity.class);
//                intent.putExtra("phone", list.get(spCodes.getSelectedItemPosition()) + phone);
//                startActivity(intent);
                Utility.showToast(context, get_pic_code_responce.getMessage());

            } else {
                if (get_pic_code_responce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(VerificationOneActivity.this);
                } else {
                    Utility.showToast(context, get_pic_code_responce.getMessage());
                }
            }

            Utility.dismissProgressDialog(dialog);
            Utility.hideKeyPad(VerificationOneActivity.this);
            etOne.requestFocus();
        } catch (Exception ex) {
            Utility.dismissProgressDialog(dialog);

        }
    }

    @Override
    public void onFailureResponce(Object obj) {
        try {
            Utility.dismissProgressDialog(dialog);
            Response<GenericResponce> response = (Response<GenericResponce>) obj;
            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                SharedClass.logout(VerificationOneActivity.this);
            } else {
                ErrorUtils.responseError(VerificationOneActivity.this, response);
            }
        } catch (Exception exp) {
        }
    }
}
