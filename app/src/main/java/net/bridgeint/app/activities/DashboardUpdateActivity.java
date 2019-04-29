package net.bridgeint.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.squareup.picasso.Picasso;

import net.bridgeint.app.R;
import net.bridgeint.app.application.Application;
import net.bridgeint.app.fragments.AboutUsFragment;
import net.bridgeint.app.fragments.ChangePasswordFragment;
import net.bridgeint.app.fragments.ChatFragment;
import net.bridgeint.app.fragments.CheckStatusFragment;
import net.bridgeint.app.fragments.ContactUsFragment;
import net.bridgeint.app.fragments.SearchFragment;
import net.bridgeint.app.fragments.StepOneLevelsOfstudy;
import net.bridgeint.app.fragments.ViewProfilefragment;
import net.bridgeint.app.interfaces.FeeBusInterface;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.interfaces.ServiceBusInterface;
import net.bridgeint.app.interfaces.UpdateBusInterface;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.LocationProviderHelper;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

import static net.bridgeint.app.utils.Constants.EVENT_DASHBOARD;
import static net.bridgeint.app.utils.Constants.EVENT_LOGOUT;
import static net.bridgeint.app.utils.Constants.PARAM_USER;

public class DashboardUpdateActivity extends BaseActivity {

    //    @BindView(R.id.ll_logout)
    LinearLayout llLogout;

    FrameLayout llFrameHelp;
    ImageView ivpopup;
    //
    private Context context;
    private TabLayout tabLayout;
    private ImageView mDrawerbutton;
    private ImageView mOpenDrawerbutton;
    private DrawerLayout mDrawerlayout;
    //DrawerItems
    private LinearLayout mViewProfile;
    private LinearLayout ll_logout;
    private LinearLayout mSearch;
    private LinearLayout mAboutUs;
    private LinearLayout mHelpLayout;
    private LinearLayout mChangePAssword;
    private LinearLayout mChangeLaguage;
    private LinearLayout mContactUs;
    int currentPosition = 0;
    boolean doubleBackToExitPressedOnce = false;
    private StepOneLevelsOfstudy stepOneLevelsOfstudy;
    private ViewProfilefragment viewProfilefragment;
    private ChangePasswordFragment changePasswordFragment;
    private AboutUsFragment aboutUsFragment;
    private ContactUsFragment contactUsFragment;
    private SearchFragment searchFragment;
    private CheckStatusFragment checkStatusFragment;
    private ChatFragment chatFragment;
    private LiveActivity liveActivity;
    private ChatActivity chatActivity;
    public static TextView toolbarText;
    private LinearLayout chatView;
    private TextView chatTitle;
    private TextView tvUsername;
    private CircleImageView adminPic, userPic;
    private ImageView adminStatusImage, ivProfilePic, icBackButton;

    private FragmentManager fragmentManager;
    private Handler mTypingHandler = new Handler();
    String imageUrl = null;
    private boolean from_direct;
    private int value = 0;
    private boolean isInFront;
    private String resourcePath;
    public String shopper_id = "hyperpay://com.devicebee.tryapply/id";
    LocationProviderHelper providerHelper;
    public boolean isOpenFreeFragment = false;
    private ProgressDialog dialog;
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            /*  DashboardActivity.this.recreate();*/
            Utility.changeStatusBarColor(this, R.color.dashboard_color);
            setContentView(R.layout.activity_main_update);
            ServerCall.updateDeviceToken(Utility.getDeviceToken(this));
            initialize();
            setupTabs();
//            logEvent(EVENT_LOGIN, bundle);
            logFirebaseEvent(EVENT_DASHBOARD.replace(" ", "_").toLowerCase());
            InflateDefaultFragment();
            if (getIntent().getBooleanExtra("notification", false)) {
                replaceChatFragment();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
//                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
//        );
//        providerHelper = new LocationProviderHelper(DashboardUpdateActivity.this, new LocationListener()
//            @Override
//            public void onLocationChanged(Location location) {
//
//            }
//        });
//        TedPermission.with(this)
//                .setPermissionListener(permissionlistener)
//                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
//                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
//                .check();
    }

