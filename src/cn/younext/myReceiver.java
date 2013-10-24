package cn.younext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class myReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
        //String action="android.intent.action.MAIN";   
        //String category="android.intent.category.LAUNCHER";   
       //Intent myi=new Intent(ctx,CustomDialog.class);   
        //myi.setAction(action);   
        //myi.addCategory(category);   
        //myi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        //ctx.startActivity(myi);   
        //start service   
		Log.d("BootReceiver", "system boot completed");    
	       if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
	            Intent bootActivityIntent=new Intent(context,main.class);
	            bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            context.startActivity(bootActivityIntent);
	       }

		
	}

}
