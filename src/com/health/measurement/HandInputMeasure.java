package com.health.measurement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.bean.User;
import com.health.database.Cache;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.Constants;
import com.health.util.L;
import com.health.util.T;
import com.health.util.TimeHelper;
import com.health.web.BackGroundThread;
import com.health.web.Uploader;
import com.health.web.WebService;
import com.health.web.BackGroundThread.BackGroundTask;

/***
 * 手写测量
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2013-12-18 下午3:01:58
 */
public class HandInputMeasure extends BaseActivity {

	protected static final int UPLOAD = 0x312877;
	private static Button homeBtn;
	private static Button returnBtn;
	private static Button machineinput;

	private static Button handinputButton;

	private static Button xueYaMaiLvBtn;
	private static Button xueYangBtn;
	private static Button tiWenBtn;
	private static Button xueTangBtn;
	private static Button niaoSuanBtn;
	private static Button zhongDanGuCunBtn;
	private static Button uploadWbcBtn;
	private static Button niaoYeFenXiBtn;
	// test_handinput_WBCBtn
	private static EditText gaoYaET;
	private static EditText diYaET;
	private static EditText maiLvET;
	private static EditText xueYangET;
	private static EditText tiWenET;
	private static EditText xueTangET;
	private static EditText niaoSuanET;
	private static EditText zhongDanGuCunET;
	private static EditText wbcET;
	private static EditText baiXiBaoET;
	private static EditText yaXiaoSuanYanET;
	private static EditText niaoDanYuanET;
	private static EditText danBaiZhiET;
	private static EditText phZhiET;
	private static EditText qianXueET;
	private static EditText tongTiET;
	private static EditText danHongShuET;
	private static EditText puTaoTangET;
	private static EditText weiShengShuCET;
	private static EditText biZhongET;

