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
    private String userID;
    //로그인 요청
    public String confirmAT(){
        System.out.println("test10 : ");
        OkHttpClient client = new OkHttpClient();
            try{
                String url="http://115.21.52.248:8080/kakao/access?access_token="+access_token;
                Request.Builder builder=new Request.Builder().url(url).get();
                Request request= builder.build();
                Response response= client.newCall(request).execute();

                if(response.body().string().equals("200")){
                    ResponseBody body=response.body();
                    if(body!=null){
                        System.out.println("body!=null");
                    }else{
                        System.out.println("정보없음!");
                    }
                    return "";
                }
                    else{
                    url="http://115.21.52.248:8080/kakao/refresh?refresh_token="+refresh_token;
                    builder=new Request.Builder().url(url).get();
                    request=builder.build();
                    response=client.newCall(request).execute();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        //access_token=null;
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

//            Intent intent = getIntent();
//            access_token = intent.getStringExtra("access_token");
//            refresh_token = intent.getStringExtra("refresh_token");
//            userID = intent.getStringExtra("userID");
            System.out.println("test101: " + access_token + " " + refresh_token + " "+userID);

        new Thread() {
            public void run() {
                test2 = new Requests("http://115.21.52.248:8080/kakao/connect").getLoginUrl();
            }
        }.start();


        //test

        if(access_token==null) {
            imageButton.setVisibility(View.VISIBLE);
            //버튼 클릭리스너
            System.out.println("get access token test: ");
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("test2 : " + access_token);
                    new Thread() {
                        public void run() {
                            test2 = new Requests("http://115.21.52.248:8080/kakao/connect").getLoginUrl();
                        }
                    }.start();

                    Intent intent = new Intent(getApplicationContext(), WebViewPage.class);
                    intent.putExtra("my_data", test2);

                    System.out.println("login button click : " + test2);
                   // 로그인하고나면 로그아웃만 생기게
                    imageButton.setVisibility(View.GONE);
                    button2.setVisibility(View.VISIBLE);
                    startActivityForResult(intent, 100);
                }
            });
        }else {
            System.out.println("access_token is not null : " + access_token);
            imageButton.setVisibility(View.GONE);
            button2.setVisibility(View.VISIBLE);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //access_token 해제하는 코드
                    new Thread() {
                        public void run() {
                            confirmAT();
                            new Requests("http://115.21.52.248:8080/kakao/logout?userID=").kakaoLogout(userID, access_token);

                        }
                    }.start();

                    System.out.println("logout button click!"+ userID+" "+access_token+" "+refresh_token );
                    imageButton.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.GONE);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent){
            super.onActivityResult(requestCode,resultCode,resultIntent);
             access_token = resultIntent.getStringExtra("access_token");
            refresh_token = resultIntent.getStringExtra("refresh_token");
             userID = resultIntent.getStringExtra("userID");
            System.out.println("get Activity result: " + access_token + " " + refresh_token + " "+userID);


    }
    @Override
    protected void onResume(){
        super.onResume();
        if(access_token==null) {
            imageButton.setVisibility(View.VISIBLE);
            //버튼 클릭리스너
            System.out.println("onResume login : ");
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("onResume login : " + access_token);
                    new Thread() {
                        public void run() {
                            test2 = new Requests("http://115.21.52.248:8080/kakao/connect").getLoginUrl();
                        }
                    }.start();

                    Intent intent = new Intent(getApplicationContext(), WebViewPage.class);
                    intent.putExtra("my_data", test2);

                    System.out.println("onResume 로그인버튼 클릭 : " + test2);
                    // 로그인하고나면 로그아웃만 생기게
                    imageButton.setVisibility(View.GONE);
                    button2.setVisibility(View.VISIBLE);
                    startActivityForResult(intent, 100);
                }
            });
        }else {
            System.out.println("onResume logout  : " + access_token);
            imageButton.setVisibility(View.GONE);
            button2.setVisibility(View.VISIBLE);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //access_token 해제하는 코드
                    new Thread() {
                        public void run() {
                            confirmAT();
                            new Requests("http://115.21.52.248:8080/kakao/logout?id=").kakaoLogout(userID, access_token);
                            access_token=null;
                        }
                    }.start();


                    imageButton.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.GONE);
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
