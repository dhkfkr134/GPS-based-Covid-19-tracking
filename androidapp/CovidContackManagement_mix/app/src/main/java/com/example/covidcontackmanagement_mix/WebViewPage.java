package com.example.covidcontackmanagement_mix;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WebViewPage extends AppCompatActivity {
    String code="";
    String ar[];
    private String access_token;
    private String refresh_token;
    private WebView webView;
    private String userID;
    private String strHostOrKakao = null;
    private void getAccess_token(String code){
        try{
            OkHttpClient client=new OkHttpClient();
            Thread.sleep(1000);
            String url = "http://115.21.52.248:8080/"+strHostOrKakao+"/token?code="+code;
            System.out.println("webview1 "+code);
            Request.Builder builder=new Request.Builder().url(url).get();
            Request request= builder.build();
            Response response= client.newCall(request).execute();
            if(response.isSuccessful()){
                ResponseBody body=response.body();
                String ar_token=body.string();
                if(ar_token.equals("failed")){
                    do {
                        Thread.sleep(1000);
                        response=client.newCall(request).execute();
                        body=response.body();
                        ar_token=body.string();
                    } while(!ar_token.equals("failed"));
                }
                System.out.println("webview2 : "+ar_token);
                ar=ar_token.split("/");
                userID=ar[0];
                access_token=ar[1];
                refresh_token=ar[2];
            }
        }catch (Exception e){

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        String myData = intent.getStringExtra("my_data");
        int hostOrUser = intent.getIntExtra("hostOrUser",-1);

        if(hostOrUser==0){
            strHostOrKakao = "host";
        }else if(hostOrUser==1){
            strHostOrKakao = "kakao";
        }
        System.out.println("strHostOrKaKao : "+strHostOrKakao);

        webView=(WebView) findViewById(R.id.kakaoLogin);
        webView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(myData);
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(myData);
                webView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
                webView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
            }
        });

    }
    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("webview3 check URL",url);
            if(url.contains("http://115.21.52.248:8080/"+strHostOrKakao+"/login?code")) {
                String s[];
                s = url.split("code=");
                System.out.println("webview4 : " + s[1]);
                new Thread() {
                    public void run() {
                        getAccess_token(s[1]);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("access_token", access_token);
                        resultIntent.putExtra("refresh_token", refresh_token);
                        resultIntent.putExtra("userID", userID);
                        setResult(100, resultIntent);
                        finish();
                    }
                }.start();
            }else if(url.contains("http://115.21.52.248:8080/"+strHostOrKakao+"/logout/check")){
                Intent resultIntent = new Intent();
                access_token=null;
                refresh_token=null;
                userID=null;
                resultIntent.putExtra("access_token", access_token);
                resultIntent.putExtra("refresh_token", refresh_token);
                resultIntent.putExtra("userID", userID);
                setResult(200, resultIntent);
                finish();
            }
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
            webView.goBack();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            System.out.println("webView100 : "+access_token+refresh_token+userID);
            intent.putExtra("access_token",access_token);
            intent.putExtra("refresh_token",refresh_token);
            intent.putExtra("userID",userID);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
