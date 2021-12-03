package com.inhatc.beacon;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer, AutoPermissionsListener {

    TextView textView;

    private BeaconManager beaconManager;

    String beaconUUID="16300016-8FFB-48D2-B060-D0F5A71096E0"; // beacon -uuid
    String ssaid = null;
    boolean isUserform = false;

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.tv_message);

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
                    Log.i(TAG, "비콘 거리: "+((Beacon)beacons.iterator().next()).getDistance()+" meters away.");
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
                        Toast.makeText(MainActivity.this, "didEnterRegion - 비콘 연결됨", Toast.LENGTH_SHORT).show();
                        textView.setText("Beacon connected");
                        ssaid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);//기기 ssaid 반환
                        //DB 연결하여 있는지 비교
                        if(isUserform){
                            //있는 경우
                        }
                        else{
                            //없는 경우
                        }
                        ssaid = null;
                        isUserform = false;
                    }
                },0);



            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "비콘 연결 종료");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "didExitRegion - 비콘 연결 끊김", Toast.LENGTH_SHORT).show();
                        textView.setText("Beacon disconnected");
                    }
                },0);


            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "didDetermineStateForRegion: "+state);
            }

        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("beacon", Identifier.parse(beaconUUID), null, null));
        } catch (RemoteException e) {    }
        try
        {
            beaconManager.startRangingBeaconsInRegion(new Region("beacon", Identifier.parse(beaconUUID), null, null));
        }
        catch (RemoteException e)
        {
        }

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



}