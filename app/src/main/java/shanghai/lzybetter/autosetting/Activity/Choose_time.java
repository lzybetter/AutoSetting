package shanghai.lzybetter.autosetting.Activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import shanghai.lzybetter.autosetting.R;

import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_TIME;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_STATE;

public class Choose_time extends BaseActivity {

    private TextClock showClock;
    private TextView showSelectedTime;
    private CircleImageView time_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);

        Toolbar toolbar = (Toolbar)findViewById(R.id.choose_time_toolbar);
        toolbar.setTitle("请选择时间");
        setSupportActionBar(toolbar);

        showClock = (TextClock)findViewById(R.id.showClock);
        showSelectedTime = (TextView)findViewById(R.id.showSelectedTime);
        showClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(Choose_time.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute;
                        showSelectedTime.setText(selectedTime);
                        time_confirm.setVisibility(View.VISIBLE);
                    }
                },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
            }
        });

        time_confirm = (CircleImageView)findViewById(R.id.time_confirm);
        time_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCreateOrEdit = getIntent().getBooleanExtra("isCreateOrEdit",true);

                Intent intent;
                if(isCreateOrEdit){
                    intent = new Intent(Choose_time.this,ChooseActionView.class);
                    intent.putExtra("type",TYPE_TIME);
                    intent.putExtra("time",showSelectedTime.getText());
                }else{
                    intent = new Intent(Choose_time.this,EditView.class);
                    String o_if = getIntent().getStringExtra("o_if");
                    String o_action = getIntent().getStringExtra("o_action");
                    intent.putExtra("o_if",o_if);
                    intent.putExtra("o_action",o_action);
                    intent.putExtra("type",TYPE_TIME);
                    String time = (String) showSelectedTime.getText();
                    String n_if = "在" + time + "后";
                    intent.putExtra("n_if",n_if);
                }

                startActivity(intent);
            }
        });
        time_confirm.setVisibility(View.GONE);
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
}
