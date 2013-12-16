package com.health.myhealth;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.health.database.Tables;
import com.health.util.TimeHelper;
import com.health.web.WebService;

public class DataService {

	private static Random random = new Random(47);

	/**
	 * 获取血压数据
	 * 
	 * @param timeIndex
	 * @param cardNo
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static double[][] getBpData(Map<String, String> paras)
			throws IOException {
		try {

			JSONObject result = WebService.query(WebService.PATH_QUERY_BP,
					paras);
			int statusCode = result.getInt(WebService.STATUS_CODE);
			if (statusCode == WebService.OK) {
				JSONArray datas = result.getJSONArray(WebService.DATA);
				double[][] bpData = new double[3][datas.length()];
				for (int i = 0; i < datas.length(); i++) {
					JSONObject data = datas.getJSONObject(i);
					String checkTime = data.getString(WebService.CHECKTIME);
					long time;
					try {
						time = TimeHelper.parseTime(checkTime);
					} catch (ParseException e) {
						e.printStackTrace();
						continue;// 如果时间解析错误就跳过这个数据
					}
					bpData[0][i] = time;
					bpData[1][i] = data.getInt(Tables.SBP);
					bpData[2][i] = data.getInt(Tables.DBP);
					
				}
				//排序
				sort(bpData);
				return bpData;
			} else
				return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

	/***
	 * 將数据按时间排序,不排序的数据加载会错误
	 * 
	 * @param bpData
	 */
	private static void sort(double[][] data) {
		int len = data[0].length;
		for (int i = 0; i < len; i++) {
			for (int j = i; j < len; j++) {
				if (data[0][i] > data[0][j]) {
					double temp = data[0][i];
					data[0][i] = data[0][j];
					data[0][j] = temp;
				}
			}
		}
	}

	/***
	 * 获取只有一个类型的数据,出血压脉率和尿液之外,其他都可以走这里
	 * 
	 * @param paras
	 * @param path
	 * @param token
	 * @return
	 * @throws IOException
	 */
	public static double[][] getSingleData(Map<String, String> paras,
			String path, String token) throws IOException {
		try {
			JSONObject result = WebService.query(path, paras);
			int statusCode = result.getInt(WebService.STATUS_CODE);
			if (statusCode == WebService.OK) {
				JSONArray datas = result.getJSONArray(WebService.DATA);
				double[][] record = new double[2][datas.length()];
				for (int i = 0; i < datas.length(); i++) {
					JSONObject data = datas.getJSONObject(i);
					String checkTime = data.getString(WebService.CHECKTIME);
					long time;
					try {
						time = TimeHelper.parseTime(checkTime);
					} catch (ParseException e) {
						e.printStackTrace();
						continue;// 如果时间解析错误就跳过这个数据
					}
					record[0][i] = time;
					record[1][i] = data.getInt(token);
					
				}
				sort(record);
				return record;
			} else
				return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
