package cn.younext;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class teleference_xiugaidialog extends Activity{
	Button save;
	Button cancel;
	EditText name;
	EditText phoneNumber;
	String peopleName;
	String peopleNumber;
	OnClickListener btnClick;
	
	DatabaseHelper helper;
	SQLiteDatabase db;
	Cursor c;
	String _id;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setContentView(R.layout.teleference_xiugaidialog);
        
        name=(EditText)findViewById(R.id.personnameedit);
        phoneNumber=(EditText)findViewById(R.id.personnumberedit);
        save=(Button)findViewById(R.id.saveBtn);
        cancel=(Button)findViewById(R.id.cancelBtn);
       
        Bundle extra=getIntent().getExtras();
        if(extra!=null)
        {
        	_id=extra.getString("_id");
        }
        helper = new DatabaseHelper(teleference_xiugaidialog.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
        db = helper.getWritableDatabase();
        c=db.query(DatabaseHelper.PHONETABLE, null,DatabaseHelper.ID+"="+_id, null, null,null,"_id asc",null);
        c.moveToNext();
        name.setText(c.getString(1));
        phoneNumber.setText(c.getString(2));
        
        
        
        
        
        btnClick= new OnClickListener(){

    		@Override
    		public void onClick(View v) {
    			if(v==save){
    				peopleName=name.getText().toString();
    				peopleNumber=phoneNumber.getText().toString();
    				if("".equals(name.getText().toString().trim())||"".equals(phoneNumber.getText().toString().trim())){
	                    Toast.makeText(teleference_xiugaidialog.this, "您输入的姓名或电话为空，请重新输入", Toast.LENGTH_LONG).show();
    						
    				}
    				else{
    					Log.v("name", peopleName);
    					Log.v("number", peopleNumber);
    					helper = new DatabaseHelper(teleference_xiugaidialog.this, DatabaseHelper.DATABASE_NAME, null,DatabaseHelper.Version);     
            	        db = helper.getWritableDatabase();
    					ContentValues values = new ContentValues();
    					values.put(DatabaseHelper.CONTECT_NAME, peopleName);//key为字段名，value为值
    					db.update(DatabaseHelper.PHONETABLE, values, "_id=?", new String[]{_id}); 
    					values.put(DatabaseHelper.CONTECT_PHONENUMBER, peopleNumber);//key为字段名，value为值
    					db.update(DatabaseHelper.PHONETABLE, values, "_id=?", new String[]{_id}); 
    					db.close();
    					teleference_xiugaidialog.this.setResult(RESULT_OK);
    					teleference_xiugaidialog.this.finish();
    					
    				}
    				
    				
    	    	}
    			else if(v==cancel){
    				teleference_xiugaidialog.this.finish();
    				
    			}
    			else{
    				
    			}
    			
    		}
        };
        save.setOnClickListener(btnClick);
        cancel.setOnClickListener(btnClick);
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        // TODO Auto-generated method stub
	        if (keyCode == KeyEvent.KEYCODE_MENU) {
	        	   
	        	teleference_xiugaidialog.this.finish();
			

	            return true;

	        }
	        return super.onKeyDown(keyCode, event);
	    }

}
