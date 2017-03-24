package shanghai.lzybetter.autosetting.Action;


import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import shanghai.lzybetter.autosetting.Application.MyApplication;

/**
 * Created by lzybetter on 2016/11/9.
 */

public class Then {


    public static void startWifi(){
        final WifiManager wifiManager = (WifiManager) MyApplication.getContext().
                getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()) {
            //开启wifi需要几秒钟，如果UI超过5s没有反应，就会FC，因此放到子线程里来做
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    wifiManager.setWifiEnabled(true);
                }
            };
            runnable.run();
        }
    }

    public static void stopWifi(){
        WifiManager wifiManager = (WifiManager)MyApplication.getContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public static void startVibrateEMode(){
        AudioManager audioManager = (AudioManager) MyApplication.getContext()
                .getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
    }

    public static void startRingingMode(){
        AudioManager audioManager = (AudioManager)MyApplication.getContext()
                .getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    public static void startSilentMode(){
        AudioManager audioManager = (AudioManager)MyApplication.getContext()
                .getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

}
