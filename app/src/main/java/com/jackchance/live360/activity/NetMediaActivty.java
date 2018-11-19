package com.jackchance.live360.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.jackchance.live360.util.Settings;
import com.jackchance.live360.model.NetDbAdapter;
import com.jackchance.live360.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NetMediaActivty extends AppCompatActivity implements View.OnClickListener{
    private Button net_setting;
    private Button net_history;
    private Button net_scan;
    private Button net_startvedio;
    private EditText texturl;
    private ListView netlist;


    private SharedPreferences settings;
    private String choosevr;

    private ArrayList<Map<String,String>> listurl;

    private Cursor cursor;
    private NetDbAdapter NetDb;

    @Override
    protected void onResume() {
        choosevr = settings.getString("choose_vr","信息为空");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);

        texturl = (EditText)findViewById(R.id.search_net);
        net_startvedio = (Button)findViewById(R.id.btn_net_vedio);
        netlist = (ListView)findViewById(R.id.list_net);

        settings = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        choosevr = settings.getString("choose_vr","信息为空");

        final ArrayList<Map<String,String>> localurl = new ArrayList<Map<String,String>>();
        Map<String,String> map1 = new HashMap<String,String>();
        map1.put("url","rtmp://live.hkstv.hk.lxdns.com/live/hks");
        Map<String,String> map2 = new HashMap<String,String>();
        map2.put("url","http://playback.ks.zb.mi.com/record/live/107578_1467605748/hls/107578_1467605748.m3u8");
        Map<String,String> map3 = new HashMap<String,String>();
        map3.put("url", "http://cxy.kssws.ks-cdn.com/h265_56c26b7a7dc5f6043.mp4");
        Map<String,String> map4 = new HashMap<String,String>();
        map4.put("url", "http://appvideo.www.gov.cn/gov/201607/23/385812/media_14168@2x.mp4");
        Map<String,String> map5 = new HashMap<String,String>();
        map5.put("url", "http://120.132.75.127/vod/flv/wanwan/wanwan_2537.flv");
        Map<String,String> map6 = new HashMap<String,String>();
        map6.put("url", "http://120.132.75.127/vod/flv/wanwan/wanwan_2320.flv");
        localurl.add(map1);
        localurl.add(map2);
        localurl.add(map3);
        localurl.add(map4);
        localurl.add(map5);
        localurl.add(map6);

        SimpleAdapter adapter = new SimpleAdapter(this,localurl,R.layout.list_history,new String[]{"url"},new int[]{R.id.list_history_txt});


        netlist.setAdapter(adapter);

        netlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                texturl.setText(localurl.get(i).get("url"));
            }
        });

        net_startvedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = texturl.getText().toString();
                NetDb = new NetDbAdapter(NetMediaActivty.this);
                NetDb.open();

                if(NetDb.getData(path)){
                    NetDb.updateData(path);
                }else{
                    NetDb.createDate(path);
                }
                NetDb.close();

                if(choosevr.equals(Settings.VRON)){
                    Intent intent  = new Intent(NetMediaActivty.this,TestVideoActivity.class);
                    intent.putExtra("path",path);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(NetMediaActivty.this,VideoPlayerActivity.class);
                    intent.putExtra("path",path);
                    startActivity(intent);
                }

            }
        });
        setActionBarLayout(R.layout.net_actionbar,this);
    }

    public void setActionBarLayout(int layoutId, Context mContext) {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            LayoutInflater inflator = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(layoutId, new LinearLayout(mContext), false);
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(v, layout);

            net_history = (Button)findViewById(R.id.net_history);
            net_setting = (Button)findViewById(R.id.net_setting);
            net_scan = (Button)findViewById(R.id.net_scan);
            net_scan.setOnClickListener(this);
            net_history.setOnClickListener(this);
            net_setting.setOnClickListener(this);

        }else{
            Toast.makeText(NetMediaActivty.this, "ActionBar不存在", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.net_history:

                listurl = new ArrayList<Map<String,String>>();
                NetDb = new NetDbAdapter(NetMediaActivty.this);
                NetDb.open();
                cursor = NetDb.getAllData();
                cursor.moveToFirst();
                if(cursor.getCount()>0){
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("url", cursor.getString(cursor.getColumnIndex(NetDbAdapter.KEY_PATH)));
                    listurl.add(map);
                }
                while(cursor.moveToNext()){
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("url", cursor.getString(cursor.getColumnIndex(NetDbAdapter.KEY_PATH)));
                    listurl.add(map);
                }
                SimpleAdapter adapter = new SimpleAdapter(this,listurl,R.layout.list_history,new String[]{"url"},new int[]{R.id.list_history_txt});

                netlist.setAdapter(adapter);
                netlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        texturl.setText(listurl.get(i).get("url"));
                    }
                });

                break;
            case R.id.net_setting:
                Intent intent = new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.net_scan:
                Intent intent1 = new Intent(this,CaptureActivity.class);
                startActivityForResult(intent1,0);

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            texturl.setText(scanResult);
        }
    }
}
