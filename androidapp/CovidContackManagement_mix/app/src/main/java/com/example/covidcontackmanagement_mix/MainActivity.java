package com.example.covidcontackmanagement_mix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class MainActivity extends AppCompatActivity {

    private Button button1;
    private TextView txtResult;
    private TextView txtResult2;
    private int gps_num = 300;
    private double[] longtitudeSet = new double[gps_num];
    private double[] latitudeSet = new double[gps_num];
    private double latitudeMedian;
    private double longtitudeMedian;
    private String intime;
    private String outtime;
    private String mugunghwa;
    private String gpsString;
    private String beforeLocation = "";
    private int sequence = 0;
    private boolean isMove = true;
    private OkHttpClient client=new OkHttpClient();

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button)findViewById(R.id.button1);
        txtResult = (TextView)findViewById(R.id.txtResult);
        txtResult2 = (TextView)findViewById(R.id.txtResult2);
        Requests request = new Requests("http://115.21.52.248:8080/location/GPS");

        MediaPlayer player = MediaPlayer.create(this, R.raw.beep);
        //player.start();




        //블루투스 시작
        Context context = MainActivity.this;

        if (savedInstanceState == null) {

            mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE))
                    .getAdapter();

            // Is Bluetooth, gps supported on this device?

            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                        0 );
            }

            if (mBluetoothAdapter != null) {

                // Is Bluetooth turned on?
                if (mBluetoothAdapter.isEnabled()) {

                    // Are Bluetooth Advertisements supported on this device?
                    if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {

                        // Everything is supported and enabled, load the fragments.
                        setupFragments();

                    } else {

                        // Bluetooth Advertisements are not supported.
                        showErrorText(R.string.bt_ads_not_supported);
                    }
                } else {

                    // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
                }
            } else {

                // Bluetooth is not supported.
                showErrorText(R.string.bt_not_supported);
            }
        }

        //블루투스끝

        //블루투스 포그라운드시작

        Intent serviceIntent1 = new Intent(this, bltScanService.class);
        //startService(serviceIntent1);

        //gps 포그라운드시작

        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);

        //포그라운드 끝



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




                    //gps처리 thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {



                                //gps가져오기
                                if ( Build.VERSION.SDK_INT >= 23 &&
                                        ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                                    ActivityCompat.requestPermissions( MainActivity.this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                                            0 );
                                }
                                else {
                                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    String provider = location.getProvider();
                                    double longitude = location.getLongitude();
                                    double latitude = location.getLatitude();
                                    double altitude = location.getAltitude();



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

                                        //move
                                        if( latitudeModeRatio < 0.5 && longtitudeModeRatio < 0.5){
                                            System.out.println("ratio: " + latitudeModeRatio + " " + longtitudeModeRatio );
                                            //머무르다가 이동할경우 서버로 stay송신
                                            if(isMove == false){

                                                //머물기끝
                                                long now = System.currentTimeMillis();
                                                Date date = new Date(now);
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                outtime = sdf.format(date);

                                                //서버로 전송
                                                System.out.println("stay -> move before: " + beforeLocation);
                                                request.postData(1, mugunghwa, gpsString, intime, outtime,"");

                                            }

                                            isMove = true;

                                        }
                                        //stay
                                        else{
                                            //위경도 평균
                                            latitudeMedian = getMedian(latitudeSet);
                                            longtitudeMedian = getMedian(longtitudeSet);
                                            System.out.println(latitudeMedian + " " + longtitudeMedian);

                                            //무궁화코드로변환
                                            List<String> dmsArr = gpsToDMS(latitudeMedian, longtitudeMedian);
                                            System.out.println("dms: "+dmsArr);
                                            String presentLocation = getGpsMgh(dmsArr.get(4));


                                            //머무르는 장소가바뀐경우 서버로전송 같은경우로테스트중
                                            if(beforeLocation.equals(presentLocation) == false) {
                                            }
                                            else{
                                                //머물기끝
                                                long now = System.currentTimeMillis();
                                                Date date = new Date(now);
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                outtime = sdf.format(date);

                                                System.out.println(mugunghwa +" " + gpsString + " " + intime + " " + outtime);
                                                request.postData(1, mugunghwa, gpsString, intime, outtime, "");

                                                //머물기시작
                                                now = System.currentTimeMillis();
                                                date = new Date(now);
                                                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                intime = sdf.format(date);
                                                mugunghwa = dmsArrTomugunghwas(dmsArr);
                                                gpsString = latitudeMedian + " " + longtitudeMedian;


                                            }

                                            if(isMove == true){
                                                System.out.println("ismove : "+ isMove);
                                                //머물기시작
                                                long now2 = System.currentTimeMillis();
                                                Date date2 = new Date(now2);
                                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                intime = sdf2.format(date2);
                                                mugunghwa = dmsArrTomugunghwas(dmsArr);
                                                gpsString = latitudeMedian + " " + longtitudeMedian;
                                            }



                                            beforeLocation = presentLocation;

                                            isMove = false;

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


    //gps도분초로 바꾸기
    private List<String> gpsToDMS(double latit, double longit){
        Double [][] DMS =new Double[10][6];
        String [] DMSS=new String[9];
        boolean xpm=true;
        boolean ypm=true;
        double er=0.1;
        int XD; int XM; double XS; int YD; int YM; double YS;
        String gps="";
        XD=(int)latit;
        if(XD<0) {
            xpm=false;
        }else{
            xpm=true;
        }
        XD=Math.abs(XD);

        latit=Math.abs(latit);
        XM=(int)(((latit)-(double)XD)*60);
        XS=((latit-XD)*60-(double)XM)*60;
        XS=((double)Math.round(XS*10)/10);
        YD=(int)longit;

        if(YD<0) {
            ypm=false;
        }else{
            ypm=true;
        }
        YD=Math.abs(YD);
        longit=Math.abs(longit);
        YM=(int)(((longit)-YD)*60);
        YS=((longit-YD)*60-YM)*60;
        YS=((double)Math.round(YS*10)/10);
        if(XS-er<0){
            if(XM-1<0){
                DMS[7][0]=(double)XD-1;
                DMS[8][0]=(double)XD-1;
                DMS[9][0]=(double)XD-1;
                DMS[7][1]=(double)(60+(XM-1));
                DMS[8][1]=(double)(60+(XM-1));
                DMS[9][1]=(double)(60+(XM-1));
                DMS[7][2]=(60+(XS-er));
                DMS[8][2]=(60+(XS-er));
                DMS[9][2]=(60+(XS-er));
            }else{
                DMS[7][0]=(double)XD;
                DMS[8][0]=(double)XD;
                DMS[9][0]=(double)XD;
                DMS[7][1]=(double)((XM-1));
                DMS[8][1]=(double)((XM-1));
                DMS[9][1]=(double)((XM-1));
                DMS[7][2]=(60+(XS-er));
                DMS[8][2]=(60+(XS-er));
                DMS[9][2]=(60+(XS-er));
            }
        }else{
            DMS[7][0]=(double)XD;
            DMS[8][0]=(double)XD;
            DMS[9][0]=(double)XD;
            DMS[7][1]=(double)((XM));
            DMS[8][1]=(double)((XM));
            DMS[9][1]=(double)((XM));
            DMS[7][2]=((XS-er));
            DMS[8][2]=((XS-er));
            DMS[9][2]=((XS-er));
        }
        if(XS+er>60){
            if(XM+1>60){
                DMS[1][0]=(double)XD+1;
                DMS[2][0]=(double)XD+1;
                DMS[3][0]=(double)XD+1;
                DMS[1][1]=(double)((XM+1)-60);
                DMS[2][1]=(double)((XM+1)-60);
                DMS[3][1]=(double)((XM+1)-60);
                DMS[1][2]=((XS+er)-60);
                DMS[2][2]=((XS+er)-60);
                DMS[3][2]=((XS+er)-60);
            }else{
                DMS[1][0]=(double)XD;
                DMS[2][0]=(double)XD;
                DMS[3][0]=(double)XD;
                DMS[1][1]=(double)((XM+1));
                DMS[2][1]=(double)((XM+1));
                DMS[3][1]=(double)((XM+1));
                DMS[1][2]=((XS+er)-60);
                DMS[2][2]=((XS+er)-60);
                DMS[3][2]=((XS+er)-60);
            }
        }else{
            DMS[1][0]=(double)XD;
            DMS[2][0]=(double)XD;
            DMS[3][0]=(double)XD;
            DMS[1][1]=(double)((XM));
            DMS[2][1]=(double)((XM));
            DMS[3][1]=(double)((XM));
            DMS[1][2]=((XS+er));
            DMS[2][2]=((XS+er));
            DMS[3][2]=((XS+er));
        }
        if(YS-er<0){
            if(YM-1<0){
                DMS[1][3]=(double)YD-1;
                DMS[4][3]=(double)YD-1;
                DMS[7][3]=(double)YD-1;
                DMS[1][4]=(double)(60+(YM-1));
                DMS[4][4]=(double)(60+(YM-1));
                DMS[7][4]=(double)(60+(YM-1));
                DMS[1][5]=(60+(YS-er));
                DMS[4][5]=(60+(YS-er));
                DMS[7][5]=(60+(YS-er));
            }else{
                DMS[1][3]=(double)YD;
                DMS[4][3]=(double)YD;
                DMS[7][3]=(double)YD;
                DMS[1][4]=(double)((YM-1));
                DMS[4][4]=(double)((YM-1));
                DMS[7][4]=(double)((YM-1));
                DMS[1][5]=(60+(YS-er));
                DMS[4][5]=(60+(YS-er));
                DMS[7][5]=(60+(YS-er));
            }
        }else{
            DMS[1][3]=(double)YD;
            DMS[4][3]=(double)YD;
            DMS[7][3]=(double)YD;
            DMS[1][4]=(double)((YM));
            DMS[4][4]=(double)((YM));
            DMS[7][4]=(double)((YM));
            DMS[1][5]=((YS-er));
            DMS[4][5]=((YS-er));
            DMS[7][5]=((YS-er));
        }
        if(YS+er>60){
            if(YM+1>60){
                DMS[3][3]=(double)YD+1;
                DMS[6][3]=(double)YD+1;
                DMS[9][3]=(double)YD+1;
                DMS[3][4]=(double)((YM+1)-60);
                DMS[6][4]=(double)((YM+1)-60);
                DMS[9][4]=(double)((YM+1)-60);
                DMS[3][5]=((YS+er)-60);
                DMS[6][5]=((YS+er)-60);
                DMS[9][5]=((YS+er)-60);
            }else{
                DMS[3][3]=(double)YD;
                DMS[6][3]=(double)YD;
                DMS[9][3]=(double)YD;
                DMS[3][4]=(double)((YM+1));
                DMS[6][4]=(double)((YM+1));
                DMS[9][4]=(double)((YM+1));
                DMS[3][5]=((YS-er)-60);
                DMS[6][5]=((YS-er)-60);
                DMS[9][5]=((YS-er)-60);
            }
        }else{
            DMS[3][3]=(double)YD;
            DMS[6][3]=(double)YD;
            DMS[9][3]=(double)YD;
            DMS[3][4]=(double)((YM));
            DMS[6][4]=(double)((YM));
            DMS[9][4]=(double)((YM));
            DMS[3][5]=((YS+er));
            DMS[6][5]=((YS+er));
            DMS[9][5]=((YS+er));
        }
        DMS[4][0]=(double)XD;
        DMS[4][1]=(double)XM;
        DMS[4][2]=(double)XS;
        DMS[6][0]=(double)XD;
        DMS[6][1]=(double)XM;
        DMS[6][2]=(double)XS;

        DMS[2][3]=(double)YD;
        DMS[2][4]=(double)YM;
        DMS[2][5]=(double)YS;
        DMS[8][3]=(double)YD;
        DMS[8][4]=(double)YM;
        DMS[8][5]=(double)YS;

        DMS[5][0]=(double)XD;
        DMS[5][1]=(double)XM;
        DMS[5][2]=XS;
        DMS[5][3]=(double)YD;
        DMS[5][4]=(double)YM;
        DMS[5][5]=YS;
        for(int i=1;i<=9;i++){
            DMSS[i-1]="";
            DMSS[i-1]+=String.valueOf((int)Math.round(DMS[i][0]));
            DMSS[i-1]+="°";
            DMSS[i-1]+=String.valueOf((int)Math.round(DMS[i][1]));
            DMSS[i-1]+="\'";
            DMSS[i-1]+=String.valueOf(((double)Math.round(DMS[i][2]*100)/100));
            DMSS[i-1]+="\"";
            if(xpm){
                DMSS[i-1]+="N ";
            }else{
                DMSS[i-1]+="S ";
            }
            DMSS[i-1]+=String.valueOf((int)Math.round(DMS[i][3]));
            DMSS[i-1]+="°";
            DMSS[i-1]+=String.valueOf((int)Math.round(DMS[i][4]));
            DMSS[i-1]+="\'";
            DMSS[i-1]+=String.valueOf(((double)Math.round(DMS[i][5]*100)/100));
            DMSS[i-1]+="\"";
            if(ypm){
                DMSS[i-1]+="E";
            }else{
                DMSS[i-1]+="W";
            }
            //System.out.println(DMSS[i-1]);
        }
        return new ArrayList<String>(Arrays.asList(DMSS));
    }

    //gps를 무궁화코드로 변환
    private String getGpsMgh(String gps){
        try{
            String url="http://115.21.52.248:3000/location?gps="+gps;
            Request.Builder builder=new Request.Builder().url(url).get();
            Request request= builder.build();
            Response response= client.newCall(request).execute();

            if(response.isSuccessful()){
                ResponseBody body=response.body();
                if(body!=null){
                    //System.out.println(body.string());
                    return body.string();
                }else{
                    System.out.println("Error");
                }
            }
            return "";
        }catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    private String dmsArrTomugunghwas(List<String> dmsArr){
        String mugunghwas = getGpsMgh(dmsArr.get(0));
        for(int i = 1; i < 9; i++){
            mugunghwas += "/" + getGpsMgh(dmsArr.get(i));
        }
        System.out.println("무궁화s: "+ mugunghwas);

        return mugunghwas;
    }


    //서버로 stay정보 보내는 함수
    public class Requests {
        protected String mURL;

        public Requests(String url) {
            mURL = url;
        }

        //post
        public boolean postData(int userId, String mugunghwa,String gpsString ,String inTime, String outTime, String address){
            OkHttpClient client = new OkHttpClient();
            try{
                RequestBody formBody=new FormBody.Builder()
                        .add("id",Integer.toString(userId))
                        .add("mcode",mugunghwa)
                        .add("loc",gpsString)
                        .add("inTime",inTime)
                        .add("outTime",outTime)
                        .add("address", address)
                        .build();
                Request request=new Request.Builder()
                        .url(mURL)
                        .post(formBody)
                        .build();
                Response response=client.newCall(request).execute();



                //post요청에서 body확인필요없겠지?
                if(response.isSuccessful()){
                    ResponseBody body=response.body();
                    System.out.println(body.string());
                    if(body!=null){
                        System.out.println(body.string());
                    }else{
                        System.out.println("Error");
                    }
                }
                return true;
            }catch(Exception e){
                System.out.println("Exception");
            }
            return false;
        }

    }


    //블루투스 함수 시작


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_ENABLE_BT:

                if (resultCode == RESULT_OK) {

                    // Bluetooth is now Enabled, are Bluetooth Advertisements supported on
                    // this device?
                    if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {

                        // Everything is supported and enabled, load the fragments.
                        setupFragments();

                    } else {

                        // Bluetooth Advertisements are not supported.
                        showErrorText(R.string.bt_ads_not_supported);
                    }
                } else {

                    // User declined to enable Bluetooth, exit the app.
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        ScannerFragment scannerFragment = new ScannerFragment();
        // Fragments can't access system services directly, so pass it the BluetoothAdapter
        scannerFragment.setBluetoothAdapter(mBluetoothAdapter);
        transaction.replace(R.id.scanner_fragment_container, scannerFragment);

        AdvertiserFragment advertiserFragment = new AdvertiserFragment();
        transaction.replace(R.id.advertiser_fragment_container, advertiserFragment);

        transaction.commit();
    }

    private void showErrorText(int messageId) {

        TextView view = (TextView) findViewById(R.id.error_textview);
        view.setText(getString(messageId));
    }


    //블루투스 함수 끝끝



}

