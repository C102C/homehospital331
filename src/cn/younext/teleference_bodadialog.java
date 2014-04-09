package cn.younext;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class teleference_bodadialog extends Activity {
	Button guaduan;
	OnClickListener btnClick;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setContentView(R.layout.teleference_bodadialog);
        
        guaduan =(Button)findViewById(R.id.guaduanBtn);
        btnClick= new OnClickListener(){

    		@Override
    		public void onClick(View v) {
    			if(v==guaduan){
    				
    	    			
    	    	}
    			else{
    				
    			}
    			
    		}
        };
        guaduan.setOnClickListener(btnClick);
	}

}