	private static DatabaseService dbService;
	private static Context context;
	private static HandInputHandler handler;
	private static ExecutorService exec = Executors.newSingleThreadExecutor();// 单线程池
	private static final String btName = "pad";
	private static final String btMac = "-";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_handinput);

		context = this;
		dbService = new DatabaseService(context);
		findView();
		handler = new HandInputHandler();

		setOnClickListener();

	}

	private static class HandInputHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Uploader.MESSAGE_UPLOADE_RESULT) {
				Bundle bundler = msg.getData();
				// String item =
				// bundler.getString(Cache.ITEM);
				int status = bundler.getInt(Uploader.STUTAS);
				switch (status) {
				case Uploader.OK:
					Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
					break;
				case Uploader.FAILURE:
					Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
					break;
				case Uploader.NET_ERROR:
					Toast.makeText(context, "网络异常,上传失败", Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					break;
				}
			}else if (msg.what == UPLOAD) {
				switch (msg.arg1) {
				case WebService.OK:
					T.showShort(context, "上传成功");
					break;
				default:
					T.showLong(context, "上传失败");
					break;
				}
			}
				
				/*
				 * if (Cache.BP.equals(item)) { switch
				 * (status) { case 0:
				 * Toast.makeText(context, "上传成功",
				 * Toast.LENGTH_SHORT) .show(); break;
				 * case 1: Toast.makeText(context,
				 * "上传失败", Toast.LENGTH_SHORT) .show();
				 * break; default: break; } } if
				 * (Cache.BO.equals(item)) { switch
				 * (status) { case 0:
				 * Toast.makeText(context, "上传成功",
				 * Toast.LENGTH_SHORT) .show(); break;
				 * case 1: Toast.makeText(context,
				 * "上传失败", Toast.LENGTH_SHORT) .show();
				 * break; default: break; } } if
				 * (Cache.TEMP.equals(item)) { switch
				 * (status) { case 0:
				 * Toast.makeText(context, "上传成功",
				 * Toast.LENGTH_SHORT) .show(); break;
				 * case 1: Toast.makeText(context,
				 * "上传失败", Toast.LENGTH_SHORT) .show();
				 * break; default: break; } } if
				 * (Cache.GLU.equals(item)) { switch
				 * (status) { case 0:
				 * Toast.makeText(context, "上传成功",
				 * Toast.LENGTH_SHORT) .show(); break;
				 * case 1: Toast.makeText(context,
				 * "上传失败", Toast.LENGTH_SHORT) .show();
				 * break; default: break; } } if
				 * (Cache.UA.equals(item)) { switch
				 * (status) { case 0:
				 * Toast.makeText(context, "上传成功",
				 * Toast.LENGTH_SHORT) .show(); break;
				 * case 1: Toast.makeText(context,
				 * "上传失败", Toast.LENGTH_SHORT) .show();
				 * break; default: break; } } if
				 * (Cache.CHOL.equals(item)) { switch
				 * (status) { case 0:
				 * Toast.makeText(context, "上传成功",
				 * Toast.LENGTH_SHORT) .show(); break;
				 * case 1: Toast.makeText(context,
				 * "上传失败", Toast.LENGTH_SHORT) .show();
				 * break; default: break; } } if
				 * (Cache.URINE.equals(item)) { switch
				 * (status) { case 0:
				 * Toast.makeText(context, "上传成功",
				 * Toast.LENGTH_SHORT) .show(); break;
				 * case 1: Toast.makeText(context,
				 * "上传失败", Toast.LENGTH_SHORT) .show();
				 * break; default: break; } }
				 */
			}		

	}

	private void setOnClickListener() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == xueYaMaiLvBtn) {
					String diYa = diYaET.getText().toString(); // 舒张压
					String gaoYa = gaoYaET.getText().toString(); // 收缩压
					String maiLv = maiLvET.getText().toString(); // 脉率

					if (diYa.length() > 0 && gaoYa.length() > 0
							&& maiLv.length() > 0) {
						xueYaMaiLvBtn.setClickable(false);
						Toast.makeText(context, "开始上传您的血压和脉率",
								Toast.LENGTH_SHORT).show();
						try {
							uploadXueYaMaiLv();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Toast.makeText(context, "请完整输入您的数据", Toast.LENGTH_SHORT)
								.show();
					}

				} else if (v == xueYangBtn) {
					String xueYang = xueYangET.getText().toString();

					if (xueYang.length() > 0) {
						xueYangBtn.setClickable(false);
						Toast.makeText(context, "开始上传您的血氧", Toast.LENGTH_SHORT)
								.show();
						try {
							uploadXueYang();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Toast.makeText(context, "请完整输入您的数据", Toast.LENGTH_SHORT)
								.show();
					}

				} else if (v == tiWenBtn) {
					String tiWen = tiWenET.getText().toString();

					if (tiWen.length() > 0) {
						tiWenBtn.setClickable(false);
						Toast.makeText(context, "开始上传您的体温", Toast.LENGTH_SHORT)
								.show();
						try {
							uploadTiWen();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Toast.makeText(context, "请完整输入您的数据", Toast.LENGTH_SHORT)
								.show();
					}

				} else if (v == xueTangBtn) {
					String xueTang = xueTangET.getText().toString();

					if (xueTang.length() > 0) {
						xueTangBtn.setClickable(false);
						Toast.makeText(context, "开始上传您的血糖", Toast.LENGTH_SHORT)
								.show();
						try {
							uploadXueYang();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Toast.makeText(context, "请完整输入您的数据", Toast.LENGTH_SHORT)
								.show();
					}

				} else if (v == niaoSuanBtn) {
					String niaoSuan = niaoSuanET.getText().toString();
					if (niaoSuan.length() > 0) {
						niaoSuanBtn.setClickable(false);
						Toast.makeText(context, "开始上传您的尿酸", Toast.LENGTH_SHORT)
								.show();
						try {
							uploadNiaoSuan();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Toast.makeText(context, "请完整输入您的数据", Toast.LENGTH_SHORT)
								.show();
					}

				} else if (v == zhongDanGuCunBtn) {
					String zhongDanGuCun = zhongDanGuCunET.getText().toString();
					if (zhongDanGuCun.length() > 0) {
						zhongDanGuCunBtn.setClickable(false);
						Toast.makeText(context, "开始上传您的总胆固醇",
								Toast.LENGTH_SHORT).show();
						try {
							uploadZhongDanGuCun();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Toast.makeText(context, "请完整输入您的数据", Toast.LENGTH_SHORT)
								.show();
					}

				} else if (v == uploadWbcBtn) {
					String wbc = wbcET.getText().toString();
					if (wbc.length() > 0) {
						uploadWbcBtn.setClickable(false);
						Toast.makeText(context, "开始上传白细胞数据", Toast.LENGTH_SHORT)
								.show();
						uploadWbc(wbc);
					} else {
						Toast.makeText(context, "请完整输入您的数据", Toast.LENGTH_SHORT)
								.show();
					}
				} else if (v == niaoYeFenXiBtn) {
					String baiXiBao = baiXiBaoET.getText().toString();
					String yaXiaoSuanYan = yaXiaoSuanYanET.getText().toString();
					String niaoDanYuan = niaoDanYuanET.getText().toString();
					String danBaiZhi = danBaiZhiET.getText().toString();
					String phZhi = phZhiET.getText().toString();
					String qianXue = qianXueET.getText().toString();
					String tongTi = tongTiET.getText().toString();
					String danHongShu = danHongShuET.getText().toString();
					String puTaoTang = puTaoTangET.getText().toString();
					String weiShengShuC = weiShengShuCET.getText().toString();
					String biZhong = biZhongET.getText().toString();
					if (baiXiBao.length() > 0 && yaXiaoSuanYan.length() > 0
							&& niaoDanYuan.length() > 0
							&& danBaiZhi.length() > 0 && phZhi.length() > 0
							&& qianXue.length() > 0 && tongTi.length() > 0
							&& danHongShu.length() > 0
							&& puTaoTang.length() > 0
							&& weiShengShuC.length() > 0
							&& biZhong.length() > 0) {

						niaoYeFenXiBtn.setClickable(false);
						Toast.makeText(context, "开始上传您的尿液分析",
								Toast.LENGTH_SHORT).show();
						try {
							uploadNiaoYeFenXi();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Toast.makeText(context, "请完整输入您的数据", Toast.LENGTH_SHORT)
								.show();
					}

				} else if (v == homeBtn) {
					HandInputMeasure.this.setResult(RESULT_OK);
					HandInputMeasure.this.finish();
				} else if (v == returnBtn) {
					HandInputMeasure.this.finish();
				} else if (v == machineinput) {
					HandInputMeasure.this.finish();
				}
			}
		};

		xueYaMaiLvBtn.setOnClickListener(onClickListener);
		xueYangBtn.setOnClickListener(onClickListener);
		tiWenBtn.setOnClickListener(onClickListener);
		xueTangBtn.setOnClickListener(onClickListener);
		niaoSuanBtn.setOnClickListener(onClickListener);
		zhongDanGuCunBtn.setOnClickListener(onClickListener);

		uploadWbcBtn.setOnClickListener(onClickListener);
		niaoYeFenXiBtn.setOnClickListener(onClickListener);
		homeBtn.setOnClickListener(onClickListener);
		returnBtn.setOnClickListener(onClickListener);
		machineinput.setOnClickListener(onClickListener);

	}

	/***
	 * 上传白细胞
	 */

	public void uploadWbc(final String wbc) {
		final User user = BaseActivity.getUser();
		if (user == null) {
			T.showLong(context, "用户登录已过期,请退出重新登录");
			return;
		}
		new BackGroundThread(new BackGroundTask() {
			@Override
			public void process() {
				try {
					JSONObject result = WebService.uploadWbc(wbc,
							user.getCardNo());
					int status = result.getInt(WebService.STATUS_CODE);
					handler.obtainMessage(UPLOAD, status, 0).sendToTarget();
				} catch (Exception e) {
					L.e(e.getMessage());
					handler.obtainMessage(UPLOAD, WebService.ERROE, 0)
							.sendToTarget();
				}
			}
		}).start();
	}

	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			HandInputMeasure.this.setResult(RESULT_OK);
			HandInputMeasure.this.finish();

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	private void findView() {
		homeBtn = (Button) findViewById(R.id.test_handinput_homeBtn);
		returnBtn = (Button) findViewById(R.id.test_handinput_returnBtn);
		machineinput = (Button) findViewById(R.id.test_machinebtn);
		handinputButton = (Button) findViewById(R.id.test_handbtn);
		machineinput.setSelected(false);
		handinputButton.setSelected(true);
		xueYaMaiLvBtn = (Button) findViewById(R.id.test_handinput_BPBtn);
		xueTangBtn = (Button) findViewById(R.id.test_handinput_XTBtn);
		tiWenBtn = (Button) findViewById(R.id.test_handinput_TWBtn);
		xueYangBtn = (Button) findViewById(R.id.test_handinput_XYBtn);
		niaoSuanBtn = (Button) findViewById(R.id.test_handinput_NSBtn);
		zhongDanGuCunBtn = (Button) findViewById(R.id.test_handinput_ZDGCBtn);
		uploadWbcBtn = (Button) findViewById(R.id.test_handinput_WBCBtn);
		niaoYeFenXiBtn = (Button) findViewById(R.id.test_handinput_NYFXBtn);

		gaoYaET = (EditText) findViewById(R.id.test_handinput_HBGedittext);
		diYaET = (EditText) findViewById(R.id.test_handinput_LBGedittext);
		xueTangET = (EditText) findViewById(R.id.test_handinput_XTedittext);
		tiWenET = (EditText) findViewById(R.id.test_handinput_TWedittext);
		xueYangET = (EditText) findViewById(R.id.test_handinput_XYedittext);
		maiLvET = (EditText) findViewById(R.id.test_handinput_maiLvET);
		niaoSuanET = (EditText) findViewById(R.id.test_handinput_NSedittext);
		zhongDanGuCunET = (EditText) findViewById(R.id.test_handinput_ZDGCedittext);
		wbcET = (EditText) findViewById(R.id.test_handinput_wbcedittext);
		baiXiBaoET = (EditText) findViewById(R.id.test_handinput_baiXiBaoET);
		yaXiaoSuanYanET = (EditText) findViewById(R.id.test_handinput_yaXiaoSuanYanET);
		niaoDanYuanET = (EditText) findViewById(R.id.test_handinput_niaoDanYuanET);
		danBaiZhiET = (EditText) findViewById(R.id.test_handinput_danBaiZiET);
		phZhiET = (EditText) findViewById(R.id.test_handinput_phZhiET);
		qianXueET = (EditText) findViewById(R.id.test_handinput_qianXueET);
		tongTiET = (EditText) findViewById(R.id.test_handinput_tongTiET);
		danHongShuET = (EditText) findViewById(R.id.test_handinput_danHongSuET);
		puTaoTangET = (EditText) findViewById(R.id.test_handinput_puTaoTangET);
		weiShengShuCET = (EditText) findViewById(R.id.test_handinput_weiShengSuCET);
		biZhongET = (EditText) findViewById(R.id.test_handinput_biZhongET);
	}

	/***
	 * 获取几个测量项目都有的几个属性
	 * 
	 * @return
	 * @throws JSONException 
	 */
	/*public Map<String, String> getDefaltAttrs() {
		String time = TimeHelper.getCurrentTime();
		String idCard = BaseActivity.getUser().getCardNo();
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put(Tables.TIME, time);
		dataMap.put(Tables.CARDNO, idCard);
		dataMap.put(WebService.STATUS, WebService.UNUPLOAD);// 状态为未上传
		dataMap.put(WebService.PLAT_ID_KEY, WebService.PLAT_ID_VALUE);
		return dataMap;
	}*/
	

	public void uploadXueYaMaiLv() throws JSONException {
		Tables table = new Tables();
		String diYa = diYaET.getText().toString(); // 舒张压
		String gaoYa = gaoYaET.getText().toString(); // 收缩压
		String maiLv = maiLvET.getText().toString(); // 脉率

		if (diYa.length() > 0 && gaoYa.length() > 0 && maiLv.length() > 0) {
			//Map<String, String> dataMap = getDefaltAttrs();
			String time=TimeHelper.getCurrentTime();
			JSONObject data=new JSONObject();
			data.put(WebService.TIME, time);
			data.put(Tables.SBP, gaoYa);
			data.put(Tables.DBP, diYa);
			data.put(Tables.PULSE, maiLv);
			data.put(WebService.DEVICENAME, btName);
			data.put(WebService.DEVICEMAC, btMac);
			//Uploader xueYaMaiLvUploader = new Uploader(dataMap, Cache.BP,
				//	WebService.PATH_BP, cache, dbService, handler,
				//	table.bpTable());
			//exec.execute(xueYaMaiLvUploader);
			uploadInBack(data, WebService.PATH_BP);
		}
	}

	public void uploadXueYang() throws JSONException {
		Tables table = new Tables();
		String xueYang = xueYangET.getText().toString();
		
		if (xueYang.length() > 0) {
			String time=TimeHelper.getCurrentTime();
			JSONObject data=new JSONObject();
			data.put(WebService.TIME, time);
			data.put(Tables.BO, xueYang);
			data.put(WebService.DEVICENAME, btName);
			data.put(WebService.DEVICEMAC, btMac);
			/*Uploader xueYangUploader = new Uploader(dataMap, Cache.BO,
					WebService.PATH_BO, cache, dbService, handler,
					table.boTable());
			exec.execute(xueYangUploader);*/
			uploadInBack(data, WebService.PATH_BO);
		}
	}

	public void uploadTiWen() throws JSONException {
		Tables table = new Tables();
		String tiWen = tiWenET.getText().toString();

		if (tiWen.length() > 0) {
			String time=TimeHelper.getCurrentTime();
			JSONObject data=new JSONObject();
			data.put(WebService.TIME, time);
			data.put(Tables.TEMP, tiWen);
			data.put(WebService.DEVICENAME, btName);
			data.put(WebService.DEVICEMAC, btMac);
			//Uploader tiWenUploader = new Uploader(dataMap, Cache.TEMP,
			//		WebService.PATH_TEMP, cache, dbService, handler,
			//		table.tempTable());
			//exec.execute(tiWenUploader);
			uploadInBack(data, WebService.PATH_TEMP);
		}
	}

	public void uploadXueTang() throws JSONException {
		//Tables table = new Tables();
		String xueTang = xueTangET.getText().toString();

		if (xueTang.length() > 0) {
			String time=TimeHelper.getCurrentTime();
			JSONObject data=new JSONObject();
			data.put(WebService.TIME, time);
			data.put(Tables.GLU, xueTang);
			data.put(WebService.DEVICENAME, btName);
			data.put(WebService.DEVICEMAC, btMac);
			//Uploader xueTangUploader = new Uploader(dataMap, Cache.GLU,
			//		WebService.PATH_GLU, cache, dbService, handler,
			//		table.gluTable());
			//exec.execute(xueTangUploader);
			uploadInBack(data, WebService.PATH_GLU);
		}
	}

	public void uploadNiaoSuan() throws JSONException {
		Tables table = new Tables();
		String niaoSuan = niaoSuanET.getText().toString();
		if (niaoSuan.length() > 0) {
			String time=TimeHelper.getCurrentTime();
			JSONObject data=new JSONObject();
			data.put(WebService.TIME, time);
			data.put(Tables.UA, niaoSuan);
			data.put(WebService.DEVICENAME, btName);
			data.put(WebService.DEVICEMAC, btMac);
			uploadInBack(data, WebService.PATH_UA);
		}
	}

	public void uploadZhongDanGuCun() throws JSONException {
		Tables table = new Tables();
		String zhongDanGuCun = zhongDanGuCunET.getText().toString();
		if (zhongDanGuCun.length() > 0) {
			String time=TimeHelper.getCurrentTime();
			JSONObject data=new JSONObject();
			data.put(WebService.TIME, time);
			data.put(Tables.CHOL, zhongDanGuCun);
			data.put(WebService.DEVICENAME, btName);
			data.put(WebService.DEVICEMAC, btMac);
			uploadInBack(data, WebService.PATH_CHOL);
			
		}
	}

	public void uploadNiaoYeFenXi() throws JSONException {
		Tables table = new Tables();
		String baiXiBao = baiXiBaoET.getText().toString();
		String yaXiaoSuanYan = yaXiaoSuanYanET.getText().toString();
		String niaoDanYuan = niaoDanYuanET.getText().toString();
		String danBaiZhi = danBaiZhiET.getText().toString();
		String phZhi = phZhiET.getText().toString();
		String qianXue = qianXueET.getText().toString();
		String tongTi = tongTiET.getText().toString();
		String danHongShu = danHongShuET.getText().toString();
		String puTaoTang = puTaoTangET.getText().toString();
		String weiShengShuC = weiShengShuCET.getText().toString();
		String biZhong = biZhongET.getText().toString();
		if (baiXiBao.length() > 0 && yaXiaoSuanYan.length() > 0
				&& niaoDanYuan.length() > 0 && danBaiZhi.length() > 0
				&& phZhi.length() > 0 && qianXue.length() > 0
				&& tongTi.length() > 0 && danHongShu.length() > 0
				&& puTaoTang.length() > 0 && weiShengShuC.length() > 0
				&& biZhong.length() > 0) {

			String time=TimeHelper.getCurrentTime();
			JSONObject data=new JSONObject();
			data.put(WebService.TIME, time);
			data.put(Tables.LEU, baiXiBao);
			data.put(Tables.NIT, yaXiaoSuanYan);
			data.put(Tables.UBG, niaoDanYuan);
			data.put(Tables.PRO, danBaiZhi);
			data.put(Tables.PH, phZhi);
			data.put(Tables.BLD, qianXue);
			data.put(Tables.KET, tongTi);
			data.put(Tables.BIL, danHongShu);
			data.put(Tables.UGLU, puTaoTang);
			data.put(Tables.VC, weiShengShuC);
			data.put(Tables.SG, biZhong);
			data.put(WebService.DEVICENAME, btName);
			data.put(WebService.DEVICEMAC, btMac);
			//Uploader niaoYeFenXiUploader = new Uploader(dataMap, Cache.URINE,
			//		WebService.PATH_URINE, cache, dbService, handler,
			//		table.urineTable());
			//exec.execute(niaoYeFenXiUploader);
			uploadInBack(data, WebService.PATH_URINE);
		}
	}
	private void uploadInBack(final JSONObject data, final String path) {
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				upload(data, path);
			}
		}).start();
	}
	public static void upload(JSONObject data, String path) {
		try {
			JSONObject para = new JSONObject();
			String idCard = BaseActivity.getUser().getCardNo();
			para.put(WebService.CARDNO, idCard);
			para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
			para.put(WebService.DATA, data);
			para.put(WebService.CRC, "");
			JSONObject result = WebService.postConenction(para, path);
			int status = result.getInt(WebService.STATUS_CODE);
			handler.obtainMessage(UPLOAD, status, 0, path)
					.sendToTarget();
		} catch (Exception e) {
			handler.obtainMessage(UPLOAD, WebService.NETERROE, 0, path)
					.sendToTarget();
		}
	}
}
