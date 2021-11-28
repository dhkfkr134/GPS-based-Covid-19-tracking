package com.example.myapplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Requests {
    protected String mURL;

    public Requests(String url) {
        mURL = url;
    }

    //post
    public boolean postData(int userId, String mcode, String loc, String inTime, String outTime){
        OkHttpClient client = new OkHttpClient();
        try{
            RequestBody formBody=new FormBody.Builder()
                    .add("id",Integer.toString(userId))
                    .add("mcode",mcode)
                    .add("loc",loc)
                    .add("inTime",inTime)
                    .add("outTime",outTime)
                    .build();
            Request request=new Request.Builder()
                    .url(mURL)
                    .post(formBody)
                    .build();
            Response response=client.newCall(request).execute();
            ResponseBody body = response.body();
            System.out.println(body.string());
            if(response.isSuccessful()){

                if(body !=null){
                    System.out.println("Response:"+body.string());
                }
            }
            return true;
        }catch(Exception e){
            System.out.println("Exception");
        }
        return false;
    }
    public boolean postBluetooth(int userId, int hostID, String inTime){
        OkHttpClient client = new OkHttpClient();
        try{
            RequestBody formBody=new FormBody.Builder()
                    .add("id",Integer.toString(userId))
                    .add("hostID",Integer.toString(hostID))
                    .add("inTime",inTime)
                    .build();
            Request request=new Request.Builder()
                    .url(mURL)
                    .post(formBody)
                    .build();
            Response response=client.newCall(request).execute();
            ResponseBody body = response.body();
            System.out.println(body.string());

            return true;
        }catch(Exception e){
            System.out.println("Exception");
        }
        return false;
    }
    public String getLoginUrl(){
        try {
            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(mURL).get();
//            builder.addHeader()
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();

            return body.string();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "false";
    }
    public boolean kakaoLogout(String userID, String access_token){
        OkHttpClient client = new OkHttpClient();
        try{
            Request.Builder builder = new Request.Builder().url(mURL+userID+"&&access_token="+access_token).get();
//            builder.addHeader()
            Request request = builder.build();
            Response response = client.newCall(request).execute();

            ResponseBody body = response.body();
            System.out.println(body.string());

            return true;
        }catch(Exception e){
            System.out.println("Exception");
        }
        return false;
    }


}
