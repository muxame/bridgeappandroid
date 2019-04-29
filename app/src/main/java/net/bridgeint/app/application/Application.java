package net.bridgeint.app.application;


import android.app.Activity;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import net.bridgeint.app.sockets.SocketManager;
import net.bridgeint.app.utils.Config;
import net.bridgeint.app.utils.SharedClass;

import java.lang.ref.WeakReference;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ufraj on 1/25/2017.
 */
public class Application extends android.app.Application {
    private String authority;
    private SocketManager socketManager;
    private static boolean isShowChat = false;
    private static boolean isBackUnable = false;
    private static Application mAppInstance;


    private static Boolean isAppOpen = false;
    private int started;
    private int stopped;

    private WeakReference<Activity> mActivity = null;
   public static int mSelection = 0;
    public  static int MaxSelection = 5;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppInstance = this;
        socketManager = new SocketManager(this, null);
        Fabric.with(this, new Crashlytics());
        if (SharedClass.isDevelopmentMode) {
            Config.app_base_url = "http://188.226.178.195/tryAndApply/";
        } else {
            /*Config.app_base_url = "http://dvice.be/tryAndApply/";*/
            Config.app_base_url = "http://188.226.178.195/tryAndApply/";
        }
//        Config.retrofit_base_url = Config.app_base_url + "api/";
//        Config.retrofit_base_url = Config.app_base_url + "api2/";
        Config.retrofit_base_url = Config.app_base_url + "api3/";

      /*  CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/refsanbi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);


        /*registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mActivity = new WeakReference<Activity>(activity);
                isAppOpen = true;
            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }

            *//** Unused implementation **//*
            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                mActivity = new WeakReference<Activity>(activity);


            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                isAppOpen = false;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }


        });*/

        registerActivityLifecycleCallbacks(new MyLifecycleHandler(){
            @Override
            protected void onAppHidden() {
                super.onAppHidden();

            }

            @Override
            protected void onAppVisible() {
                super.onAppVisible();

            }

            @Override
            protected void onAppVisible(Activity activity) {
                super.onAppVisible(activity);
                mActivity = new WeakReference<Activity>(activity);
                isAppOpen = true;
            }

            @Override
            protected void onAppHidden(Activity activity) {
                super.onAppHidden(activity);
                isAppOpen = false;
            }

            @Override
            protected void onActivityCreatedA(Activity activity) {
                super.onActivityCreatedA(activity);
                mActivity = new WeakReference<Activity>(activity);
            }

            @Override
            protected void onActivityResumedA(Activity activity) {
                super.onActivityResumedA(activity);
                mActivity = new WeakReference<Activity>(activity);
            }
        });

    }




    public Application() {

    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public static void setShowChat(boolean isShowChat) {
        Application.isShowChat = isShowChat;
    }

    public static boolean isIsBackUnable() {
        return isBackUnable;
    }

    public static void setIsBackUnable(boolean isBackUnable) {
        Application.isBackUnable = isBackUnable;
    }

    public static boolean isShowChat() {
        return isShowChat;
    }

    public static Application getAppInstance() {
        return mAppInstance;
    }

    public Activity getCurrentActivity() {
        return mActivity.get();
    }

    public static boolean getIsAppOpen(){
        return isAppOpen;
    }

}
