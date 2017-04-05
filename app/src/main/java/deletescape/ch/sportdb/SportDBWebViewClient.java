package deletescape.ch.sportdb;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SportDBWebViewClient extends WebViewClient {
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if(!url.startsWith("https://www.sportdb.ch/extranet/start.do")&&!url.startsWith("https://www.sportdb.ch/extranet/mobile/mobileAwk.do")&&!url.startsWith("https://www.sportdb.ch/extranet/j_security_check")&&!url.startsWith("https://www.sportdb.ch/extranet/benutzer/benutzerStart.do?ButtonLogout=true")){
            view.loadUrl("https://www.sportdb.ch/extranet/mobile/mobileAwk.do");
        }
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        super.onPageFinished(view, url);
        view.evaluateJavascript("$('.ui-btn-logout').hide()", null);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }
}
