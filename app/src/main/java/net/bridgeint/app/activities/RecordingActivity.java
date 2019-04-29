package net.bridgeint.app.activities;

import android.content.Context;
import android.os.Bundle;

import net.bridgeint.app.R;

public class RecordingActivity extends BaseActivity {


    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_recording);
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initialize() {
        context = RecordingActivity.this;

    }
}
