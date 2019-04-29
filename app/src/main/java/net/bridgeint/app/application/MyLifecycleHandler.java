package net.bridgeint.app.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
//    private int resumed;
//    private int paused;
    private int started;
    private int stopped;

    private static final String TAG = "MyLifecycleHandler";

    private boolean isOnline;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        onActivityCreatedA(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        onActivityResumedA(activity);
//        ++resumed;
        if (isOnline != started > stopped) {
            isOnline = !isOnline;
            if (isOnline) {
                onAppVisible();
                onAppVisible(activity);
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        ++paused;
//        android.util.Log.w(TAG, "application is in foreground: " + (resumed > paused));
//        android.util.Log.w(TAG, "application is visible: " + (started > stopped));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        if (isOnline != started > stopped) {
            isOnline = !isOnline;
            if (!isOnline) {
                onAppHidden();
                onAppHidden(activity);
            }
        }
    }

    protected void onAppVisible() {

    }

    protected void onAppHidden() {

    }

    protected void onAppVisible(Activity activity) {

    }

    protected void onAppHidden(Activity activity) {

    }

    protected void onActivityCreatedA(Activity activity) {

    }

    protected void onActivityResumedA(Activity activity) {

    }


    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:
    /*
    // Replace the four variables above with these four
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }
    */
}