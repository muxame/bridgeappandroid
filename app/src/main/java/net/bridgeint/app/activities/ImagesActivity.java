package net.bridgeint.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import net.bridgeint.app.R;
import net.bridgeint.app.adapters.UniversityImagesAdapter;
import net.bridgeint.app.models.BannerModelForAdapter;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class ImagesActivity extends Activity {

    private ImageView ibBack;
    private ViewPager pager;
    private ArrayList<BannerModelForAdapter> banners;
    private int position = 0;
    private static int NUM_PAGES = 0;
    boolean isChat =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        initialize();

        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            ibBack.setRotation(180);
        }
    }

    private void initialize() {
//        logFirebaseEvent(EVENT_UNIVERSITY_MEDIA_DETAILS.replace(" ", "_").toLowerCase());
        ibBack = findViewById(R.id.ibBack);
        pager = findViewById(R.id.pager);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().hasExtra("position"))
            position = getIntent().getIntExtra("position", 0);
        if (getIntent().hasExtra("banners"))
            banners = getIntent().getParcelableArrayListExtra("banners");
        else
            banners = new ArrayList<>();
        if(getIntent().hasExtra("from")){
            isChat = true;
        }



        UniversityImagesAdapter bannerAdapter = new UniversityImagesAdapter(this, banners,isChat);
        pager.setAdapter(bannerAdapter);
        pager.setCurrentItem(position);
        CircleIndicator indicator = findViewById(R.id.indicator);
        NUM_PAGES = banners.size();

        indicator.setViewPager(pager);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (position == NUM_PAGES) {
                    position = 0;
                }
                pager.setCurrentItem(position++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
//        SpringDotsIndicator springDotsIndicator = (SpringDotsIndicator) findViewById(R.id.spring_dots_indicator);
//
//
//        springDotsIndicator.setViewPager(pager);
    }
}
