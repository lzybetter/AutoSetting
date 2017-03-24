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
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

import shanghai.lzybetter.autosetting.Activity.EditView;
import shanghai.lzybetter.autosetting.Activity.MainActivity;
import shanghai.lzybetter.autosetting.Application.MyApplication;
import shanghai.lzybetter.autosetting.Class.If_Action_Save;
import shanghai.lzybetter.autosetting.Class.Setted;
import shanghai.lzybetter.autosetting.R;
import shanghai.lzybetter.autosetting.Reciever.TimeReceiver;
import shanghai.lzybetter.autosetting.Reciever.WIFIStateReciever;

import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_TIME;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_OFF;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_STATE;

/**
 * Created by lzybetter on 2017/3/7.
 */

public class SettedAdapter extends RecyclerView.Adapter<SettedAdapter.ViewHolder> {

    private List<Setted> settedList;
    private Setted setted;
    private Context mContext;
    private ViewHolder viewHolder;
    private int position;

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView list_imageView;
        TextView list_if;
        TextView list_action;
        View setted_item;

        public ViewHolder(View itemView) {
            super(itemView);
            list_imageView = (ImageView)itemView.findViewById(R.id.list_imageView);
            list_if = (TextView)itemView.findViewById(R.id.list_if);
            list_action = (TextView)itemView.findViewById(R.id.list_action);
            setted_item = itemView;
        }
    }

    public SettedAdapter(List<Setted> settedList,Context context) {
        this.settedList = settedList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setted,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        viewHolder = holder;
        holder.setted_item.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                position = holder.getAdapterPosition();
                menu.add(0,0,0,"编辑").setOnMenuItemClickListener(new SettedItemLongClickListener());
                menu.add(0,1,0,"删除").setOnMenuItemClickListener(new SettedItemLongClickListener());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setted = settedList.get(position);
        if(setted.getType() == TYPE_WIFI_STATE){
            holder.list_imageView.setImageResource(R.drawable.ic_wifi);
        }else if(setted.getType() == TYPE_TIME){
            holder.list_imageView.setImageResource(R.drawable.ic_time);
        }else if(setted.getType() == TYPE_WIFI_OFF){
            holder.list_imageView.setImageResource(R.drawable.ic_wifi_off);
        }
        holder.list_if.setText(setted.getList_if());
        holder.list_action.setText(setted.getAction());
    }

    @Override
    public int getItemCount() {
        return settedList.size();
    }

    class SettedItemLongClickListener implements MenuItem.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case 0:
                    Setted selected = settedList.get(position);
                    Intent intent = new Intent(mContext, EditView.class);
                    intent.putExtra("o_if",selected.getList_if());
                    intent.putExtra("o_action",selected.getAction());
                    intent.putExtra("type",selected.getType());
                    mContext.startActivity(intent);
                    break;
                case 1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("确认删除？");
                    builder.setMessage("此内容将删除，且不可恢复，请确认");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Setted selected = settedList.get(position);
                            DataSupport.deleteAll(If_Action_Save.class,
                                    "if_title = ? and action_title = ?",selected.getList_if()
                                    ,selected.getAction());
                            if(selected.getType() == TYPE_WIFI_STATE){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        List<If_Action_Save> if_action_saves = DataSupport.findAll(If_Action_Save.class);
                                        boolean isThereWifi = false;
                                        for(If_Action_Save if_action_save : if_action_saves){
                                            if(if_action_save.getType() == TYPE_WIFI_STATE){
                                                isThereWifi = true;
                                            }
                                        }
                                        if(!isThereWifi){
                                            WIFIStateReciever wifiStateReciever = new WIFIStateReciever();
                                            MyApplication.getContext().unregisterReceiver(wifiStateReciever);
                                        }
                                    }
                                }).run();
                            }else if(selected.getType() == TYPE_TIME){
                                String action = selected.getList_if();
                                String time = action.replace("在","").replace("后","");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(time.split(":")[0]));
                                calendar.set(Calendar.MINUTE,Integer.valueOf(time.split(":")[1]));
                                calendar.set(Calendar.SECOND,0);
                                calendar.set(Calendar.MILLISECOND,0);
                                int id = (int)(calendar.getTimeInMillis()/60/1000);
                                AlarmManager alarmManager = (AlarmManager)mContext
                                        .getSystemService(Context.ALARM_SERVICE);
                                Intent intent_time = new Intent(mContext, TimeReceiver.class);
                                PendingIntent pi = PendingIntent.getBroadcast(mContext,
                                        id,intent_time,0);
                                alarmManager.cancel(pi);
                            }else if(selected.getType() == TYPE_WIFI_OFF){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        List<If_Action_Save> if_action_saves = DataSupport.findAll(If_Action_Save.class);
                                        boolean isThereWifi = false;
                                        for(If_Action_Save if_action_save : if_action_saves){
                                            if(if_action_save.getType() == TYPE_WIFI_OFF){
                                                isThereWifi = true;
                                            }
                                        }
                                        if(!isThereWifi){
                                            WIFIStateReciever wifiStateReciever = new WIFIStateReciever();
                                            MyApplication.getContext().unregisterReceiver(wifiStateReciever);
                                        }
                                    }
                                }).run();
                            }
                            settedList.remove(selected);
                            setted.getSettedAdapter().notifyDataSetChanged();
                            if(settedList.size() == 0){
                                selected.getRecyclerView().setVisibility(View.GONE);
                                selected.getTextView().setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}
