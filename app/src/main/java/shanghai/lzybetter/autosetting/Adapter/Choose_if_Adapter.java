package shanghai.lzybetter.autosetting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import shanghai.lzybetter.autosetting.Activity.ChooseActionView;
import shanghai.lzybetter.autosetting.Activity.Choose_Wifi;
import shanghai.lzybetter.autosetting.Activity.Choose_time;
import shanghai.lzybetter.autosetting.Class.Choose_if;
import shanghai.lzybetter.autosetting.R;

import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_TIME;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_OFF;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_STATE;

/**
 * Created by lzybetter on 2017/3/7.
 */

public class Choose_if_Adapter extends RecyclerView.Adapter<Choose_if_Adapter.ViewHolder> {

    private List<Choose_if> choose_if_list = new ArrayList<>();
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView choose_if_image;
        TextView choose_list_if;
        CardView choose_if_item;

        public ViewHolder(View itemView) {
            super(itemView);

            choose_if_item = (CardView)itemView.findViewById(R.id.choose_if_item);
            choose_if_image = (ImageView)itemView.findViewById(R.id.choose_if_image);
            choose_list_if = (TextView)itemView.findViewById(R.id.choose_list_if);
        }
    }

    public Choose_if_Adapter(List<Choose_if> choose_if_list) {
        this.choose_if_list = choose_if_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.choose_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Choose_if choose_if = choose_if_list.get(position);
        if(choose_if.getType() == TYPE_WIFI_STATE){
            Glide.with(mContext).load(R.drawable.choose_wifi).into(holder.choose_if_image);
        }else if(choose_if.getType() == TYPE_TIME){
            Glide.with(mContext).load(R.drawable.choose_clock).into(holder.choose_if_image);
        }else if(choose_if.getType() == TYPE_WIFI_OFF){
            Glide.with(mContext).load(R.drawable.choose_wifi_off).into(holder.choose_if_image);
        }
        holder.choose_list_if.setText(choose_if.getChoose_list_if());
        holder.choose_if_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choose_if.getType() == TYPE_WIFI_STATE){
                    Intent intent = new Intent(mContext, Choose_Wifi.class);
                    intent.putExtra("isCreateOrEdit",true);
                    mContext.startActivity(intent);
                }else if(choose_if.getType() == TYPE_TIME){
                    Intent intent = new Intent(mContext, Choose_time.class);
                    intent.putExtra("isCreateOrEdit",true);
                    mContext.startActivity(intent);
                }else if(choose_if.getType() == TYPE_WIFI_OFF){
                    Intent intent = new Intent(mContext, ChooseActionView.class);
                    intent.putExtra("isCreateOrEdit",true);
                    intent.putExtra("type",TYPE_WIFI_OFF);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return choose_if_list.size();
    }


}
