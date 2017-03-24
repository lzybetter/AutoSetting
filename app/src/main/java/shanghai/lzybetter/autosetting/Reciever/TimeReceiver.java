package shanghai.lzybetter.autosetting.Reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import shanghai.lzybetter.autosetting.Action.Then;

import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_RING;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_SILENT;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_VIBRATE;

/**
 * Created by lzybetter on 2017/3/19.
 */

public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
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
