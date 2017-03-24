package shanghai.lzybetter.autosetting.Reciever;

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

public class DelayReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isAction = true;
        WifiManager wifiManager = (WifiManager) MyApplication.getContext()
                .getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI){
            //已连接到WIFI，检测是否为指定的WIFI
            String ssid = wifiManager.getConnectionInfo().getSSID();
            ssid = ssid.replace("\"","");
            List<If_Action_Save> if_action_saves = DataSupport.findAll(If_Action_Save.class);
            for(If_Action_Save if_action_save : if_action_saves){
                String if_title = if_action_save.getIf_title()
                        .replace("连接到","").replace("时","");
                if(ssid.equals(if_title)){
                    isAction = false;
                }
            }
        }else{
            //没有连接到WIFI
            isAction = true;
        }
        if(isAction){
            //isAction为true，执行所储存的操作
            int type = intent.getIntExtra("action_type",0);
            switch (type){
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
