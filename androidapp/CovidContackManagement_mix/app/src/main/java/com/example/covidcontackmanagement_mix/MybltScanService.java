package com.example.covidcontackmanagement_mix;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

public class MybltScanService extends Service {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback;
    private ScanResult res;

    public MybltScanService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("Mybltstart");

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


        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        mScanCallback = new SampleScanCallback();
        mBluetoothLeScanner.startScan(buildScanFilters(), buildScanSettings(), mScanCallback);

        //System.out.println("res rssi: " + res.getRssi());

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        mBluetoothLeScanner.stopScan(mScanCallback);

    }



    private List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();

        ScanFilter.Builder builder = new ScanFilter.Builder();
        // Comment out the below line to see all BLE devices around you
        builder.setServiceUuid(Constants.Service_UUID);
        scanFilters.add(builder.build());

        return scanFilters;
    }
    private ScanSettings buildScanSettings() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        return builder.build();
    }

    private class SampleScanCallback extends ScanCallback {

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

            for (ScanResult result : results) {
                //mAdapter.add(result);
            }
            //mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            res=result;

            ScanRecord scanRecord = result.getScanRecord();
            byte[] servicedata =  scanRecord.getServiceData(Constants.Service_UUID);
            int rssi = res.getRssi();
            System.out.println("rssi: " + rssi +"\n"
                    + servicedata[0] + servicedata[1]);

            if(rssi>-70) {
                //미디어플레이
                //MediaPlayer player = MediaPlayer.create(, R.raw.beep);
                MediaPlayer player = MediaPlayer.create(getApplicationContext(),R.raw.beep);
                player.start();
            }
            //mAdapter.add(result);
            //mAdapter.notifyDataSetChanged();
        }



        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            //Toast.makeText(getActivity(), "Scan failed with error: " + errorCode, Toast.LENGTH_LONG)
            //        .show();
        }

    }

}