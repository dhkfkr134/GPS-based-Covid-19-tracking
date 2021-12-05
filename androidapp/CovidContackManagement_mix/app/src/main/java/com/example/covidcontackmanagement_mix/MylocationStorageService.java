package com.example.covidcontackmanagement_mix;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MylocationStorageService extends Service {

    private static final int TODO = 1;// 내가넣음
    private Button button1;
    private TextView txtResult;
    private TextView txtResult2;
    private int gps_num =300 ;
    private double[] longtitudeSet = new double[gps_num];
    private double[] latitudeSet = new double[gps_num];
    private double latitudeMedian;
    private double longtitudeMedian;
    private String userID = "01";
    private String intime;
    private String outtime;
    private String mugunghwa;
    private String gpsString;
    private String beforeLocation = "";
    private int sequence = 0;
    private boolean isMove = true;
    private OkHttpClient client = new OkHttpClient();

    private BluetoothAdapter mBluetoothAdapter;
    private Thread gpsThread;

    public MylocationStorageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("before:" + userID);
        userID = intent.getStringExtra("userID");
        System.out.println("fix:" + userID);

        //bluetooth foreground start
        // Initializes Bluetooth adapter.

        //userid

        //gps foreground start
        Requests request = new Requests("http://115.21.52.248:8080/location/GPS");

        Intent testIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(this, 0, testIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        System.out.println("startservice");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel", "play!!",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Notification과 채널 연걸
            NotificationManager mNotificationManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
            mNotificationManager.createNotificationChannel(channel);

            // Notification 세팅
            NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "channel")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("현재 실행 중인 앱 이름")
                    .setContentIntent(pendingIntent)
                    .setContentText("");

            // id 값은 0보다 큰 양수가 들어가야 한다.
            mNotificationManager.notify(1, notification.build());
            // foreground에서 시작
            startForeground(1, notification.build());
        }




        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String provider = location.getProvider();
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        double altitude = location.getAltitude();


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
        gpsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    if(Thread.interrupted()) {
                        break;
                    }


                    //gps가져오기


                    if (ActivityCompat.checkSelfPermission(MylocationStorageService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MylocationStorageService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String provider = location.getProvider();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    double altitude = location.getAltitude();



                    //stay move파악하기
                    System.out.println( sequence + provider  + "   " + latitude + "   " + longitude);

                    longtitudeSet[sequence] = longitude;
                    latitudeSet[sequence] = latitude;


                    if(sequence == gps_num-1){

                        System.out.println("intime: " + intime + "outtime: " + outtime);

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

                        System.out.println( "lat = " + latitudeModeRatio + "lon = " + longtitudeModeRatio + "res = " + (latitudeModeRatio < 0.5 && longtitudeModeRatio < 0.5));
                        if( latitudeModeRatio < 0.5 && longtitudeModeRatio < 0.5){
                            System.out.println("Move");
                            System.out.println("ratio: " + latitudeModeRatio + " " + longtitudeModeRatio );
                            //머무르다가 이동할경우 서버로 stay송신
                            if(isMove == false){


                                //서버로 전송
                                System.out.println("stay -> move before: " + beforeLocation);
                                request.postData(userID, "SM"+mugunghwa, gpsString, intime, outtime,"");

                            }

                            isMove = true;


                        }
                        //stay
                        else{
                            System.out.println("Stay");

                            //위경도 평균
                            latitudeMedian = getMedian(latitudeSet);
                            longtitudeMedian = getMedian(longtitudeSet);
                            //System.out.println(latitudeMedian + " " + longtitudeMedian);

                            //무궁화코드로변환
                            List<String> dmsArr = gpsToDMS(latitudeMedian, longtitudeMedian);
                            //System.out.println("dms: "+dmsArr);
                            String presentLocation = getGpsMgh(dmsArr.get(4));

                            //머물기 끝시간 계산
                            long now ;
                            Date date ;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                            //장소 변경한경우 서버로 전달
                            if(beforeLocation.equals(presentLocation) == false && isMove == false) {

                                System.out.println(mugunghwa +" " + gpsString + " " + intime + " " + outtime);
                                request.postData(userID, "SS" +mugunghwa, gpsString, intime, outtime, "");

                                //머물기 시작시간계산


                                now = System.currentTimeMillis();
                                date = new Date(now);

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                cal.add(Calendar.MINUTE, -5);


                                intime = sdf.format(cal.getTime());
                                mugunghwa = dmsArrTomugunghwas(dmsArr);
                                gpsString = latitudeMedian + " " + longtitudeMedian;
                            }



                            //새로운 장소 stay시작한경우 변수들 갱신
                            if(!beforeLocation.equals(presentLocation)){

                                //머물기 시작시간계산
                                now = System.currentTimeMillis();
                                date = new Date(now);

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                cal.add(Calendar.MINUTE, -5);


                                intime = sdf.format(cal.getTime());
                                mugunghwa = dmsArrTomugunghwas(dmsArr);
                                gpsString = latitudeMedian + " " + longtitudeMedian;
                            }

                            //머물기 끝시간 계산
                            now = System.currentTimeMillis();
                            date = new Date(now);
                            outtime = sdf.format(date);


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





                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }


                }
            }
        });
        gpsThread.start();
        //thread끝







        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        try {
            gpsThread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gpsThread.interrupt();



    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();


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

    public float modeRatio(float[] arr) {

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

        System.out.println("최빈수 : " + modeNum + "    cnt : " + modeCnt + "arr.leng : " + arr.length);


        return (float)modeCnt/arr.length;
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
        double er=0.2;
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
        if((XS*10)%2==1){
            XS-=0.1;
        }
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
        if((YS*10)%2==1){
            YS-=0.1;
        }
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
        public boolean postData(String userId, String mugunghwa,String gpsString ,String inTime, String outTime, String address){
            OkHttpClient client = new OkHttpClient();
            try{
                RequestBody formBody=new FormBody.Builder()
                        .add("id",userId)
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

    /*
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



     */
}