    private void initialize() {
        context = DashboardUpdateActivity.this;
        fragmentManager = getSupportFragmentManager();
        tabLayout = findViewById(R.id.tabs);
        llLogout = findViewById(R.id.ll_logout);
        llFrameHelp = findViewById(R.id.ll_frame_help);
        ivpopup = findViewById(R.id.iv_popup);
        mDrawerbutton = findViewById(R.id.iv_drawermenu);
        mOpenDrawerbutton = findViewById(R.id.iv_open_drawer);
        mDrawerlayout = findViewById(R.id.drawer_layout);
        mViewProfile = findViewById(R.id.ll_view_profile);
        icBackButton = findViewById(R.id.ic_backbtn);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            icBackButton.setRotation(180);
        }
        mSearch = findViewById(R.id.ll_search);
        ivProfilePic = findViewById(R.id.iv_profile_pic);
        tvUsername = findViewById(R.id.tv_user_name);
        mChangePAssword = findViewById(R.id.ll_change_password);
        mChangeLaguage = findViewById(R.id.ll_change_langauge);
        mAboutUs = findViewById(R.id.ll_about_us);
        mHelpLayout = findViewById(R.id.ll_help);
        mContactUs = findViewById(R.id.ll_contact_us);
        if (getIntent().hasExtra("from")) {
            if (getIntent().getExtras().getString("from").equalsIgnoreCase("signup")) {
              //  startActivity(new Intent(DashboardUpdateActivity.this, HelpActivity.class));
                startActivity(new Intent(DashboardUpdateActivity.this, HelpActivity.class));
            }
        }
        mOpenDrawerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerlayout.closeDrawer(Gravity.END);
            }
        });
        icBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mChangeLaguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerlayout.closeDrawer(Gravity.END);
                Utility.showLanguagePopup(DashboardUpdateActivity.this, "test", new ResponceCallback() {
                    @Override
                    public void onSuccessResponce(Object obj) {
                        Utility.hideConfirmDialog();
                    }

                    @Override
                    public void onFailureResponce(Object obj) {
                        Utility.hideConfirmDialog();
                    }
                }, DashboardUpdateActivity.this);
            }
        });
        mHelpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerlayout.closeDrawer(Gravity.END);
                //startActivity(new Intent(DashboardUpdateActivity.this, HelpActivity.class));
                startActivity(new Intent(DashboardUpdateActivity.this, HelpActivity.class));
            }
        });
        mChangePAssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordFragment = new ChangePasswordFragment();
                replaceFragment(changePasswordFragment, "changePassword");
                mDrawerlayout.closeDrawer(Gravity.END);
            }
        });
        mViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfilefragment = new ViewProfilefragment();
                replaceFragment(viewProfilefragment, "viewprofile");
                mDrawerlayout.closeDrawer(Gravity.END);
            }
        });
        mContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUsFragment = new ContactUsFragment();
                replaceFragment(contactUsFragment, "contactUs");
                mDrawerlayout.closeDrawer(Gravity.END);
            }
        });
        mAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutUsFragment = new AboutUsFragment();
                replaceFragment(aboutUsFragment, "aboutUs");
                mDrawerlayout.closeDrawer(Gravity.END);
            }
        });
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment = new SearchFragment();
                replaceFragment(searchFragment, "search");
                mDrawerlayout.closeDrawer(Gravity.END);
            }
        });
        mDrawerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER, SharedClass.userModel.getFirstName() + " " + SharedClass.userModel.getLastName());
        logEvent(EVENT_DASHBOARD, bundle);
