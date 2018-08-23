package deletescape.ch.sportdb

import android.graphics.Bitmap
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class SportDBWebViewClient : WebViewClient() {
    var onTitleListener: (String) -> Unit = {}
    var onSubTitleListener: (String) -> Unit = {}
    var onActivityListener: () -> Unit = {}

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        if (!url.startsWith("https://www.sportdb.ch/extranet/start.do") && !url.startsWith("https://www.sportdb.ch/extranet/mobile/mobileAwk.do") && !url.startsWith("https://www.sportdb.ch/extranet/j_security_check") && !url.startsWith("https://www.sportdb.ch/extranet/benutzer/benutzerStart.do?ButtonLogout=true") && !url.startsWith("https://www.sportdb.ch/extranet/public/passwordLost.do?")) {
            if (url == "https://www.sportdb.ch/extranet/public/passwordLost.do") {
                Toast.makeText(view.context, R.string.password_reset_success, Toast.LENGTH_LONG).show()
            }
            view.loadUrl("https://www.sportdb.ch/extranet/mobile/mobileAwk.do")
        }
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        view.evaluateJavascript("$('.ui-header').hide()", null)
        view.evaluateJavascript("var element = document.getElementsByClassName('main')[0]; element.parentNode.removeChild(element);", null)
        if (url.endsWith("#activity")) {
            val titleCallback = ValueCallback<String> { value ->
                    onTitleListener.invoke(trimOneChar(value))
            }
            val subtitleCallback = ValueCallback<String> { value ->
                    onSubTitleListener.invoke(trimOneChar(value))
            }
            view.evaluateJavascript("$('#activity-title').text()", titleCallback)
            view.evaluateJavascript("$('#activity-subtitle').text()", subtitleCallback)
            onActivityListener.invoke()
        } else {
                onTitleListener.invoke(view.title)
                onSubTitleListener.invoke("")
        }
        applyStyle(view)
    }

    private fun trimOneChar(string: String): String {
        return string.substring(1, string.length - 1)
    }

    private fun applyStyle(view: WebView) {
        loadNativeDroid(view)
        view.evaluateJavascript("document.getElementsByTagName('body')[0].style.height = 'auto'", null)
        view.evaluateJavascript("document.styleSheets[0].disabled = true", null)
        view.evaluateJavascript("document.styleSheets[3].disabled = true", null)
    }

    private fun loadNativeDroid(view: WebView) {
        val ref = "https://cdn.rawgit.com/deletescape/nativeDroid2/8000fb4b/css/nativedroid2.css"
        val loadCSS = "var fileref=document.createElement('link')\n" +
                "fileref.setAttribute('rel', 'stylesheet')\n" +
                "fileref.setAttribute('type', 'text/css')\n" +
                "fileref.setAttribute('href', '$ref')\n" +
                "document.getElementsByTagName('head')[0].appendChild(fileref)"
        view.evaluateJavascript(loadCSS, null)
    }
}
