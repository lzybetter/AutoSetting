package shanghai.lzybetter.autosetting.Reciever;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import org.litepal.crud.DataSupport;

import java.util.List;

import shanghai.lzybetter.autosetting.Action.Then;
import shanghai.lzybetter.autosetting.Application.MyApplication;
import shanghai.lzybetter.autosetting.Class.If_Action_Save;

import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_RING;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_SILENT;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_VIBRATE;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_OFF;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_STATE;
import static shanghai.lzybetter.autosetting.Application.MyApplication.getContext;

/**
 * Created by lzybetter on 2017/3/14.
 */

public class WIFIStateReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
            if (!action.equals(Intent.ACTION_USER_PRESENT)) {
                WifiManager wifiManager = (WifiManager) MyApplication.getContext()
                        .getSystemService(Context.WIFI_SERVICE);
                ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    String ssid = wifiManager.getConnectionInfo().getSSID();
                    ssid = ssid.replace("\"", "");
                    List<If_Action_Save> if_action_saves = DataSupport.findAll(If_Action_Save.class);
                    for (If_Action_Save if_action_save : if_action_saves) {
                        String if_title = if_action_save.getIf_title()
                                .replace("连接到", "").replace("时", "");
                        if (if_action_save.getType() == TYPE_WIFI_STATE) {
                            if (if_title.equals(ssid)) {
                                switch (if_action_save.getAction_type()) {
                                    case TYPE_VIBRATE:
                                        Then.startVibrateEMode();
                                        break;
                                    case TYPE_RING:
                                        Then.startRingingMode();
                                        break;
                                    case TYPE_SILENT:
                                        Then.startSilentMode();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                } else {
                    List<If_Action_Save> if_action_saves = DataSupport.findAll(If_Action_Save.class);
                    for (If_Action_Save if_action_save : if_action_saves) {
                        if (if_action_save.getType() == TYPE_WIFI_OFF) {
                            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                            Intent intent_delay = new Intent(getContext(), DelayReceiver.class);
                            intent_delay.putExtra("action_type", if_action_save.getAction_type());
                            PendingIntent pi = PendingIntent.getBroadcast(getContext(),
                                    0, intent_delay, 0);
                            long delayTime = System.currentTimeMillis() + 30 * 1000;
                            alarmManager.set(AlarmManager.RTC_WAKEUP,
                                    delayTime, pi);//延时30s，如果30s后仍未连接到所指定的WIFI则执行内容
                        }
                    }
                }
            }
    }
}
