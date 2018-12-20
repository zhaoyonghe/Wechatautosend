package com.example.asus.wechatautosend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.asus.wechatautosend.AutoSendMsgService.LABELNAME;
import static com.example.asus.wechatautosend.AutoSendMsgService.COLLECTNAME;
import static com.example.asus.wechatautosend.AutoSendMsgService.CONTACTDONE;
import static com.example.asus.wechatautosend.AutoSendMsgService.GROUPDONE;

public class MainActivity extends AppCompatActivity {
    private EditText labelname,collectname;
    private TextView send;
    private Switch contactswitch;
    private Switch groupswitch;
    private AccessibilityManager accessbilityManager;
    private Boolean needcontactsend = true;
    private Boolean needgroupsend = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = (TextView) findViewById(R.id.send);
        labelname = (EditText)findViewById(R.id.labelname);
        collectname = (EditText)findViewById(R.id.collectname);
        contactswitch = (Switch)findViewById(R.id.contactswitch);
        contactswitch.setChecked(true);
        contactswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                needcontactsend = b;
            }
        });
        groupswitch = (Switch)findViewById(R.id.groupswitch);
        groupswitch.setChecked(true);
        groupswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                needgroupsend = b;
                Log.d("switchswitch", String.valueOf(b));
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessbilityManager = (AccessibilityManager)getSystemService(ACCESSIBILITY_SERVICE);
                String lname = labelname.getText().toString();
                String cname = collectname.getText().toString();
                if((TextUtils.isEmpty(lname) && needcontactsend )|| TextUtils.isEmpty(cname)){
                    Toast.makeText(MainActivity.this, "内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!accessbilityManager.isEnabled()){
                    Toast.makeText(MainActivity.this, "打开微信自动发消息", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                    return;
                }
                goWechat();
                int sleeptime = 0;
                /**
                while(sleeptime<120){
                    sleeptime++;
                    Log.d("sleeping", "sleeping");
                    try {
                        Thread.currentThread().sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                        return;
                    }
                }*/
                goWechat();
            }
        });
    }
    private void goWechat(){
        CONTACTDONE = !needcontactsend;
        GROUPDONE = !needgroupsend;
        LABELNAME = labelname.getText().toString();
        COLLECTNAME = collectname.getText().toString();
        AutoSendMsgService.setContactnum(-1);
        AutoSendMsgService.setContactcuridx(-1);
        AutoSendMsgService.setGroupnum(-1);
        AutoSendMsgService.setGroupcuridx(-1);
        AutoSendMsgService.setContactnamelist(null);
        AutoSendMsgService.setGroupnamelist(null);
        try {
            Thread.currentThread().sleep(100);
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setClassName(WechatWrapper.WECAHT_PACKAGENAME, WechatWrapper.WechatClass.WECHAT_CLASS_LAUNCHUI);
        startActivity(i);
    }
}
