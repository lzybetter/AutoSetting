package shanghai.lzybetter.autosetting.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import shanghai.lzybetter.autosetting.Adapter.WifiAdapter;
import shanghai.lzybetter.autosetting.Application.MyApplication;
import shanghai.lzybetter.autosetting.Class.WifiSSID;
import shanghai.lzybetter.autosetting.R;

public class Choose_Wifi extends BaseActivity {

    private RecyclerView wifi_list;
    private List<WifiSSID> ssid_list = new ArrayList<>();
    private WifiManager wifiManager ;
    private boolean saveOrCurrent = false; //false代表save，true代表Current
    private WifiAdapter wifiAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__wifi);

        Toolbar toolbar = (Toolbar)findViewById(R.id.wifi_list_toolbar);
        toolbar.setTitle("选择要使用的wifi");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);

        wifi_list = (RecyclerView)findViewById(R.id.wifi_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        wifi_list.setLayoutManager(layoutManager);

        if(ContextCompat.checkSelfPermission(Choose_Wifi.this,Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Choose_Wifi.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE,},1);
        }

        getSavedWifi();
    }

    private void getSavedWifi(){
        List<WifiConfiguration> wifiConfigurationList = wifiManager.getConfiguredNetworks();
        if(ssid_list != null){
            ssid_list.clear();
        }
        for(WifiConfiguration wifiConfiguration : wifiConfigurationList){
            String ssid = wifiConfiguration.SSID;
            ssid = ssid.replace("\"","");
            WifiSSID wifiSSID = new WifiSSID(ssid);
            ssid_list.add(wifiSSID);
        }
        boolean isCreateOrEdit = getIntent().getBooleanExtra("isCreateOrEdit",true);
        if(isCreateOrEdit){
            wifiAdapter = new WifiAdapter(ssid_list,this,isCreateOrEdit);
        }else {
            String o_if = getIntent().getStringExtra("o_if");
            String o_action = getIntent().getStringExtra("o_action");
            wifiAdapter = new WifiAdapter(ssid_list,this,isCreateOrEdit,
                    o_if,o_action);
        }
        wifi_list.setAdapter(wifiAdapter);
    }

    private void getAvailableWifi(){
        wifiManager.startScan();
        List<ScanResult> scanResultList = wifiManager.getScanResults();
        List<ScanResult> finalList = noSaveSSID(scanResultList);
        if(ssid_list != null){
            ssid_list.clear();
        }
        for (ScanResult scanResult : finalList){
            String ssid = scanResult.SSID;
            WifiSSID wifiSSID = new WifiSSID(ssid);
            ssid_list.add(wifiSSID);
        }
        boolean isCreateOrEdit = getIntent().getBooleanExtra("isCreateOrEdit",true);
        wifiAdapter = new WifiAdapter(ssid_list,this,isCreateOrEdit);
        wifi_list.setAdapter(wifiAdapter);
    }

    private List<ScanResult> noSaveSSID(List<ScanResult> scanResultList) {
        List<ScanResult> newList = new ArrayList<>();
        for(ScanResult result : scanResultList){
            if(!TextUtils.isEmpty(result.SSID) && !containName(newList, result.SSID)){
                newList.add(result);
            }
        }
        return newList;
    }

    private boolean containName(List<ScanResult> newList, String ssid) {
        boolean isContain = false;
        for(ScanResult saveList : newList){
            if(saveList.SSID.equals(ssid)){
                isContain = true;
            }
        }
        return isContain;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wifi_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_switch:
                if(saveOrCurrent){
                    getSavedWifi();
                    saveOrCurrent = false;
                    Toast.makeText(this,"保存的WIFI",Toast.LENGTH_SHORT).show();
                }else {
                    getAvailableWifi();
                    saveOrCurrent = true;
                    Toast.makeText(this,"可用的WIFI",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_choose_exit:
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
