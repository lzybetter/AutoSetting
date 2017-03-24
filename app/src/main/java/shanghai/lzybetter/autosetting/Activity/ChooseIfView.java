package shanghai.lzybetter.autosetting.Activity;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import shanghai.lzybetter.autosetting.Adapter.Choose_if_Adapter;
import shanghai.lzybetter.autosetting.Class.Choose_if;
import shanghai.lzybetter.autosetting.R;

import static shanghai.lzybetter.autosetting.Application.MyApplication.IF_TIME;
import static shanghai.lzybetter.autosetting.Application.MyApplication.IF_WIFI;
import static shanghai.lzybetter.autosetting.Application.MyApplication.IF_WIFI_OFF;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_TIME;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_OFF;
import static shanghai.lzybetter.autosetting.Application.MyApplication.TYPE_WIFI_STATE;

public class ChooseIfView extends BaseActivity {

    private RecyclerView choose_list;
    private List<Choose_if> if_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_if_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.choose_tool);
        toolbar.setTitle("选择触发条件");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        choose_list = (RecyclerView)findViewById(R.id.choose_list);

        initList();
    }

    private void initList() {
        Choose_if if_wifi = new Choose_if(TYPE_WIFI_STATE,IF_WIFI);
        if_list.add(if_wifi);
        Choose_if if_time = new Choose_if(TYPE_TIME,IF_TIME);
        if_list.add(if_time);
        Choose_if if_wifi_off = new Choose_if(TYPE_WIFI_OFF,IF_WIFI_OFF);
        if_list.add(if_wifi_off);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        choose_list.setLayoutManager(layoutManager);
        Choose_if_Adapter adapter = new Choose_if_Adapter(if_list);
        choose_list.setAdapter(adapter);
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
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }
}
