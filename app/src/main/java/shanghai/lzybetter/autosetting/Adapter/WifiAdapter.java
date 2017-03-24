package shanghai.lzybetter.autosetting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import shanghai.lzybetter.autosetting.Activity.ChooseActionView;
import shanghai.lzybetter.autosetting.Activity.EditView;
import shanghai.lzybetter.autosetting.Class.WifiSSID;
import shanghai.lzybetter.autosetting.R;

import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_STATE;

/**
 * Created by lzybetter on 2017/3/8.
 */

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {

    private List<WifiSSID> wifi_ssid;
    private Context mContext;
    private boolean isCreateOrEdit;//true表示新创建，false表示编辑
    private String o_if,o_action;

    public WifiAdapter(List<WifiSSID> wifi_ssid, Context context,boolean isCreateOrEdit,
                       String o_if,String o_action) {
        this.wifi_ssid = wifi_ssid;
        mContext = context;
        this.isCreateOrEdit = isCreateOrEdit;
        this.o_if = o_if;
        this.o_action = o_action;
    }

    public WifiAdapter(List<WifiSSID> wifi_ssid, Context context,boolean isCreateOrEdit){
        this.wifi_ssid = wifi_ssid;
        mContext = context;
        this.isCreateOrEdit = isCreateOrEdit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wifi_list,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.wifi_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String ssid = (String)viewHolder.wifi_ssid.getText();
                if(isCreateOrEdit){
                    intent = new Intent(mContext, ChooseActionView.class);
                    intent.putExtra("ssid",ssid);
                }else {
                    intent = new Intent(mContext, EditView.class);
                    intent.putExtra("o_if",o_if);
                    intent.putExtra("o_action",o_action);
                    String n_if = "连接到" + ssid + "时";
                    intent.putExtra("n_if",n_if);
                }
                intent.putExtra("type",TYPE_WIFI_STATE);
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WifiSSID wifiSSID = wifi_ssid.get(position);
        holder.wifi_logo.setImageResource(R.drawable.ic_wifi);
        holder.wifi_ssid.setText(wifiSSID.getSSID());
    }

    @Override
    public int getItemCount() {
        return wifi_ssid.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView wifi_logo;
        TextView wifi_ssid;
        LinearLayout wifi_list;
        public ViewHolder(View itemView) {
            super(itemView);
            wifi_logo = (ImageView)itemView.findViewById(R.id.wifi_logo);
            wifi_ssid = (TextView)itemView.findViewById(R.id.wifi_ssid);
            wifi_list = (LinearLayout)itemView.findViewById(R.id.wifi_list);
        }
    }

}
