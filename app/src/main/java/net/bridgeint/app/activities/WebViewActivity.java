package net.bridgeint.app.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    WebView webView;
    TextView header;
    String url;
    @BindView(R.id.backBtn)
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            backBtn.setRotation(180);
        }
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());
        header = findViewById(R.id.header);
        if (getIntent().hasExtra(Constants.WEB_LINK)) {
            header.setText(getString(R.string.term_condition));
            url = getIntent().getStringExtra(Constants.WEB_LINK);
        } else if (getIntent().hasExtra(Constants.FB_LINK)) {
            header.setText("Facebook");
            url = getIntent().getStringExtra(Constants.FB_LINK);
        } else if (getIntent().hasExtra(Constants.TWITTER_LINK)) {
            header.setText("Twitter");
            url = getIntent().getStringExtra(Constants.TWITTER_LINK);
        } else if (getIntent().hasExtra(Constants.INSTAGRAM_LINK)) {
            header.setText("Instagram");
            url = getIntent().getStringExtra(Constants.INSTAGRAM_LINK);
        }
        try {
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(url);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
