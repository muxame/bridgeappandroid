package net.bridgeint.app.services;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import net.bridgeint.app.utils.Utility;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Utility.addTokenToSharedPreference(this,refreshedToken);
        //ServerCall.updateDeviceToken(Utility.getDeviceToken(this));
        
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server 
        //Not required for current project 
    }
}