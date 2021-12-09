package com.inhatc.beacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

public class BeaconActivity extends AppCompatActivity implements BeaconConsumer, AutoPermissionsListener {

    TextView textView;

    private BeaconManager beaconManager;

    String beaconUUID="16300016-8FFB-48D2-B060-D0F5A71096E0"; // beacon -uuid
    String ssaid = null;

    private String TAG = "BeaconActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        textView=findViewById(R.id.text);

        AutoPermissions.Companion.loadAllPermissions(this,101); // AutoPermissions(자동 권한 승인)

        beaconManager = BeaconManager.getInstanceForApplication(this); //비콘 인스턴스 생성
        //비콘 정해진 레이아웃 존재 (https://stackoverflow.com/questions/33594197/altbeacon-setbeaconlayout)
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));//ibeacon layout
        beaconManager.bind(this);//연결

    }

    @Override //바인딩 후 먼저 실행되는 메소드
    public void onBeaconServiceConnect() { beaconManager.removeAllMonitorNotifiers(); //기존거 지우기
        Handler handler = new Handler(Looper.getMainLooper());
        beaconManager.setRangeNotifier(new RangeNotifier() //새로 만들기
        {
            @Override
            public void didRangeBeaconsInRegion(Collection beacons, Region region)
            {
                if (beacons.size() > 0)//반복
                {
                    Log.i(TAG, "비콘 거리: "+((Beacon)beacons.iterator().next()).getDistance()+"m");
                }
            }
        });
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "비콘 연결");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BeaconActivity.this, "비콘 연결됨", Toast.LENGTH_SHORT).show();
                        textView.setText("Beacon connected");
                        Intent intent = getIntent();
                        String phone = intent.getStringExtra("Phone");
                        insertTempData i = new insertTempData();
                        i.execute("http:/192.168.219.100:8181/insertTemp.php",phone);
                    }
                },0);
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "비콘 연결 종료");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BeaconActivity.this, "비콘 연결 끊김", Toast.LENGTH_SHORT).show();
                        textView.setText("Beacon disconnected");
                    }
                },0);
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
            }

        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("beacon", Identifier.parse(beaconUUID), null, null));
        } catch (RemoteException e) {    }
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("beacon", Identifier.parse(beaconUUID), null, null));
        } catch (RemoteException e) {    }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onDenied(int i, String[] strings) {
        //autopermission 거부결과
    }

    @Override
    public void onGranted(int i, String[] strings) {
        //autopermission 승인결과
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    class insertTempData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String phone = (String) params[1];
            String uri = params[0];
            String postparam = "phone=" + phone;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.connect();

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(postparam.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = con.getResponseCode();

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = con.getInputStream();
                } else {
                    inputStream = con.getErrorStream();
                }
                StringBuilder sb = new StringBuilder();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result){

        }
    }

}