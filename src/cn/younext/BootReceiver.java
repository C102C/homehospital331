package cn.younext;

import com.health.MainUi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/***
 * 开机自启动广播监听器
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-3-13 上午9:52:06
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("BootReceiver", "system boot completed");
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent bootActivityIntent = new Intent(context, MainUi.class);
			bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(bootActivityIntent);
		}
	}

}
