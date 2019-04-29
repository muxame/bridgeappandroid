package net.bridgeint.app.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import net.bridgeint.app.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class UniDashboardActivity extends BaseActivity {

    private Toolbar toolbar;
    private Context context;
    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_uni_dashboard);
            initialize();
            setupToolbar();
            //Utility.changeStatusBarColor(this,  R.color.colorPrimary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        toolbar = findViewById(R.id.toolbar);
        calendarView = findViewById(R.id.calendarView);

        context = UniDashboardActivity.this;
        java.util.Calendar cal = java.util.Calendar.getInstance();

        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE) + 1))
                //.setMaximumDate(CalendarDay.from(2016, 5, 12))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);

        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Antic-Regular.ttf");

        TextView tv = toolbar.findViewById(R.id.toolbar_title);
        tv.setTypeface(face);
        tv.setText(getString(R.string.live));
        tv.setTextSize(20f);

    }
}
