package com.health.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.health.BaseActivity;
import com.health.bean.Group;
import com.health.bean.Session;
import com.health.bean.User;
import com.health.util.L;
import com.health.util.MyMD5;
import com.health.util.TimeHelper;

public class WebService {

	// private static final String DOMAIN =
	// "http://pat.uuhealth.com.cn/pat/index/";
	// private static final String DOMAIN =
	// "http://www.itonehealth.com/pat/index/";
	public static final String DOMAIN_V2 = "http://3.uuhealth.com.cn/UtekWebService/";

	private static final String DOMAIN = DOMAIN_V2;

	public static final String STATUS = "status";// 上传状态
	public static final String UPLOADED = "已上传";// 已经上传
	public static final String UNUPLOAD = "未上传";// 未上传
  
	//
	public static final String TIME = "checkTime";
	public static final String DEVICENAME = "deviceName";
	public static final String DEVICEMAC = "deviceMac";

	public static final String PATH = "path";
	public static final String PATH_BP = "8101";
	public static final String PATH_BO = "8102";
	public static final String PATH_TEMP = "8103";
	public static final String PATH_GLU = "8104";
	public static final String PATH_CHOL = "8105";
	public static final String PATH_UA = "8106";
	public static final String PATH_URINE = "8107";
	public static final String PATH_ECG = "8108";// 上传心电
	// public static final String PATH_EVG = "800";
	public static final String PATH_QUERY_BP = "8201";
	public static final String PATH_QUERY_PULSE = "8200";
	public static final String PATH_QUERY_BO = "8202";
	public static final String PATH_QUERY_TEMP = "8203";
	public static final String PATH_QUERY_GLU = "8204";
	public static final String PATH_QUERY_CHOL = "8205";
	public static final String PATH_QUERY_UA = "8206";
	public static final String PATH_QUERY_URINE = "8207";

	public static final String STATUS_CODE = "statusCode";
	public static final String STATUS_MSG = "statusMsg";
	public static final int OK = 0;// 成功
	public static final int ERROE = 1;
	public static final int NETERROE = 0x711588;

	public static final String PLAT_ID_KEY = "orgid";
	public static final String PLAT_ID_VALUE = "21";

	public static final String STARTTIME = "startTime";
	public static final String ENDTIME = "endTime";
	public static final String PAGECOUNT = "pageCount";
	public static final String PAGEINDEX = "pageIndex";
	public static final String CHECKTIME = "checkTime";
	public static final String TOKEN = "token";
	public static final String DATA = "data";
	public static final String PASS_WORD = "password";
	private static final String TAG = "WebService";
	// 新接口2014/3/18
	public static final String PATH_USER_LIST = "8340";// 机构用户列表
	public static final String PATH_GROUP_LOGIN = "8331";

	public static final String GUID_KEY = "guid";
	public static final String GUID_VALUE = "3b8afc6f-7847-4a3a-ace7-77f3931e9b17";

	// 机构用户
	public static final String CARDNO = "cardNo";
	public static final String USERID = "userID";
	public static final String BIRTHDAY = "birthday";
	public static final String SEX = "sex";
	public static final String IMAGEURL = "imageUrl";
	public static final String EMAIL = "email";
	public static final String NICKNAME = "nickName";
	public static final String CUSTOMERGUID = "customerGuid";
	public static final String NAME = "name";
	public static final String USERGUID = "userGuid";
	public static final String MOBILE = "mobile";
	public static final String PARAM = "param"; // 机构用户的查找参数
	// 随访记录
	public static final String CONTENT = "content";
	public static final String PATH_UPLOAD_VISIT_CONTENT = "8343";
	public static final String PATH_GET_VISIT_CONTENT = "8344";
	// 白细胞
	public static final String PATH_UPLOAD_WBC = "8111";
	public static final String PATH_QUERY_WBC = "8211";
	public static final String DOCTOR_NAME = "userName";
	public static final String WBC = "wbc";
	public static final int LOGOUT = 0x711587;

	public static final String CRC = "crc";// 校验

	private static JSONObject upload(JSONObject json, String url)
			throws JSONException, IOException {
		StringBuilder builder = new StringBuilder(url);
		builder.append("?");
		builder.append(json.toString());
		return httpConenction(builder.toString());

	}