//        logFirebaseEvent("milind_screen");
//
        if (!SharedClass.userModel.getFbId().equalsIgnoreCase("")) {
            mChangePAssword.setVisibility(View.GONE);
        } else {
            mChangePAssword.setVisibility(View.VISIBLE);
        }

        tvUsername.setText(SharedClass.userModel.getFirstName() + " " + SharedClass.userModel.getLastName());
        Picasso.with(this).load(Constants.IMAGE_URL + SharedClass.userModel.getImage()).into(ivProfilePic);
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerlayout.closeDrawer(Gravity.END);
                Utility.showConfirmDialog(DashboardUpdateActivity.this, getString(R.string.logout_text), new ResponceCallback() {
                    @Override
                    public void onSuccessResponce(Object obj) {
                        logout();
                    }

                    @Override
                    public void onFailureResponce(Object obj) {
                        Utility.hideConfirmDialog();
                    }
                });
            }
        });
    }

    public void openDrawer() {
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            mDrawerlayout.openDrawer(Gravity.LEFT);
        } else {
            mDrawerlayout.openDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        menu.getItem(0).setVisible(false);
        return true;
    }

    private void logout() {
        ServerCall.logout(DashboardUpdateActivity.this, dialog, "Logging out", new ResponceCallback() {
            @Override
            public void onSuccessResponce(Object obj) {
                GenericResponce genericResponce = ((Response<GenericResponce>) obj).body();
                try {
                    if (!genericResponce.getError()) {
                        BaseActivity baseActivity = DashboardUpdateActivity.this;
                        logEvent(EVENT_LOGOUT);
                        logFirebaseEvent(EVENT_LOGOUT.replace(" ", "_").toLowerCase());
                        SharedClass.ClearAllData();
                        Utility.logout(DashboardUpdateActivity.this);
                        Utility.setUsingFirstTime(DashboardUpdateActivity.this);
                        startActivity(new Intent(DashboardUpdateActivity.this, SigninActivity.class));
                        finish();
                        //Utility.showToast(getActivity(), genericResponce.getMessage());
                        Utility.hideLoadingDialog();
                    } else {
                        if (genericResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                            SharedClass.logout(DashboardUpdateActivity.this);
                        } else {
                            Utility.showToast(DashboardUpdateActivity.this, genericResponce.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Utility.hideLoadingDialog();
                }
            }

            @Override
            public void onFailureResponce(Object obj) {
                Utility.hideLoadingDialog();
                try {
                    Response<GenericResponce> response = (Response<GenericResponce>) obj;
                    if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(DashboardUpdateActivity.this);
                    } else {
                        ErrorUtils.responseError(DashboardUpdateActivity.this, response);
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miFilter:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home_selector).setText(getString(R.string.home)));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.check_status_selector).setText(getString(R.string.search)));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.live_selector).setText(getString(R.string.services)));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.chat_selector).setText(getString(R.string.profile)));

        LinearLayout tabOne = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tvtabOne = tabOne.findViewById(R.id.tab);
        ImageView ivtabOne = tabOne.findViewById(R.id.iv_tab_image);
        ivtabOne.setImageResource(R.drawable.home_selector);
        tvtabOne.setText(getString(R.string.home));
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_selector, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tvtabTwo = tabTwo.findViewById(R.id.tab);
        ImageView ivtabTwo = tabTwo.findViewById(R.id.iv_tab_image);
        ivtabTwo.setImageResource(R.drawable.check_status_selector);
        tvtabTwo.setText(getString(R.string.check_status));

        tabLayout.getTabAt(1).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tvtabThree = tabThree.findViewById(R.id.tab);
        ImageView ivtabThree = tabThree.findViewById(R.id.iv_tab_image);
        ivtabThree.setImageResource(R.drawable.live_selector);
        tvtabThree.setText(getString(R.string.live));

