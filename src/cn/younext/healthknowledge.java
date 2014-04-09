package cn.younext;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class healthknowledge extends Activity{
	Button homeBtn;
	Button healthknowledge_canshuBtn;
	Button healthknowledge_richangBtn;
	Button healthknowledge_jinjiBtn;
	TextView txt;
	OnClickListener btnClick;
	int userid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthknowledge);
        homeBtn = (Button)findViewById(R.id.healthknowledge_homeBtn);
        healthknowledge_canshuBtn=(Button)findViewById(R.id.healthknowledge_canshuBtn);
        healthknowledge_richangBtn=(Button)findViewById(R.id.healthknowledge_richangBtn);
        healthknowledge_jinjiBtn=(Button)findViewById(R.id.healthknowledge_jinjiBtn);
        btnClick= new OnClickListener(){

    		@Override
    		public void onClick(View v) {

        		if(v==homeBtn){
        		healthknowledge.this.finish();	
        		}
        		else if(v==healthknowledge_canshuBtn){
        		txt=(TextView)findViewById(R.id.healthknowledge_text);
        		txt.setText("参数介绍\n" +
        				"Boys and girls,don't give up babyyou know? babauh uh dreams come true yeuh uh uh 相信自己为了实现梦想 飞翔吧uh uh uh 敞开心怀");
        		txt.showContextMenu();
        		}
        		else if(v==healthknowledge_richangBtn){
            		txt=(TextView)findViewById(R.id.healthknowledge_text);
            		txt.setText("日常保健"); 		
        		}
        		else if(v==healthknowledge_jinjiBtn){
            		txt=(TextView)findViewById(R.id.healthknowledge_text);
            		txt.setText("紧急救助");
        		}
        		else{  			
        		}	
    		}
        };
        homeBtn.setOnClickListener(btnClick);
        healthknowledge_canshuBtn.setOnClickListener(btnClick);
        healthknowledge_richangBtn.setOnClickListener(btnClick);
        healthknowledge_jinjiBtn.setOnClickListener(btnClick);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_MENU) {
        	   
        	healthknowledge.this.finish();
		

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
