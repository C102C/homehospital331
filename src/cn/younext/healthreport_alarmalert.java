package cn.younext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class healthreport_alarmalert extends Activity{

	Button closeBtn;
	OnClickListener btnClick;
	final String FILE_PATH="/sys/class/ledctrl/";
	final String FILE_NAME="ledctrl";
	File file;
	FileOutputStream out;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);       
	    setContentView(R.layout.healthreport_alarmalert);

	    file=new File(FILE_PATH,FILE_NAME);
	    try {
			out=new FileOutputStream(file);
			int i=1;
			out.write(i);
			out.close();
			
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
	    
	
		closeBtn=(Button)findViewById(R.id.alarmalert_closeBtn);

		
		btnClick= new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(v==closeBtn){
					try {
						out=new FileOutputStream(file);
						int i=0;
						out.write(i);
						out.close();
						
					} catch (FileNotFoundException e) {

					} catch (IOException e) {

					}

					healthreport_alarmalert.this.finish();
					
				}
				else{
					
				}
				
			}
			
		};
		closeBtn.setOnClickListener(btnClick);
		
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_MENU) {
        	   

			try {
				out=new FileOutputStream(file);
				int i=0;
				out.write(i);
				out.close();
				
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			}

			healthreport_alarmalert.this.finish();
			
		
		

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
