package shanghai.lzybetter.autosetting.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import shanghai.lzybetter.autosetting.Adapter.Choose_Action_Adapter;
import shanghai.lzybetter.autosetting.Class.Choose_Action;
import shanghai.lzybetter.autosetting.R;

import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_RING;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_SILENT;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_TIME;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_VIBRATE;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_OFF;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_STATE;

public class ChooseActionView extends BaseActivity {

    private RecyclerView action_List;
    private List<Choose_Action> choose_actionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);

        String title = null;
        String if_title = null;
        Intent intent = getIntent();
        int if_type = 0;
        boolean isCreateOrEdit = intent.getBooleanExtra("isCreateOrEdit",true);
        if(isCreateOrEdit){
            if_type = intent.getIntExtra("type",0);
            if(if_type == TYPE_WIFI_STATE){
                if_title = intent.getStringExtra("ssid");
                title = "连接到" + if_title + "时...";
            }else if(if_type == TYPE_TIME){
                if_title = intent.getStringExtra("time");
                title = "在" + if_title + "后...";
            }else if(if_type == TYPE_WIFI_OFF){
                if_title = "WIFI连接断开后";
                title = "WIFI连接断开后";
            }
        }else{
            title = "编辑";
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.action_tool);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        action_List = (RecyclerView)findViewById(R.id.choose_action);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        action_List.setLayoutManager(layoutManager);
        Choose_Action choose_action;
        choose_action = new Choose_Action(TYPE_VIBRATE,"震动",if_title,if_type);
        choose_actionList.add(choose_action);
        choose_action = new Choose_Action(TYPE_SILENT,"静音",if_title,if_type);
        choose_actionList.add(choose_action);
        choose_action = new Choose_Action(TYPE_RING,"响铃",if_title,if_type);
        choose_actionList.add(choose_action);
        Choose_Action_Adapter choose_action_adapter = null;
        if(isCreateOrEdit){
            choose_action_adapter = new Choose_Action_Adapter(choose_actionList,this);
        }else{
            choose_action_adapter = new Choose_Action_Adapter(choose_actionList,this,
                    intent.getStringExtra("o_if"),intent.getStringExtra("o_action"));
        }
        action_List.setAdapter(choose_action_adapter);
    }
}
