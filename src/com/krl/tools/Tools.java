package com.krl.tools;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.krl.data.NetWork.NetType;


public class Tools {
	public static byte[] shortToBytes(short n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n & 0xFF);
		b[1] = (byte) ((n >> 8) & 0xFF);
		return b;
	}

	public static short bytesToShort(byte[] b, int offset) {
		return (short) ((((b[offset + 1] + 0x100) % 0x100) << 8) + ((b[offset] + 0x100) % 0x100));
	}

	/***
	 * 将汉字转换成byte 数组
	 * 
	 * @param data
	 *            待转换数组
	 * @param count
	 *            转换的长度
	 * @return
	 */
	public static byte[] stringToBytes(String data, int count) {
		byte[] tempdata = new byte[count];
		byte[] temp;
		try {
			temp = data.getBytes("GBK");
			System.arraycopy(temp, 0, tempdata, 0, temp.length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempdata;
	}

	public static void stringToBytes(String data, byte[] buffer, int offset,
			int count) {
		try {
			byte[] temp = data.getBytes("GBK");
			System.arraycopy(temp, 0, buffer, offset,
					Math.min(temp.length, count));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * 将字符数组转换成字符串
	 * 
	 * @param data
	 *            待转换数组
	 * @return 转换之后的字符串
	 */
	public static String bytesToString(byte[] data, int offset, int size) {
		String temp = "";
		int count = 0;
		for (int i = 0; i < size; i++) {
			if (data[i + offset] != 0x00) {
				count++;
			} else {
				break;
			}
		}
		if (count == 0) {
			return temp;
		}
		byte[] tempdata = new byte[count];
		System.arraycopy(data, offset, tempdata, 0, count);
		try {
			temp = new String(tempdata, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return temp;
	}

	/***
	 * 将获取的字节长度转换成INT
	 * 
	 * @param b0
	 *            低1字节
	 * @param b1
	 *            低2字节
	 * @param b2高1字节
	 * @param b3高2字节
	 */
	public static int bytesToInt(byte[] buffer, int offset) {
		int length = 0;
		length = (buffer[offset + 3] + 0x100) % 0x100;
		length = length << 8;
		length += (buffer[offset + 2] + 0x100) % 0x100;
		length = length << 8;
		length += (buffer[offset + 1] + 0x100) % 0x100;
		length = length << 8;
		length += (buffer[offset] + 0x100) % 0x100;
		return length;
	}

	/***
	 * 4字节 int 转换成byte[];
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] intToBytes(int data) {
		byte[] tempbyte = new byte[4];
		tempbyte[0] = (byte) (data & 0x000000FF);
		tempbyte[1] = (byte) ((data >> 8) & 0x000000FF);
		tempbyte[2] = (byte) ((data >> 16) & 0x000000FF);
		tempbyte[3] = (byte) ((data >> 24) & 0x000000FF);
		return tempbyte;
	}

	public static byte[] intToBytes(int data, byte[] buffer, int offset) {
		byte[] tempbyte = buffer;
		tempbyte[offset] = (byte) (data & 0x000000FF);
		tempbyte[offset + 1] = (byte) ((data >> 8) & 0x000000FF);
		tempbyte[offset + 2] = (byte) ((data >> 16) & 0x000000FF);
		tempbyte[offset + 3] = (byte) ((data >> 24) & 0x000000FF);
		return tempbyte;
	}

	public static byte[] floatToBytes(float data) {
		return intToBytes(Float.floatToIntBits(data));
	}

	/***
	 * 检查网络连接状态
	 * 
	 * @return
	 */
	public static NetType cheackNetConnection(Context context) {
		ConnectivityManager ConManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取WIFI网络连接状态
		NetworkInfo.State wifi_state = ConManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState(); //
		if (wifi_state == NetworkInfo.State.CONNECTED) {
			return NetType.NET_WIFI;
		} else {
			// 判断3G网络连接状态
			NetworkInfo.State mobile_state = ConManager.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).getState();
			if (mobile_state == NetworkInfo.State.CONNECTED) {
				return NetType.NET_3G;
			} else {
				return NetType.NET_NONE;
			}
		}
	}

	/*****
	 * 获取当前时间
	 */
	public static String GetCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	public static String GetCurrentTime(String format) {
		SimpleDateFormat sdformat = new SimpleDateFormat(format);
		return sdformat.format(new Date());
	}
}