	/***
	 * 上传数据
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static JSONObject upload(JSONObject json) throws IOException {
		try {
			String url = DOMAIN + json.getString(PATH);
			json.remove(PATH);
			// json = encodeJsonUrlFormat(json);
			return upload(json, url);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 登录
	 * 
	 * @param cardNo
	 * @param password
	 * @return
	 */
	public static JSONObject login(String cardNo, String password) {
		JSONObject json = new JSONObject();
		JSONObject result = null;
		try {
			json.put("cardNo", cardNo);
			json.put("password", password);
			json.put(PLAT_ID_KEY, PLAT_ID_VALUE);
			String url = DOMAIN + "8031?" + json.toString();
			result = httpConenction(url);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static JSONObject httpConenction(String pathUrl)
			throws JSONException, IOException {
		Log.i(TAG + " httpConenction", pathUrl);
		JSONObject json = null;
		// String url =
		URLEncoder.encode(pathUrl, "UTF-8");
		URL realUrl = new URL(pathUrl);
		HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);// 发送POST请求必须设置允许输出
		conn.setUseCaches(false);// 不使用Cache
		conn.setRequestMethod("POST");
		conn.setReadTimeout(15000);
		conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
		conn.setRequestProperty("Charset", "utf-8");
		conn.setRequestProperty("Content-Length", "0");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		int state = conn.getResponseCode();
		if (state == 200) {
			InputStream re = conn.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(re));
			StringBuffer buffer = new StringBuffer();
			String line = new String();
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String responseString = buffer.toString();
			json = new JSONObject(responseString);
		}
		Log.i(TAG, json.toString());
		return json;
	}

	/***
	 * 设置session会话
	 * 
	 * @param conn
	 */
	private static void setSession3(HttpURLConnection conn) {
		Session session = BaseActivity.getSession();
		long cTime = System.currentTimeMillis();
		long week = 1000 * 60 * 60 * 24 * 7;
		if (session == null || cTime - session.getTime() > week) {
			String cookie = conn.getHeaderField("Set-Cookie");
			if (cookie == null)
				return;
			String[] cookies = cookie.split(";");
			for (String c : cookies) {
				if (c.contains("ession")) {
					L.i("save:", c);
					BaseActivity.setSession(new Session(c, cTime));
				}
			}
		} else {
			L.i("session.getId():", session.getId());
			conn.setRequestProperty("Cookie", session.getId());
		}
	}

