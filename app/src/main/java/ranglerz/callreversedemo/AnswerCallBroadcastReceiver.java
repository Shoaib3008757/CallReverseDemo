package ranglerz.callreversedemo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

/**
 * Created by User-10 on 27-Nov-17.
 */

public class AnswerCallBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        if (arg1.getAction().equals("android.intent.action.PHONE_STATE")) {

            String state = arg1.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Log.d("TAG", "Inside Extra state off hook");
                //String number = arg1.getStringExtra(TelephonyManager.EXTRA_ou);
                //Log.e("TAG", "outgoing number : " + number);
            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Log.e("TAG", "Inside EXTRA_STATE_RINGING");
                String number = arg1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.e("TAG", "incoming number : " + number);

                if (number != null) {
                    endCall(arg0);
                    Log.e("TAG", "Call Ended: ");

                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + number));
                    if (ActivityCompat.checkSelfPermission(arg0, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    arg0.startActivity(intent);




                }


            }
            else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Log.d("TAG", "Inside EXTRA_STATE_IDLE");
            }
        }


    }

    private void endCall(Context context) {

        try {
            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> c = Class.forName(telephony.getClass().getName());

            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);

            ITelephony telephonyService = (ITelephony) m.invoke(telephony);

            // Funciona en 2.2
            // Funciona en 2.3.3
            telephonyService.endCall();

            //logManager.debug("ITelepony was used (endCall)");
        } catch (Exception e) {
            //logManager.error("Error ending call: " + e.getMessage());
            //logManager.debug("Error ending call", e);
        }
    }
}