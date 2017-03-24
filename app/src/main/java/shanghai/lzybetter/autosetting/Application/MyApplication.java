package shanghai.lzybetter.autosetting.Application;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by lzybetter on 2017/3/7.
 */

public class MyApplication extends Application {

    public static final String SAVE_FILE = "AUTO_SETTING";
    public static final int TYPE_TIME  = 1;
    public static final int TYPE_WIFI_STATE = 2;
    public static final int TYPE_WIFI_OFF = 3;
    public static final String IF_WIFI = "连接到指定WIFI时";
    public static final String IF_TIME = "在指定的时间内";
    public static final String IF_WIFI_OFF = "WIFI连接断开时";
    public static final int TYPE_RING = 11;
    public static final int TYPE_VIBRATE = 12;
    public static final int TYPE_SILENT = 13;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext() {
        return context;
    }


}
