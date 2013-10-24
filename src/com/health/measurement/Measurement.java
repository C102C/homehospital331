package com.health.measurement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.younext.BatteryView;
import cn.younext.R;
import cn.younext.test_handinput;
import cn.younext.test_taixin;
import cn.younext.test_tizhong;
import cn.younext.test_xindian;
import cn.younext.test_xuetang;
import cn.younext.test_xueyang;
import cn.younext.test_zhifang;
import cn.younext.R.id;
import cn.younext.R.layout;
import cn.younext.R.string;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Measurement extends Activity{
	Button homeBtn;
	Button zhifang;
	Button tizhong;
	Button xindian;
	Button xuetang;
	Button xueyang;	
	Button bp;
	Button taixin;
	BatteryView myview;
	Button handinput;
	
	
	OnClickListener btnClick;

	TextView user;
	int userid;
	String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test);
        DigitalClock mDigitalClock=(DigitalClock)findViewById(R.id.test_digitalclock);
        myview=(BatteryView)findViewById(R.id.batteryview);
       // registerReceiver(mIntentReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle extra=getIntent().getExtras();
        if(extra!=null)
        {
        	userid=extra.getInt("userid");
        	username=extra.getString("username");
        	//Toast.makeText(myhealth.this,username, Toast.LENGTH_LONG).show();
        	//Log.v("userid_Myhealth", String.valueOf(userid));
        	//Log.v("username_myhealth", username);
        }

        user=(TextView)findViewById(R.id.test_user);
        user.setText(getString(R.string.myhealth_Welcome)+username);
        
    	
    homeBtn = (Button)findViewById(R.id.test_homebutton);
    tizhong = (Button)findViewById(R.id.test_tizhong);
    xueyang= (Button)findViewById(R.id.test_xueyang);
    xindian = (Button)findViewById(R.id.test_xindian);
    taixin = (Button)findViewById(R.id.test_taixin);
    zhifang = (Button)findViewById(R.id.test_zhifang);
    xuetang = (Button)findViewById(R.id.test_xuetang);
    bp = (Button)findViewById(R.id.test_bp);
    handinput=(Button)findViewById(R.id.test_handbtn);
    btnClick= new OnClickListener(){

		@Override
		public void onClick(View v) {
           Intent i = new Intent();
    		if(v==homeBtn){
    			Measurement.this.finish();	
    		}
    		else if(v==bp) {
    			i.setClass(Measurement.this, MeasureBp.class);
    			startActivity(i);    			
    		}
    		else if(v==handinput){
    			i.setClass(Measurement.this, test_handinput.class);
    			i.putExtra("userid", userid);
			    i.putExtra("username", username);
			    Log.v("handinputbtn","press");
    			startActivityForResult(i,1);  			
    		}
    		else if(v==zhifang){  
    			i.setClass(Measurement.this, test_zhifang.class);
    			i.putExtra("userid", userid);
			    i.putExtra("username", username);
    			startActivityForResult(i,1);
    		}
    		else if(v==tizhong){  
    			Log.v("tizhong", "tizhong");
    			i.setClass(Measurement.this, test_tizhong.class);
    			i.putExtra("userid", userid);
			    i.putExtra("username", username);
    			startActivityForResult(i,1);
    		}
    		else if(v==xuetang){  
    			i.setClass(Measurement.this, test_xuetang.class);
    			i.putExtra("userid", userid);
			    i.putExtra("username", username);
    			startActivityForResult(i,1);
    		}
    		
    		else if(v==xueyang){  
    			i.setClass(Measurement.this, test_xueyang.class);
    			i.putExtra("userid", userid);
			    i.putExtra("username", username);
    			startActivityForResult(i,1);
    		}
    		
    		else if(v==taixin){  
    			i.setClass(Measurement.this, test_taixin.class);
    			i.putExtra("userid", userid);
			    i.putExtra("username", username);
    			startActivityForResult(i,1);
    		}
    		else if(v==xindian){
    			i.setClass(Measurement.this, test_xindian.class);
    			i.putExtra("userid", userid);
			    i.putExtra("username", username);
			    
			    FileOutputStream out;
			    final String FILE_PATH="/sys/class/";
			    final String LEDFILE="ledctrl";
			    File ledfile;
			    try{
	               	 //写入“edittext01”文件
			    	ledfile= new File(FILE_PATH ,LEDFILE);
			    	
  		          	if(!ledfile.exists()){
  		          		ledfile.createNewFile();
  		        		 out = new FileOutputStream(ledfile);
  		        		 String text=Integer.toString(1);
  		                 out.write(text.getBytes());
  		                 out.close();
  		        	 }
	 
	               	 out = new FileOutputStream(ledfile);
	               	 //String test=String.valueOf(userid);
	               	 String test="1";
	               	 out.write(test.getBytes());
	               	 out.close();
	               	 Log.v("write", "write");
	               	
	               	 
	               } catch (IOException e){
	               	 //将出错信息打印到Logcat
	               	 Log.e("writefile", e.toString());
	               //	drug_calendar.this.finish();
	                } 
    			startActivityForResult(i,1);
    		} 		
   		
    		else{}
		}
    };
    homeBtn.setOnClickListener(btnClick);
    
    xueyang.setOnClickListener(btnClick);
    xindian.setOnClickListener(btnClick);
    taixin.setOnClickListener(btnClick);
    zhifang.setOnClickListener(btnClick);
    handinput.setOnClickListener(btnClick);
    xuetang.setOnClickListener(btnClick);
    tizhong.setOnClickListener(btnClick); 
    bp.setOnClickListener(btnClick);
    
    }
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
       switch (requestCode) { 
       case 1: 
           if (resultCode == RESULT_OK) 
           { 
               Measurement.this.finish();
           } 
           break; 
       default: 
           break; 
       } 
    }
    protected void onResume()
    {
      super.onResume();    
      // 注册消息处理器
    //  registerReceiver(mIntentReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
   
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

   	 public void onReceive(Context context, Intent intent){
   		 
            String action = intent.getAction();
          
            //要看看是不是我们要处理的消息
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {

           	 //充电状态
           	 if(intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)==BatteryManager.BATTERY_STATUS_CHARGING){
           		myview.tag=0;
           	
           		 Log.v("电量",Integer.toString(intent.getIntExtra("level", 0)));
           		 myview.invalidate();
           	 }
           	 else if(intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)==BatteryManager.BATTERY_STATUS_NOT_CHARGING){
           		 myview.tag = 1;
           		 myview.dianliang=intent.getIntExtra("level", 0);
           		 myview.invalidate();
          
           	 }
              

            }
            else{
           		 myview.tag = 1;
          		 myview.dianliang=intent.getIntExtra("level", 0);
          		 myview.invalidate();

          	 }
   	 }
   	 
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_MENU) {
        	   

			Measurement.this.finish();	
		
		

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
