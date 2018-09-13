## 实现安卓手机上拒接来电

### 1.获取电话权限
```
  //判断是否有电话权限
  if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) ||
                     (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
                 ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE}, 100);
          }
```
**需要注意的是，这里要申请电话权限，和读取电话信息权限（READ_PHONE_STATE），READ_PHONE_STATE是为了获取电话的手机号，方便过滤拦截**

### 2. 监听电话状态
```
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

```

### 3.添加电话状态监听
```
   telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            phoneStateListener = new PhoneCallListener();
            telephonyManager.listen(phoneStateListener, PhoneCallListener.LISTEN_CALL_STATE);
            
```
### 4.挂断电话

- 新建AIDL文件，通过反射获取挂断电话API，按照系统ITelephony.aidl 文件的路径，新建一个相同的文件，其接口方法为endCall().注意的是路径必须完全相同：

```
package com.android.internal.telephony;

// Declare any non-default types here with import statements

interface ITelephony {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
       boolean endCall();
}

```
- 反射挂断电话方法
```
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

```