package shanghai.lzybetter.autosetting.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

import shanghai.lzybetter.autosetting.Class.If_Action_Save;
import shanghai.lzybetter.autosetting.R;
import shanghai.lzybetter.autosetting.Reciever.TimeReceiver;

import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_RING;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_SILENT;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_TIME;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_VIBRATE;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_STATE;

public class EditView extends BaseActivity {

    private TextView o_if_show,o_action_show;
    private TextView n_if_show,n_action_show;
    private LinearLayout n_if_lin, n_action_lin;
    private int n_action_type;
    private int o_action_type;


    private String o_if,o_action;
    private String n_if,n_action;
    private int type;
    private boolean isSaved = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_toolbar);
        toolbar.setTitle("编辑");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        o_if_show = (TextView)findViewById(R.id.o_if_show);
        o_action_show = (TextView)findViewById(R.id.o_action_show);
        n_if_lin = (LinearLayout)findViewById(R.id.n_if_lin);
        n_if_show = (TextView)findViewById(R.id.n_if_show);
        n_action_lin = (LinearLayout)findViewById(R.id.n_action_lin);
        n_action_show = (TextView)findViewById(R.id.n_action_show);

        Intent intent = getIntent();
        o_if = intent.getStringExtra("o_if");
        o_action = intent.getStringExtra("o_action");
        switch (o_action){
            case "静音":
                o_action_type = TYPE_SILENT;
                break;
            case "震动":
                o_action_type = TYPE_VIBRATE;
                break;
            case "响铃":
                o_action_type = TYPE_RING;
                break;
            default:
                break;
        }
        type = intent.getIntExtra("type",0);
        n_if = intent.getStringExtra("n_if");
        n_action = intent.getStringExtra("n_action");
        n_action_type = intent.getIntExtra("n_action_type",0);


        o_if_show.setText(o_if);
        o_action_show.setText(o_action);
        o_if_show.setOnClickListener(new EditListener());
        o_action_show.setOnClickListener(new EditListener());

        if( n_if != "" && n_if != null && !n_if.equals(o_if)){
            n_if_lin.setVisibility(View.VISIBLE);
            n_if_show.setText(n_if);
            isSaved = false;
        }

        if(n_action != "" && n_action != null && !n_action.equals(o_action)){
            n_action_lin.setVisibility(View.VISIBLE);
            n_action_show.setText(n_action);
            isSaved = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_save:
                if(!isSaved){
                    If_Action_Save if_action_save = new If_Action_Save();
                    if(n_if_show.getText() != null){
                        if_action_save.setIf_title(n_if);
                    }
                    if(n_action_show.getText() != null){
                        if_action_save.setAction_title(n_action);
                        if_action_save.setAction_type(n_action_type);
                    }
                    if(type == TYPE_TIME){
                        if(n_if_show.getText() != null) {
                            //如果修改的触发条件是时间，那么需要取消原有的闹钟设置，改为现有的时间
                            String time = n_if.replace("在", "").replace("后", "");
                            Calendar calendar_n = Calendar.getInstance();
                            calendar_n.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
                            calendar_n.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
                            calendar_n.set(Calendar.SECOND, 0);
                            calendar_n.set(Calendar.MILLISECOND, 0);
                            int id_n = (int) (calendar_n.getTimeInMillis() / 60 / 1000);//生成现在的新ID
                            time = o_if.replace("在", "").replace("后", "");
                            Calendar calendar_o = Calendar.getInstance();
                            calendar_o.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
                            calendar_o.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
                            calendar_o.set(Calendar.SECOND, 0);
                            calendar_o.set(Calendar.MILLISECOND, 0);
                            int id_o = (int) (calendar_o.getTimeInMillis() / 60 / 1000);//原来的ID
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent intent_time = new Intent(this, TimeReceiver.class);
                            PendingIntent pi_o = PendingIntent.getBroadcast(this,
                                    id_o,intent_time,0);
                            alarmManager.cancel(pi_o);//取消原始的闹钟
                            PendingIntent pi_n = PendingIntent.getBroadcast(this,
                                    id_n,intent_time,0);
                            if(n_action_show.getText() != null){
                                intent_time.putExtra("action_type",n_action_type);
                            }else {
                                intent_time.putExtra("action_type",o_action_type);
                            }
                            //设置新的闹钟
                            if(calendar_n.getTimeInMillis() <= System.currentTimeMillis()){
                                //设定的时间比当前时间小，将触发时间设为第二天的设定时刻
                                long trrige_Time = calendar_n.getTimeInMillis()+24*60*60*1000;
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                        trrige_Time,24*60*60*1000, pi_n);
                            }else{
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                        calendar_n.getTimeInMillis(),24*60*60*1000,
                                        pi_n);
                            }
                        }
                    }
                    if_action_save.updateAll("if_title = ? and action_title = ?",o_if,o_action);
                    Toast.makeText(EditView.this, "保存成功", Toast.LENGTH_SHORT).show();
                    isSaved = true;//已经保存，将isSaved设置为true
                }
                Intent intent1 = new Intent(EditView.this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.edit_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("确认删除？");
                builder.setMessage("此内容将删除，且不可恢复，请确认");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(If_Action_Save.class,
                                "if_title = ? and action_title = ?",o_if
                                ,o_action);
                        if(type == TYPE_TIME){
                            String time = n_if.replace("在", "").replace("后", "");
                            Calendar calendar_o = Calendar.getInstance();
                            calendar_o.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
                            calendar_o.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
                            calendar_o.set(Calendar.SECOND, 0);
                            calendar_o.set(Calendar.MILLISECOND, 0);
                            int id_o = (int) (calendar_o.getTimeInMillis() / 60 / 1000);//原来的ID
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent intent_time = new Intent(EditView.this, TimeReceiver.class);
                            intent_time.putExtra("action_type",n_action_type);
                            PendingIntent pi_o = PendingIntent.getBroadcast(EditView.this,
                                    id_o,intent_time,0);
                            alarmManager.cancel(pi_o);//取消闹钟
                        }
                        Intent intent = new Intent(EditView.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
            case R.id.edit_exit:
                AlertDialog.Builder builder_exit = new AlertDialog.Builder(this);
                builder_exit.setTitle("确认退出？");
                builder_exit.setMessage("确认退出么？");
                builder_exit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCollector.allFinish();
                    }
                });
                builder_exit.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder_exit.setCancelable(false);
                builder_exit.show();
            case android.R.id.home:
                if(!isSaved){
                    final AlertDialog.Builder delet_builder = new AlertDialog.Builder(this);
                    delet_builder.setTitle("请确认");
                    delet_builder.setMessage("您尚未保存，是否舍弃修改的内容");
                    delet_builder.setCancelable(false);
                    delet_builder.setPositiveButton("是，放弃修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(EditView.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    delet_builder.setNegativeButton("否，返回保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    delet_builder.show();
                }else{
                    Intent intent = new Intent(EditView.this,MainActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }

    class EditListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.o_if_show:
                    Intent intent;
                    if(type == TYPE_WIFI_STATE){
                        intent = new Intent(EditView.this,Choose_Wifi.class);
                    }else {
                        intent = new Intent(EditView.this,Choose_time.class);
                    }
                    intent.putExtra("isCreateOrEdit",false);
                    intent.putExtra("o_if",o_if);
                    intent.putExtra("o_action",o_action);
                    startActivity(intent);
                    break;
                case R.id.o_action_show:
                    Intent intent_action = new Intent(EditView.this,ChooseActionView.class);
                    intent_action.putExtra("isCreateOrEdit",false);
                    intent_action.putExtra("o_if",o_if);
                    intent_action.putExtra("o_action",o_action);
                    startActivity(intent_action);
                    break;
                default:
                    break;
            }
        }
    }
}
