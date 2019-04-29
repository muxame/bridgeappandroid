package net.bridgeint.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import net.bridgeint.app.R;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.RegisterResponse;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.FbLogin;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button btnFbLogin;
    private CallbackManager callbackManager;
    private LoginButton btnFb;
    private ProgressDialog dialog;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            initialize();
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setCustomTabs();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initialize() {
        scrollView = findViewById(R.id.scrollView);
        callbackManager = CallbackManager.Factory.create();
        btnFb = findViewById(R.id.btnFb);
        btnFbLogin = findViewById(R.id.btnFbLogin);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        context = MainActivity.this;
        dialog = new ProgressDialog(this);
        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FbLogin fbLogin = new FbLogin();
                fbLogin.fbLogin(context, btnFb, callbackManager);

                fbLogin.setOnItemClickListener(new FbLogin.MyClickListener() {
                    @Override
                    public void onItemClick(JSONObject response) {
                        try {
                            String id = response.getString("id");
                            String first_name = response.getString("first_name");
                            String last_name = response.getString("last_name");
                            Log.e("names", first_name + "-" + last_name);
                            String email = response.getString("email");
                            URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                            fbId = id;
                            firstName = first_name;
                            lastName = last_name;
                            ServerCall.signUpFb(id, MainActivity.this, dialog, email, first_name, last_name, "Student", Utility.getDeviceToken(MainActivity.this), rcp);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        btnFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFb.performClick();
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        /*adapter.addFragment(new SignUpFragment(), getString(R.string.sign_up));
        adapter.addFragment(new SignInFragment(), getString(R.string.login));*/
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void setCustomTabs() {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Antic-Regular.ttf");

        TextView SignUpTab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        SignUpTab.setText(getString(R.string.sign_up));
        SignUpTab.setTypeface(face);
        tabLayout.getTabAt(0).setCustomView(SignUpTab);
        TextView LoginTab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        LoginTab.setText(getString(R.string.login));
        LoginTab.setTypeface(face);
        tabLayout.getTabAt(1).setCustomView(LoginTab);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    ScrollView scrollView;
    String fbId, firstName, lastName;
    ResponceCallback rcp = new ResponceCallback() {
        @Override
        public void onSuccessResponce(Object obj) {
            RegisterResponse registerResponse = ((Response<RegisterResponse>) obj).body();
            try {
                if (!registerResponse.getError()) {
                    SharedClass.userModel = registerResponse.getUserModel();
                    Log.e("User Model", new Gson().toJson(registerResponse.getUserModel()));
                    Utility.setUserLoginFb(context, fbId, firstName, lastName);
                    //context.startActivity(new Intent(context, DashboardActivity.class));
                    if (registerResponse.getUserModel().getIsPhoneVerified()) {
                        context.startActivity(new Intent(context, DashboardUpdateActivity.class));
                        finish();
                    } else {
                        context.startActivity(new Intent(context, VerificationOneActivity.class));
                        finish();
                    }
                } else {
                    Utility.showToast(context, registerResponse.getMessage());
                }

                Utility.dismissProgressDialog(dialog);
            } catch (Exception ex) {
                Utility.dismissProgressDialog(dialog);

            }
        }

        @Override
        public void onFailureResponce(Object obj) {
            try {
                Utility.dismissProgressDialog(dialog);
                Response<RegisterResponse> response = (Response<RegisterResponse>) obj;
                if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(MainActivity.this);
                } else {
                    ErrorUtils.responseError(MainActivity.this, response);
                }
            } catch (Exception exp) {
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (tabLayout.getSelectedTabPosition() == 2) {
            Utility.hideKeyPad(this);
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
