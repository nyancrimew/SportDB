package deletescape.ch.sportdb

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.webkit.WebView

class MainActivity : AppCompatActivity() {
    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        webView = findViewById(R.id.webview)
        val client = SportDBWebViewClient()

        client.onActivityListener = { setDisplayHomeAsUp(true) }
        client.onTitleListener = { title -> toolbar.title = title }
        client.onSubTitleListener = { subtitle -> toolbar.subtitle = subtitle }

        webView!!.webViewClient = client
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.domStorageEnabled = true
        webView!!.loadUrl("https://www.sportdb.ch/extranet/mobile/mobileAwk.do")

        val prefs = getPreferences(Context.MODE_PRIVATE)
        if (!prefs.getBoolean("disclaimer_shown", false) || BuildConfig.DEBUG) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.disclaimer_title)
                    .setMessage(R.string.disclaimer_body)
                    .setPositiveButton(R.string.ok, null)
                    .show()
            prefs.edit().putBoolean("disclaimer_shown", true).apply()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (webView!!.canGoBack()) {
            webView!!.goBack()
            setDisplayHomeAsUp(false)
            return true
        }
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            if (webView!!.url.endsWith("#activity")) {
                setDisplayHomeAsUp(false)
            }
            webView!!.goBack()
        } else {
            finish()
        }
    }

    private fun setDisplayHomeAsUp(enabled: Boolean) {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(enabled)
    }
}
