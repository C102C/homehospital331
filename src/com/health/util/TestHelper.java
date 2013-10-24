package com.health.util;

import java.util.Arrays;

import android.test.AndroidTestCase;

public class TestHelper extends AndroidTestCase {
	public static void testCopy() {
		byte[] data = { 1, 2, 3, 4, 5, 6 };
		byte[] d = MyArrays.copyOfRange(data, 1, 3);
		System.out.println(Arrays.toString(d));
	}
}
