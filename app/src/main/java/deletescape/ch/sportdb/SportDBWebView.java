package deletescape.ch.sportdb;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

public class SportDBWebView extends WebView {
    private OnScrollChangeListener listener;
    public SportDBWebView(Context context) {
        super(context);
    }

    public SportDBWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SportDBWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SportDBWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(listener != null){
            listener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    void setOnScrollChangeListener(OnScrollChangeListener listener){
        this.listener = listener;
    }
    interface OnScrollChangeListener{
        void onScrollChange(SportDBWebView v, int l, int t, int oldl, int oldt);
    }
}