//        tabThree.setText(getString(R.string.live));
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.live_selector, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        LinearLayout tabFour = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tvtabFour = tabFour.findViewById(R.id.tab);
        ImageView ivtabFour = tabFour.findViewById(R.id.iv_tab_image);
        ivtabFour.setImageResource(R.drawable.chat_selector);
        tvtabFour.setText(getString(R.string.chat));

        tabLayout.getTabAt(3).setCustomView(tabFour);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (Utility.isNetConnected(DashboardUpdateActivity.this)) {
                    replaceFragment(tabLayout.getSelectedTabPosition());
                    currentPosition = tabLayout.getSelectedTabPosition();
                } else {
                    Utility.showNetworkDailogSingle(DashboardUpdateActivity.this, Constants.NET_CONNECTION_LOST, new ResponceCallback() {
                        @Override
                        public void onSuccessResponce(Object obj) {
                            Process.killProcess(Process.myPid());
                            System.exit(1);
                        }

                        @Override
                        public void onFailureResponce(Object obj) {

                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (Utility.isNetConnected(DashboardUpdateActivity.this)) {
                    replaceFragment(tabLayout.getSelectedTabPosition());
                    currentPosition = tabLayout.getSelectedTabPosition();
                } else {
                    Utility.showNetworkDailogSingle(DashboardUpdateActivity.this, Constants.NET_CONNECTION_LOST, new ResponceCallback() {
                        @Override
                        public void onSuccessResponce(Object obj) {
                            Process.killProcess(Process.myPid());
                            System.exit(1);
                        }

                        @Override
                        public void onFailureResponce(Object obj) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }

    private void replaceFragment(int position) {
        if (position == 0) {
            Log.e("clear", "clearing");
            if (ApplyModel.universityModel == null)
                ApplyModel.clearAll();
            stepOneLevelsOfstudy = new StepOneLevelsOfstudy();
            clearBackStack();
            clearfragmentBackstack();

            replaceFragment(stepOneLevelsOfstudy, "dashboard");

//            chatView.setVisibility(View.GONE);
//            userPic.setVisibility(View.GONE);
            position = 0;
        } else if (position == 1) {
            if (isInFront)
                SharedClass.isApply = false;
            checkStatusFragment = new CheckStatusFragment();
            clearBackStack();
            clearfragmentBackstack();
            replaceFragment(checkStatusFragment, "search");

//            chatView.setVisibility(View.GONE);
//            userPic.setVisibility(View.GONE);
            position = 1;
        } else if (position == 2) {
            ApplyModel.clearAll();
            liveActivity = new LiveActivity();
            clearBackStack();
            clearfragmentBackstack();
            replaceFragment(liveActivity, "Live");
            position = 2;
        } else if (position == 3) {
            ApplyModel.clearAll();
            chatActivity = new ChatActivity();
            clearBackStack();
            clearfragmentBackstack();
            replaceFragment(chatActivity, "Chat");
            Application.setIsBackUnable(false);
//            chatView.setVisibility(View.GONE);
//            userPic.setVisibility(View.VISIBLE);
            position = 3;
        }

    }

    public void backbuttonHide() {
        icBackButton.setVisibility(View.GONE);
    }

    public void backButtonshow() {
        icBackButton.setVisibility(View.VISIBLE);
    }

    public void openFreeFragment() {
        isOpenFreeFragment = true;
        tabLayout.getTabAt(2).select();

//        currentPosition = 2;
    }

    public void openFragment(final Fragment fragment) {
        Log.d("fragment", "Count: " + fragmentManager.getBackStackEntryCount());
        /*clearBackStack();*/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (fragmentManager.getBackStackEntryCount() > 1 && !from_direct) {
                    fragmentManager.popBackStack();
                }
                if (from_direct) {
                    from_direct = false;
                    if (fragment instanceof StepOneLevelsOfstudy) {
                        transaction.replace(R.id.container, fragment);
                    } else {
                        transaction.add(R.id.container, fragment);
                    }
                    transaction.commitAllowingStateLoss();
                    value = 1;
                } else {
                    value = 0;
                    if (fragment instanceof StepOneLevelsOfstudy) {
                        transaction.replace(R.id.container, fragment);
                    } else {
                        transaction.add(R.id.container, fragment);
                    }
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    }

    public void replaceFragment(Fragment fragment, String transactionTag) {
        if (fragment instanceof StepOneLevelsOfstudy) {
            backbuttonHide();
        } else {
            backButtonshow();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();

//        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        transaction.addToBackStack(transactionTag)
                .replace(R.id.container, fragment)
                .commit();


//        updateToolbar(transactionTag);
    }

    public boolean clearfragmentBackstack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if ((data != null && data.hasExtra("data")) || SharedClass.openImagePicker) {
                SharedClass.openImagePicker = false;

            } else {
                if (requestCode == Constants.MOVE_TO_APPLY) {
                   /* if (ApplyModel.universityModel != null) {
                        if (tabLayout != null && tabLayout.getSelectedTabPosition() == 0 && dashboardFragment != null) {
                            dashboardFragment.MoveToApply(DashboardUpdateActivity.this);
                        } else if (tabLayout != null && tabLayout.getSelectedTabPosition() == 1) {
                            Intent intent = new Intent(DashboardUpdateActivity.this, DegreeActivity.class);
                            startActivityForResult(intent, Constants.MOVE_TO_RESULT);
                        }
                    }*/
                }
//                else {
////                    if (ApplyModel.universityModel == null) {
////                        from_direct = true;
////                        tabLayout.getTabAt(1).select();
////                    } else {
//                        /*Intent intent = new Intent(context, DocumentsActivity.class);
//                        startActivity(intent);*/
////                    }
//                }
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            if (intent.getScheme() != null && intent.getScheme().equals("hyperpay")) {
                shopper_id = intent.getData().getQueryParameter("id");
                Log.e("shopperId", shopper_id);
                if (Constants.eventFocus.equalsIgnoreCase("serviceApplication")) {
                    EventBus.getDefault().post(new ServiceBusInterface(shopper_id));
                } else if (Constants.eventFocus.equalsIgnoreCase("updateApplication")) {
                    EventBus.getDefault().post(new UpdateBusInterface(shopper_id));
                } else if (Constants.eventFocus.equalsIgnoreCase("feeApplication")) {
                    EventBus.getDefault().post(new FeeBusInterface(shopper_id));
                }

            }

            if(intent != null && intent.getExtras() != null && intent.getExtras().containsKey("notification")){
                if (intent.getExtras().getBoolean("notification", false)) {
                    replaceFragment(3);
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isInFront = true;
        if (tabLayout != null) {
            if (tabLayout.getSelectedTabPosition() != 2 && tabLayout.getSelectedTabPosition() != 3) {
                if (tabLayout.getSelectedTabPosition() == 1) {

                } else if (tabLayout.getSelectedTabPosition() != 0 && ApplyModel.degreeId == null) {
                    Log.e("Selecting", "Slct");
                }
            }
            //            else if (tabLayout.getSelectedTabPosition() == 3) {
            ////                if (imageUrl != null && !imageUrl.equals(Constants.IMAGE_URL + SharedClass.userModel.getImage())) {
            ////                    Picasso.with(context).invalidate(imageUrl);
            ////                    imageUrl = Constants.IMAGE_URL + SharedClass.userModel.getImage();
            ////                    Picasso.with(context).load(imageUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.user_placeholder).resize(Constants.LOW_WIDTH, Constants.LOW_HEIGHT).into(userPic);
            ////                } else {
            ////                    imageUrl = Constants.IMAGE_URL + SharedClass.userModel.getImage();
            ////                    Picasso.with(context).load(imageUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.user_placeholder).resize(Constants.LOW_WIDTH, Constants.LOW_HEIGHT).into(userPic);
            //                }
            //
            //                //replaceFragment(0);
            //                //tabLayout.getTabAt(0).select();
            //            }
        }
    }

    @Override
    public void onBackPressed() {

        if (currentPosition > 0 && fragmentManager.getBackStackEntryCount() == 1) {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            replaceFragment(tabLayout.getSelectedTabPosition());
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
            if (fragmentManager.getBackStackEntryCount() == 1) {
                if (value == 0) {
                    if (doubleBackToExitPressedOnce) {
                        super.onBackPressed();
                        finishAffinity();
                        return;
                    }
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, R.string.exit_confirmation, Toast.LENGTH_SHORT).show();
                } else {
                    value = 0;
                    tabLayout.getTabAt(0).select();
                }
            } else {

                Log.d("fragment", "set Default_  Count:" + fragmentManager.getBackStackEntryCount() + " value: " + value);
                super.onBackPressed();
            }
        }

    }


    private void InflateDefaultFragment() {
        position = 0;
        if (fragmentManager.getBackStackEntryCount() < 1) {
            stepOneLevelsOfstudy = new StepOneLevelsOfstudy();
            Log.d("fragment", "set Default_  Count:" + fragmentManager.getBackStackEntryCount());
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, stepOneLevelsOfstudy)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void updateTitleInChat(final String msg) {
        try {
            mTypingHandler.removeCallbacks(onTypingTimeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTypingHandler.postDelayed(onTypingTimeout, 1000);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatTitle.setText(msg);
            }
        });
    }

    public boolean clearBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatTitle.setText("");
                }
            });
        }
    };

/*
    public void openApplyActivity() {
        if (ApplyModel.universityModel != null) {
            if (tabLayout != null && tabLayout.getSelectedTabPosition() == 0 && dashboardFragment != null) {
                dashboardFragment.MoveToApply(DashboardUpdateActivity.this);
            } else if (tabLayout != null && tabLayout.getSelectedTabPosition() == 1) {
                Intent intent = new Intent(DashboardUpdateActivity.this, DegreeActivity.class);
                startActivityForResult(intent, Constants.MOVE_TO_RESULT);
            }
        }
    }
*/

    public void updateAdminStatus(int status) {
        if (status == Constants.ADMIN_ONLINE) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    adminStatusImage.setImageResource(R.drawable.online_circle);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    adminStatusImage.setImageResource(R.drawable.stayaway_circle);
                }
            });
        }
    }

    public static int position = 0;

    private void replaceChatFragment() {
        position = 5;
        ApplyModel.clearAll();
//        chatFragment = new ChatFragment();
//        openFragment(chatFragment);

        chatActivity = new ChatActivity();
        openFragment(chatActivity);

       // tabLayout.getTabAt(3).select();
        toolbarText.setVisibility(View.GONE);
        chatView.setVisibility(View.VISIBLE);
        userPic.setVisibility(View.GONE);
    }

    public void openchatfragment() {
        tabLayout.getTabAt(3).select();
    }

    public void showHelpPopup() {

        if (llFrameHelp != null && llFrameHelp.getVisibility() != View.VISIBLE) {
            if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
                ivpopup.setImageResource(R.drawable.ic_popup_ar);
            } else {
                ivpopup.setImageResource(R.drawable.ic_popup);
            }
            llFrameHelp.setVisibility(View.VISIBLE);
            ivpopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideHelpPopup();
                }
            });
        }
    }

    public void hideHelpPopup() {
        if (llFrameHelp != null && llFrameHelp.getVisibility() == View.VISIBLE) {
            llFrameHelp.setVisibility(View.GONE);
        }
    }
}