	/***
	 * 上传数据
	 * 
	 * @param info
	 * @return
	 */
	public static JSONObject logup(JSONObject info) {
		JSONObject result = null;
		// String url = DOMAIN + "8030?" +
		// info.toString();
		String url = DOMAIN + "8030?";
		try {
			result = httpConenction(info.toString(), url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * 查询数据
	 * 
	 * @param queryPath
	 * @param json
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public static JSONObject query(String queryPath, JSONObject paras)
			throws Exception {
		JSONObject result = postConenction(paras, queryPath);
		return result;
	}

	/***
	 * 对空格和加号进行编码，否则url包含空格会报错
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject encodeJson(JSONObject json) throws JSONException {
		JSONObject newJson = new JSONObject();
		final String space = " ";
		final String plus = "+";
		if (json.has(DATA)) {
			JSONObject data = json.getJSONObject(DATA);
			JSONObject newData = new JSONObject();
			Iterator<String> iter = data.keys();
			while (iter.hasNext()) {
				String key = iter.next();
				String value = data.getString(key);
				if (key.contains(space) || key.contains(plus))
					key = URLEncoder.encode(key);
				if (value.contains(space) || value.contains(plus))
					value = URLEncoder.encode(value);
				newData.put(key, value);
			}
			newJson.put(DATA, newData);
		}
		json.remove(DATA);

		Iterator<String> iter = json.keys();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = json.getString(key);
			if (key.contains(space) || key.contains(plus))
				key = URLEncoder.encode(key);
			if (value.contains(space) || value.contains(plus))
				value = URLEncoder.encode(value);
			newJson.put(key, value);
		}
		return newJson;
	}

	private static Map<String, String> encodeUrl(Map<String, String> dataMap) {
		final String space = " ";
		Map<String, String> newMap = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : dataMap.entrySet()) {
			String key = entry.getKey();
			if (key.contains(space))
				key = URLEncoder.encode(key);
			String value = entry.getValue();
			if (value.contains(space))
				value = URLEncoder.encode(value);
			newMap.put(key, value);
		}
		return newMap;
	}

	/***
	 * 以post方式请求
	 * 
	 * @param params
	 * @param pathUrl
	 * @return
	 * @throws Exception
	 */
	public static JSONObject httpConenction(String params, String pathUrl)
			throws Exception {
		System.out.println(pathUrl + params);
		params = URLEncoder.encode(params);
		JSONObject json = null;
		byte[] data = params.getBytes();
		URL realUrl = new URL(pathUrl);
		HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
		conn.setDoOutput(true);// 发送POST请求必须设置允许输出
		conn.setUseCaches(false);// 不使用Cache
		conn.setRequestMethod("POST");
		conn.setReadTimeout(5000);
		conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		// setSession(conn);
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(data);
		outStream.flush();
		int state = conn.getResponseCode();
		if (state == 200) {
			InputStream re = conn.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(re));
			StringBuffer buffer = new StringBuffer();
			String line = new String();
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String responseString = buffer.toString();
			json = new JSONObject(responseString);
			System.out.println("httpConenction:" + responseString);
		}
		outStream.close();
		return json;
	}

	public static JSONObject postConenction(JSONObject params, String path)
			throws Exception {	
		return httpConenction(params.toString(), DOMAIN_V2 + path);
	}

	/***
	 * 获取当前医生负责的用户列表
	 * 
	 * @param doctorID
	 * @param cPage
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getUserList(String doctorID, int cPage) {
		try {
			JSONObject json = new JSONObject();
			json.put("pageIndex", String.valueOf(cPage));
			json.put("pageCount", "20");
			json.put("guid", GUID_VALUE);
			json.put(TOKEN, BaseActivity.getGroup().getToken());
			StringBuilder builder = new StringBuilder(DOMAIN_V2
					+ PATH_USER_LIST);
			builder.append("?");
			builder.append(json.toString());
			JSONObject result = httpConenction(builder.toString());
			if (result.getInt(STATUS_CODE) != OK)
				throw new Exception("error");
			return result;
		} catch (Exception e) {
			try {
				L.e(TAG, e.toString());
				JSONObject json = new JSONObject();
				json.put(STATUS_CODE, OK);
				String[] sex = { "男", "女" };
				JSONArray array = new JSONArray();
				for (int i = 0; i < 15; i++) {
					JSONObject user = new JSONObject();
					user.put("name", "用户" + i);
					user.put("count", "33077219780501000" + i);
					user.put("sex", sex[i % 2]);
					array.put(user);
				}
				json.put("users", array);
				return json;
			} catch (Exception e2) {
				L.e(TAG, e.toString());
				return null;
			}
		}
	}

	public static JSONObject groupLogin3(String count, String passWord) {
		try {
			Map<String, String> para = new HashMap<String, String>();
			para.put("username", count);
			para.put("password", passWord);
			para.put("guid", GUID_VALUE);
			String crc = count + passWord + GUID_VALUE;
			crc = MyMD5.getMD5(crc);
			para.put("crc", crc);
			JSONObject json = new JSONObject(para);
			String url = DOMAIN_V2 + PATH_GROUP_LOGIN + "?" + json.toString();
			return httpConenction(url);
		} catch (Exception e) {
			L.i(e.toString());
			return null;
		}
	}

	public static JSONObject getWbcData(int pIndex) throws Exception {
		User user = BaseActivity.getUser();
		Group g = BaseActivity.getGroup();
		if (user == null || g == null)
			return null;
		JSONObject para = new JSONObject();
		para.put(WebService.CARDNO, user.getCardNo());
		para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
		para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
		para.put(WebService.DOCTOR_NAME, g.getUserName());
		para.put(WebService.PASS_WORD, g.getPassword());
		JSONObject data = new JSONObject();
		data.put(WebService.PAGEINDEX, String.valueOf(pIndex));
		para.put(WebService.DATA, data);
		StringBuilder sb = new StringBuilder(WebService.DOMAIN_V2);
		sb.append(WebService.PATH_QUERY_WBC);
		sb.append("?");
		sb.append(para.toString());
		JSONObject resultJs = WebService.httpConenction(sb.toString());
		return resultJs;
	}

	/***
	 * 上传白细胞数据
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static JSONObject uploadWbc(String text, String cardNo)
			throws Exception {
		JSONObject para = new JSONObject();
		para.put(WebService.CARDNO, cardNo);
		para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
		JSONObject data = new JSONObject();
		data.put(WebService.WBC, text);
		data.put(WebService.CHECKTIME, TimeHelper.getCurrentTime());
		para.put(WebService.DATA, data);
		JSONObject result = WebService.postConenction(para,
				WebService.PATH_UPLOAD_WBC);
		return result;
	}

	/***
	 * 健康档案的部分
	 * 
	 * @author jiqunpeng
	 * 
	 *         创建时间：2014-3-21 下午6:29:15
	 */
	public static class Archive {
		// 用户档案
		public static final String GET_HEALTH_ARCHIVE = "8342";// 获取档案
		public static final String UPLOAD_HEALTH_ARCHIVE = "8341";// 上传档案
		public static final String MEMBERCODE = "memberCode";
		public static final String NAME = "name";
		public static final String SEX = "sex";
		public static final String MOBILE = "mobile";
		public static final String EMAIL = "email";
		public static final String BIRTHDAY = "birthday";
		public static final String OTHERPLACE = "otherPlace";
		public static final String PARK = "park";
		public static final String HOUSING = "housing";
		public static final String TEBINZHI = "tebinzhi";
		public static final String OLDSTATION = "oldStation";
		public static final String ERRORS = "Errors";
		public static final String SHIREZHI = "shirezhi";
		public static final String YINXUZHI = "yinxuzhi";
		public static final String LIVING = "living";
		public static final String MARITAL_STATUS = "maritalStatus";
		public static final String PINGHEZHI = "pinghezhi";
		public static final String OLDUNIVERSITY = "oldUniversity";
		public static final String MEDICAL_PAYMENT = "medicalPayment";
		public static final String YANGXUZHI = "yangxuzhi";
		public static final String TANSHIZHI = "tanshizhi";
		public static final String QIYUZHI = "qiyuzhi";
		public static final String XUEYUZHI = "xueyuzhi";
		public static final String QIXUZHI = "qixuzhi";
		public static final String EDU = "edu";
		public static final String LIVE_SOURCE = "liveSource";
	}

	/**
	 * 查询用户列表，
	 * 
	 * @param cPage
	 * @param query
	 *            搜索关键字
	 * @return
	 */
	public static JSONObject getUserList(int cPage, String query) {
		try {
			JSONObject json = new JSONObject();
			json.put(WebService.PAGEINDEX, String.valueOf(cPage));
			json.put(WebService.PAGECOUNT, "30");
			json.put(WebService.GUID_KEY, WebService.GUID_VALUE);
			// json.put(WebService.TOKEN,
			// BaseActivity.getGroup().getToken());
			json.put(WebService.DOCTOR_NAME, BaseActivity.getGroup()
					.getUserName());
			json.put(WebService.PASS_WORD, BaseActivity.getGroup()
					.getPassword());
			if (query != null)
				json.put(WebService.PARAM, query);
			StringBuilder builder = new StringBuilder(WebService.DOMAIN_V2
					+ WebService.PATH_USER_LIST);
			builder.append("?");
			builder.append(json.toString());
			JSONObject result = WebService.httpConenction(builder.toString());
			return result;
		} catch (Exception e) {
			L.e("getUserList", e.getMessage());
			return null;
		}
	}

	public class StatusCode {
		public static final int OK = 0;
		public static final int FAILED_LOG_UP = 1;
		public static final int CAN_NOT_IDENTIFY = 4;
		public static final int ERROE_ID_CARD = 5;
		public static final int ERROE_TIME = 6;
		public static final int NULL_NAME_PASSWORD = 7;
		public static final int CAN_NOT_LOGIN = 8;
		public static final int FAILED_LOGIN = 9;
		public static final int OUT_DATE_LOGIN = 10;
		public static final int ERROE_DATA_PROVE = 11;
		public static final int EXISTS_USER = 12;
		public static final int UNKNOWN_ERROR = 255;		

	}
}
