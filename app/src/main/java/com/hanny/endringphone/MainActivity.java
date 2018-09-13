package com.hanny.endringphone;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //判断是否有电话权限
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE}, 100);
        } else {

            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            phoneStateListener = new PhoneCallListener();
            telephonyManager.listen(phoneStateListener, PhoneCallListener.LISTEN_CALL_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                phoneStateListener = new PhoneCallListener();
                telephonyManager.listen(phoneStateListener, PhoneCallListener.LISTEN_CALL_STATE);
            }
        }
    }

    //监听来电状态
    public class PhoneCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e("callPhone", "通话！！！");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e("callPhone", "响铃！！！");
                    PhoneUtils.endPhone(MainActivity.this);
                    break;
            }
            super.onCallStateChanged(state, phoneNumber);
        }
    }

}
