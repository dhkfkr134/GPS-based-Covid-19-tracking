package com.example.covidcontackmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private Button button1;
    private TextView txtResult;
    private TextView txtResult2;
    private int gps_num = 300;
    private double[] longtitudeSet = new double[gps_num];
    private double[] latitudeSet = new double[gps_num];
    private int sequence = 0;
    private boolean isMove = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button)findViewById(R.id.button1);
        txtResult = (TextView)findViewById(R.id.txtResult);
        txtResult2 = (TextView)findViewById(R.id.txtResult2);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );
                }
                else{
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String provider = location.getProvider();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    double altitude = location.getAltitude();

                    txtResult.setText("위치정보 : " + provider + "\n" +
                            "위도 : " + latitude + "\n" +
                            "경도 : " + longitude + "\n" +
                            "고도  : " + altitude);
                    //System.out.println(longitude);

                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            0,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            0,
                            gpsLocationListener);


                    //thread 연습
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                System.out.println("thread test");
                                //gps가져오기
                                if ( Build.VERSION.SDK_INT >= 23 &&
                                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                                    ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                                            0 );
                                }
                                else {
                                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    String provider = location.getProvider();
                                    double longitude = location.getLongitude();
                                    double latitude = location.getLatitude();
                                    double altitude = location.getAltitude();
                                    System.out.println("위치정보 : " + provider + "\n" +
                                            "위도 : " + latitude + "\n" +
                                            "경도 : " + longitude + "\n" +
                                            "고도  : " + altitude);

                                    //stay move파악하기
                                    System.out.println(provider  + "   " + latitude + "   " + longitude);

                                    longtitudeSet[sequence] = longitude;
                                    latitudeSet[sequence] = latitude;


                                    if(sequence == gps_num-1){

                                        float[] roundedLongatitudeSet = new float[gps_num];
                                        float[] roundedlatitudeSet = new float[gps_num];
                                        int i = 0;
                                        for(i = 0; i<gps_num; i++ ){
                                            roundedLongatitudeSet[i] =  Float.parseFloat(String.format("%.4f", longtitudeSet[i]));
                                            roundedlatitudeSet[i] =  Float.parseFloat(String.format("%.4f", latitudeSet[i]));
                                        }
                                        float latitudeModeRatio = modeRatio(roundedlatitudeSet);
                                        float longtitudeModeRatio = modeRatio(roundedLongatitudeSet);

                                        if( latitudeModeRatio < 0.5 && longtitudeModeRatio < 0.5){
                                            isMove = true;
                                        }
                                        else{
                                            isMove = false;
                                            double latitudeMedian = getMedian(latitudeSet);
                                            double longtitudeMedian = getMedian(longtitudeSet);

                                            System.out.println("위도: " + latitudeMedian + "\n" + longtitudeMedian);
                                        }

                                        sequence = 0;
                                    }
                                    else{
                                        sequence++;
                                    }
                                    //stay move파악끝

                                    //보내기. stay일 경우 서버로 보내기.
                                }




                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    //thread끝


                }


            }
        });




    }
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

            txtResult.setText("위치정보 : " + provider + "\n" +
                    "위도 : " + latitude + "\n" +
                    "경도 : " + longitude + "\n" +
                    "고도  : " + altitude);
            /*
            System.out.println(provider  + "   " + latitude + "   " + longitude);

            longtitudeSet[sequence] = longitude;
            latitudeSet[sequence] = latitude;


            if(sequence == gps_num-1){

                float[] roundedLongatitudeSet = new float[gps_num];
                float[] roundedlatitudeSet = new float[gps_num];
                int i = 0;
                for(i = 0; i<gps_num; i++ ){
                    roundedLongatitudeSet[i] =  Float.parseFloat(String.format("%.4f", longtitudeSet[i]));
                    roundedlatitudeSet[i] =  Float.parseFloat(String.format("%.4f", latitudeSet[i]));
                }
                float latitudeModeRatio = modeRatio(roundedlatitudeSet);
                float longtitudeModeRatio = modeRatio(roundedLongatitudeSet);

                if( latitudeModeRatio < 0.5 && longtitudeModeRatio < 0.5){
                    isMove = true;
                }
                else{
                    isMove = false;
                    double latitudeMedian = getMedian(latitudeSet);
                    double longtitudeMedian = getMedian(longtitudeSet);

                    System.out.println("위도: " + latitudeMedian + "\n" + longtitudeMedian);
                }

                sequence = 0;
            }
            else{
                sequence++;
            }

             */
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };
    // 소수의 최빈값의 비율을 리턴
    public int modeRatio(float[] arr) {

        float[] mode = new float[gps_num];

        float[] value = new float[gps_num];
        int[] valueCount = new int[gps_num];
        int valueVariation = 0;
        boolean valueExist = false;

        for(int i = 0; i<arr.length; i++){
            for(int j = 0; j<valueVariation; j++) {
                if (arr[i] == value[j]) {
                    valueCount[j]++;
                    valueExist = true;
                }
            }
            if(valueExist == false){
                value[valueVariation] = arr[i];
                valueCount[valueVariation]++;
                valueVariation++;
            }
            else valueExist = false;

        }

        float modeNum = 0; // 최빈수
        int modeCnt = 0; // 최빈수가 출현한 횟수

        for(int i=0; i<valueVariation; i++) {
            if (modeCnt < valueCount[i]) {
                modeCnt = valueCount[i];
                modeNum = value[i];
            }
        }
        System.out.println("최빈수 : " + modeNum + "    cnt : " + modeCnt);

        txtResult2.setText("최빈수 : " + modeNum + "    cnt : " + modeCnt);
        return modeCnt;
    }
    //소수의 중앙값 리턴
    private static double getMedian(double[] arrayDouble) {

        // TODO Auto-generated method stub


        Arrays.sort(arrayDouble);//오름차순 정렬


        int size = arrayDouble.length;

        double result;

        if (size % 2 == 0) { //배열크기가 짝수일경우

            int m = size / 2;

            int n = size / 2 - 1;

            result = (float) (arrayDouble[m] + arrayDouble[n]) / 2; //중앙값 2개의  평균

        } else { //배열크기가 홀수인경우

            int m = size / 2;

            result = arrayDouble[m]; //중앙값

        }
        //System.out.print(result);
        return result;
    }





}

