package com.health.util;

import java.lang.reflect.Array;

import android.util.Log;

public class MyArrays<T> {
	private static final String TAG = "MyArrays";

	private MyArrays() {// 工具类，外部不能实例化
	}

	/**
	 * 将data的[start,end)左闭右区间开部分复制返回
	 * 
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	public static <T> T[] copyOfRange(T[] data, int start, int end,
			Class<T> type) {
		@SuppressWarnings("unchecked")
		T[] buffer = (T[]) Array.newInstance(type, end - start);
		for (int i = start; i < end; i++) {
			buffer[i] = data[i - start];
		}
		return buffer;
	}

	/**
	 * 将data的[start,end)左闭右区间开部分复制返回
	 * 
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	public static byte[] copyOfRange(byte[] data, int start, int end) {
		// Log.i(TAG, "start:" + start + ", end:" +
		// end);
		byte[] buffer = new byte[end - start];
		for (int i = start; i < end; i++) {
			buffer[i - start] = data[i];
		}
		return buffer;
	}

	/**
	 * 将data的[0,length)部分复制返回，如果length大于data长度，
	 * 则后面的部分置为0
	 * 
	 * @param data
	 * @param length
	 * @return
	 */
	public static byte[] copyOf(byte[] data, int length) {
		byte[] buffer = new byte[length];
		if (length < data.length)// 返回[0,length)部分
			return copyOfRange(data, 0, length);
		int i;
		for (i = 0; i < data.length; i++)
			buffer[i] = data[i];
		for (; i < length; i++)
			// 超过的部分设置为0
			buffer[i] = 0;
		return buffer;

	}

}
