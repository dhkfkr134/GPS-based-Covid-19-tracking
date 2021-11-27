package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private Button button1, button2;
    private ImageButton imageButton;
    private String test2;
    private String access_token=null;
    private String refresh_token=null;

    public String confirmAT(){
        OkHttpClient client = new OkHttpClient();
        if(access_token==null){

            try {
                String url = "http://115.21.52.248:8080/kakao/connect";
                Request.Builder builder=new Request.Builder().url(url).get();
                Request request= builder.build();
                Response response= client.newCall(request).execute();
                ResponseBody body=response.body();
                return body.string();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            try{
                String url="http://115.21.52.248:8080/kakao/access?access_token="+access_token;
                Request.Builder builder=new Request.Builder().url(url).get();
                Request request= builder.build();
                Response response= client.newCall(request).execute();
                if(response.isSuccessful()){
                    ResponseBody body=response.body();
                    if(body!=null){
                        System.out.println(body.string());
                    }else{
                        System.out.println("정보없음!");
                    }
                    return "";
                }else{
                    url="http://115.21.52.248:8080/kakao/refresh?refresh_token="+refresh_token;
                    builder=new Request.Builder().url(url).get();
                    request=builder.build();
                    response=client.newCall(request).execute();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return "";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        Intent intent = getIntent();
        access_token = intent.getStringExtra("access_token");
        refresh_token = intent.getStringExtra("refresh_token");
        System.out.println("@@@@@@@@: "+access_token + " "+refresh_token);

        new Thread() {
            public void run() {
                test2 = confirmAT();
            }
        }.start();

        //버튼 클릭리스너
        if(access_token==null) {
            //무조건 로그인버튼보이게
            button2.setVisibility(View.GONE);
            imageButton.setVisibility(View.VISIBLE);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("accesstoken : " + access_token);

                    Intent intent = new Intent(getApplicationContext(), WebViewPage.class);
                    intent.putExtra("my_data", test2);

                    System.out.println("@@@@@@@@@@@ 로그인버튼 클릭 : " + test2);
                    //로그인하고나면 로그아웃만 생기게
                    imageButton.setVisibility(View.GONE);
                    button2.setVisibility(View.VISIBLE);
                    startActivity(intent);
                }
            });
        }else {
            imageButton.setVisibility(View.GONE);
            button2.setVisibility(View.VISIBLE);
            button2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //access_token 해제하는 코드
                }
            });
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gps잠금 버튼
            }
        });
    }
}
