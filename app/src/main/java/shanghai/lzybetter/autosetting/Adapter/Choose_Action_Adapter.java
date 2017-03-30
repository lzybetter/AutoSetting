package shanghai.lzybetter.autosetting.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Calendar;


import java.util.List;

import shanghai.lzybetter.autosetting.Activity.EditView;
import shanghai.lzybetter.autosetting.Activity.MainActivity;
import shanghai.lzybetter.autosetting.Application.MyApplication;
import shanghai.lzybetter.autosetting.Class.Choose_Action;
import shanghai.lzybetter.autosetting.Class.If_Action_Save;
import shanghai.lzybetter.autosetting.R;
import shanghai.lzybetter.autosetting.Reciever.TimeReceiver;
import shanghai.lzybetter.autosetting.Reciever.WIFIStateReciever;

import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_RING;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_SILENT;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_TIME;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_VIBRATE;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_OFF;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_STATE;

/**
 * Created by lzybetter on 2017/3/9.
 */

public class Choose_Action_Adapter extends RecyclerView.Adapter<Choose_Action_Adapter.ViewHolder> {

    private List<Choose_Action> choose_actionList;
    private Context mContext;
    private String o_if,o_action;
    private boolean isCreateOrEdit;//true表示新创建，false表示编辑

    public Choose_Action_Adapter(List<Choose_Action> choose_actionList,Context context) {
        this.choose_actionList = choose_actionList;
        mContext = context;
        isCreateOrEdit = true;
    }

    public Choose_Action_Adapter(List<Choose_Action> choose_actionList,Context context,
                                 String o_if, String o_action) {
        this.choose_actionList = choose_actionList;
        mContext = context;
        this.o_if = o_if;
        this.o_action = o_action;
        isCreateOrEdit = false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.action_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Choose_Action choose_action = choose_actionList.get(position);
        if(choose_action.getType() == TYPE_RING){
            Glide.with(mContext).load(R.drawable.ic_ring).into(holder.action_image);
        }else if(choose_action.getType() == TYPE_VIBRATE){
            Glide.with(mContext).load(R.drawable.ic_vibrate).into(holder.action_image);
        }else if(choose_action.getType() == TYPE_SILENT){
            Glide.with(mContext).load(R.drawable.ic_silent).into(holder.action_image);
        }
        holder.action_title.setText(choose_action.getAction_title());
        holder.action_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action_title = null;
                if(choose_action.getType() == TYPE_RING){
                    action_title = "响铃";
                }else if(choose_action.getType() == TYPE_VIBRATE){
                    action_title = "震动";
                }else if(choose_action.getType() == TYPE_SILENT){
                    action_title = "静音";
                }
                if(isCreateOrEdit){
                    String if_title = null;
                    if(choose_action.getIf_type() == TYPE_WIFI_STATE){
                        if_title = "连接到" + choose_action.getIf_title() + "时";
                    }else if(choose_action.getIf_type() == TYPE_TIME){
                        if_title = "在" + choose_action.getIf_title() + "后";
                    }else if(choose_action.getIf_type() == TYPE_WIFI_OFF){
                        if_title = "WIFI连接断开时";
                    }
                    final String if_title_temp = if_title;
                    final String action_title_temp = action_title;
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("请确认");
                    builder.setMessage("您选择的是:\n" + if_title + action_title + "\n请确认");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            If_Action_Save if_action_save = new If_Action_Save();
                            if_action_save.setType(choose_action.getIf_type());
                            if_action_save.setIf_title(if_title_temp);
                            if_action_save.setAction_title(action_title_temp);
                            if_action_save.setAction_type(choose_action.getType());
                            if_action_save.save();
                            if(choose_action.getIf_type() == TYPE_WIFI_STATE){
                                IntentFilter wifiFilter = new IntentFilter();
                                wifiFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                                wifiFilter.addAction(Intent.ACTION_USER_PRESENT);
                                WIFIStateReciever wifiStateReciever = new WIFIStateReciever();
                                MyApplication.getContext().registerReceiver(wifiStateReciever,wifiFilter);
                            }else if(choose_action.getIf_type() == TYPE_TIME){
                                String time = if_title_temp.replace("在","").replace("后","");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(time.split(":")[0]));
                                calendar.set(Calendar.MINUTE,Integer.valueOf(time.split(":")[1]));
                                calendar.set(Calendar.SECOND,0);
                                calendar.set(Calendar.MILLISECOND,0);
                                AlarmManager alarmManager = (AlarmManager)mContext
                                        .getSystemService(Context.ALARM_SERVICE);
                                int id = (int)(calendar.getTimeInMillis()/60/1000);
                                Intent intent_time = new Intent(mContext, TimeReceiver.class);
                                intent_time.putExtra("action_type",choose_action.getType());
                                PendingIntent pi = PendingIntent.getBroadcast(mContext,
                                        id,intent_time,0);
                                if(calendar.getTimeInMillis() <= System.currentTimeMillis()){
                                    //设定的时间比当前时间小，将触发时间设为第二天的设定时刻
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                            calendar.getTimeInMillis()+24*60*60*1000,24*60*60*1000,
                                            pi);
                                }else{
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                            calendar.getTimeInMillis(),24*60*60*1000,
                                            pi);
                                }
                            }else if(choose_action.getIf_type() == TYPE_WIFI_OFF){
                                PackageManager manager =  mContext.getPackageManager();
                                Intent test = new Intent("ConnectivityManager.CONNECTIVITY_ACTION");
                                List<ResolveInfo> resolveInfos = manager.queryBroadcastReceivers(test,
                                        PackageManager.GET_INTENT_FILTERS);
                                if(resolveInfos.size() == 0){
                                    IntentFilter wifiFilter = new IntentFilter();
                                    wifiFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                                    wifiFilter.addAction(Intent.ACTION_USER_PRESENT);
                                    WIFIStateReciever wifiStateReciever = new WIFIStateReciever();
                                    MyApplication.getContext().registerReceiver(wifiStateReciever,wifiFilter);
                                }
                            }
                            Intent intent = new Intent(mContext, MainActivity.class);
                            mContext.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }else{
                    Intent intent = new Intent(mContext, EditView.class);
                    intent.putExtra("n_action",action_title);
                    intent.putExtra("o_if",o_if);
                    intent.putExtra("o_action",o_action);
                    intent.putExtra("n_action_type",choose_action.getType());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return choose_actionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView action_image;
        TextView action_title;
        CardView action_item;
        public ViewHolder(View itemView) {
            super(itemView);
            action_image = (ImageView)itemView.findViewById(R.id.action_image);
            action_title = (TextView)itemView.findViewById(R.id.action_title);
            action_item = (CardView)itemView.findViewById(R.id.action_item);
        }
    }
}
