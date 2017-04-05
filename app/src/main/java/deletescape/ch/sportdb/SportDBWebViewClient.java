package deletescape.ch.sportdb;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SportDBWebViewClient extends WebViewClient {
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if(!url.startsWith("https://www.sportdb.ch/extranet/start.do")&&!url.startsWith("https://www.sportdb.ch/extranet/mobile/mobileAwk.do")&&!url.startsWith("https://www.sportdb.ch/extranet/j_security_check")&&!url.startsWith("https://www.sportdb.ch/extranet/benutzer/benutzerStart.do?ButtonLogout=true")){
            view.loadUrl("https://www.sportdb.ch/extranet/mobile/mobileAwk.do");
        } else{
            Toast.makeText(view.getContext(), url, Toast.LENGTH_LONG).show();
        }
    }
}
