package com.hanny.endringphone;

import android.content.Context;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PhoneUtils {
    public static void endPhone(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Method method = null;
        try {
            method = TelephonyManager.class.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            try {
                ITelephony iTelephony = (ITelephony) method.invoke(telephonyManager);
                iTelephony.endCall();
                Log.e("callPhone","挂断！！！");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
