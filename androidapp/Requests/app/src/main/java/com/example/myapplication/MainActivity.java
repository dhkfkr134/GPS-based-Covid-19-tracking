package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.text.SimpleDateFormat;
import java.util.Date;
public class MainActivity extends AppCompatActivity {

    private Button button1, button2;
    private String test2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);


        new Thread() {
            public void run() {
                new Requests("http://115.21.52.248:8080/kakao/connect").getLoginUrl();
            }
        }.start();

        //버튼 클릭리스너
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebViewPage.class);
                intent.putExtra("my_data", test2);
                System.out.println("@@@@@@@@@@@ button2 : " + test2);
                startActivity(intent);

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //test
                //new RestAPITask("https://api.github.com/").execute();

                new Thread() {
                    public void run() {
                        Date test = new Date();
                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                        //new Requests("http://115.21.52.248:8080/location/GPS").postData(1,"TCR3/QRX29/WVW30/WFD6/HPI7/VNC11/CVB9/EVL17/BGK9","위도 경도",date.format(test).toString(),date.format(test).toString());
                        //new Requests("http://115.21.52.248:8080/location/bluetooth").postBluetooth(1,1,date.format(test));
                        test2 = new Requests("http://115.21.52.248:8080/kakao/connect").getLoginUrl();
                        }
                }.start();
            }
        });
    }
}
