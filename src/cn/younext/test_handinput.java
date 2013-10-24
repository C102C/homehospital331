package cn.younext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class test_handinput extends Activity{

	Button homeBtn;
	
	Button xueyaBtn;	
	Button mailvBtn;
	Button zhifangBtn;
	Button xuetangBtn;
	Button tiwenBtn;
	Button taixinBtn;
	Button tizhongBtn;
	Button xueyangBtn;
	BatteryView myview;
	EditText gaoyaEdit;
	EditText diyaEdit;
	EditText mailvEdit;
	EditText zhifangEdit;
	EditText xuetangEdit;
	EditText tiwenEdit;
	EditText taixinEdit;
	EditText tizhongEdit;
	EditText xueyangEdit;

	
	Button handinput;
	Button returnBtn;
	Button machineinput;
	OnClickListener btnClick;
	
	Cursor cursor;
	DatabaseHelper helper;
	SQLiteDatabase db;
	
	private int mHour;
	private int mMinute;
	private int mYear;
	private int mMonth;
	private int mDay;

	TextView user;
	int userid;
	String username;
	String cardNum;
	edu.hitsz.android.Client client = new edu.hitsz.android.Client();
	

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test_handinput);
        DigitalClock mDigitalClock=(DigitalClock)findViewById(R.id.test_handinput_clock);
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

        user=(TextView)findViewById(R.id.test_handinput_user);
        user.setText(getString(R.string.myhealth_Welcome)+username);
        
        
        
        xueyaBtn = (Button)findViewById(R.id.test_handinput_BPBtn);
        mailvBtn = (Button)findViewById(R.id.test_handinput_HRBtn);
        zhifangBtn = (Button)findViewById(R.id.test_handinput_ZFLBtn);
        xuetangBtn = (Button)findViewById(R.id.test_handinput_XTBtn);
        tiwenBtn = (Button)findViewById(R.id.test_handinput_TWBtn);
        taixinBtn = (Button)findViewById(R.id.test_handinput_TXBtn);
        tizhongBtn = (Button)findViewById(R.id.test_handinput_TZBtn);
        xueyangBtn = (Button)findViewById(R.id.test_handinput_XYBtn);
        
        gaoyaEdit = (EditText)findViewById(R.id.test_handinput_HBGedittext);
        diyaEdit = (EditText)findViewById(R.id.test_handinput_LBGedittext);
        mailvEdit = (EditText)findViewById(R.id.test_handinput_HRedittext);
        zhifangEdit = (EditText)findViewById(R.id.test_handinput_ZFLedittext);
        xuetangEdit = (EditText)findViewById(R.id.test_handinput_XTedittext);
        tiwenEdit = (EditText)findViewById(R.id.test_handinput_TWedittext);
        taixinEdit = (EditText)findViewById(R.id.test_handinput_TXedittext);
        tizhongEdit = (EditText)findViewById(R.id.test_handinput_TZedittext);
        xueyangEdit = (EditText)findViewById(R.id.test_handinput_XYedittext);
        
        
        
    	
    homeBtn = (Button)findViewById(R.id.test_handinput_homeBtn);
    returnBtn=(Button)findViewById(R.id.test_handinput_returnBtn);
    machineinput=(Button)findViewById(R.id.test_handinput_machinebtn);
   // xueya = (Button)findViewById(R.id.test_xueya);
   // xueyang= (Button)findViewById(R.id.test_xueyang);
   // xindian = (Button)findViewById(R.id.test_xindian);
  //  taixin = (Button)findViewById(R.id.test_taixin);
  //  zhifang = (Button)findViewById(R.id.test_zhifang);
  //  handinput=(Button)findViewById(R.id.test_handbtn);
    
    
    helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
    db = helper.getWritableDatabase();//打开数据库
    cursor=db.query(DatabaseHelper.USER_MANAGE, null, DatabaseHelper.ID+"="+userid, null, null, null,DatabaseHelper.ID+" desc",null);
    cursor.moveToNext();
    cardNum = cursor.getString(2);
    db.close();
    
    
    
    
    
    
    btnClick= new OnClickListener(){

		@Override
		public void onClick(View v) {
           Intent i = new Intent();
    		if(v==homeBtn){
    	        test_handinput.this.setResult(RESULT_OK);
    		    test_handinput.this.finish();	
    		}
    		else if(v==returnBtn){
    			test_handinput.this.finish();
    		}
    		else if(v==xueyaBtn){
    			String gaoya=gaoyaEdit.getText().toString();
    			String diya=diyaEdit.getText().toString();
				if("".equals(gaoyaEdit.getText().toString().trim())||"".equals(diyaEdit.getText().toString().trim())){
                    Toast.makeText(test_handinput.this, "您输入的高压或低压为空，请重新输入", Toast.LENGTH_LONG).show();
						
				}
				else{
					Log.v("name", gaoya);
					Log.v("number", diya);
					helper = new DatabaseHelper(test_handinput.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        	        db = helper.getWritableDatabase();
        	        final Calendar c = Calendar.getInstance();
        	        mYear = c.get(Calendar.YEAR); //获取当前年份

        	        mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
        	        if(mMonth==13){
        	        	mMonth=1;	        	
        	        }
        	        mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        	        mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
        	        mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数

        			/*db.execSQL("INSERT INTO "+DatabaseHelper.XUEYATABLE+
        					"("+DatabaseHelper.GAOYA+","+DatabaseHelper.DIYA+","
        					+DatabaseHelper.YEAR+","+DatabaseHelper.MONTH+","+DatabaseHelper.DAY+","+DatabaseHelper.HOUR+","+DatabaseHelper.MINUTE+","+DatabaseHelper.USER_ID+") VALUES "+"("+gaoya+","
        					+diya+","+mYear+","+mMonth+","+mDay+","+mHour+","+mMinute+","+userid+")");
        			db.close();*/
        	        
        	       // cursor=db.query(DatabaseHelper.XUEYATABLE, null, null, null, null,DatabaseHelper.ID+" desc", null);
        	       // if()){
        	        	
        	        //}
        	       // cursor.moveToNext();
        	       // int _id=cursor.getInt(0);
        	        ContentValues values = new ContentValues();      
        	        values.put(DatabaseHelper.GAOYA, gaoya); 
        	        values.put(DatabaseHelper.DIYA, diya);
        	        values.put(DatabaseHelper.YEAR, mYear);
        	        values.put(DatabaseHelper.MONTH, mMonth);
        	        values.put(DatabaseHelper.DAY, mDay);
        	        values.put(DatabaseHelper.HOUR, mHour);
        	        values.put(DatabaseHelper.MINUTE, mMinute);
        	        values.put(DatabaseHelper.USER_ID, userid);
        	       // values.put(DatabaseHelper.ID, _id);
        	           
        	        db.insert(   
        	        		DatabaseHelper.XUEYATABLE, DatabaseHelper.ID, values);   
        	        db.close();
        	        
        	        
        			try {
        				client.insertBPWithoutpulse(cardNum, gaoya, diya);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				Toast.makeText(test_handinput.this, R.string.NetworkDisconneted, Toast.LENGTH_LONG).show();
        			}
					
				}
    		}
    		else if(v==mailvBtn){
    			String mailv=mailvEdit.getText().toString();
				if("".equals(mailvEdit.getText().toString().trim())){
                    Toast.makeText(test_handinput.this, "您输入的脉率为空，请重新输入", Toast.LENGTH_LONG).show();
						
				}
				else{
					Log.v("name", mailv);
					helper = new DatabaseHelper(test_handinput.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        	        db = helper.getWritableDatabase();
        	        final Calendar c = Calendar.getInstance();
        	        mYear = c.get(Calendar.YEAR); //获取当前年份

        	        mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
        	        if(mMonth==13){
        	        	mMonth=1;	        	
        	        }
        	        mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        	        mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
        	        mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
        	        
        	        
        	        
        			db.execSQL("INSERT INTO "+DatabaseHelper.MAILVTABLE+
        					"("+DatabaseHelper.MAILV+","+DatabaseHelper.YEAR+","+DatabaseHelper.MONTH+","+DatabaseHelper.DAY+","+DatabaseHelper.HOUR+","+DatabaseHelper.MINUTE+","+DatabaseHelper.USER_ID+") VALUES "+"("+mailv+","+mYear+","+mMonth+","+mDay+","+mHour+","+mMinute+","+userid+")");
        			db.close();
        			
        			try {
        				client.insertPulse(cardNum, mailv);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				Toast.makeText(test_handinput.this, R.string.NetworkDisconneted, Toast.LENGTH_LONG).show();
        			}
					
					
				}
    		}
    		else if(v==zhifangBtn){
    			String zhifanglv=zhifangEdit.getText().toString();
				if("".equals(zhifangEdit.getText().toString().trim())){
                    Toast.makeText(test_handinput.this, "您输入的脉率为空，请重新输入", Toast.LENGTH_LONG).show();
						
				}
				else{
					Log.v("name", zhifanglv);
					helper = new DatabaseHelper(test_handinput.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        	        db = helper.getWritableDatabase();
        	        final Calendar c = Calendar.getInstance();
        	        mYear = c.get(Calendar.YEAR); //获取当前年份

        	        mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
        	        if(mMonth==13){
        	        	mMonth=1;	        	
        	        }
        	        mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        	        mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
        	        mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
        			db.execSQL("INSERT INTO "+DatabaseHelper.ZHIFANGTABLE+
        					"("+DatabaseHelper.ZHIFANGLV+","+DatabaseHelper.YEAR+","+DatabaseHelper.MONTH+","+DatabaseHelper.DAY+","+DatabaseHelper.HOUR+","+DatabaseHelper.MINUTE+","+DatabaseHelper.USER_ID+") VALUES "+"("+zhifanglv+","+mYear+","+mMonth+","+mDay+","+mHour+","+mMinute+","+userid+")");
        			db.close();
        			
        			try {
        				client.insertFat(cardNum, zhifanglv);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				Toast.makeText(test_handinput.this, R.string.NetworkDisconneted, Toast.LENGTH_LONG).show();
        			}
					
					
				}
    		}
    		else if(v==xuetangBtn){
    			String xuetang=xuetangEdit.getText().toString();
				if("".equals(xuetangEdit.getText().toString().trim())){
                    Toast.makeText(test_handinput.this, "您输入的血糖为空，请重新输入", Toast.LENGTH_LONG).show();
						
				}
				else{
					Log.v("name", xuetang);
					helper = new DatabaseHelper(test_handinput.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        	        db = helper.getWritableDatabase();
        	        final Calendar c = Calendar.getInstance();
        	        mYear = c.get(Calendar.YEAR); //获取当前年份

        	        mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
        	        if(mMonth==13){
        	        	mMonth=1;	        	
        	        }
        	        mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        	        mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
        	        mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
        			db.execSQL("INSERT INTO "+DatabaseHelper.XUETANGTABLE+
        					"("+DatabaseHelper.XUETANG+","+DatabaseHelper.YEAR+","+DatabaseHelper.MONTH+","+DatabaseHelper.DAY+","+DatabaseHelper.HOUR+","+DatabaseHelper.MINUTE+","+DatabaseHelper.USER_ID+") VALUES "+"("+xuetang+","+mYear+","+mMonth+","+mDay+","+mHour+","+mMinute+","+userid+")");
        			db.close();
        			
        			try {
        				client.insertGLU(cardNum, xuetang);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				Toast.makeText(test_handinput.this, R.string.NetworkDisconneted, Toast.LENGTH_LONG).show();
        			}
					
					
				}
    		}
    		else if(v==tiwenBtn){
    			String tiwen=tiwenEdit.getText().toString();
				if("".equals(tiwenEdit.getText().toString().trim())){
                    Toast.makeText(test_handinput.this, "您输入的体温为空，请重新输入", Toast.LENGTH_LONG).show();
						
				}
				else{
					Log.v("name", tiwen);
					helper = new DatabaseHelper(test_handinput.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        	        db = helper.getWritableDatabase();
        	        final Calendar c = Calendar.getInstance();
        	        mYear = c.get(Calendar.YEAR); //获取当前年份

        	        mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
        	        if(mMonth==13){
        	        	mMonth=1;	        	
        	        }
        	        mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        	        mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
        	        mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
        			db.execSQL("INSERT INTO "+DatabaseHelper.TIWENTABLE+
        					"("+DatabaseHelper.TIWEN+","+DatabaseHelper.YEAR+","+DatabaseHelper.MONTH+","+DatabaseHelper.DAY+","+DatabaseHelper.HOUR+","+DatabaseHelper.MINUTE+","+DatabaseHelper.USER_ID+") VALUES "+"("+tiwen+","+mYear+","+mMonth+","+mDay+","+mHour+","+mMinute+","+userid+")");
        			db.close();
        			
        			try {
        				client.insertTemperature(cardNum, tiwen);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				Toast.makeText(test_handinput.this, R.string.NetworkDisconneted, Toast.LENGTH_LONG).show();
        			}
					
					
				}
    		}
    		else if(v==taixinBtn){
    			String taixin=taixinEdit.getText().toString();
				if("".equals(taixinEdit.getText().toString().trim())){
                    Toast.makeText(test_handinput.this, "您输入的胎心为空，请重新输入", Toast.LENGTH_LONG).show();
						
				}
				else{
					Log.v("name", taixin);
					helper = new DatabaseHelper(test_handinput.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        	        db = helper.getWritableDatabase();
        	        final Calendar c = Calendar.getInstance();
        	        mYear = c.get(Calendar.YEAR); //获取当前年份

        	        mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
        	        if(mMonth==13){
        	        	mMonth=1;	        	
        	        }
        	        mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        	        mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
        	        mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
        			db.execSQL("INSERT INTO "+DatabaseHelper.TAIXINTABLE+
        					"("+DatabaseHelper.TAIXIN+","+DatabaseHelper.YEAR+","+DatabaseHelper.MONTH+","+DatabaseHelper.DAY+","+DatabaseHelper.HOUR+","+DatabaseHelper.MINUTE+","+DatabaseHelper.USER_ID+") VALUES "+"("+taixin+","+mYear+","+mMonth+","+mDay+","+mHour+","+mMinute+","+userid+")");
        			db.close();
        			
        			try {
        				client.insertHeart(cardNum, taixin);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				Toast.makeText(test_handinput.this, R.string.NetworkDisconneted, Toast.LENGTH_LONG).show();
        			}
					
					
				}
    		
    			
    		}
    		
    		else if(v==tizhongBtn){
    			String tizhong=tizhongEdit.getText().toString();
				if("".equals(tizhongEdit.getText().toString().trim())){
                    Toast.makeText(test_handinput.this, "您输入的体重为空，请重新输入", Toast.LENGTH_LONG).show();
						
				}
				else{
					Log.v("name", tizhong);
					helper = new DatabaseHelper(test_handinput.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        	        db = helper.getWritableDatabase();
        	        final Calendar c = Calendar.getInstance();
        	        mYear = c.get(Calendar.YEAR); //获取当前年份

        	        mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
        	        if(mMonth==13){
        	        	mMonth=1;	        	
        	        }
        	        mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        	        mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
        	        mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
        			db.execSQL("INSERT INTO "+DatabaseHelper.TIZHONGTABLE+
        					"("+DatabaseHelper.TIZHONG+","+DatabaseHelper.YEAR+","+DatabaseHelper.MONTH+","+DatabaseHelper.DAY+","+DatabaseHelper.HOUR+","+DatabaseHelper.MINUTE+","+DatabaseHelper.USER_ID+") VALUES "+"("+tizhong+","+mYear+","+mMonth+","+mDay+","+mHour+","+mMinute+","+userid+")");
        			db.close();
        			
        			try {
        				client.insertWeight(cardNum, tizhong);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				Toast.makeText(test_handinput.this, R.string.NetworkDisconneted, Toast.LENGTH_LONG).show();
        			}
					
					
				}
    		
    			
    		}
    		else if(v==xueyangBtn){
    			String xueyang=xueyangEdit.getText().toString();
				if("".equals(xueyangEdit.getText().toString().trim())){
                    Toast.makeText(test_handinput.this, "您输入的血氧为空，请重新输入", Toast.LENGTH_LONG).show();
						
				}
				else{
					Log.v("name", xueyang);
					helper = new DatabaseHelper(test_handinput.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        	        db = helper.getWritableDatabase();
        	        final Calendar c = Calendar.getInstance();
        	        mYear = c.get(Calendar.YEAR); //获取当前年份

        	        mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
        	        if(mMonth==13){
        	        	mMonth=1;	        	
        	        }
        	        mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        	        mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
        	        mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
        			db.execSQL("INSERT INTO "+DatabaseHelper.XUEYANGTABLE+
        					"("+DatabaseHelper.XUEYANG+","+DatabaseHelper.YEAR+","+DatabaseHelper.MONTH+","+DatabaseHelper.DAY+","+DatabaseHelper.HOUR+","+DatabaseHelper.MINUTE+","+DatabaseHelper.USER_ID+") VALUES "+"("+xueyang+","+mYear+","+mMonth+","+mDay+","+mHour+","+mMinute+","+userid+")");
        			db.close();
        			
        			try {
        				client.insertBO(cardNum, xueyang);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				Toast.makeText(test_handinput.this, R.string.NetworkDisconneted, Toast.LENGTH_LONG).show();
        			}
					
					
				}
    		
    			
    		}
    		/*else if(v==xueya){  
    			i.setClass(test.this, test_xueya.class);
    			i.putExtra("userid", userid);
			    i.putExtra("username", username);
    			startActivityForResult(i,1);
    		}
    		else if(v==xueyang){
    			//i.setClass(test.this, test_xueyang.class);
    			//startActivityForResult(i,1);
    		}*/
    		else if(v==machineinput){
    			test_handinput.this.finish();
    			
    		}
    		/*else if(v==xindian){
    			i.setClass(test.this, test_xindian.class);
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
    		
    		
    		
    		
    		
    		else if(v==taixin){
    			//i.setClass(test.this, test_taixin.class);
    			//startActivityForResult(i,1);
    		}
    		else if(v==zhifang){
    			//i.setClass(test.this, test_zhifang.class);
    			//startActivityForResult(i,1);
    		}*/
    		else{}
		}
    };
    homeBtn.setOnClickListener(btnClick);
    returnBtn.setOnClickListener(btnClick);
    machineinput.setOnClickListener(btnClick);
    
    xueyaBtn.setOnClickListener(btnClick);
    mailvBtn.setOnClickListener(btnClick);
    zhifangBtn.setOnClickListener(btnClick);
    xuetangBtn.setOnClickListener(btnClick);
    tiwenBtn.setOnClickListener(btnClick);
    taixinBtn.setOnClickListener(btnClick);
    tizhongBtn.setOnClickListener(btnClick);
    xueyangBtn.setOnClickListener(btnClick);
    //xueya.setOnClickListener(btnClick);
    //xueyang.setOnClickListener(btnClick);
    //xindian.setOnClickListener(btnClick);
    //taixin.setOnClickListener(btnClick);
    //zhifang.setOnClickListener(btnClick);
    
    
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
        	   
        	test_handinput.this.setResult(RESULT_OK);
		    test_handinput.this.finish();
		

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }




}
