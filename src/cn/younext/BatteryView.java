package cn.younext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



public class  BatteryView extends View{
	private Bitmap battery0;
	private Bitmap battery1;
	private Bitmap battery2;
	private Bitmap battery3;
	private Bitmap battery4;

	AnimationDrawable frameAnimation=null;
	Context mContext = null;
	Drawable mBitAnimation = null;

	public int tag=1;
	public int dianliang;
	public BatteryView(Context context, AttributeSet attr){
		super(context,attr);
		setFocusable(false);
	    setFocusableInTouchMode(false);
	    mContext = context;
	    frameAnimation = new AnimationDrawable();
	    battery0= BitmapFactory.decodeResource(getResources(), R.drawable.battery0);	
		battery1 = BitmapFactory.decodeResource(getResources(), R.drawable.battery1);
		battery2= BitmapFactory.decodeResource(getResources(), R.drawable.battery2);	
	    battery3 = BitmapFactory.decodeResource(getResources(), R.drawable.battery3);
	    battery4 = BitmapFactory.decodeResource(getResources(), R.drawable.battery4);
    
	}
	 
	@Override
    protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Paint mPaint = new Paint();
		Log.v("充电", "充电");
		  
		//未充电
		if(tag==1){
			Log.v("未充电", "未充电");
			frameAnimation.stop(); 
			if(dianliang<=10){
				this.setBackgroundResource(R.drawable.battery0); 
				
				
			}
			else if(dianliang>10&&dianliang<=35){
				this.setBackgroundResource(R.drawable.battery1); 
				
			}
			else if(dianliang>35&&dianliang<=60){
				this.setBackgroundResource(R.drawable.battery2); 
				
			}
			else if(dianliang>65&&dianliang<=90){
				this.setBackgroundResource(R.drawable.battery3); 
				
			}
			else if(dianliang>90){
				this.setBackgroundResource(R.drawable.battery4); 
				
			}
			
		}
		//充电
		else{
			
		      this.setBackgroundResource(R.drawable.rocket_thrust); 
		      frameAnimation = (AnimationDrawable)this.getBackground();
			 
			  frameAnimation.start();
		}
		
	}
	public boolean onTouchEvent(MotionEvent event) {
		  if (event.getAction() == MotionEvent.ACTION_DOWN) {
			  
		   
		    return true;
		  }
		
		  return super.onTouchEvent(event);
		}


	
}