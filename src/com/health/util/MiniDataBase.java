package com.health.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 保存数据到缓存SharedPreferences
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2013-10-25 上午10:22:42
 */
public class MiniDataBase {
	public static final String PC300 = "PC300";
	public static final String BENECHECK = "BeneCheck";//百捷血糖设备
	public static final String GMPUA = "GmpUa";//尿液分析仪

	Context context;
	private SharedPreferences sharedPrefrences;
	private Editor editor;

	public MiniDataBase(Context context) {
		this.context = context;
		sharedPrefrences = context.getSharedPreferences("padhealth",
				Context.MODE_PRIVATE);
		editor = sharedPrefrences.edit();
	}

	/**
	 * 保存设备地址
	 * 
	 * @param device
	 * @param address
	 */
	public void saveDeviceAddress(String device, String address) {
		editor.putString(device, address);
		editor.commit();// 提交
	}

	/**
	 * 获取设备的地址
	 * 
	 * @param device
	 * @return
	 */
	public String getDeviceAddress(String device) {
		return sharedPrefrences.getString(device, null);
	}
}
