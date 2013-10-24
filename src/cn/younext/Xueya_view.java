package cn.younext;


import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;

import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class  Xueya_view extends View{
	Cursor c;
	DatabaseHelper helper;
	SQLiteDatabase db;

	public static final String DatabaseName="testrecord";
	public static final int Version = 1;
	private int mHour;
	private int mMinute;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int nowmonth;
	private int startyear ;
    private int startmonth;
    private int startday;
    private int starthour;
    private int startminute;
    private int leftyear;
    private int leftmonth;
    private int leftday;
    private int lefthour;
    private int leftminute;
    private int presstag=0;
    private int index;
    
     int user_tag;

	int tag =1;

	int keynum = 0;
   
    
	private int i=0;
	ArrayList<Float> gaoya = new ArrayList<Float>();
	ArrayList<Float> diya = new ArrayList<Float>();
	ArrayList<Float> year = new ArrayList<Float>();
	ArrayList<Float> month = new ArrayList<Float>();
	ArrayList<Float> minute = new ArrayList<Float>();
	ArrayList<Float> hour = new ArrayList<Float>();
	ArrayList<Float> day = new ArrayList<Float>();
	ArrayList<Integer> keycount = new ArrayList<Integer>();
	ArrayList<Float> keyx = new ArrayList<Float>();
	ArrayList<Float> keyy = new ArrayList<Float>();
	ArrayList<Float> keydiya = new ArrayList<Float>();
	ArrayList<Float> keygaoya = new ArrayList<Float>();
	ArrayList<Float> keyy_gao = new ArrayList<Float>();
	ArrayList<Integer> id = new ArrayList<Integer>();
	
	//用户1
	ArrayList<Float> gaoya1 = new ArrayList<Float>();
	ArrayList<Float> diya1 = new ArrayList<Float>();
	ArrayList<Float> year1 = new ArrayList<Float>();
	ArrayList<Float> month1 = new ArrayList<Float>();
	ArrayList<Float> minute1 = new ArrayList<Float>();
	ArrayList<Float> hour1 = new ArrayList<Float>();
	ArrayList<Float> day1 = new ArrayList<Float>();
	ArrayList<Integer> id1 = new ArrayList<Integer>();
	//用户2
	ArrayList<Float> gaoya2 = new ArrayList<Float>();
	ArrayList<Float> diya2 = new ArrayList<Float>();
	ArrayList<Float> year2 = new ArrayList<Float>();
	ArrayList<Float> month2 = new ArrayList<Float>();
	ArrayList<Float> minute2 = new ArrayList<Float>();
	ArrayList<Float> hour2 = new ArrayList<Float>();
	ArrayList<Float> day2 = new ArrayList<Float>();
	ArrayList<Integer> id2 = new ArrayList<Integer>();
	//用户3
	ArrayList<Float> gaoya3 = new ArrayList<Float>();
	ArrayList<Float> diya3 = new ArrayList<Float>();
	ArrayList<Float> year3 = new ArrayList<Float>();
	ArrayList<Float> month3 = new ArrayList<Float>();
	ArrayList<Float> minute3 = new ArrayList<Float>();
	ArrayList<Float> hour3 = new ArrayList<Float>();
	ArrayList<Float> day3 = new ArrayList<Float>();
	ArrayList<Integer> id3 = new ArrayList<Integer>();
	//用户4
	ArrayList<Float> gaoya4 = new ArrayList<Float>();
	ArrayList<Float> diya4= new ArrayList<Float>();
	ArrayList<Float> year4 = new ArrayList<Float>();
	ArrayList<Float> month4 = new ArrayList<Float>();
	ArrayList<Float> minute4 = new ArrayList<Float>();
	ArrayList<Float> hour4 = new ArrayList<Float>();
	ArrayList<Float> day4 = new ArrayList<Float>();
	ArrayList<Integer> id4 = new ArrayList<Integer>();
	//用户5
	ArrayList<Float> gaoya5 = new ArrayList<Float>();
	ArrayList<Float> diya5 = new ArrayList<Float>();
	ArrayList<Float> year5 = new ArrayList<Float>();
	ArrayList<Float> month5 = new ArrayList<Float>();
	ArrayList<Float> minute5 = new ArrayList<Float>();
	ArrayList<Float> hour5 = new ArrayList<Float>();
	ArrayList<Float> day5 = new ArrayList<Float>();
	ArrayList<Integer> id5 = new ArrayList<Integer>();

    float prevx=0;
    float prevy=0;
    float prevy_gao = 0;
    float x=0;
    float y=0;
    float y_gao = 0;
	public Xueya_view(Context context, AttributeSet attr)
	{
		super(context,attr);
		setFocusable(true);
	    setFocusableInTouchMode(true);
	    
	
    	
		final Calendar calendar = Calendar.getInstance();
	        mYear = calendar.get(Calendar.YEAR); //获取当前年份
	        mMonth = calendar.get(Calendar.MONTH)+1;//获取当前月份
	        if(mMonth==13){
	        	mMonth=1;	        	
	        }
	        mDay = calendar.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
	        mHour = calendar.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
	        mMinute = calendar.get(Calendar.MINUTE);//获取当前的分钟数
	        
	        startyear =mYear;
	         startmonth = mMonth;
	         startday = mDay;
	         starthour = mHour;
	        startminute = mMinute;
	       //读数据库
	        helper = new DatabaseHelper(getContext(), DatabaseName, null,Version);  
		    db = helper.getWritableDatabase();
		    c=db.query("xueya", null, "(year="+mYear+" and month<"+mMonth+" or year="+mYear+" and month="+mMonth+" and day<="+mDay+" or year="+(mYear-1)+" and month>"+mMonth+" or year="+(mYear-1)+" and month="+mMonth+" and day>="+mDay+") and gaoya>=40 and gaoya<=190"+" and diya>=40 and diya<=190", null, null,null,"_id desc",null);
		    final int diyaIndex = c.getColumnIndexOrThrow("diya");
		    final int gaoyaIndex = c.getColumnIndexOrThrow("gaoya");
		    final int yearIndex = c.getColumnIndexOrThrow("year");
		    final int monthIndex = c.getColumnIndexOrThrow("month");
		    final int dayIndex = c.getColumnIndexOrThrow("day");
		    final int hourIndex = c.getColumnIndexOrThrow("hour");
		    final int minuteIndex = c.getColumnIndexOrThrow("minute");
		    final int idIndex = c.getColumnIndexOrThrow("_id");
		    final int useridIndex = c.getColumnIndexOrThrow("userid");
		   
		    for (c.moveToFirst();!(c.isAfterLast());c.moveToNext()){
		    	
		    	if(c.getInt(useridIndex)==1){
		    		diya1.add((float)c.getInt(diyaIndex));
		    		gaoya1.add((float)c.getInt(gaoyaIndex));
			    	year1.add((float)c.getInt(yearIndex));
			    	month1.add((float)c.getInt(monthIndex));
			    	minute1.add((float)c.getInt(minuteIndex));
			    	hour1.add((float)c.getInt(hourIndex));
			    	day1.add((float)c.getInt(dayIndex));
			    	id1.add((int)c.getInt(idIndex));
			    	
		    	}
		    	    
		    	
		    	else if(c.getInt(useridIndex)==2){
		    		diya2.add((float)c.getInt(diyaIndex));
		    		gaoya2.add((float)c.getInt(gaoyaIndex));
			    	year2.add((float)c.getInt(yearIndex));
			    	month2.add((float)c.getInt(monthIndex));
			    	minute2.add((float)c.getInt(minuteIndex));
			    	hour2.add((float)c.getInt(hourIndex));
			    	day2.add((float)c.getInt(dayIndex));
			    	id2.add((int)c.getInt(idIndex));
		    	}
		    	else if(c.getInt(useridIndex)==3){
		    		diya3.add((float)c.getInt(diyaIndex));
		    		gaoya3.add((float)c.getInt(gaoyaIndex));
			    	year3.add((float)c.getInt(yearIndex));
			    	month3.add((float)c.getInt(monthIndex));
			    	minute3.add((float)c.getInt(minuteIndex));
			    	hour3.add((float)c.getInt(hourIndex));
			    	day3.add((float)c.getInt(dayIndex));
			    	id3.add((int)c.getInt(idIndex));
		    	}
		    	else if(c.getInt(useridIndex)==4){
		    		diya4.add((float)c.getInt(diyaIndex));
		    		gaoya4.add((float)c.getInt(gaoyaIndex));
			    	year4.add((float)c.getInt(yearIndex));
			    	month4.add((float)c.getInt(monthIndex));
			    	minute4.add((float)c.getInt(minuteIndex));
			    	hour4.add((float)c.getInt(hourIndex));
			    	day4.add((float)c.getInt(dayIndex));
			    	id4.add((int)c.getInt(idIndex));
		    	}
		    	else if(c.getInt(useridIndex)==5){
		    		diya5.add((float)c.getInt(diyaIndex));
		    		gaoya5.add((float)c.getInt(gaoyaIndex));
			    	year5.add((float)c.getInt(yearIndex));
			    	month5.add((float)c.getInt(monthIndex));
			    	minute5.add((float)c.getInt(minuteIndex));
			    	hour5.add((float)c.getInt(hourIndex));
			    	day5.add((float)c.getInt(dayIndex));
			    	id5.add((int)c.getInt(idIndex));
		    	}
		
		    }
		    
		  
		    db.close();
		    
		    
	}
	
	   private int startX = 0;
       private int currX = 0;

      
       private int leftsideyear =0;
       private int leftsidemonth = 0;
       private int leftsideday = 0;
       
       private Bitmap background;
       private Bitmap red_small;
       private Bitmap redpoint_over;
       private Bitmap redpoint;
       private Bitmap green_small;
       private Bitmap greenpoint_over;
       private Bitmap greenpoint;
       private Bitmap danwei;
     
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		diya.clear();
		gaoya.clear();
		year.clear();
    	month.clear();
    	minute.clear();
    	hour.clear();
    	day.clear();
    	id.clear();
	   if(user_tag==1){
	    	for(i=0;i<diya1.size();i++){
	    		diya.add(i, diya1.get(i));
	    		gaoya.add(i, gaoya1.get(i));
		    	year.add(i, year1.get(i));
		    	month.add(i, month1.get(i));
		    	minute.add(i, minute1.get(i));
		    	hour.add(i, hour1.get(i));
		    	day.add(i, day1.get(i));
		    	id.add(i, id1.get(i));
	    	}
	    	
	    }
	    else if(user_tag==2){
	    	for(i=0;i<diya2.size();i++){
	    		diya.add(i, diya2.get(i));
	    		gaoya.add(i, gaoya2.get(i));
		    	year.add(i, year2.get(i));
		    	month.add(i, month2.get(i));
		    	minute.add(i, minute2.get(i));
		    	hour.add(i, hour2.get(i));
		    	day.add(i, day2.get(i));
		    	id.add(i, id2.get(i));
	    	}
	    }
	    else if(user_tag==3){
	    	for(i=0;i<diya3.size();i++){
	    		diya.add(i, diya3.get(i));
	    		gaoya.add(i, gaoya3.get(i));
		    	year.add(i, year3.get(i));
		    	month.add(i, month3.get(i));
		    	minute.add(i, minute3.get(i));
		    	hour.add(i, hour3.get(i));
		    	day.add(i, day3.get(i));
		    	id.add(i, id3.get(i));
	    	}
	    }
	    else if(user_tag==4){
	    	for(i=0;i<diya4.size();i++){
	    		diya.add(i, diya4.get(i));
	    		gaoya.add(i, gaoya4.get(i));
		    	year.add(i, year4.get(i));
		    	month.add(i, month4.get(i));
		    	minute.add(i, minute4.get(i));
		    	hour.add(i, hour4.get(i));
		    	day.add(i, day4.get(i));
		    	id.add(i, id4.get(i));
	    	}
	    }
	    else if(user_tag==5){
	    	for(i=0;i<diya5.size();i++){
	    		diya.add(i, diya5.get(i));
	    		gaoya.add(i, gaoya5.get(i));
		    	year.add(i, year5.get(i));
		    	month.add(i, month5.get(i));
		    	minute.add(i, minute5.get(i));
		    	hour.add(i, hour5.get(i));
		    	day.add(i, day5.get(i));
		    	id.add(i, id5.get(i));
		    	
	    	}
	    }
	   
	
		Paint mPaint = new Paint();
		Paint textPaint = new Paint();
		Paint pointPaint = new Paint();
		Paint gaoyaPaint = new Paint();
	
		textPaint.setAntiAlias(true);
		textPaint.setARGB(255, 91,123,140);
		textPaint.setTextSize(16);
		
		pointPaint.setAntiAlias(true);
		pointPaint.setColor(Color.RED);
		pointPaint.setStrokeWidth(3);
		pointPaint.setStyle(Paint.Style.FILL);
		
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.GREEN);
		mPaint.setStrokeWidth(1);
		
		gaoyaPaint.setAntiAlias(true);
		gaoyaPaint.setColor(Color.RED);
		gaoyaPaint.setStrokeWidth(1);
		
	
        
		background = BitmapFactory.decodeResource(getResources(), R.drawable.otherbackground);
		red_small = BitmapFactory.decodeResource(getResources(), R.drawable.red_small);
		redpoint = BitmapFactory.decodeResource(getResources(), R.drawable.redpoint);
		redpoint_over = BitmapFactory.decodeResource(getResources(), R.drawable.redpoint_over);
		green_small = BitmapFactory.decodeResource(getResources(), R.drawable.green_small);
		greenpoint = BitmapFactory.decodeResource(getResources(), R.drawable.greenpoint);
		greenpoint_over = BitmapFactory.decodeResource(getResources(), R.drawable.greenpoint_over);
		canvas.drawBitmap(background, 90, 5, mPaint);
		danwei = BitmapFactory.decodeResource(getResources(), R.drawable.danwei_xueya);
		canvas.drawBitmap(danwei, 5, 5, mPaint);
		
		Paint rectpaint = new Paint();
		rectpaint.setARGB(150,220, 240, 254);
		canvas.drawRect(90, 254-(90-40)*240/150, 691, 254-(60-40)*240/150, rectpaint);
		Paint rectgaopaint = new Paint();
		rectgaopaint.setARGB(150,253, 236, 236);
		canvas.drawRect(90, 254-(140-40)*240/150, 691, 254-(90-40)*240/150, rectgaopaint);
		 //纵坐标
		for(i=0;i<6;i++){
			canvas.drawLine(80, 14+(i*48),90,14+(i*48), textPaint);
			canvas.drawText(Integer.toString(190-(i*30)),43,20+(i*48),textPaint);
		}
		//最小的图，一个页面365天
		
		if(tag==1){
			
			ArrayList<Float> smallx = new ArrayList<Float>();
			ArrayList<Float> smally = new ArrayList<Float>();
			ArrayList<Float> smally_gao = new ArrayList<Float>();
		//标出当前月份
			
		canvas.drawText(Integer.toString(mMonth), 660, 295, textPaint);
		canvas.drawLine(665,263,665,273,textPaint);
      
        for(i=1;i<13;i++){
			if(mMonth-i<1)
				nowmonth= 12+(mMonth-i);
			else nowmonth = mMonth-i;
			canvas.drawText(Integer.toString((nowmonth)),660-(550*i)/12, 295, textPaint);
			//标出横轴上的点
			if(i%2==0)
			    canvas.drawLine(665-(550*i)/12,263,665-(550*i)/12,273,textPaint);
			else
				canvas.drawLine(665-(550*i)/12,263,665-(550*i)/12,270,textPaint);
		
	
		}
		
		
	//计算数据库中的最近点和当前时间的距离，给出连线的起始点
        if(diya.size()>0){
        	if(year.get(0)==mYear&&month.get(0)!=mMonth){
            	prevx = 665-((mMonth-month.get(0)-1)*24*30*60+(31-day.get(0))*24*60+(24-hour.get(0)-1)*60+(60-minute.get(0))+(mDay-1)*24*60+mHour*60+mMinute)*550/(365*24*60);
                prevy = 254-(diya.get(0)-40)*240/150;
                prevy_gao = 254-(gaoya.get(0)-40)*240/150;
              
    		}
    		else if (year.get(0)!=mYear){
    			prevx = 665-((mMonth-1)*24*30*60+(mDay-1)*24*60+mHour*60+mMinute+(12-month.get(0))*30*60*24+(31-day.get(0))*24*60+(24-hour.get(0)-1)*60+(60-minute.get(0)))*550/(365*24*60);
    			 prevy = 254-(diya.get(0)-40)*240/150;
                 prevy_gao = 254-(gaoya.get(0)-40)*240/150;
    			
    		}
    		else if (year.get(0)==mYear&&month.get(0)==mMonth&&day.get(0)!=mDay){
    			prevx =665- ((mDay-day.get(0)-1)*24*60+mHour*60+mMinute+(24-hour.get(0)-1)*60+(60-minute.get(0)))*550/(365*24*60);
    			 prevy = 254-(diya.get(0)-40)*240/150;
                 prevy_gao = 254-(gaoya.get(0)-40)*240/150;
    			
    		}
    		else if (year.get(0)==mYear&&month.get(0)==mMonth&&day.get(0)==mDay&&hour.get(0)!=mHour){
    			prevx = 665-((mHour-hour.get(0)-1)*60+mMinute+(60-minute.get(0)))*550/(365*24*60);
    			 prevy = 254-(diya.get(0)-40)*240/150;
                 prevy_gao = 254-(gaoya.get(0)-40)*240/150;
    			
    		}
    		else if (year.get(0)==mYear&&month.get(0)==mMonth&&day.get(0)==mDay&&hour.get(0)==mHour){
    			prevx = 665-((mMinute-minute.get(0)))*550/(365*24*60);
    			prevy = 254-(diya.get(0)-40)*240/150;
                prevy_gao = 254-(gaoya.get(0)-40)*240/150;
    			
    		}
        	smallx.add(prevx);
            smally.add(prevy);
            smally_gao.add(prevy_gao);
        }
        
  
		for(i=1;i<diya.size();i++){
			
			if(year.get(i)==mYear&&month.get(i)!=mMonth){
	        	
	            canvas.drawLine(prevx,prevy ,665-((mMonth-month.get(i)-1)*24*30*60+(30-day.get(i))*24*60+(24-hour.get(i)-1)*60+(60-minute.get(i))+(mDay-1)*24*60+mHour*60+mMinute)*550/(365*24*60) ,254-(diya.get(i)-40)*240/150 , mPaint);
	            canvas.drawLine(prevx,prevy_gao ,665-((mMonth-month.get(i)-1)*24*30*60+(30-day.get(i))*24*60+(24-hour.get(i)-1)*60+(60-minute.get(i))+(mDay-1)*24*60+mHour*60+mMinute)*550/(365*24*60) ,254-(gaoya.get(i)-40)*240/150 , gaoyaPaint);
	            prevx = 665-((mMonth-month.get(i)-1)*24*30*60+(31-day.get(i))*24*60+(24-hour.get(i)-1)*60+(60-minute.get(i))+(mDay-1)*24*60+mHour*60+mMinute)*550/(365*24*60);
	            prevy = 254-(diya.get(i)-40)*240/150;
                prevy_gao = 254-(gaoya.get(i)-40)*240/150;
	          
			}
			else if (year.get(i)!=mYear){
			
				canvas.drawLine(prevx,prevy,665-((mMonth-1)*24*30*60+(mDay-1)*24*60+mHour*60+mMinute+(12-month.get(i))*30*60*24+(31-day.get(i))*24*60+(24-hour.get(i)-1)*60+(60-minute.get(i)))*550/(365*24*60),254-(diya.get(i)-40)*240/150,mPaint);
				canvas.drawLine(prevx,prevy_gao,665-((mMonth-1)*24*30*60+(mDay-1)*24*60+mHour*60+mMinute+(12-month.get(i))*30*60*24+(31-day.get(i))*24*60+(24-hour.get(i)-1)*60+(60-minute.get(i)))*550/(365*24*60),254-(gaoya.get(i)-40)*240/150,gaoyaPaint);
				prevx = 665-((mMonth-1)*24*30*60+(mDay-1)*24*60+mHour*60+mMinute+(12-month.get(i))*30*60*24+(30-day.get(i))*24*60+(24-hour.get(i)-1)*60+(60-minute.get(i)))*550/(365*24*60);
				prevy = 254-(diya.get(i)-40)*240/150;
                prevy_gao = 254-(gaoya.get(i)-40)*240/150;
		
			}
			else if (year.get(i)==mYear&&month.get(i)==mMonth&&day.get(i)!=mDay){
			
				canvas.drawLine(prevx, prevy, ((mDay-day.get(i)-1)*24*60+mHour*60+mMinute+(24-hour.get(i)-1)*60+(60-minute.get(i)))*550/(365*24*60), 254-(diya.get(i)-40)*240/150,mPaint);
				canvas.drawLine(prevx, prevy_gao, ((mDay-day.get(i)-1)*24*60+mHour*60+mMinute+(24-hour.get(i)-1)*60+(60-minute.get(i)))*550/(365*24*60), 254-(gaoya.get(i)-40)*240/150,gaoyaPaint);
				prevx = 665-((mDay-day.get(i)-1)*24*60+mHour*60+mMinute+(24-hour.get(i)-1)*60+(60-minute.get(i)))*550/(365*24*60);
				prevy = 254-(diya.get(i)-40)*240/150;
                prevy_gao = 254-(gaoya.get(i)-40)*240/150;
			
			}
			else if (year.get(i)==mYear&&month.get(i)==mMonth&&day.get(i)==mDay&&hour.get(i)!=mHour){
			
				canvas.drawLine(prevx, prevy, 560-((mHour-hour.get(i)-1)*60+mMinute+(60-minute.get(i)))*550/(365*24*60), 254-(diya.get(i)-40)*240/150,mPaint);
				canvas.drawLine(prevx, prevy_gao, 560-((mHour-hour.get(i)-1)*60+mMinute+(60-minute.get(i)))*550/(365*24*60), 254-(gaoya.get(i)-40)*240/150,gaoyaPaint);
				prevx = 665-((mHour-hour.get(i)-1)*60+mMinute+(60-minute.get(i)))*550/(365*24*60);
				prevy = 254-(diya.get(i)-40)*240/150;
                prevy_gao = 254-(gaoya.get(i)-40)*240/150;
		
			}
			else if (year.get(i)==mYear&&month.get(i)==mMonth&&day.get(i)==mDay&&hour.get(i)==mHour){
			
				canvas.drawLine(prevx, prevy, 560-((mMinute-minute.get(i)))*550/(365*24*60),254-(diya.get(i)-40)*240/150,mPaint);
				canvas.drawLine(prevx, prevy_gao, 560-((mMinute-minute.get(i)))*550/(365*24*60),254-(gaoya.get(i)-40)*240/150,gaoyaPaint);
				prevx = 665-((mMinute-minute.get(i)))*550/(365*24*60);
				prevy = 254-(diya.get(i)-40)*240/150;
                prevy_gao = 254-(gaoya.get(i)-40)*240/150;
		
			}
			
			smallx.add(prevx);
	        smally.add(prevy);
	        smally_gao.add(prevy_gao);
		}
		for(i=0;i<smallx.size();i++){
			canvas.drawCircle(smallx.get(i), smally.get(i), 4, pointPaint);
			canvas.drawCircle(smallx.get(i), smally_gao.get(i), 4, pointPaint);
		}
			

		}
		
		//中等大小的图，一个页面30天
		else if (tag==2){
			
			ArrayList<Float> middlex = new ArrayList<Float>();
			ArrayList<Float> middley = new ArrayList<Float>();
			ArrayList<Float> middlegaoya = new ArrayList<Float>();
			ArrayList<Float> middlediya = new ArrayList<Float>();
			ArrayList<Float> middley_gao = new ArrayList<Float>();
			
			for(i=0;i<11;i++){
				if(i%2==0)
				    canvas.drawLine(665-55*i,263,665-55*i,273,textPaint);
				else
					canvas.drawLine(665-55*i,263,665-55*i,270,textPaint);
			}

			
			
			//画刻度，一页标三个日期，相隔10天
			if(startday>30){
				for(i=0;i<6;i++)
					canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-6*i), 660-(550*i)/5, 295, textPaint);	
			}
			else if(startday<=30&&startday>24){
				for(i=0;i<5;i++)
					canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-6*i), 660-(550*i)/5, 295, textPaint);
				
				//前面一月是30天
				if(startmonth==12||startmonth==10||startmonth==7||startmonth==5){
					canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(startday), 110, 295, textPaint);
				}
                //前面一月是31天
				else if(startmonth==2||startmonth==4||startmonth==6||startmonth==9||startmonth==11||startmonth==8){
					canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(1+startday), 110, 295, textPaint);
				}
                //前面一月是上一年12月
                else if(startmonth==1)
                    canvas.drawText(Integer.toString(12)+"."+Integer.toString(1+startday), 110, 295, textPaint); 
                //前面一月是2月
				else if(startmonth==3){
					if(startyear%4==0&&startyear%100!=0)
                        canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(startday-1),110, 295, textPaint);
				    else if(startyear%400==0)
                        canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(startday-1), 110, 295, textPaint);
                    else
                        canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(startday-2), 110, 295,textPaint);
                } 
			}
			else if(startday<=24&&startday>18){
				for(i=0;i<4;i++)
					canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-6*i), 660-(550*i)/5, 295, textPaint);
				
			    //前面一月是30天
                if(startmonth==12||startmonth==10||startmonth==7||startmonth==5)
                    for(i=1;i<3;i++)
                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(30+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
              
                //前面一月是31天
				else if(startmonth==2||startmonth==4||startmonth==6||startmonth==9||startmonth==11||startmonth==8)
					for(i=1;i<3;i++)
                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(31+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                //前面一月是上一年12月
                else if(startmonth==1)
                	for(i=1;i<3;i++)
                    	canvas.drawText(Integer.toString(12)+"."+Integer.toString(31+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                //前面一月是2月
				else if(startmonth==3){
					if(startyear%4==0&&startyear%100!=0)
						for(i=1;i<3;i++)
	                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(29+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
				    else if(startyear%400==0)
				    	for(i=1;i<3;i++)
	                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(29+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                    else
                    	for(i=1;i<3;i++)
                        	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(28+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                }
            }
            
			else if(startday<=18&&startday>12){
				for(i=0;i<3;i++)
					canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-6*i), 660-(550*i)/5, 295, textPaint);
				
			    //前面一月是30天
                if(startmonth==12||startmonth==10||startmonth==7||startmonth==5)
                    for(i=1;i<4;i++)
                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(30+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
              
                //前面一月是31天
				else if(startmonth==2||startmonth==4||startmonth==6||startmonth==9||startmonth==11||startmonth==8)
					for(i=1;i<4;i++)
                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(31+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                //前面一月是上一年12月
                else if(startmonth==1)
                	for(i=1;i<4;i++)
                    	canvas.drawText(Integer.toString(12)+"."+Integer.toString(31+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                //前面一月是2月
				else if(startmonth==3){
					if(startyear%4==0&&startyear%100!=0)
						for(i=1;i<4;i++)
	                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(29+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
				    else if(startyear%400==0)
				    	for(i=1;i<4;i++)
	                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(29+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                    else
                    	for(i=1;i<4;i++)
                        	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(28+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                }
                
                
            }
			else if(startday<=12&&startday>6){

				for(i=0;i<2;i++)
					canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-6*i), 660-(550*i)/5, 295, textPaint);
				
			    //前面一月是30天
                if(startmonth==12||startmonth==10||startmonth==7||startmonth==5)
                    for(i=1;i<5;i++)
                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(30+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
              
                //前面一月是31天
				else if(startmonth==2||startmonth==4||startmonth==6||startmonth==9||startmonth==11||startmonth==8)
					for(i=1;i<5;i++)
                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(31+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                //前面一月是上一年12月
                else if(startmonth==1)
                	for(i=1;i<5;i++)
                    	canvas.drawText(Integer.toString(12)+"."+Integer.toString(31+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                //前面一月是2月
				else if(startmonth==3){
					if(startyear%4==0&&startyear%100!=0)
						for(i=1;i<5;i++)
	                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(29+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
				    else if(startyear%400==0)
				    	for(i=1;i<5;i++)
	                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(29+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                    else
                    	for(i=1;i<5;i++)
                        	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(28+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                }
            
			}
			else if(startday<=6&&startday>=1){

					canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday), 660, 295, textPaint);
				
			    //前面一月是30天
                if(startmonth==12||startmonth==10||startmonth==7||startmonth==5)
                    for(i=1;i<6;i++)
                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(30+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
              
                //前面一月是31天
				else if(startmonth==2||startmonth==4||startmonth==6||startmonth==9||startmonth==11||startmonth==8)
					for(i=1;i<6;i++)
                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(31+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                //前面一月是上一年12月
                else if(startmonth==1)
                	for(i=1;i<6;i++)
                    	canvas.drawText(Integer.toString(12)+"."+Integer.toString(31+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                //前面一月是2月
				else if(startmonth==3){
					if(startyear%4==0&&startyear%100!=0)
						for(i=1;i<6;i++)
	                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(29+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
				    else if(startyear%400==0)
				    	for(i=1;i<6;i++)
	                    	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(29+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                    else
                    	for(i=1;i<6;i++)
                        	canvas.drawText(Integer.toString(startmonth-1)+"."+Integer.toString(28+startday-6*(6-i)), 110+(i-1)*110, 295, textPaint);
                }
            
			}

			
				
				
				//计算一页的前刻度leftyear,leftmonth,leftday...
				lefthour = starthour;
				leftminute = startminute;
				if(startmonth==1||startmonth==2||startmonth==4||startmonth==6||startmonth==8||startmonth==9||startmonth==11){//前一月是31天，向前跨越30天不可能超过前一月
					if(startday==31){
						leftyear = startyear;
						leftmonth = startmonth;
						leftday = startday-30;
					}
					else if(startday<=30){
						if(startmonth==1){
						leftyear = startyear-1;
						leftmonth = 12;
						}
						else{
							leftyear = startyear;
							leftmonth = startmonth-1;
							leftday = startday+1;
						}
					}
				}
				else if(startmonth==5||startmonth==7||startmonth==10||startmonth==12){//前一月是30天，向前跨越30天不可能超过前一月
					if(startday==31){
						leftyear = startyear;
						leftmonth = startmonth;
						leftday = startday-30;
					}
					else if(startday<=30){
						    leftyear = startyear;
							leftmonth = startmonth-1;
							leftday = startday;
						
					}
				}
				else if(startmonth==3){
					if(startday==31){
						leftyear = startyear;
						leftmonth = startmonth;
						leftday = startday-30;
					}
					else if(startday!=31){
					    if(startyear%400==0){
						     if(startday>1){
						    	 leftyear = startyear;
								 leftmonth = startmonth-1;
								 leftday = startday-1;
						     }
						     else if(startday==1){
						    	 leftyear = startyear;
								 leftmonth = startmonth-2;
								 leftday = 31;
						     }
					    }
					    else if(startyear%4==0&&startyear%100!=0){
					    	if(startday>1){
						    	 leftyear = startyear;
								 leftmonth = startmonth-1;
								 leftday = startday-1;
						     }
						     else if(startday==1){
						    	 leftyear = startyear;
								 leftmonth = startmonth-2;
								 leftday = 31;
						     }
					    }
					    else{
					    	if(startday>2){
						    	 leftyear = startyear;
								 leftmonth = startmonth-1;
								 leftday = startday-2;
						     }
						     else if(startday==1||startday==2){
						    	 leftyear = startyear;
								 leftmonth = startmonth-2;
								 leftday = 31-startday;
						     }
					    }
					}
					
				}//计算一页的前刻度leftyear,leftmonth,leftday...
				
				//画线
				int pointcount = 0;
				
				for(i=0;i<diya.size();i++){
					
					x = -1;
					if(startmonth==leftmonth){
					    if(year.get(i)==startyear&&month.get(i)==startmonth){
					
							if(day.get(i)<startday&&day.get(i)>leftday){
							   x = 665-((startday-day.get(i)-1)*24*60+starthour*60+startminute+(24-hour.get(i)-1)*60+60-minute.get(i))*550/(30*24*60);
							}
							else if(day.get(i)==startday&&hour.get(i)<starthour){
							  x = 665-((starthour-hour.get(i)-1)*60+60-minute.get(i)+startminute)*550/(30*24*60);
							}
							else if(day.get(i)==startday&&hour.get(i)==starthour&&minute.get(i)<=startminute){
								x = 665 - (startminute-minute.get(i))*550/(30*24*60);
							}
							else if(day.get(i)==leftday&&hour.get(i)>lefthour){
								x = 115+((hour.get(i)-lefthour-1)*60+60-leftminute+minute.get(i))*550/(30*24*60);
							}
							else if(day.get(i)==leftday&&hour.get(i)==lefthour&&minute.get(i)>leftminute){
								x = 115+(minute.get(i)-leftminute)*550/(30*24*60);
							}
						
						
					}
					}
					else if(leftmonth==startmonth-1){
						if(year.get(i)==startyear&&month.get(i)==startmonth){
							if(day.get(i)<startday){
								   x = 665-((startday-day.get(i)-1)*24*60+starthour*60+startminute+(24-hour.get(i)-1)*60+60-minute.get(i))*550/(30*24*60);
								}
								else if(day.get(i)==startday&&hour.get(i)<starthour){
								  x = 665-((starthour-hour.get(i)-1)*60+60-minute.get(i)+startminute)*550/(30*24*60);
								}
								else if(day.get(i)==startday&&hour.get(i)==starthour&&minute.get(i)<=startminute){
									x = 665 - (startminute-minute.get(i))*550/(30*24*60);
								}
								
						}
						else if(year.get(i)==leftyear&&month.get(i)==leftmonth){
							if(day.get(i)>leftday){
								   x =115+((day.get(i)-leftday-1)*24*60+hour.get(i)*60+minute.get(i)+(24-lefthour-1)*60+60-leftminute)*550/(30*24*60);
								}
							
								else if(day.get(i)==leftday&&hour.get(i)>lefthour){
									x = 115+((hour.get(i)-lefthour-1)*60+60-leftminute+minute.get(i))*550/(30*24*60);
								}
								else if(day.get(i)==leftday&&hour.get(i)==lefthour&&minute.get(i)>leftminute){
									x = 115+(minute.get(i)-leftminute)*550/(30*24*60);
								}
						}
					}
					else if(leftmonth==12&&startmonth==1){
						if(year.get(i)==startyear&&month.get(i)==startmonth){
							if(day.get(i)<startday){
								   x = 665-((startday-day.get(i)-1)*24*60+starthour*60+startminute+(24-hour.get(i)-1)*60+60-minute.get(i))*550/(30*24*60);
								}
								else if(day.get(i)==startday&&hour.get(i)<starthour){
								  x = 665-((starthour-hour.get(i)-1)*60+60-minute.get(i)+startminute)*550/(30*24*60);
								}
								else if(day.get(i)==startday&&hour.get(i)==starthour&&minute.get(i)<=startminute){
									x = 665 - (startminute-minute.get(i))*550/(30*24*60);
								}
								
						}
						else if(year.get(i)==leftyear&&month.get(i)==leftmonth){
							if(day.get(i)>leftday){
								   x = 115+((day.get(i)-leftday-1)*24*60+hour.get(i)*60+minute.get(i)+(24-lefthour-1)*60+60-leftminute)*550/(30*24*60);
								}
							
								else if(day.get(i)==leftday&&hour.get(i)>lefthour){
									x = 115+((hour.get(i)-lefthour-1)*60+60-leftminute+minute.get(i))*550/(30*24*60);
								}
								else if(day.get(i)==leftday&&hour.get(i)==lefthour&&minute.get(i)>leftminute){
									x = 115+(minute.get(i)-leftminute)*550/(30*24*60);
								}
						}
					}
					else if(leftmonth==startmonth-2){//只有包含2月的时候
						if(startday==1&&leftday==30){
							if(year.get(i)==startyear&&month.get(i)==1&&day.get(i)==31)
								x = 115+(hour.get(i)*60+minute.get(i)+(24-lefthour-1)*60+60-leftminute)*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==1&&day.get(i)==30&&hour.get(i)>lefthour)
								x = 115+((hour.get(i)-lefthour-1)*60+minute.get(i)+60-leftminute)*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==1&&day.get(i)==30&&hour.get(i)==lefthour&&minute.get(i)>leftminute)
								x =115+ (minute.get(i)-leftminute)*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==startmonth&&day.get(i)==startday&&hour.get(i)<starthour)
								x = 665-((starthour-hour.get(i)-1)*60+startminute+60-minute.get(i))*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==startmonth&&day.get(i)==startday&&hour.get(i)==starthour&&minute.get(i)<startminute)
								x = 665-(startminute-minute.get(i))*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==2)
								x = 115+((24-lefthour-1)*60+60-leftminute+24*60+(day.get(i)-1)*24*60+hour.get(i)*60+minute.get(i))*550/(30*24*60);
						}
						else if(startday==2&&leftday==31){
							if(year.get(i)==startyear&&month.get(i)==1&&day.get(i)==31&&hour.get(i)>lefthour)
								x = 115+((hour.get(i)-lefthour-1)*60+minute.get(i)+60-leftminute)*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==1&&day.get(i)==31&&hour.get(i)==lefthour&&minute.get(i)>leftminute)
								x = 115+(minute.get(i)-leftminute)*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==2)
								x = 115+((24-lefthour-1)*60+60-leftminute+(day.get(i)-1)*24*60+hour.get(i)*60+minute.get(i))*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==3&&day.get(i)==1)
								x = 665-(starthour*60+startminute+(24-hour.get(i)-1)*60+60-minute.get(i))*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==startmonth&&day.get(i)==startday&&hour.get(i)<starthour)
								x = 665-((starthour-hour.get(i)-1)*60+startminute+60-minute.get(i))*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==startmonth&&day.get(i)==startday&&hour.get(i)==starthour&&minute.get(i)<startminute)
								x = 665-(startminute-minute.get(i))*550/(30*24*60);
						}
						else if(startday==1&&leftday==31){
							if(year.get(i)==startyear&&month.get(i)==1&&day.get(i)==31&&hour.get(i)>lefthour)
								x = 115+((hour.get(i)-lefthour-1)*60+minute.get(i)+60-leftminute)*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==1&&day.get(i)==31&&hour.get(i)==lefthour&&minute.get(i)>leftminute)
								x = 115+(minute.get(i)-leftminute)*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==startmonth&&day.get(i)==startday&&hour.get(i)<starthour)
								x = 665-((starthour-hour.get(i)-1)*60+startminute+60-minute.get(i))*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==startmonth&&day.get(i)==startday&&hour.get(i)==starthour&&minute.get(i)<startminute)
								x = 665-(startminute-minute.get(i))*550/(30*24*60);
							else if(year.get(i)==startyear&&month.get(i)==2)
								x = 115+((24-lefthour-1)*60+60-leftminute+(day.get(i)-1)*24*60+hour.get(i)*60+minute.get(i))*550/(30*24*60);
						}
					}
					
					if(x>=0){
					   if(pointcount>0){
						   canvas.drawLine(prevx, prevy, x, 254-(diya.get(i)-40)*240/150, mPaint);
						   canvas.drawLine(prevx, prevy_gao, x, 254-(gaoya.get(i)-40)*240/150, gaoyaPaint);
					   }
						middlegaoya.add(gaoya.get(i));
						middlediya.add(diya.get(i));
					    middlex.add(x-6);
						middley.add(254-(diya.get(i)-40)*240/150-6);
						middley_gao.add(254-(gaoya.get(i)-40)*240/150-6);
						
						prevx = x;
						prevy = 254-(diya.get(i)-40)*240/150;
						prevy_gao = 254-(gaoya.get(i)-40)*240/150;
						pointcount++;
					}
					
				}
				for(i=0;i<middlex.size();i++){
					if(middlegaoya.get(i)>=90&&middlegaoya.get(i)<=140)
						canvas.drawBitmap(green_small, middlex.get(i), middley_gao.get(i), mPaint);
					else
						canvas.drawBitmap(red_small, middlex.get(i), middley_gao.get(i), mPaint);
					if(middlediya.get(i)>=60&&middlediya.get(i)<=90)
					    canvas.drawBitmap(green_small, middlex.get(i), middley.get(i), mPaint);
					else
						canvas.drawBitmap(red_small, middlex.get(i), middley.get(i), mPaint);
				}
					
				//画线画点。。。。
    
			
		}
		
		//最大的图，可点击，一个页面10天
		else if(tag==3){
			
			for(i=0;i<11;i++){
				if(i%2==0)
				    canvas.drawLine(665-55*i,263,665-55*i,273,textPaint);
				else
					canvas.drawLine(665-55*i,263,665-55*i,270,textPaint);
			}

			//画刻度及一页的前刻度
			lefthour = starthour;
			leftminute = startminute;
			if(startday>10){
				leftmonth = startmonth;
				leftday = startday - 10;
				for(i=0;i<6;i++)
					canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
				
			}
			else if(startday<=10){
			   if(startmonth==3){
				   if(startyear%400==0){
					   leftyear = startyear;
					   leftmonth = startmonth-1;
					   leftday = startday+19;
					     if(startday<=2){
					    	 canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday), 660, 295, textPaint);
					    	 for(i=1;i<6;i++)
									canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(29+startday-2*i), 660-(550*i)/5, 295, textPaint);	
							
					     }
					     else if(startday>2&&startday<=4){
					    	 for(i=0;i<2;i++)
									canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
					    	 for(i=2;i<6;i++)
									canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(29+startday-2*i), 660-(550*i)/5, 295, textPaint);	
					    	 
					     }
					     else if(startday>4&&startday<=6){
					    	 for(i=0;i<3;i++)
									canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
					    	 for(i=3;i<6;i++)
									canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(29+startday-2*i), 660-(550*i)/5, 295, textPaint);	
					    	 
					     }
					     else if(startday>6&&startday<=8){
					    	 for(i=0;i<4;i++)
									canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
					    	 for(i=4;i<6;i++)
									canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(29+startday-2*i), 660-(550*i)/5, 295, textPaint);	
					     }
					     else if(startday>8&&startday<=10){
					    	 for(i=0;i<5;i++)
									canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
					    	 for(i=5;i<6;i++)
									canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(29+startday-2*i), 660-(550*i)/5, 295, textPaint);	
					    	 
					     }
					   
				    }
				    else if(startyear%4==0&&startyear%100!=0){
						   leftyear = startyear;
						   leftmonth = startmonth-1;
						   leftday = startday+19;
						   if(startday<=2){
						    	 canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday), 660, 295, textPaint);
						    	 for(i=1;i<6;i++)
										canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(29+startday-2*i), 660-(550*i)/5, 295, textPaint);	
								
						     }
						     else if(startday>2&&startday<=4){
						    	 for(i=0;i<2;i++)
										canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 for(i=2;i<6;i++)
										canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(29+startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 
						     }
						     else if(startday>4&&startday<=6){
						    	 for(i=0;i<3;i++)
										canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 for(i=3;i<6;i++)
										canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(29+startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 
						     }
						     else if(startday>6&&startday<=8){
						    	 for(i=0;i<4;i++)
										canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 for(i=4;i<6;i++)
										canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(29+startday-2*i), 660-(550*i)/5, 295, textPaint);	
						     }
						     else if(startday>8&&startday<=10){
						    	 for(i=0;i<5;i++)
										canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 for(i=5;i<6;i++)
										canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(29+startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 
						     }
						     }
					    
				    else{
						   leftyear = startyear;
						   leftmonth = startmonth-1;
						   leftday = startday+18;
						   if(startday<=2){
						    	 canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday), 660, 295, textPaint);
						    	 for(i=1;i<6;i++)
										canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(28+startday-2*i), 660-(550*i)/5, 295, textPaint);	
								
						     }
						     else if(startday>2&&startday<=4){
						    	 for(i=0;i<2;i++)
										canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 for(i=2;i<6;i++)
										canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(28+startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 
						     }
						     else if(startday>4&&startday<=6){
						    	 for(i=0;i<3;i++)
										canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 for(i=3;i<6;i++)
										canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(28+startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 
						     }
						     else if(startday>6&&startday<=8){
						    	 for(i=0;i<4;i++)
										canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 for(i=4;i<6;i++)
										canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(28+startday-2*i), 660-(550*i)/5, 295, textPaint);	
						     }
						     else if(startday>8&&startday<=10){
						    	 for(i=0;i<5;i++)
										canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 for(i=5;i<6;i++)
										canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(28+startday-2*i), 660-(550*i)/5, 295, textPaint);	
						    	 
						     }
					    }
			   }
			   
			  else if(startmonth==1||startmonth==2||startmonth==4||startmonth==6||startmonth==8||startmonth==9||startmonth==11){//前面一月是31天
				  if(startmonth==1){
					  leftyear = startyear-1;
					  leftmonth = 12;
					  leftday = startday+21;
				  }
				  else{
					  leftyear = startyear;
					  leftmonth = startmonth-1;
					  leftday = startday+21;
				  }
				  
				  if(startday<=2){
				    	 canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday), 660, 295, textPaint);
				    	 for(i=1;i<6;i++)
								canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(31+startday-2*i), 660-(550*i)/5, 295, textPaint);	
						
				     }
				     else if(startday>2&&startday<=4){
				    	 for(i=0;i<2;i++)
								canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 for(i=2;i<6;i++)
								canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(31+startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 
				     }
				     else if(startday>4&&startday<=6){
				    	 for(i=0;i<3;i++)
								canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 for(i=3;i<6;i++)
								canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(31+startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 
				     }
				     else if(startday>6&&startday<=8){
				    	 for(i=0;i<4;i++)
								canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 for(i=4;i<6;i++)
								canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(31+startday-2*i), 660-(550*i)/5, 295, textPaint);	
				     }
				     else if(startday>8&&startday<=10){
				    	 for(i=0;i<5;i++)
								canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 for(i=5;i<6;i++)
								canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(31+startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 
				     }
			  }
			  else if(startmonth==5||startmonth==7||startmonth==10||startmonth==12){//前面一月是30天
				  leftyear = startyear;
				  leftmonth = startmonth-1;
				  leftday = startday+20;
				  if(startday<=2){
				    	 canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday), 660, 295, textPaint);
				    	 for(i=1;i<6;i++)
								canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(30+startday-2*i), 660-(550*i)/5, 295, textPaint);	
						
				     }
				     else if(startday>2&&startday<=4){
				    	 for(i=0;i<2;i++)
								canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 for(i=2;i<6;i++)
								canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(30+startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 
				     }
				     else if(startday>4&&startday<=6){
				    	 for(i=0;i<3;i++)
								canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 for(i=3;i<6;i++)
								canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(30+startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 
				     }
				     else if(startday>6&&startday<=8){
				    	 for(i=0;i<4;i++)
								canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 for(i=4;i<6;i++)
								canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(30+startday-2*i), 660-(550*i)/5, 295, textPaint);	
				     }
				     else if(startday>8&&startday<=10){
				    	 for(i=0;i<5;i++)
								canvas.drawText(Integer.toString(startmonth)+"."+Integer.toString(startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 for(i=5;i<6;i++)
								canvas.drawText(Integer.toString(leftmonth)+"."+Integer.toString(30+startday-2*i), 660-(550*i)/5, 295, textPaint);	
				    	 
				     }
			  }
			 
	   }//画刻度及一页的前刻度
			
			//画线
			int pointcount = 0;
			
			for(i=0;i<diya.size();i++){
				x = -1;
				
				if(startmonth==leftmonth){
				    if(year.get(i)==startyear&&month.get(i)==startmonth){
				
						if(day.get(i)<startday&&day.get(i)>leftday){
						   x = 665-((startday-day.get(i)-1)*24*60+starthour*60+startminute+(24-hour.get(i)-1)*60+60-minute.get(i))*550/(10*24*60);
						}
						else if(day.get(i)==startday&&hour.get(i)<starthour){
						  x = 665-((starthour-hour.get(i)-1)*60+60-minute.get(i)+startminute)*550/(10*24*60);
						}
						else if(day.get(i)==startday&&hour.get(i)==starthour&&minute.get(i)<=startminute){
							x = 665 - (startminute-minute.get(i))*550/(10*24*60);
						}
						else if(day.get(i)==leftday&&hour.get(i)>lefthour){
							x = 115+((hour.get(i)-lefthour-1)*60+60-leftminute+minute.get(i))*550/(10*24*60);
						}
						else if(day.get(i)==leftday&&hour.get(i)==lefthour&&minute.get(i)>leftminute){
							x = 115+(minute.get(i)-leftminute)*550/(10*24*60);
						}
					
					
				}
				}
				else if(leftmonth==startmonth-1){
					if(year.get(i)==startyear&&month.get(i)==startmonth){
						if(day.get(i)<startday){
							   x = 665-((startday-day.get(i)-1)*24*60+starthour*60+startminute+(24-hour.get(i)-1)*60+60-minute.get(i))*550/(10*24*60);
							}
							else if(day.get(i)==startday&&hour.get(i)<starthour){
							  x = 665-((starthour-hour.get(i)-1)*60+60-minute.get(i)+startminute)*550/(10*24*60);
							}
							else if(day.get(i)==startday&&hour.get(i)==starthour&&minute.get(i)<=startminute){
								x = 665 - (startminute-minute.get(i))*550/(10*24*60);
							}
							
					}
					else if(year.get(i)==leftyear&&month.get(i)==leftmonth){
						if(day.get(i)>leftday){
							   x = 115+((day.get(i)-leftday-1)*24*60+hour.get(i)*60+minute.get(i)+(24-lefthour-1)*60+60-leftminute)*550/(10*24*60);
							}
						
							else if(day.get(i)==leftday&&hour.get(i)>lefthour){
								x = 115+((hour.get(i)-lefthour-1)*60+60-leftminute+minute.get(i))*550/(10*24*60);
							}
							else if(day.get(i)==leftday&&hour.get(i)==lefthour&&minute.get(i)>leftminute){
								x = 115+(minute.get(i)-leftminute)*550/(10*24*60);
							}
					}
				}
				else if(leftmonth==12&&startmonth==1){
					if(year.get(i)==startyear&&month.get(i)==startmonth){
						if(day.get(i)<startday){
							   x = 665-((startday-day.get(i)-1)*24*60+starthour*60+startminute+(24-hour.get(i)-1)*60+60-minute.get(i))*550/(10*24*60);
							}
							else if(day.get(i)==startday&&hour.get(i)<starthour){
							  x = 665-((starthour-hour.get(i)-1)*60+60-minute.get(i)+startminute)*550/(10*24*60);
							}
							else if(day.get(i)==startday&&hour.get(i)==starthour&&minute.get(i)<=startminute){
								x = 665 - (startminute-minute.get(i))*550/(10*24*60);
							}
							
					}
					else if(year.get(i)==leftyear&&month.get(i)==leftmonth){
						if(day.get(i)>leftday){
							   x = 115+((day.get(i)-leftday-1)*24*60+hour.get(i)*60+minute.get(i)+(24-lefthour-1)*60+60-leftminute)*550/(10*24*60);
							}
						
							else if(day.get(i)==leftday&&hour.get(i)>lefthour){
								x = 115+((hour.get(i)-lefthour-1)*60+60-leftminute+minute.get(i))*550/(10*24*60);
							}
							else if(day.get(i)==leftday&&hour.get(i)==lefthour&&minute.get(i)>leftminute){
								x = 115+(minute.get(i)-leftminute)*550/(10*24*60);
							}
					}
				}
				
				if(x>=0){
					keycount.add(pointcount, i);
					keydiya.add(pointcount,diya.get(i));
					keygaoya.add(pointcount,gaoya.get(i));
					keyx.add(pointcount, x);
					keyy.add(pointcount, 254-(diya.get(i)-40)*240/150);
					keyy_gao.add(pointcount, 254-(gaoya.get(i)-40)*240/150);
					if(pointcount>0){
						canvas.drawLine(prevx, prevy, x, 254-(diya.get(i)-40)*240/150, mPaint);
						canvas.drawLine(prevx, prevy_gao, x, 254-(gaoya.get(i)-40)*240/150, gaoyaPaint);
					}
						
				
					prevx = x;
					prevy = 254-(diya.get(i)-40)*240/150;
					prevy_gao = 254-(gaoya.get(i)-40)*240/150;
					pointcount++;
					keynum = pointcount;
				}	
			
	}
			for(i=0;i<pointcount;i++){
				if(presstag == 1){
					if(keycount.get(i)==index){
						if(keydiya.get(i)>=60&&keydiya.get(i)<=90)
						    canvas.drawBitmap(greenpoint_over, (float) (keyx.get(i)-9.5), (float) (keyy.get(i)-9.5), mPaint);
						else
							canvas.drawBitmap(redpoint_over, (float) (keyx.get(i)-9.5), (float) (keyy.get(i)-9.5), mPaint);
						if(keygaoya.get(i)>=90&&keygaoya.get(i)<=140)
						    canvas.drawBitmap(greenpoint_over, (float) (keyx.get(i)-9.5), (float) (keyy_gao.get(i)-9.5), mPaint);
						else
							canvas.drawBitmap(redpoint_over, (float) (keyx.get(i)-9.5), (float) (keyy_gao.get(i)-9.5), mPaint);
					}
					    
					else{
						if(keydiya.get(i)>=60&&keydiya.get(i)<=90)
						    canvas.drawBitmap(greenpoint, (float) (keyx.get(i)-9.5), (float) (keyy.get(i)-9.5), mPaint);
						else
							canvas.drawBitmap(redpoint, (float) (keyx.get(i)-9.5), (float) (keyy.get(i)-9.5), mPaint);
						if(keygaoya.get(i)>=90&&keygaoya.get(i)<=140)
						    canvas.drawBitmap(greenpoint, (float) (keyx.get(i)-9.5), (float) (keyy_gao.get(i)-9.5), mPaint);
						else
							canvas.drawBitmap(redpoint, (float) (keyx.get(i)-9.5), (float) (keyy_gao.get(i)-9.5), mPaint);
				
					}
						
				}
					
				else{
					if(keydiya.get(i)>=60&&keydiya.get(i)<=90)
					    canvas.drawBitmap(greenpoint, (float) (keyx.get(i)-9.5), (float) (keyy.get(i)-9.5), mPaint);
					else
						canvas.drawBitmap(redpoint, (float) (keyx.get(i)-9.5), (float) (keyy.get(i)-9.5), mPaint);
					if(keygaoya.get(i)>=90&&keygaoya.get(i)<=140)
					    canvas.drawBitmap(greenpoint, (float) (keyx.get(i)-9.5), (float) (keyy_gao.get(i)-9.5), mPaint);
					else
						canvas.drawBitmap(redpoint, (float) (keyx.get(i)-9.5), (float) (keyy_gao.get(i)-9.5), mPaint);
					
				}
				   
			}
			
	}
	}

    public boolean onTouchEvent(MotionEvent event) {
    	int x = (int) event.getX();
    	int y = (int) event.getY();
         int offset = 0;
         int offminute = 0;
         int offhour = 0;
         int offday = 0;


         switch (event.getAction()) {
             case MotionEvent.ACTION_DOWN:
            	
            	 
            	 startX = currX = x;
            	 if(tag==3){
            		 
            		 if(keynum!=0){
            			 for(i=0;i<keynum;i++){
            				
            				 if(x>=keyx.get(i)-12&&x<=keyx.get(i)+12&&(y<=keyy.get(i)+12&&y>=keyy.get(i)-12||y<=keyy_gao.get(i)+12&&y>=keyy_gao.get(i)-12)){
            					index = keycount.get(i);
            					
            					 presstag = 1;
            					
                                 new AlertDialog.Builder(getContext())
                                 .setMessage(Integer.toString(Math.round(year.get(index)))+"."+Integer.toString(Math.round( month.get(index)))+"."+Integer.toString(Math.round( day.get(index)))+"   "+Integer.toString(Math.round(hour.get(index)))+":"+Integer.toString(Math.round(minute.get(index)))+"   "+"低压："+Integer.toString(Math.round(diya.get(index)))+"   "+"高压："+Integer.toString(Math.round(gaoya.get(index))))
                                 .setNeutralButton("删除", new DialogInterface.OnClickListener()
                                		 {
                                			 public void onClick(DialogInterface dialog, int whichButton)
                                			 {
                                				 new AlertDialog.Builder(getContext())
                                        		 .setMessage("确定删除吗？")
                                        		 .setNeutralButton("确定", new DialogInterface.OnClickListener()
                                        		 {
                                        			 public void onClick(DialogInterface dialog, int whichButton)
                                        			 {
                                        				 if(diya.size()>0){
                                        					
                                        					 if(user_tag==1){
                                    							 year1.remove(index);
                                        						 month1.remove(index);
                                        						 day1.remove(index);
                                        						 hour1.remove(index);
                                        						 minute1.remove(index);
                                        						 diya1.remove(index);
                                        						 gaoya1.remove(index);
                                        						 id1.remove(index);
                                    						 }
                                    						 else if(user_tag==2){
                                    							 year2.remove(index);
                                        						 month2.remove(index);
                                        						 day2.remove(index);
                                        						 hour2.remove(index);
                                        						 minute2.remove(index);
                                        						 diya2.remove(index);
                                        						 gaoya2.remove(index);
                                        						 id2.remove(index);
                                    						 }
                                    						 else if(user_tag==3){
                                    							 year3.remove(index);
                                        						 month3.remove(index);
                                        						 day3.remove(index);
                                        						 hour3.remove(index);
                                        						 minute3.remove(index);
                                        						 diya3.remove(index);
                                        						 gaoya3.remove(index);
                                        						 id3.remove(index);
                                    						 }
                                    						 else if(user_tag==4){
                                    							 year4.remove(index);
                                        						 month4.remove(index);
                                        						 day4.remove(index);
                                        						 hour4.remove(index);
                                        						 minute4.remove(index);
                                        						 diya4.remove(index);
                                        						 gaoya4.remove(index);
                                        						 id4.remove(index);
                                    						 }
                                    						 else if(user_tag==5){
                                    							 year5.remove(index);
                                        						 month5.remove(index);
                                        						 day5.remove(index);
                                        						 hour5.remove(index);
                                        						 minute5.remove(index);
                                        						 diya5.remove(index);
                                        						 gaoya5.remove(index);
                                        						 id5.remove(index);
                                    						 }
                                        						 db = helper.getWritableDatabase();
                                        						 db.delete("xueya", "_id="+Integer.toString(id.get(index)), null);
                                        						 db.close();
                                        					 }
                                        			
                                        				 invalidate();
                                        				 
                                        					 
                                        			 }
                                        		 })
                                        		 .setNegativeButton("取消", new DialogInterface.OnClickListener()
                                        		 {
                                        			 public void onClick(DialogInterface d,int which)
                                        			 {
                                        				 d.dismiss();
                                        			 }
                                        		 })
                                        		 .show();
                                			 }
                                		 })
                                
                                  .setNegativeButton("取消", new DialogInterface.OnClickListener()
                                		 {
                                			 public void onClick(DialogInterface d,int which)
                                			 {
                                				 d.dismiss();
                                			 }
                                		 })
                                 .show();
            					
            					
            				 }
            			 }
            		 }
            	 }
            	
                 invalidate();

                 break;
             
             case MotionEvent.ACTION_MOVE:
            	 currX = x;
                 offset = startX - currX;
           
                 //从左向右拖拽，时间前移
                
                	 
               if (offset < 0){
                	
                	 //计算前移后开始时间分钟数
                	
                	 if(tag ==2)
                      	offminute=(int)(-offset)*(30*24*60)/570;
                     	 else if(tag==3)
                     		 offminute=(int)(-offset)*(10*24*60)/570;
                	
                	 
                	 if((startminute-offminute)<=0){
                		if(startminute <=offminute%60)
                		 { offhour = offminute/60+1;
                		     startminute = 60 - (offminute%60-startminute);}
                		 else {offhour = offminute/60;
                		 startminute = startminute - offminute%60;}
                	 }
                	 else startminute = startminute-offminute;
                	
                	 //计算前移后开始时间小时数
                	 if((starthour-offhour)<=0){
                		 if(starthour <= offhour%24){
                			 offday = offhour/24+1;
                			 starthour = 24 - (offhour%24-starthour);
              		 }
                		 else{
                			 offday = offhour/24;
                			 starthour = starthour - offhour%24;
                		 }
                		 
                	 }
               	 else starthour = starthour - offhour;
                	 
                	 //计算前移后开始日期（年月日）
                	 if((startday - offday)<=0){
                		 if(startmonth!=3){
                			 if(startmonth==1){
                				 startday = 31-(offday-startday);
                				 startmonth = 12;
                				 startyear = startyear - 1;
                			 }
                			 else if(startmonth==2||startmonth==4||startmonth==6||startmonth==8||startmonth==9||startmonth==11){
                				 startday = 31-(offday-startday);
                				 startmonth = startmonth-1;
                			 }
                			
                			 else if(startmonth==5||startmonth==7||startmonth==10||startmonth==12){
                				 startday = 30-(offday-startday);
                				 startmonth = startmonth-1;
                			 }
               		
                 	 }
                		 else{
                			 //润年
                			 if(startyear%400==0){
                				 if((offday-startday)>=29){
                					 startmonth = startmonth-2;
                					 startday = 31-(offday-startday-29);

                				 }
                				 else{
                					 startmonth = startmonth-1;
                					 startday = 29-(offday-startday);

                				 }
                			 }
                			 else if(startyear%4==0&&startyear%100!=0){
                				 if((offday-startday)>=29){
                					 startmonth = startmonth-2;
                					 startday = 31-(offday-startday-29);

                				 }
                				 else{
                					 startmonth = startmonth-1;
                					 startday = 29-(offday-startday);

                				 }
                			 }
                			 //非润年
                			 else{
                				 if((offday-startday)>=28){
                					 startmonth = startmonth-2;
                					 startday = 31-(offday-startday-28);

                				 }
                				 else{
                					 startmonth = startmonth-1;
                					 startday = 28-(offday-startday);

                				 }
                			 }
                		 }
                	 }
                	 
                	 else if((startday - offday)>0){
                		 startday = startday - offday;
                	 }
                
                //加上边界
                if(tag==2){	 
                	if(mMonth==12){//前后31
                		leftsideyear=mYear;
                		leftsidemonth=1;
                		leftsideday=mDay;
                	}
                	
                	else if(mMonth==3||mMonth==5||mMonth==8||mMonth==10){//前31后30
                		if(mDay==31){
                			leftsideyear=mYear-1;
                    		leftsidemonth=mMonth+2;
                    		leftsideday=1;
                		}
                		else{
                			leftsideyear=mYear-1;
                    		leftsidemonth=mMonth+1;
                    		leftsideday=mDay;
                		}
                			
                	}
                	else if(mMonth==4||mMonth==6||mMonth==9||mMonth==11){
                		leftsideyear=mYear-1;
                		leftsidemonth=mMonth+1;
                		leftsideday=mDay+1;
                	}
                	
                	else if(mMonth==7){
                		leftsideyear=mYear-1;
                		leftsidemonth=8;
                		leftsideday=mDay;
                	}
    
                	else if(mMonth==2){
                		if((mYear-1)%400==0){
                			leftsideyear=mYear-1;
                    		leftsidemonth=3;
                    		leftsideday=mDay+2;
                		}
                		else if((mYear-1)%4==0&&(mYear-1)%100!=0){
                			leftsideyear=mYear-1;
                    		leftsidemonth=3;
                    		leftsideday=mDay+2;
                		}
                		else{
                			leftsideyear=mYear-1;
                    		leftsidemonth=3;
                    		leftsideday=mDay+3;
                		}
                	}
                	else if(mMonth==1){
                		if((mYear-1)%400==0){
                			if(mDay!=30&&mDay!=31){
                  			   leftsideyear=mYear-1;
                      		   leftsidemonth=2;
                      		   leftsideday=mDay;
                  			}
                  			else {
                  				 leftsideyear=mYear-1;
                       		     leftsidemonth=3;
                       		     leftsideday=2+mDay-31;
                  			}
                		}
                		else if((mYear-1)%4==0&&(mYear-1)%100!=0){
                			if(mDay!=30&&mDay!=31){
                 			   leftsideyear=mYear-1;
                     		   leftsidemonth=2;
                     		   leftsideday=mDay;
                 			}
                 			else {
                 				 leftsideyear=mYear-1;
                      		     leftsidemonth=3;
                      		     leftsideday=2+mDay-31;
                 			}
                 			
                		}
                		else{
                			if(mDay!=30&&mDay!=31&&mDay!=29){
                				leftsideyear=mYear-1;
                        		leftsidemonth=2;
                        		leftsideday=mDay;
                			}
                			else{
                				leftsideyear=mYear-1;
                        		leftsidemonth=3;
                        		leftsideday=3+mDay-31;
                			}
                			
                		}
                		
                	}
                }
                else if(tag==3){//一页10天，从一年前的今天的第二天开始
                	if(mMonth==12){
                		if(mDay+11>31){
                		    leftsideyear=mYear;
                		    leftsidemonth=1;
                		    leftsideday=mDay+11-31;
                		}
                		else{
                			leftsideyear=mYear-1;
                		    leftsidemonth=mMonth;
                		    leftsideday=mDay+11;
                		}
                	}
                	else if(mMonth==1||mMonth==3||mMonth==5||mMonth==7||mMonth==8||mMonth==10){
                		if(mDay+11>31){
                		    leftsideyear=mYear-1;
                		    leftsidemonth=mMonth+1;
                		    leftsideday=mDay+11-31;
                		}
                		else{
                			leftsideyear=mYear-1;
                		    leftsidemonth=mMonth;
                		    leftsideday=mDay+11;
                		}
                	}
                	else if(mMonth==4||mMonth==6||mMonth==9||mMonth==11){
                		if(mDay+11>30){
                		    leftsideyear=mYear-1;
                		    leftsidemonth=mMonth+1;
                		    leftsideday=mDay+11-30;
                		}
                		else{
                			leftsideyear=mYear-1;
                		    leftsidemonth=mMonth;
                		    leftsideday=mDay+11;
                		}
                	}
                	else if(mMonth==2){
                		if((mYear-1)%400==0){
                			if(mDay+11>29){
                    		    leftsideyear=mYear-1;
                    		    leftsidemonth=mMonth+1;
                    		    leftsideday=mDay+11-29;
                    		}
                    		else{
                    			leftsideyear=mYear-1;
                    		    leftsidemonth=mMonth;
                    		    leftsideday=mDay+11;
                    		}
                		}
                		else if((mYear-1)%4==0&&(mYear-1)%100!=0){
                			if(mDay+11>29){
                    		    leftsideyear=mYear-1;
                    		    leftsidemonth=mMonth+1;
                    		    leftsideday=mDay+11-29;
                    		}
                    		else{
                    			leftsideyear=mYear-1;
                    		    leftsidemonth=mMonth;
                    		    leftsideday=mDay+11;
                    		}
                		}
                		else{
                			if(mDay+11>28){
                    		    leftsideyear=mYear-1;
                    		    leftsidemonth=mMonth+1;
                    		    leftsideday=mDay+11-28;
                    		}
                    		else{
                    			leftsideyear=mYear-1;
                    		    leftsidemonth=mMonth;
                    		    leftsideday=mDay+11;
                    		}
                		}
                	}
                }
                	if(startyear<leftsideyear){
                		startyear=leftsideyear;
                		startmonth=leftsidemonth;
                		startday=leftsideday;
                		starthour=mHour;
                		startminute=mMinute;
                	}
                	else if(startyear==leftsideyear&&startmonth<leftsidemonth){
                		startyear=leftsideyear;
                		startmonth=leftsidemonth;
                		startday=leftsideday;
                		starthour=mHour;
                		startminute=mMinute;
                	}
                	else if(startyear==leftsideyear&&startmonth==leftsidemonth&&startday<leftsideday){
                		startyear=leftsideyear;
                		startmonth=leftsidemonth;
                		startday=leftsideday;
                		starthour=mHour;
                		startminute=mMinute;
                	}
                	
                	
                	
               }
                 
                 //从右向左拖拽，时间后移
                 else if(offset >0){
                 	
                 	//计算后移后开始时间分钟数
                	 if(tag ==2)
                 	offminute=(int)(offset)*(30*24*60)/570;
                	 else if(tag==3)
                		 offminute=(int)(offset)*(10*24*60)/570;
                 	if((startminute+offminute)>=60){
                 		if(startminute + offminute%60>=60)
                 		 { offhour = offminute/60+1;
                 		     startminute = offminute%60+startminute-60;}
                 		 else {offhour = offminute/60;
                 		 startminute = startminute + offminute%60;}
                 	}
                 	else startminute = startminute+offminute;
                      
                      //计算后移后开始时间小时数
                 	if((starthour+offhour)>=24){
                 		if((starthour + offhour%24)>=24){
                 			 offday = offhour/24+1;
                 			 starthour = offhour%24+starthour-24;
               		    }
                 		else{
                 			 offday = offhour/24;
                 			 starthour = starthour + offhour%24;
                 		}
                 		 
                 	}
                	    else starthour = starthour + offhour;
                     
                     //计算后移后开始日期（年月日）
                     if(startmonth==3||startmonth==5||startmonth==7||startmonth==8||startmonth==10){
                         if(startday+offday>31){
                             startday = startday+offday-31;
                             startmonth = startmonth+1;
                         }
                         else
                             startday = startday+offday;
                     }
                     else if(startmonth==12){
                         if(startday+offday>31){
                             startday = startday+offday-31;
                             startmonth = 1;
                             startyear = startyear +1;
                         }
                         else
                             startday = startday+offday;
                     }
                     else if(startmonth==4||startmonth==6||startmonth==9||startmonth==11){
                         if(startday+offday>30){
                             startday = startday+offday-30;
                             startmonth = startmonth+1;
                         }
                         else
                             startday = startday+offday;
                     }
                     else if(startmonth==1){
                         if(startyear%400==0){
                             if(startday+offday>60){
                                 startmonth = 3;
                                 startday = startday+offday-60;
                             }
                             else if((startday+offday)>31&&(startday+offday)<=60){
                                 startmonth = 2;
                                 startday = startday+offday-31;
                             }
                             else if((startday+offday)<=31){
                            	 startday = startday + offday;
                             }
                         }
                         else if(startyear%4==0&&startyear%100!=0){
                             if(startday+offday>60){
                                 startmonth = 3;
                                 startday = startday+offday-60;
                             }
                             else if((startday+offday)>31&&(startday+offday)<=60){
                                 startmonth = 2;
                                 startday = startday+offday-31;
                             }
                             else if((startday+offday)<=31){
                            	 startday = startday + offday;
                             }
                         }
                         else{
                             if(startday+offday>59){
                                 startmonth = 3;
                                 startday = startday+offday-59;
                             }
                             else if((startday+offday)>31&&(startday+offday)<=59){
                                 startmonth = 2;
                                 startday = startday+offday-31;
                             }
                             else if((startday+offday)<=31){
                            	 startday = startday + offday;
                             }
                         }
                     }
                     else if(startmonth==2){
                         if(startyear%400==0){
                             if(startday+offday>29){
                                 startday = startday+offday-29;
                                 startmonth = startmonth+1;
                             }
                             else
                                 startday = startday+offday;
                         }
                         
                         else if(startyear%4==0&&startyear%100!=0){
                             if(startday+offday>29){
                                 startday = startday+offday-29;
                                 startmonth = startmonth+1;
                             }
                             else
                                 startday = startday+offday;
                             
                         }
                         else{
                             if(startday+offday>28){
                                 startday = startday+offday-28;
                                 startmonth = startmonth+1;
                             }
                             else
                                 startday = startday+offday;
                             
                         }
                     }    
                    
                 //加右移边界
                 if(startyear>mYear){
                     startyear = mYear;
                     startmonth = mMonth;
                     startday = mDay;
                     starthour = mHour;
                     startminute = mMinute;
                 }
                 else if(startyear==mYear&&startmonth>mMonth){
                     startyear = mYear;
                     startmonth = mMonth;
                     startday = mDay;
                     starthour = mHour;
                     startminute = mMinute;
                 }
                 else if(startyear==mYear&&startmonth==mMonth&&startday>mDay){
                     startyear = mYear;
                     startmonth = mMonth;
                     startday = mDay;
                     starthour = mHour;
                     startminute = mMinute;
                 }
                 else if(startyear==mYear&&startmonth==mMonth&&startday==mDay&&starthour>mHour){
                     startyear = mYear;
                     startmonth = mMonth;
                     startday = mDay;
                     starthour = mHour;
                     startminute = mMinute;
                 }
                 else if(startyear==mYear&&startmonth==mMonth&&startday==mDay&&starthour==mHour&&startminute>mMinute){
                     startyear = mYear;
                     startmonth = mMonth;
                     startday = mDay;
                     starthour = mHour;
                     startminute = mMinute;
                 }
               
                 }
                  	
                 	
                 
                 invalidate();
                 break;
             case MotionEvent.ACTION_UP:
            	 presstag = 0;
            	 invalidate();
                 break;
         }

         return true;
   
   }
}