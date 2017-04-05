package deletescape.ch.sportdb;

import android.graphics.Bitmap;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SportDBWebViewClient extends WebViewClient {
    private OnTitleListener onTitleListener;
    private OnSubTitleListener onSubTitleListener;
    private OnActivityListener onActivityListener;

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (!url.startsWith("https://www.sportdb.ch/extranet/start.do") && !url.startsWith("https://www.sportdb.ch/extranet/mobile/mobileAwk.do") && !url.startsWith("https://www.sportdb.ch/extranet/j_security_check") && !url.startsWith("https://www.sportdb.ch/extranet/benutzer/benutzerStart.do?ButtonLogout=true")) {
            view.loadUrl("https://www.sportdb.ch/extranet/mobile/mobileAwk.do");
        }
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        super.onPageFinished(view, url);
        view.evaluateJavascript("$('.ui-header').hide()", null);
        view.evaluateJavascript("var element = document.getElementsByClassName('main')[0]; element.parentNode.removeChild(element);", null);
        if (url.endsWith("#activity")) {
            ValueCallback<String> titleCallback = new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    if(onTitleListener != null){
                        onTitleListener.onTitle(trimOneChar(value));
                    }
                }
            };
            ValueCallback<String> subtitleCallback = new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    if(onSubTitleListener != null){
                        onSubTitleListener.onSubTitle(trimOneChar(value));
                    }
                }
            };
            view.evaluateJavascript("$('#activity-title').text()", titleCallback);
            view.evaluateJavascript("$('#activity-subtitle').text()", subtitleCallback);
            onActivityListener.onActivity();
        } else{
            if(onTitleListener != null){
                onTitleListener.onTitle(view.getTitle());
            }
            if(onSubTitleListener != null){
                onSubTitleListener.onSubTitle("");
            }
        }
        applyStyle(view);
    }
    private String trimOneChar(String string){
        return string.substring(1, string.length() - 1);
    }
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    private void applyStyle(WebView view){
        view.evaluateJavascript("document.getElementsByTagName('body')[0].style.height = 'auto'", null);
        view.evaluateJavascript("$('*').css('font-family','sans-serif')", null);
        view.evaluateJavascript("$('*').css('text-shadow','none')", null);
    }

    public void setOnTitleListener(OnTitleListener listener) {
        onTitleListener = listener;
    }
    public void setOnSubTitleListener(OnSubTitleListener listener) {
        onSubTitleListener = listener;
    }
    public void setOnActivityListener(OnActivityListener listener){
        onActivityListener = listener;
    }

    interface OnTitleListener {
        void onTitle(String title);
    }
    interface OnSubTitleListener {
        void onSubTitle(String subtitle);
    }
    interface OnActivityListener {
        void onActivity();
    }
}
