package com.health.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.test.AndroidTestCase;
import android.util.Log;

import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.web.WebService;

public class TestHelper extends AndroidTestCase {
	private static final String TAG = "TestHelper";

	public static void testCopy() {
		byte[] data = { 1, 2, 3, 4, 5, 6 };
		byte[] d = MyArrays.copyOfRange(data, 1, 3);
		System.out.println(Arrays.toString(d));
	}

	public void testInsertDb() {
		DatabaseService dbService = new DatabaseService(getContext());
		Tables tables = new Tables();
		Map<String, String> ValueMap = new HashMap<String, String>();
		ValueMap.put(Tables.TIME, "2013-12-12 08:59:32");
		ValueMap.put(Tables.DEVICENAME, "pc300");
		ValueMap.put(Tables.DEVICEMAC, "AB:CD:14:34:56:78");
		ValueMap.put(Tables.CARDNO, "123456");
		ValueMap.put(Tables.PULSE, "98");
		dbService.insert(tables.pulseTable(), ValueMap);
	}

	public void testSelectDb() {
		DatabaseService dbService = new DatabaseService(getContext());
		Tables tables = new Tables();
		List<Map<String, String>> result = dbService.select("123456",
				tables.pulseTable());
		Log.i(TAG, result.toString());
	}

	public void testWeb() throws Exception {
		String para = "{\"cardNo\":\"utektest\",\"password\":\"utek 123\"}";
		String url = "http://pat.uuhealth.com.cn/pat/index/8031?" + para;
		// Map<String,String > map = new
		// HashMap<String,String>();
		// map.put("cardNo", "utektest00");
		// map.put("password", "utek123");
		// para = WebService.getStringBuilder(map);

		// / url =
		// "http://pat.uuhealth.com.cn/pat/index/8006?{%22cardNo%22:%22utektest%22,%22deviceMac%22:%2200:23:01:08:0A:A7%22,%22ua%22:%2263%22,%22time%22:%222013-8-7%2015:51:00%22,%22deviceName%22:%22BeneCheck%22}";
		// url =
		// "http://pat.uuhealth.com.cn/pat/index/8030?{\"name\":\"ÀîÌìÍõ\",\"sex\":\"ÄÐ\",\"birthday\":\"1978/05/01\",\"orgid\":\"21\",\"cardNo\":\"330772197805283754\",\"password\":\"abcd4321\",\"mobile\":\"13812344321\",\"email\":\"123456789@qq.com\"}";
		// url =
		// "http://pat.uuhealth.com.cn/pat/index/8031?";
		// url =
		// "http://pat.uuhealth.com.cn/pat/index/8003?{\"cardNo\":\"330772197805283758\",\"deviceMac\":\"94:21:97:60:08:9B\",\"temp\":\"30.69\",\"checkTime\":\"2013-11-29 19:01:42\",\"deviceName\":\"PC_300SNT\"}";
		// url =
		// "http://pat.uuhealth.com.cn/pat/index/8003?{cardNo:330772197805283758,deviceMac:94:21:97:60:08:9B,temp:30.69,checkTime:2013-11-29 19:01:42,deviceName:PC_300SNT}";
		// url =
		// "http://pat.uuhealth.com.cn/pat/index/8011?{\"cardNo\":330772197805283758,\"startTime\":\"2013-12-03 00:00:00\"}";
		url = "http://pat.uuhealth.com.cn/pat/index/8003?{%22cardNo%22:%22330772197805283758%22,%22deviceMac%22:%2294:21:97:60:08:9B%22,%22temp%22:%2230.69%22,%22checkTime%22:%222013-11-29%2019:01:42%22,%22deviceName%22:%22PC_300SNT%22}";
		JSONObject json = WebService.httpConenction(url);
		System.out.println(json);
	}
}
