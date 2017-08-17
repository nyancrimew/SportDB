package deletescape.ch.sportdb;

import android.graphics.Bitmap;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SportDBWebViewClient extends WebViewClient implements SportDBWebView.OnScrollChangeListener{
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
        ((SportDBWebView) view).setOnScrollChangeListener(this);
    }
    private String trimOneChar(String string){
        return string.substring(1, string.length() - 1);
    }
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    private void applyStyle(WebView view){
        loadNativeDroid(view);
        view.evaluateJavascript("document.getElementsByTagName('body')[0].style.height = 'auto'", null);
        view.evaluateJavascript("document.styleSheets[0].addRule('.ui-checkbox-off::after', 'background: none !important', 1)", null);
        view.evaluateJavascript("document.styleSheets[0].addRule('.ui-checkbox-on::after', 'background: none !important', 1)", null);
        applyOtherStyles(view);
    }

    private void applyOtherStyles(WebView view) {
        view.evaluateJavascript("$('#endOfList').remove()", null);
        applyGroupIcons(view);
    }

    private void applyGroupIcons(WebView view) {
        view.evaluateJavascript("$('.ui-icon-group').css('background-image', 'url(https://storage.googleapis.com/material-icons/external-assets/v4/icons/svg/ic_people_black_18px.svg)')", null);
        view.evaluateJavascript("$('.ui-icon-group').css('background-size', 'contain')", null);
        view.evaluateJavascript("$('.ui-icon-group').css('background-repeat', 'no-repeat')", null);
        view.evaluateJavascript("$('.ui-icon-group').css('width', '20px')", null);
        view.evaluateJavascript("$('.ui-icon-group').css('height', '20px')", null);
        view.evaluateJavascript("$('.ui-icon-group').css('margin', '6px 10px 0 0')", null);
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

    @Override
    public void onScrollChange(SportDBWebView v, int l, int t, int oldl, int oldt) {
        if(t > oldt){
            applyOtherStyles(v);
        }
    }

    public void loadNativeDroid(WebView view){
        String loadCSS = "var fileref=document.createElement('link')\n" +
                "fileref.setAttribute('rel', 'stylesheet')\n" +
                "fileref.setAttribute('type', 'text/css')\n" +
                "fileref.setAttribute('href', %s)\n" +
                "document.getElementsByTagName('head')[0].appendChild(fileref)";
        view.evaluateJavascript(String.format(loadCSS, "'https://cdn.rawgit.com/wildhaber/nativeDroid2/1c2bb6ec/css/nativedroid2.css'"), null);
        view.evaluateJavascript(String.format(loadCSS, "'https://cdn.rawgit.com/wildhaber/nativeDroid2/1c2bb6ec/css/nativedroid2.color.green.css'"), null);
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
