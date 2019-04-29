package net.bridgeint.app.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.bridgeint.app.dialogs.NotificationDialog;
import net.bridgeint.app.fragments.ChatFragment;
import net.bridgeint.app.utils.SharedClass;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.bridgeint.app.utils.Constants;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by DeviceBee on 8/15/2017.
 */

public class BaseActivity extends AppCompatActivity {

    static AppEventsLogger logger;
    static FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger=AppEventsLogger.newLogger(this);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("myFunction"));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(getVisibleFragment() instanceof ChatFragment) {
            }else {
                NotificationDialog dialog = new NotificationDialog();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                dialog.show(fragmentManager, "");
                dialog.setOnItemClickListener(new NotificationDialog.MyClickListener() {
                    @Override
                    public void gotoChat() {
                        Intent intent1 = new Intent(BaseActivity.this, DashboardUpdateActivity.class);
                        intent1.putExtra("notification", true);
                        startActivity(intent1);
                        finishAffinity();
                    }
                });
            }
            //alert data here
        }
    };

    public Fragment getVisibleFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public static void logEvent(String eventName, Bundle param) {
//        if (logger != null) {
//            logger.logEvent(eventName, param);
//        }
    }
    public static void logEvent(String eventName) {
//        if (logger != null) {
//            logger.logEvent(eventName);
//        }
    }

    public static void logFirebaseEvent(String eventName){
        mFirebaseAnalytics.logEvent(eventName, getUserDetails());
        Log.e("===FirebaseAnalytics", "logFirebaseEvent: "+eventName);
    }

   /* public static void firebaseEvent(){
        event();
    }
*/

    public static Bundle getUserDetails(){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_USER, SharedClass.userModel.getFirstName() + " " + SharedClass.userModel.getLastName());
        bundle.putString(Constants.PARAM_EMAIL,SharedClass.userModel.getEmail());
        return bundle;
    }

  /*  public static void event(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "xyz");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "text");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }*/
}
