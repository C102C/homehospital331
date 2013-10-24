package cn.younext;


import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import android.widget.DigitalClock;

import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;

public class healthinformation extends Activity{
	Button homeBtn;
	Button changshiBtn;
	Button bangbangBtn;
	BatteryView myview;
	OnClickListener btnClick;
	String text;
	ListView list;

	TextView user;
	int userid;
	String username;
	TextView textcontent;
	
	Cursor c;
	DatabaseHelper helper;
	SQLiteDatabase db;



	
	 public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);  
    	 requestWindowFeature(Window.FEATURE_NO_TITLE);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
         				WindowManager.LayoutParams.FLAG_FULLSCREEN);
         setContentView(R.layout.healthinfo);
         DigitalClock mDigitalClock=(DigitalClock)findViewById(R.id.healthinformation_digitalclock);
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

         user=(TextView)findViewById(R.id.healthinformation_user);
         user.setText(getString(R.string.myhealth_Welcome)+username);

         homeBtn = (Button)findViewById(R.id.healthinfo_homebutton);
         changshiBtn = (Button)findViewById(R.id.healthinfo_changshiBtn);
         bangbangBtn = (Button)findViewById(R.id.healthinfo_bangbangBtn);
         list = (ListView)findViewById(R.id.healthinfo_list);
         //textcontent=(TextView)findViewById(R.id.healthinfo_list_textcontent);
         btnClick= new OnClickListener(){

      		@Override
      		public void onClick(View v){
      			if(v==homeBtn)
      				healthinformation.this.finish();
      			else if(v==changshiBtn){
      				//textview.setText(R.string.healthinfo_FoodContent);
      				helper = new DatabaseHelper(healthinformation.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        	        db = helper.getWritableDatabase();
        	        c=db.query(DatabaseHelper.HEALTHINFO_CHANGSHITABLE, null, null, null, null,null,"_id desc",null);//机场名字查询
        	        //c.moveToNext();
                	//Log.v("before executing",c.getString(3));
        	        ListAdapter adapter = new SimpleCursorAdapter(healthinformation.this, 
        				R.layout.healthinfo_list,
        				c,
        				new String[] {DatabaseHelper.HEALTHINFOTABLE_TEXTTITLE,DatabaseHelper.HEALTHINFOTABLE_TEXTCONTENT},
        				new int[]{R.id.healthinfo_list_texttitle,R.id.healthinfo_list_textcontent}); 
        
        		    list.setAdapter(adapter);
        		/*    list.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Cursor c1=c;
		                    c1.moveToPosition(arg2);
		                    textcontent.setText(c1.getString(3));
		                    
							
						}
        		    	
        		    });*/
      				
      			
      				}
      			else if (v==bangbangBtn){
      				//textview.setText(R.string.healthinfo_FoodContent);
      				helper = new DatabaseHelper(healthinformation.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        	        db = helper.getWritableDatabase();
        	        c=db.query(DatabaseHelper.HEALTHINFO_BANGBANGTABLE, null, null, null, null,null,"_id desc",null);//机场名字查询
        	        //c.moveToNext();
                	//Log.v("before executing",c.getString(3));
        	        ListAdapter adapter = new SimpleCursorAdapter(healthinformation.this, 
        				R.layout.healthinfo_list,
        				c,
        				new String[] {DatabaseHelper.HEALTHINFOTABLE_TEXTTITLE,DatabaseHelper.HEALTHINFOTABLE_TEXTCONTENT},
        				new int[]{R.id.healthinfo_list_texttitle,R.id.healthinfo_list_textcontent}); 
        
        		    list.setAdapter(adapter);
        		/*    list.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Cursor c1=c;
		                    c1.moveToPosition(arg2);
		                    textcontent.setText(c1.getString(3));
		                    
							
						}
        		    	
        		    });*/
      				
      			
      				}

      			else {}
      		}
         
	 };
	        homeBtn.setOnClickListener(btnClick);
	        changshiBtn.setOnClickListener(btnClick);
	        bangbangBtn.setOnClickListener(btnClick);

	

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
	        	   
	        	healthinformation.this.finish();
			

	            return true;

	        }
	        return super.onKeyDown(keyCode, event);
	    }

}
