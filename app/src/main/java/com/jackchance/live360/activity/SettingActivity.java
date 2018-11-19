package com.jackchance.live360.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.jackchance.live360.util.Settings;
import com.jackchance.live360.R;

public class SettingActivity extends AppCompatActivity{

    public static final int GET_JSONDATA = 0x1001;
    public static final int PLAY_VIDEO = 0x1002;


    private SharedPreferences settings ;
    private SharedPreferences.Editor editor;
    private Switch debugswitch;
    private Switch vrswitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settings = getSharedPreferences("SETTINGS",Context.MODE_PRIVATE);
        editor = settings.edit();
        String choosedebug = settings.getString("choose_debug","信息为空");
        String choosevr = settings.getString("choose_vr","信息为空");

        debugswitch = (Switch)findViewById(R.id.switch_set);
        vrswitch = (Switch)findViewById(R.id.switch_vr);

        initSetting(choosedebug,choosevr);

        vrswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    editor.putString("choose_vr", Settings.VRON);
                    Toast.makeText(SettingActivity.this, "VR被打开", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("choose_vr",Settings.VROFF);
                    Toast.makeText(SettingActivity.this, "VR被关闭", Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }
        });

        debugswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    editor.putString("choose_debug",Settings.DEBUGON);
                    Toast.makeText(SettingActivity.this, "Debug被打开", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("choose_debug",Settings.DEBUGOFF);
                    Toast.makeText(SettingActivity.this, "Debug被关闭", Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }
        });
    }

    private void initSetting(String choosedebug,String choosevr) {

        editor.putString("choose_decode",Settings.USEHARD);
        editor.putString("choose_view",Settings.USETEXTURE);

        switch (choosedebug){
            case Settings.DEBUGOFF:
                debugswitch.setChecked(false);
                break;
            case Settings.DEBUGON:
                debugswitch.setChecked(true);
                break;
            default:
                debugswitch.setChecked(false);
                editor.putString("choose_debug",Settings.DEBUGOFF);
                break;
        }
        switch (choosevr){
            case Settings.VROFF:
                vrswitch.setChecked(false);
                break;
            case Settings.VRON:
                vrswitch.setChecked(true);
                break;
            default:
                vrswitch.setChecked(false);
                editor.putString("choose_vr",Settings.VROFF);
                break;
        }
        editor.commit();

    }


}
