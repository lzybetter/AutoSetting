package shanghai.lzybetter.autosetting.Activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzybetter on 2017/3/7.
 */

public class ActivityCollector {

    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void allFinish(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
