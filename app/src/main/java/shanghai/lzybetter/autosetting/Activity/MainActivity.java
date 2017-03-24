package shanghai.lzybetter.autosetting.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import shanghai.lzybetter.autosetting.Application.MyApplication;
import shanghai.lzybetter.autosetting.Class.If_Action_Save;
import shanghai.lzybetter.autosetting.R;
import shanghai.lzybetter.autosetting.Class.Setted;
import shanghai.lzybetter.autosetting.Adapter.SettedAdapter;
import shanghai.lzybetter.autosetting.Reciever.TimeReceiver;
import shanghai.lzybetter.autosetting.Reciever.WIFIStateReciever;

import static android.view.View.GONE;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_TIME;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_OFF;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_STATE;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SettedAdapter settedAdapter;

    private TextView main_empty;

    private List<Setted> settedList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.main_recyclerView);
        main_empty = (TextView)findViewById(R.id.main_empty);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.main_fab);
        fab.setOnClickListener(new FabOnClickListener());

    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        settedAdapter = new SettedAdapter(settedList,this);
        getSaveSetting();
        if(settedList.size() == 0){
            recyclerView.setVisibility(GONE);
            main_empty.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            main_empty.setVisibility(GONE);
        }
        recyclerView.setAdapter(settedAdapter);
    }

    private void getSaveSetting(){
        List<If_Action_Save> if_action_saves = DataSupport.findAll(If_Action_Save.class);
        boolean isWifi = false;
        for(If_Action_Save if_action_save : if_action_saves){
            if(if_action_save.getIf_title() != "" && if_action_save.getIf_title() != null
                    && if_action_save.getAction_title() != "" && if_action_save.getAction_title() != null){
                Setted newSetting = new Setted(if_action_save.getType()
                        ,if_action_save.getIf_title(),if_action_save.getAction_title());
                settedList.add(newSetting);
                if(if_action_save.getType() == TYPE_WIFI_STATE){
                    isWifi = true;
                }else if(if_action_save.getType() == TYPE_TIME){
                    String action = newSetting.getList_if();
                    String time = action.replace("在","").replace("后","");
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(time.split(":")[0]));
                    calendar.set(Calendar.MINUTE,Integer.valueOf(time.split(":")[1]));
                    calendar.set(Calendar.SECOND,0);
                    int id = (int)(calendar.getTimeInMillis()/60/1000);
                    Intent intent_time = new Intent(this, TimeReceiver.class);
                    intent_time.putExtra("action_type",if_action_save.getAction_type());
                    PendingIntent pi = PendingIntent.getBroadcast(this, id,intent_time,
                            PendingIntent.FLAG_NO_CREATE);
                    if(pi == null){
                        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),24*60*60*1000,pi
                                );
                    }
                }else if(if_action_save.getType() == TYPE_WIFI_OFF){
                    isWifi = true;
                }
                newSetting.setSettedAdapter(settedAdapter);
                newSetting.setRecyclerView(recyclerView);
                newSetting.setTextView(main_empty);
                if(recyclerView.getVisibility() == GONE){
                    recyclerView.setVisibility(View.VISIBLE);
                    main_empty.setVisibility(GONE);
                }
            }
        }
        if(isWifi){
            IntentFilter wifiFilter = new IntentFilter();
            wifiFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            wifiFilter.addAction(Intent.ACTION_USER_PRESENT);
            WIFIStateReciever wifiStateReciever = new WIFIStateReciever();
            MyApplication.getContext().registerReceiver(wifiStateReciever,wifiFilter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("确认退出？");
                builder.setMessage("确认退出么？");
                builder.setCancelable(false);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCollector.allFinish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
        }
        return true;
    }

    class FabOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,ChooseIfView.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认退出？");
        builder.setMessage("确认退出么？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCollector.allFinish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        settedList.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
