package cn.younext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Calendar;

import com.android.etcomm.sdk.FileItem;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class myhealth_testrecord extends Activity {     
    /** Called when the activity is first created. */     
    private TabHost tabHost;  
	Button homeBtn;
	Button returnBtn;
	Button nearweak;
	Button nearmonth;
	Button nearseason;
	Button zhengchang;
	BatteryView myview;
	Button big;
	Button small;
	Button xueya_big;
	Button xueya_small;
	Button tizhong_big;
	Button tizhong_small;
	Button zhifang_big;
	Button zhifang_small;
	Button xuetang_big;
	Button xuetang_small;
	Button tiwen_big;
	Button tiwen_small;
	Button xueyang_big;
	Button xueyang_small;
	Button taixin_big;
	Button taixin_small;

	Field mBottomLeftStrip; 
    Field mBottomRightStrip; 

	
	Myview mailv_tag;
	Xueya_view xueya_tag;
	Tizhong_view tizhong_tag;
	Zhifang_view zhifang_tag;
	Xuetang_view xuetang_tag;
	Tiwen_view tiwen_tag;
	Xueyang_view xueyang_tag;
	Taixin_view taixin_tag;
		
	OnClickListener btnClick;
	OnClickListener btnClick_xueya;
	OnClickListener btnClick_xindian;
	OnClickListener btnClick_mailvoxi;
	OnClickListener btnClick_xueyaoxi;
	OnClickListener btnClick_tizhongoxi;
	OnClickListener btnClick_zhifangoxi;
	OnClickListener btnClick_xuetangoxi;
	OnClickListener btnClick_tiwenoxi;
	OnClickListener btnClick_xueyangoxi;
	OnClickListener btnClick_taixinoxi;
	
	ArrayAdapter<FileItem> files;
	FileItem file;
	private ListView lstData = null;
	
	public static final String DatabaseName="xueyarecord";
	public static final int Version = 1;
	Cursor c;
	DatabaseHelper helper;
	SQLiteDatabase db;
	long id;

	private int mHour;
	private int mMinute;
	private int mYear;
	private int mMonth;
	private int mDay;
	
	TextView user;
	int userid;
	String username;
     
    @Override     
    public void onCreate(Bundle savedInstanceState) {     
        super.onCreate(savedInstanceState);  
        
        
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myhealth_testrecord);
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

        user=(TextView)findViewById(R.id.myhealth_testrecord_user);
        user.setText(getString(R.string.myhealth_Welcome)+username);
        
        DigitalClock mDigitalClock=(DigitalClock)findViewById(R.id.myhealth_testrecord_digitalclock);   
        
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

     
        tabHost.addTab(tabHost.newTabSpec("Tab_1")
                .setIndicator(this.getResources().getString(R.string.myhealth_testrecord_BloodPressure))
                .setContent(R.id.tab1) );   
        tabHost.addTab(tabHost.newTabSpec("Tab_2")     
                .setIndicator(this.getResources().getString(R.string.myhealth_testrecord_Weight))
                .setContent(R.id.tab2));   
        
        tabHost.addTab(tabHost.newTabSpec("Tab_3")     
                .setIndicator(this.getResources().getString(R.string.myhealth_testrecord_PulseRate))
                .setContent(R.id.tab3) );
        tabHost.addTab(tabHost.newTabSpec("Tab_4")     
       		 .setIndicator(this.getResources().getString(R.string.myhealth_testrecord_ZhifangRate))
               .setContent(R.id.tab4)); 
        tabHost.addTab(tabHost.newTabSpec("Tab_5")     
        		.setIndicator(this.getResources().getString(R.string.myhealth_testrecord_Electrocardio))
                .setContent(R.id.tab5));
        tabHost.addTab(tabHost.newTabSpec("Tab_6")     
        		 .setIndicator(this.getResources().getString(R.string.myhealth_testrecord_Xuetang))
                .setContent(R.id.tab6)); 
        tabHost.addTab(tabHost.newTabSpec("Tab_7")     
        		 .setIndicator(this.getResources().getString(R.string.myhealth_testrecord_BodyTemperature))
                .setContent(R.id.tab7)); 
        tabHost.addTab(tabHost.newTabSpec("Tab_8")     
        		 .setIndicator(this.getResources().getString(R.string.myhealth_testrecord_BloodOxygen))
                .setContent(R.id.tab8)); 
        tabHost.addTab(tabHost.newTabSpec("Tab_9")     
        		 .setIndicator(this.getResources().getString(R.string.myhealth_testrecord_FetalHeart))
                .setContent(R.id.tab9)); 
        tabHost.setCurrentTab(5);     
      
       final TabWidget tabWidget = tabHost.getTabWidget();
     
   
					for (int i =0; i < tabWidget.getChildCount(); i++) {
						((TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title)).setTextSize(18);
						((TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title)).setTextColor(Color.WHITE);
						((TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title)).setPadding(0,0,0,25);
						View vvv = tabWidget.getChildAt(i);
						if(i==tabWidget.getChildCount()-1)
							vvv.getLayoutParams().width = 84;
						
						else
							vvv.getLayoutParams().width = 86;
							
					}

					for (int i =0; i < tabWidget.getChildCount(); i++) {
						 	
			                View vvv = tabWidget.getChildAt(i);

			                if(tabHost.getCurrentTab()==i){
			                	
								((TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title)).setTextColor(Color.BLACK);
			                	
			                	if(i==0){
			                        vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhostleft_over));
			                     
			                	}
			                	else if(i==tabWidget.getChildCount()-1){
			                		vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhostright_over));
			                	
			                	}
			                	else{
			                		vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhost_over));
			                	
			                	}
			                }
			                else {
			                	((TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title)).setTextColor(Color.WHITE);
			                	
			                	if(i==0){
			                        vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhostleft));
			                      
			                	}
			                	else if(i==tabWidget.getChildCount()-1){
			                	 vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhostright));
			                	
			                	}
			                	else{
			                        vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhost));
			                     
			                	}
			                }

					}


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {     
     
            public void onTabChanged(String tabId) { 
            	for (int i =0; i < tabWidget.getChildCount(); i++) {
            	      View vvv = tabWidget.getChildAt(i);
            	      if(tabHost.getCurrentTab()==i){
            	    		((TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title)).setTextColor(Color.BLACK);
            	    	  if(i==0){
		                        vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhostleft_over));
		                      
            	    	  }
		                	else if(i==tabWidget.getChildCount()-1){
		                		vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhostright_over));
		                	
		                	}
		                	else{
		                		vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhost_over));
		                		
		                	}
            	      }
            	      else {
            	    		((TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title)).setTextColor(Color.WHITE);
            	    	if(i==0){
		                        vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhostleft));
		                      
            	    	  }
		                	else if(i==tabWidget.getChildCount()-1){
		                		vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhostright));
		                		
		                	}
		                	else{
		                        vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tabhost));
		                      
		                	}
            	      }
            	     }

             
              if(tabId=="Tab_3"){
            	 
            	  mailv_tag=(Myview)findViewById(R.id.myview);
                  big=(Button)findViewById(R.id.Button_big);
                  small=(Button)findViewById(R.id.Button_small);
                  mailv_tag.user_tag=userid;
                  Log.v("dsfdsafasdf", Integer.toString(mailv_tag.user_tag));
                  btnClick_mailvoxi = new OnClickListener(){
            		   public void onClick(View v){
            			  
            			   
            			   if(v==big){
            				   
            				   if(mailv_tag.tag!=3)
            					   {
            					   mailv_tag.tag=mailv_tag.tag+1;}
            				  
            			   }
            				  
            			   else if(v==small){
            				   if(mailv_tag.tag!=1)
            					   mailv_tag.tag=mailv_tag.tag-1;
            			   
            			   }
            			   mailv_tag.invalidate();
            		   }
                  
                   };
                   mailv_tag.invalidate();
                big.setOnClickListener(btnClick_mailvoxi);
               	small.setOnClickListener(btnClick_mailvoxi);
              }
                
            	
            		
            		 //实验打开file
            else if(tabId=="Tab_5"){
                    lstData = (ListView) findViewById(R.id.ecg_files);
                    files=new ArrayAdapter<FileItem>(myhealth_testrecord.this, R.layout.file);
                    lstData.setAdapter(files);
                    for (int i = 0; i < fileList().length; i++) {
            			String file = fileList()[i];

            			try {
            				InputStream stream = openFileInput(file);

            				int length = stream.available();
            				byte[] data = new byte[length];

            				stream.read(data);
            				FileItem item = new FileItem(i, data, length);
            				helper = new DatabaseHelper(getBaseContext(), DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
            			    db = helper.getWritableDatabase();//打开数据库
            			    String[] filename1=item.fileName.split(" ");
            			    String[] filename2=filename1[0].split("-");
            			    String[] filename3=filename1[1].split(":");
            			    String fileName=filename2[0]+filename2[1]+filename2[2]+filename3[0]+filename3[1]+filename3[2];
            			    c=db.query(DatabaseHelper.ECGFILE_TABLE, null,DatabaseHelper.ECGFILE_USERID+"="+userid+" and "+ DatabaseHelper.ECGFILE_FILENAME+"="+fileName, null, null, null,DatabaseHelper.ID+" desc",null);
            			    Log.v("filename2", fileName);
            			    Log.v("userid2", String.valueOf(userid));
            			    c.moveToFirst();
            			    if(!(c.isAfterLast())){
            			    	Log.v("true", "true");
            			    	files.add(item);
            			    }
            				stream.close();
            			} catch (FileNotFoundException e) {
            				e.printStackTrace();
            			} catch (IOException e) {
            				e.printStackTrace();
            			}
            		}
                    
                    
                    lstData.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            			@Override
            			public void onCreateContextMenu(ContextMenu arg0, View arg1, ContextMenuInfo arg2) {
            				arg0.setHeaderTitle(R.string.ecg_choose);

            				MenuItem item = null;

            				item = arg0.add(R.string.ecg_view);
            				item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            					@Override
            					public boolean onMenuItemClick(MenuItem item) {
            						if (file != null) viewFile(file);
            						return false;
            					}
            				});

            				item = arg0.add(R.string.ecg_delete);
            				item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            					@Override
            					public boolean onMenuItemClick(MenuItem item) {
            						if (file != null) {
            							myhealth_testrecord.this.deleteFile(file.fileName);
            							files.remove(file);
            						}

            						file = null;
            						return false;
            					}
            				});

            				item = arg0.add(R.string.ecg_clear);
            				item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            					@Override
            					public boolean onMenuItemClick(MenuItem item) {
            						for (int i = 0; i < files.getCount(); i++) {
            							myhealth_testrecord.this.deleteFile(files.getItem(i).fileName);
            						}
            						
            						files.clear();
            						file = null;
            						return false;
            					}
            				});
            			}
            		});

            		lstData.setOnItemClickListener(new OnItemClickListener() {
            			@Override
            			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            				file = (FileItem) arg0.getItemAtPosition(arg2);
            				if (file != null)
            					viewFile(file);
            			}
            		});

            		lstData.setOnItemLongClickListener(new OnItemLongClickListener() {
            			@Override
            			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            				file = (FileItem) arg0.getItemAtPosition(arg2);
            				return false;
            			}
            		});
              	   nearweak=(Button)findViewById(R.id.xindian_nearweak);
             	   nearmonth=(Button)findViewById(R.id.xindian_nearmonth);
             	   nearseason=(Button)findViewById(R.id.xindian_nearseason);
             	   btnClick_xindian = new OnClickListener(){
             		   public void onClick(View v){
             			   if(v==nearweak){
             				    Log.v("xindian","nearweek");
             			        final Calendar calendar = Calendar.getInstance();
             			        mYear = calendar.get(Calendar.YEAR); //获取当前年份
             			        mMonth = calendar.get(Calendar.MONTH)+1;//获取当前月份
             			        if(mMonth==13){
             			        	mMonth=1;	        	
             			        }
             			        mDay = calendar.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
             			        mHour = calendar.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
             			        mMinute = calendar.get(Calendar.MINUTE);//获取当前的分钟数
                                lstData = (ListView) findViewById(R.id.ecg_files);
                                files=new ArrayAdapter<FileItem>(myhealth_testrecord.this, R.layout.file);
                                lstData.setAdapter(files);
                                for (int i = 0; i < fileList().length; i++) {
                        			String file = fileList()[i];

                        			try {
                        				InputStream stream = openFileInput(file);

                        				int length = stream.available();
                        				byte[] data = new byte[length];

                        				stream.read(data);

                        				FileItem item = new FileItem(i, data, length);
                        				Log.v("fileName", item.fileName);
                        				String array[]=item.fileName.split("-");
                        				int year=Integer.parseInt(array[0]);
                        				
                        				int month=Integer.parseInt(array[1]);
                        				String arrayforday[]=array[2].split(" ");
                        				int day=Integer.parseInt(arrayforday[0]);
                        				Log.v("month", String.valueOf(month));
                        				Log.v("day", String.valueOf(day));
                        				
                        				helper = new DatabaseHelper(getBaseContext(), DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
                        			    db = helper.getWritableDatabase();//打开数据库
                        			    String[] filename1=item.fileName.split(" ");
                        			    String[] filename2=filename1[0].split("-");
                        			    String[] filename3=filename1[1].split(":");
                        			    String fileName=filename2[0]+filename2[1]+filename2[2]+filename3[0]+filename3[1]+filename3[2];
                        			    c=db.query(DatabaseHelper.ECGFILE_TABLE, null,DatabaseHelper.ECGFILE_USERID+"="+userid+" and "+ DatabaseHelper.ECGFILE_FILENAME+"="+fileName, null, null, null,DatabaseHelper.ID+" desc",null);
                        			    Log.v("filename2", fileName);
                        			    Log.v("userid2", String.valueOf(userid));
                        			    c.moveToFirst();
                        			    if(!(c.isAfterLast())){

                        				
                        				
                        				
                        				
                                        if(mDay>7&&day<=mDay&&year==mYear&&month==mMonth){
                            				files.add(item);
                                        	
                                        }
                                        else if(mDay<=7&&(mMonth==2||mMonth==4||mMonth==6||mMonth==8||mMonth==9||mMonth==11)){
                                        	if(year==mYear&&month==mMonth){
                                            	files.add(item);                                       		
                                        	}
                                        	else if(year==mYear&&month==mMonth-1&&day>=mDay+31-7){
                                            	files.add(item);
                                        	}
                                        	else{}

                                        }
                                        else if(mDay<=7&&(mMonth==5||mMonth==7||mMonth==10||mMonth==12)){
                                        	if(year==mYear&&month==mMonth){
                                            	files.add(item);                                       		
                                        	}
                                        	else if(year==mYear&&month==mMonth-1&&day>=mDay+30-7){
                                            	files.add(item);
                                        	}
                                        	else{}
                                        	
                                        }
                                        else if(mDay<=7&&mMonth==3){
                                        	if(year==mYear&&month==mMonth){
                                            	files.add(item);                                       		
                                        	}
                                        	else if(year==mYear&&month==mMonth-1&&day>=mDay+28-7){
                                            	files.add(item);
                                        	}
                                        	else{}
                                        	
                                        }
                                        else if(mDay<=7&&mMonth==1){
                                        	if(year==mYear&&month==mMonth){
                                            	files.add(item);                                       		
                                        	}
                                        	else if(year==mYear-1&&month==12&&day>=mDay+31-7){
                                            	files.add(item);
                                        	}
                                        	else{}
                                        	
                                        }
                                        else{}
                                        
                                        
                        			    }
                        				stream.close();
                        			} catch (FileNotFoundException e) {
                        				e.printStackTrace();
                        			} catch (IOException e) {
                        				e.printStackTrace();
                        			}
                        		}
 
             			      
             			   }
             			   else if(v==nearmonth){
            				    Log.v("xindian","nearweek");
             			        final Calendar calendar = Calendar.getInstance();
             			        mYear = calendar.get(Calendar.YEAR); //获取当前年份
             			        mMonth = calendar.get(Calendar.MONTH)+1;//获取当前月份
             			        if(mMonth==13){
             			        	mMonth=1;	        	
             			        }
             			        mDay = calendar.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
             			        mHour = calendar.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
             			        mMinute = calendar.get(Calendar.MINUTE);//获取当前的分钟数
                                lstData = (ListView) findViewById(R.id.ecg_files);
                                files=new ArrayAdapter<FileItem>(myhealth_testrecord.this, R.layout.file);
                                lstData.setAdapter(files);
                                for (int i = 0; i < fileList().length; i++) {
                        			String file = fileList()[i];

                        			try {
                        				InputStream stream = openFileInput(file);

                        				int length = stream.available();
                        				byte[] data = new byte[length];

                        				stream.read(data);

                        				FileItem item = new FileItem(i, data, length);
                        				Log.v("fileName", item.fileName);
                        				String array[]=item.fileName.split("-");
                        				int year=Integer.parseInt(array[0]);
                        				Log.v("year", String.valueOf(year));
                        				int month=Integer.parseInt(array[1]);
                        				String arrayforday[]=array[2].split(" ");
                        				int day=Integer.parseInt(arrayforday[0]);
                        				Log.v("month", String.valueOf(month));
                        				Log.v("day", String.valueOf(day));
                        				
                        				helper = new DatabaseHelper(getBaseContext(), DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
                        			    db = helper.getWritableDatabase();//打开数据库
                        			    String[] filename1=item.fileName.split(" ");
                        			    String[] filename2=filename1[0].split("-");
                        			    String[] filename3=filename1[1].split(":");
                        			    String fileName=filename2[0]+filename2[1]+filename2[2]+filename3[0]+filename3[1]+filename3[2];
                        			    c=db.query(DatabaseHelper.ECGFILE_TABLE, null,DatabaseHelper.ECGFILE_USERID+"="+userid+" and "+ DatabaseHelper.ECGFILE_FILENAME+"="+fileName, null, null, null,DatabaseHelper.ID+" desc",null);
                        			    Log.v("filename2", fileName);
                        			    Log.v("userid2", String.valueOf(userid));
                        			    c.moveToFirst();
                        			    if(!(c.isAfterLast())){
                                        if(mMonth!=1){
                                        	if(year==mYear&&month==mMonth){
                                            	files.add(item);                                       		
                                        	}
                                        	else if(year==mYear&&month==mMonth-1&&day>mDay){
                                            	files.add(item);
                                        	}
                                        	else{}

                                        }
                                        else{
                                        	if(year==mYear&&month==mMonth){
                                            	files.add(item);                                       		
                                        	}
                                        	else if(year==mYear&&month==12&&day>mDay){
                                            	files.add(item);
                                        	}
                                        	else{}
                                        	
                                        } 
                        			    }
                        				stream.close();
                        			} catch (FileNotFoundException e) {
                        				e.printStackTrace();
                        			} catch (IOException e) {
                        				e.printStackTrace();
                        			}
                        		}
      				   
             			   }
             			   else if(v==nearseason){
           				    Log.v("xindian","nearweek");
         			        final Calendar calendar = Calendar.getInstance();
         			        mYear = calendar.get(Calendar.YEAR); //获取当前年份
         			        mMonth = calendar.get(Calendar.MONTH)+1;//获取当前月份
         			        if(mMonth==13){
         			        	mMonth=1;	        	
         			        }
         			        mDay = calendar.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
         			        mHour = calendar.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
         			        mMinute = calendar.get(Calendar.MINUTE);//获取当前的分钟数
                            lstData = (ListView) findViewById(R.id.ecg_files);
                            files=new ArrayAdapter<FileItem>(myhealth_testrecord.this, R.layout.file);
                            lstData.setAdapter(files);
                            for (int i = 0; i < fileList().length; i++) {
                    			String file = fileList()[i];

                    			try {
                    				InputStream stream = openFileInput(file);

                    				int length = stream.available();
                    				byte[] data = new byte[length];

                    				stream.read(data);

                    				FileItem item = new FileItem(i, data, length);
                    				Log.v("fileName", item.fileName);
                    				String array[]=item.fileName.split("-");
                    				int year=Integer.parseInt(array[0]);
                    				Log.v("year", String.valueOf(year));
                    				int month=Integer.parseInt(array[1]);
                    				String arrayforday[]=array[2].split(" ");
                    				int day=Integer.parseInt(arrayforday[0]);
                    				Log.v("month", String.valueOf(month));
                    				Log.v("day", String.valueOf(day));
                    				
                    				
                    				
                    				helper = new DatabaseHelper(getBaseContext(), DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
                    			    db = helper.getWritableDatabase();//打开数据库
                    			    String[] filename1=item.fileName.split(" ");
                    			    String[] filename2=filename1[0].split("-");
                    			    String[] filename3=filename1[1].split(":");
                    			    String fileName=filename2[0]+filename2[1]+filename2[2]+filename3[0]+filename3[1]+filename3[2];
                    			    c=db.query(DatabaseHelper.ECGFILE_TABLE, null,DatabaseHelper.ECGFILE_USERID+"="+userid+" and "+ DatabaseHelper.ECGFILE_FILENAME+"="+fileName, null, null, null,DatabaseHelper.ID+" desc",null);
                    			    Log.v("filename2", fileName);
                    			    Log.v("userid2", String.valueOf(userid));
                    			    c.moveToFirst();
                    			    if(!(c.isAfterLast())){
                                    if(mMonth!=1&&mMonth!=2&&mMonth!=3){
                                    	if(year==mYear&&month==mMonth){
                                        	files.add(item);                                       		
                                    	}
                                    	else if(year==mYear&&month==mMonth-1){
                                        	files.add(item);
                                    	}
                                    	else if(year==mYear&&month==mMonth-2){
                                        	files.add(item);
                                    	}
                                    	else if(year==mYear&&month==mMonth-3&&day>mDay){
                                        	files.add(item);
                                    	}
                                    	else{}

                                    }
                                    else if(mMonth==1){
                                    	if(year==mYear&&month==mMonth){
                                        	files.add(item);                                       		
                                    	}
                                    	else if(year==mYear-1&&month==12){
                                        	files.add(item);
                                    	}
                                    	else if(year==mYear-1&&month==11){
                                        	files.add(item);
                                    	}
                                    	else if(year==mYear-1&&month==10&&day>mDay){
                                        	files.add(item);
                                    	}
                                    	else{}
                                    	}       
                                    else if(mMonth==2){
                                    	if(year==mYear&&month==mMonth){
                                        	files.add(item);                                       		
                                    	}
                                    	else if(year==mYear&&month==1){
                                        	files.add(item);
                                    	}
                                    	else if(year==mYear-1&&month==12){
                                        	files.add(item);
                                    	}
                                    	else if(year==mYear-1&&month==11&&day>mDay){
                                        	files.add(item);
                                    	}
                                    	else{}
                                    	}
                                    else if(mMonth==3){
                                    	if(year==mYear&&month==mMonth){
                                        	files.add(item);                                       		
                                    	}
                                    	else if(year==mYear&&month==2){
                                        	files.add(item);
                                    	}
                                    	else if(year==mYear&&month==1){
                                        	files.add(item);
                                    	}
                                    	else if(year==mYear-1&&month==12&&day>mDay){
                                        	files.add(item);
                                    	}
                                    	else{}
                                    	}
                    			    }
                    				stream.close();
                    			} catch (FileNotFoundException e) {
                    				e.printStackTrace();
                    			} catch (IOException e) {
                    				e.printStackTrace();
                    			}
                    		}
         	             	
           			    	  
           			      }
          				   
             			   else{
             				   
             			   }
             		   }
             	   
             		   
             	   };
                   nearweak.setOnClickListener(btnClick_xindian);
                   nearmonth.setOnClickListener(btnClick_xindian);
                   nearseason.setOnClickListener(btnClick_xindian);
            		
                }//实验打开file
                
                
               else if(tabId=="Tab_1"){

            	   xueya_tag=(Xueya_view)findViewById(R.id.xueya_view);
                   xueya_big=(Button)findViewById(R.id.Button_xueyabig);
                   xueya_small=(Button)findViewById(R.id.Button_xueyasmall);
                   xueya_tag.user_tag=userid;
                   btnClick_xueyaoxi = new OnClickListener(){
             		   public void onClick(View v){
             			  
             			   
             			   if(v==xueya_big){
             				   
             				   if(xueya_tag.tag!=3)
             					   {
             					  xueya_tag.tag=xueya_tag.tag+1;
             				        }
  
             			   }
             			   else if(v==xueya_small){
             				   if(xueya_tag.tag!=1)
             					  xueya_tag.tag=xueya_tag.tag-1;
             			 
             			   }
             			  xueya_tag.invalidate();
             		   }
                   
                    };
                    xueya_tag.invalidate();
                    xueya_big.setOnClickListener(btnClick_xueyaoxi);
                	xueya_small.setOnClickListener(btnClick_xueyaoxi);
               
               } 
               else if(tabId=="Tab_2"){


            	   tizhong_tag=(Tizhong_view)findViewById(R.id.tizhong_view);
                   tizhong_big=(Button)findViewById(R.id.Button_tizhongbig);
                   tizhong_small=(Button)findViewById(R.id.Button_tizhongsmall);
                   tizhong_tag.user_tag=userid;
                    btnClick_tizhongoxi = new OnClickListener(){
              		   public void onClick(View v){
              			 
              			   
              			   if(v==tizhong_big){
              				   
              				   if(tizhong_tag.tag!=3)
              					   {
              					 tizhong_tag.tag=tizhong_tag.tag+1;
              				        }
   
              			   }
              			   else if(v==tizhong_small){
              				   if(tizhong_tag.tag!=1)
              					 tizhong_tag.tag=tizhong_tag.tag-1;
              			 
              			   }
              			 tizhong_tag.invalidate();
              		   }
                    
                     };
                     tizhong_tag.invalidate();
                     tizhong_big.setOnClickListener(btnClick_tizhongoxi);
                     tizhong_small.setOnClickListener(btnClick_tizhongoxi);
                
                
               }
               else if(tabId=="Tab_4"){

            	  zhifang_tag=(Zhifang_view)findViewById(R.id.zhifang_view);
                  zhifang_big=(Button)findViewById(R.id.Button_zhifangbig);
                  zhifang_small=(Button)findViewById(R.id.Button_zhifangsmall);
                  zhifang_tag.user_tag=userid;
                   btnClick_zhifangoxi = new OnClickListener(){
             		   public void onClick(View v){
             			 
             			   
             			   if(v==zhifang_big){
             				   
             				   if(zhifang_tag.tag!=3)
             					   {
             					  zhifang_tag.tag=zhifang_tag.tag+1;
             				        }
  
             			   }
             			   else if(v==zhifang_small){
             				   if(zhifang_tag.tag!=1)
             					  zhifang_tag.tag=zhifang_tag.tag-1;
             			 
             			   }
             			  zhifang_tag.invalidate();
             		   }
                   
                    };
                    zhifang_tag.invalidate();
                    zhifang_big.setOnClickListener(btnClick_zhifangoxi);
                    zhifang_small.setOnClickListener(btnClick_zhifangoxi);
               
               } 
               else if(tabId=="Tab_6"){

             	  xuetang_tag=(Xuetang_view)findViewById(R.id.xuetang_view);
                   xuetang_big=(Button)findViewById(R.id.Button_xuetangbig);
                   xuetang_small=(Button)findViewById(R.id.Button_xuetangsmall);
                   xuetang_tag.user_tag=userid;
                    btnClick_xuetangoxi = new OnClickListener(){
              		   public void onClick(View v){
              			
              			   
              			   if(v==xuetang_big){
              				   
              				   if(xuetang_tag.tag!=3)
              					   {
              					 xuetang_tag.tag=xuetang_tag.tag+1;
              				        }
   
              			   }
              			   else if(v==xuetang_small){
              				   if(xuetang_tag.tag!=1)
              					 xuetang_tag.tag=xuetang_tag.tag-1;
              			 
              			   }
              			 xuetang_tag.invalidate();
              		   }
                    
                     };
                     xuetang_tag.invalidate();
                     xuetang_big.setOnClickListener(btnClick_xuetangoxi);
                     xuetang_small.setOnClickListener(btnClick_xuetangoxi);
                
                } 
               else if(tabId=="Tab_7"){

              	  tiwen_tag=(Tiwen_view)findViewById(R.id.tiwen_view);
              	tiwen_big=(Button)findViewById(R.id.Button_tiwenbig);
              	tiwen_small=(Button)findViewById(R.id.Button_tiwensmall);
              	tiwen_tag.user_tag=userid;     
                     btnClick_tiwenoxi = new OnClickListener(){
               		   public void onClick(View v){
               			
               			   
               			   if(v==tiwen_big){
               				   
               				   if(tiwen_tag.tag!=3)
               					   {
               					tiwen_tag.tag=tiwen_tag.tag+1;
               				        }
    
               			   }
               			   else if(v==tiwen_small){
               				   if(tiwen_tag.tag!=1)
               					tiwen_tag.tag=tiwen_tag.tag-1;
               			 
               			   }
               			 tiwen_tag.invalidate();
               		   }
                     
                      };
                      tiwen_tag.invalidate();
                      tiwen_big.setOnClickListener(btnClick_tiwenoxi);
                      tiwen_small.setOnClickListener(btnClick_tiwenoxi);
                 
                 } 
               else if(tabId=="Tab_8"){

               	 xueyang_tag=(Xueyang_view)findViewById(R.id.xueyang_view);
               	xueyang_big=(Button)findViewById(R.id.Button_xueyangbig);
               	xueyang_small=(Button)findViewById(R.id.Button_xueyangsmall);
               	xueyang_tag.user_tag=userid;         
                      btnClick_xueyangoxi = new OnClickListener(){
                		   public void onClick(View v){
                			  
                			   
                			   if(v==xueyang_big){
                				   
                				   if(xueyang_tag.tag!=3)
                					   {
                					   xueyang_tag.tag=xueyang_tag.tag+1;
                				        }
     
                			   }
                			   else if(v==xueyang_small){
                				   if(xueyang_tag.tag!=1)
                					   xueyang_tag.tag=xueyang_tag.tag-1;
                			 
                			   }
                			   xueyang_tag.invalidate();
                		   }
                      
                       };
                       xueyang_tag.invalidate();
                       xueyang_big.setOnClickListener(btnClick_xueyangoxi);
                       xueyang_small.setOnClickListener(btnClick_xueyangoxi);
                  
                  } 
               else if(tabId=="Tab_9"){

                 	 taixin_tag=(Taixin_view)findViewById(R.id.taixin_view);
                 	taixin_big=(Button)findViewById(R.id.Button_taixinbig);
                 	taixin_small=(Button)findViewById(R.id.Button_taixinsmall);
                 	taixin_tag.user_tag=userid;       
                        btnClick_taixinoxi = new OnClickListener(){
                  		   public void onClick(View v){
                  			
                  			   
                  			   if(v==taixin_big){
                  				   
                  				   if(taixin_tag.tag!=3)
                  					   {
                  					 taixin_tag.tag=taixin_tag.tag+1;
                  				        }
       
                  			   }
                  			   else if(v==taixin_small){
                  				   if(taixin_tag.tag!=1)
                  					 taixin_tag.tag=taixin_tag.tag-1;
                  			 
                  			   }
                  			 taixin_tag.invalidate();
                  		   }
                        
                         };
                         taixin_tag.invalidate();
                         taixin_big.setOnClickListener(btnClick_taixinoxi);
                         taixin_small.setOnClickListener(btnClick_taixinoxi);
                    
                    }

            }         
        });  
        
        
        homeBtn = (Button)findViewById(R.id.testrecord_homebutton);
        returnBtn = (Button)findViewById(R.id.testrecord_returnbutton);
        btnClick= new OnClickListener(){

    		@Override
    		public void onClick(View v) {

        		if(v==homeBtn){
        			//Intent i = new Intent();
        			myhealth_testrecord.this.setResult(RESULT_OK);
        		    myhealth_testrecord.this.finish();	
        		}
        		else if(v==returnBtn){
        			myhealth_testrecord.this.finish();
        		}
        		else{  			
        		}	
    		}
        };
        homeBtn.setOnClickListener(btnClick);
        returnBtn.setOnClickListener(btnClick);
  }
 
	private void viewFile(FileItem file) {
		Intent in = new Intent(this, EcgViewerActivity.class);
		
		in.putExtra("data", file);

		startActivity(in);
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
	        	   

    			//Intent i = new Intent();
    			myhealth_testrecord.this.setResult(RESULT_OK);
    		    myhealth_testrecord.this.finish();	
    		
			

	            return true;

	        }
	        return super.onKeyDown(keyCode, event);
	    }
	
}

