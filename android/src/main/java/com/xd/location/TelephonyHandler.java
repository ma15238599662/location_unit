package com.xd.location;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

public class TelephonyHandler {
  private TelephonyManager tm;
  private int ms_dbm_info = -1; // dBm
  private Activity mActivity;
  private String signalinfo;

  private PhoneStateListener phoneStateListener = new PhoneStateListener() {
    // private int asu = 0,lastSignal = 0;

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
      //这个ltedbm 是4G信号的值
      signalinfo = signalStrength.toString();
      String[] parts = signalinfo.split(" ");
      String ltedbm = String.valueOf(parts[9]);
      //这个dbm 是2G和3G信号的值
      int asu = signalStrength.getGsmSignalStrength();
      int dbm = -113 + 2 * asu;
      if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
        ms_dbm_info = Integer.parseInt(ltedbm);
        Log.i("NetWorkUtil", "网络：LTE 信号强度：" + ltedbm + "======Detail:" + signalinfo);
      } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS)                                               {
        String bin;
        if (dbm > -75) {
            bin = "网络很好";
        } else if (dbm > -85) {
            bin = "网络不错";
        } else if (dbm > -95) {
            bin = "网络还行";
        } else if (dbm > -100) {
            bin = "网络很差";
        } else {
            bin = "网络错误";
        }
        ms_dbm_info = dbm;
        Log.i("NetWorkUtil", "网络：WCDMA 信号值：" + dbm + "========强度：" + bin + "======Detail:" + signalinfo);
      } else {
        String bin;
        if (asu < 0 || asu >= 99) bin = "网络错误";
        else if (asu >= 16) bin = "网络很好";
        else if (asu >= 8) bin = "网络不错";
        else if (asu >= 4) bin = "网络还行";
        else bin = "网络很差";
        // Log.i("NetWorkUtil", "网络：GSM 信号值：" + dbm + "========强度：" + bin + "======Detail:" + signalinfo);
        ms_dbm_info = dbm;
      }
      super.onSignalStrengthsChanged(signalStrength);


      // asu = signalStrength.getGsmSignalStrength();
      // Log.i("asu", "" + asu);
      // lastSignal = -113 + 2 * asu;
      // ms_dbm_info = lastSignal;
      // Log.i("lastSignal", "" + lastSignal);
      // super.onSignalStrengthsChanged(signalStrength);

    }

    // ms_dbm_info = lastSignal;
  };

  public Map<String, Object> getTelephonyData() {
    Map<String, Object> telData = new HashMap<String, Object>();
      telData.put("BS_signal_strength", ms_dbm_info);
      telData.put("signalinfo", signalinfo);
    return telData;
  }

  public TelephonyHandler(Activity activity) {
    this.mActivity = activity;
    this.tm = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
    // List<NeighboringCellInfo> infos= tm.getNeighboringCellInfo();
  }

  public void start() {
    // tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    tm.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
  }

  public void stop() {
    tm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
  }
}











// public class MainActivity extends AppCompatActivity {

//     private TelephonyManager tm;
//     private ms_dbm_info; // dBm

//     private PhoneStateListener phoneStateListener = new PhoneStateListener() {
//         private int asu = 0,lastSignal = 0;
//         @Override
//         public void onSignalStrengthsChanged(SignalStrength signalStrength) {
//             asu = signalStrength.getGsmSignalStrength();
//             lastSignal = -113 + 2 * asu;
//             ms_dbm_info = lastSignal;
//             super.onSignalStrengthsChanged(signalStrength);
//         }
//     };

//     @Override
//     protected void onCreate(Bundle savedInstanceState) {
//         super.onCreate(savedInstanceState);
//         tm = ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE));
//         tm.listen(phoneStateListener, 290);
//     }

// }