package net.bridgeint.app.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by ufraj on 2/27/2017.
 */
public class FbLogin {
    boolean flag=false;
    private static MyClickListener myClickListener;

    public interface MyClickListener {
        void onItemClick(JSONObject response);
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        FbLogin.myClickListener = myClickListener;
    }

    public boolean fbLogin(final Context context,LoginButton loginButton,CallbackManager callbackManager)
    {

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_photos"));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                Log.i("LoginActivity",
                                        String.valueOf(response.getJSONObject()));

                                FacebookRequestError a =response.getError();

                                System.out.println("yuooo "+a);

                                try {
                                    String id = object.getString("id");
                                    try {

                                        LoginManager.getInstance().logOut();

                                        System.out.println("yuooooo ");
                                        if(response.getError()==null)
                                        myClickListener.onItemClick(response.getJSONObject());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                System.out.println("heyt cancel");

            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("heytt "+exception);

            }
        });

        return flag;
    }
}