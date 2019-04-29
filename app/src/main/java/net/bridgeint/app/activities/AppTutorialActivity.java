package net.bridgeint.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;

import net.bridgeint.app.R;
import net.bridgeint.app.adapters.SlidingImage_Adapter;
import net.bridgeint.app.views.XTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class AppTutorialActivity extends BaseActivity {

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.one_tut, R.drawable.two_tut,R.drawable.four_tut};
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.tv_next)
    XTextView tvNext;
    @BindView(R.id.tv_skip)
    XTextView tvSkip;
    @BindView(R.id.tv_previous)
    XTextView tvPrevious;

    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_screen);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);


        SlidingImage_Adapter adapter = new SlidingImage_Adapter(AppTutorialActivity.this, ImagesArray){
            @Override
            protected void onItemClick(int position) {
                super.onItemClick(position);
//                if (currentPage == NUM_PAGES) {
//                    startActivity(new Intent(AppTutorialActivity.this, SigninActivity.class));
//                    finish();
//                }
//                pager.setCurrentItem(currentPage++, true);
            }
        };
        pager.setAdapter(adapter);


        indicator.setViewPager(pager);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppTutorialActivity.this, SigninActivity.class));
                finish();
            }
        });

//Set circle indicator radius

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                pager.setCurrentItem(currentPage++, true);
            }
        };
        tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage == 0) {
                    tvPrevious.setVisibility(View.GONE);
                }
                pager.setCurrentItem(currentPage--, true);
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage == NUM_PAGES) {
                    startActivity(new Intent(AppTutorialActivity.this, SigninActivity.class));
                    finish();
                }
                pager.setCurrentItem(currentPage++, true);
            }
        });
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (currentPage == 2) {
                    tvNext.setVisibility(View.VISIBLE);
                    tvNext.setText(getString(R.string.done));
                } else if (currentPage > 0) {
                    tvPrevious.setVisibility(View.VISIBLE);
                    tvNext.setText(getString(R.string.next_btn));
                } else if(currentPage<1){
                    tvPrevious.setVisibility(View.GONE);
                    tvNext.setText(getString(R.string.next_btn));
                }
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}
