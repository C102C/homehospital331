package cn.younext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class myhealth extends Activity{
	Button homeBtn;
	Button healthreport;
	Button testrecord;
	OnClickListener btnClick;
	BatteryView myview;
	TextView user;
	int userid;
	String username;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.myhealth);
        DigitalClock mDigitalClock=(DigitalClock)findViewById(R.id.myhealth_digitalclock);
        myview=(BatteryView)findViewById(R.id.batteryview);
        registerReceiver(mIntentReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle extra=getIntent().getExtras();
        if(extra!=null)
        {
        	userid=extra.getInt("userid");
        	username=extra.getString("username");
        	//Toast.makeText(myhealth.this,username, Toast.LENGTH_LONG).show();
        	//Log.v("userid_Myhealth", String.valueOf(userid));
        	//Log.v("username_myhealth", username);
        }

        user=(TextView)findViewById(R.id.myhealth_user);
        user.setText(getString(R.string.myhealth_Welcome)+" "+username);
 
        
/*    	final String FILE_PATH="/sys/class/ledctrl/";
    	final String FILE_NAME="ledctrl";
    	File file;
    	FileOutputStream out;
        
        
        file=new File(FILE_PATH,FILE_NAME);
	    try {
			out=new FileOutputStream(file);
			int i=1;
			out.write(i);
			Log.v("i", "i");
			out.close();
			
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
        */
        
        
        
        


   // c=db.query("xueya", null,"year="+mYear+" and month="+mMonth+" or year="+(mYear-1)+" and month=12"+" and day>"+(28+mDay-7), null, null,null,"_id desc",null);
        homeBtn = (Button)findViewById(R.id.homebutton);
        healthreport=(Button)findViewById(R.id.myhealth_healthreport);
        testrecord=(Button)findViewById(R.id.myhealth_testrecord);
        btnClick= new OnClickListener(){

		    @Override
		    public void onClick(View v) {
			    Intent i=new Intent();
    		    if(v==homeBtn){
    		    	
    		    	
    		    	
    		    	
/*    		    	final String FILE_PATH="/sys/class/ledctrl/";
    		    	final String FILE_NAME="ledctrl";
    		    	File file;
    		    	FileOutputStream out;
    		        
    		        
    		        file=new File(FILE_PATH,FILE_NAME);
    			    try {
    					out=new FileOutputStream(file);
    					int k=0;
    					out.write(k);
    					Log.v("k", "k");
    					out.close();
    					
    				} catch (FileNotFoundException e) {

    				} catch (IOException e) {

    				}*/
    		    	
    		    	
    		    	
    		    	
    		    	
    		    myhealth.this.finish();	
    		    }
    		
    		    else if(v==healthreport){
    			i.setClass(myhealth.this, history_report.class);
			    i.putExtra("userid", userid);
			    i.putExtra("username", username);
    			startActivityForResult(i,1);
    		    }
    		    else if(v==testrecord){
    			    i.setClass(myhealth.this, myhealth_testrecord.class);
			        i.putExtra("userid", userid);
			        i.putExtra("username", username);
    			    startActivityForResult(i,1);
    		    }
    		    else{}
    		}	
		
        };
        homeBtn.setOnClickListener(btnClick);
        healthreport.setOnClickListener(btnClick);
        testrecord.setOnClickListener(btnClick);
    
    }
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
       switch (requestCode) { 
       case 1: 
           if (resultCode == RESULT_OK) 
           { 
        	   
               myhealth.this.finish();
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
      registerReceiver(mIntentReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
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
        	   
        	myhealth.this.finish();
		

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
