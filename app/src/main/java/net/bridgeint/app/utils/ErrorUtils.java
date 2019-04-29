package net.bridgeint.app.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by laptop on 12/06/2017.
 */

public class ErrorUtils {

    public static void responseError(Activity activity, Response response)
    {
        int code=response.code();
        switch (code)
        {
            case 404:
                Toast.makeText(activity, "API Not found", Toast.LENGTH_SHORT).show();
                break;
            case 500:
                Toast.makeText(activity, "Server is broken", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(activity, "Server Response Error", Toast.LENGTH_SHORT).show();
                break;
        }
        try{
            Log.e("retrofit_error",response.errorBody().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
