package org.baseballbaedal.baseballbaedal.BusinessMan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.baseballbaedal.baseballbaedal.BaseActivity;
import org.baseballbaedal.baseballbaedal.R;

public class AddressWebViewActivity extends BaseActivity {

    private WebView browser;

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {
//            Bundle extra = new Bundle();
            Intent intent = new Intent();
//            extra.putString("data", data);
            intent.putExtra("data",data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_web_view);

        browser = (WebView) findViewById(R.id.webView);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                browser.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        //browser.loadUrl("file:///android_asset/daum.html");
        //browser.loadUrl("http://www.daddyface.com/public/daum.html");
        browser.loadUrl("http://cdn.rawgit.com/jolly73-df/DaumPostcodeExample/master/DaumPostcodeExample/app/src/main/assets/daum.html");
    }
}
