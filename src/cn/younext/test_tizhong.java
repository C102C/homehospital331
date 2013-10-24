package cn.younext;

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
import android.widget.Button;
import android.widget.TextView;

public class test_tizhong extends Activity{

	Button homeBtn;
	Button returnBtn;
	Button nextBtn;
	OnClickListener btnClick;
	BatteryView myview;
	TextView user;
	String username;
	int userid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);       
        setContentView(R.layout.test_tizhong);
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

        user=(TextView)findViewById(R.id.test_tizhong_user);
        user.setText(getString(R.string.myhealth_Welcome)+username);
        
        homeBtn = (Button)findViewById(R.id.test_tizhong_homeBtn);
        returnBtn=(Button)findViewById(R.id.test_tizhong_returnBtn);
        nextBtn=(Button)findViewById(R.id.test_tizhong_nextBtn);
        btnClick= new OnClickListener(){
    		@Override
    		public void onClick(View v) {

        		if(v==homeBtn){      		
    			    test_tizhong.this.setResult(RESULT_OK);
    		        test_tizhong.this.finish();
        		}
        		else if(v==returnBtn){
        			test_tizhong.this.finish();	
        		}
        		else if(v==nextBtn){
        			Intent i = new Intent();
        			i.setClass(test_tizhong.this, test_tizhong1.class);
        			i.putExtra("userid", userid);
    			    i.putExtra("username", username);
        			startActivityForResult(i,1);
        		}
        		else{  			
        		}	
    		}
        };
        homeBtn.setOnClickListener(btnClick);
        returnBtn.setOnClickListener(btnClick);
        nextBtn.setOnClickListener(btnClick);

    }
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
       switch (requestCode) { 
       case 1: 
           if (resultCode == RESULT_OK) 
           { 
        	   test_tizhong.this.setResult(RESULT_OK);
               test_tizhong.this.finish();
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
        	   
        	test_tizhong.this.setResult(RESULT_OK);
	        test_tizhong.this.finish();
		

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }


}
