package deletescape.ch.sportdb;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        webView = findViewById(R.id.webview);
        SportDBWebViewClient client = new SportDBWebViewClient();

        client.setOnActivityListener(new SportDBWebViewClient.OnActivityListener() {
            @Override
            public void onActivity() {
                setDisplayHomeAsUp(true);
            }
        });
        client.setOnTitleListener(new SportDBWebViewClient.OnTitleListener() {
            @Override
            public void onTitle(String title) {
                toolbar.setTitle(title);
            }
        });
        client.setOnSubTitleListener(new SportDBWebViewClient.OnSubTitleListener() {
            @Override
            public void onSubTitle(String subtitle) {
                toolbar.setSubtitle(subtitle);
            }
        });

        webView.setWebViewClient(client);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://www.sportdb.ch/extranet/mobile/mobileAwk.do");

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if(!prefs.getBoolean("disclaimer_shown", false) || BuildConfig.DEBUG){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.disclaimer_title)
                    .setMessage(R.string.disclaimer_body)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            prefs.edit().putBoolean("disclaimer_show", true).apply();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (webView.canGoBack()) {
            webView.goBack();
            setDisplayHomeAsUp(false);
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                if (webView.getUrl().endsWith("#activity")) {
                    setDisplayHomeAsUp(false);
                }
                webView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setDisplayHomeAsUp(boolean enabled) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(enabled);
        }
    }
}
