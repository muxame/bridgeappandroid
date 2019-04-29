package net.bridgeint.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import net.bridgeint.app.R;
import net.bridgeint.app.adapters.MyPagerAdapter;
import net.bridgeint.app.views.NonSwipeableViewPager;
import net.bridgeint.app.views.XTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

import static net.bridgeint.app.utils.Constants.EVENT_HELP;

public class HelpActivity extends BaseActivity {

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.one, R.drawable.two, R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six};
    @BindView(R.id.pager)
    NonSwipeableViewPager pager;
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
        setContentView(R.layout.activity_help_screen);
        ButterKnife.bind(this);
        init();
        logFirebaseEvent(EVENT_HELP.replace(" ", "_").toLowerCase());
    }

    private void init() {

//        logFirebaseEvent(EVENT_HELP.replace(" ", "_").toLowerCase());
//        for (int i = 0; i < IMAGES.length; i++)
//            ImagesArray.add(IMAGES[i]);


//        SlidingImage_Adapter adapter = new SlidingImage_Adapter(HelpActivity.this, ImagesArray){
//            @Override
//            protected void onItemClick(int position) {
//                super.onItemClick(position);
//                if (currentPage == NUM_PAGES) {
//                    finish();
//                    onBackPressed();
//
//                }
//                pager.setCurrentItem(currentPage++, true);
//            }
//        };
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);


        indicator.setViewPager(pager);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

//Set circle indicator radius

        NUM_PAGES = IMAGES.length;
        currentPage = 1;
        // Auto start of viewpager
        /*final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                pager.setCurrentItem(currentPage++, true);
            }
        };*/
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
                    startActivity(new Intent(HelpActivity.this, SigninActivity.class));
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
               // currentPage = position;
//                if (currentPage == 2) {
//                    tvNext.setVisibility(View.VISIBLE);
//                    tvNext.setText(getString(R.string.done));
//                } else if (currentPage > 0) {
//                    tvPrevious.setVisibility(View.VISIBLE);
//                    tvNext.setText(getString(R.string.next_btn));
//                } else if(currentPage<1){
//                    tvPrevious.setVisibility(View.GONE);
//                    tvNext.setText(getString(R.string.next_btn));
//                }
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
    public void stepNext(){
        if (currentPage == NUM_PAGES) {
//            startActivity(new Intent(HelpActivity.this, SigninActivity.class));
            finish();
        }
        Log.d("Page >> " , "Page : " + currentPage);
        pager.setCurrentItem(currentPage++, true);

    }
}
