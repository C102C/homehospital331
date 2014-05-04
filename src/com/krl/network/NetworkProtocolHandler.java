package com.krl.network;

import java.io.IOException;
import java.io.OutputStream;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.krl.data.PatientTableCol;
import com.krl.tools.Tools;



public class NetworkProtocolHandler {
	private static final String TAG = "NetworkProtocolHandler";
	private Handler mHandler = null;
	private boolean mLogin = false;
	private int mCenterID = 0;
	private int mCheckStationID = 0;
	private OutputStream mOutputStream = null;
	private NetworkPacket mCurPacket = null;
	// private Handler mHandler = null;
	private final int mRcvBufferSize = 1024;
	private byte[] mRcvBuffer = null;
	private int mRcvSize = 0;

	public NetworkProtocolHandler(OutputStream outputstream, Handler handler) {
		mHandler = handler;
		this.mOutputStream = outputstream;
		mCurPacket = new NetworkPacket();
		mRcvBuffer = new byte[mRcvBufferSize];
	}

	public OutputStream GetOutStream() {
		return mOutputStream;
	}

	public void reset() {

	}

	public void SendPacket(byte[] packet) {
		try {
			this.mOutputStream.write(packet);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			if (null != mHandler) {
				Message msg = this.mHandler
						.obtainMessage(MESSAGE.MESSAGE_CONNECT);
				Bundle data = new Bundle();
				data.putInt(MESSAGE.MESSAGE_KEY_CONNECT, -3);
				msg.setData(data);
				mHandler.sendMessage(msg);
			}
		}
	}

	public int Parse(byte[] buffer, int offset, int size) {
		int parser_len = 0, remain = size;
		int[] n = { 0 };
		while (parser_len < size) {
			if (mCurPacket.Parse(buffer, parser_len + offset, remain, n)) {
				PacketHandle(mCurPacket);
				mCurPacket.Reset();
			}
			parser_len += n[0];
			remain = size - parser_len;
			if (!mCurPacket.IsInited() && (remain < NetworkPacket.HEADER_SIZE)) {
				break;
			}
		}
		if (remain > 0) {
			System.arraycopy(buffer, size - remain, buffer, 0, remain);
		}
		return remain;
	}

	public boolean IsLogin() {
		return this.mLogin;
	}

	public void UploadPatientInfo()// 上传病人信息
	{
		// if(mOutputStream==null)
		// {
		// Log.e(TAG,"上传病人信息失败，没有连接到服务器");
		// return ;
		// }
		// //String selection =
		// PatientTableCol.PERSONDATAID+"=0 and "+PatientTableCol.CHECKSTATIONID+"="+StaticApp.getinstance().mCheckstationId;
		// String selection = PatientTableCol.PERSONDATAID+"=0";
		// Log.v(TAG,"上传病人信息SQL:"+selection);
		// Cursor cursor =
		// StaticApp.getinstance().getDB().Query(null,null,PatientTableCol._ID);
		// if(cursor.getCount()>0)
		// {
		// while(!cursor.isLast())
		// {
		// DataRecord dr = new DataRecord();
		// dr.fromCursor(cursor);
		// SendPacket(NetworkPacketFactory.make_patient_info((byte)1, (byte)1,
		// dr));
		// cursor.moveToNext();
		// }
		// }
	}

	public void AskSyncPatientInfo()// 请求同步病人信息
	{
		// Properties btProper = StaticApp.getinstance().loadConfig();
		// String lastSyncTime = (String)
		// btProper.get(Staticecg.CONFIG_LAST_SYNC_TIME +
		// StaticApp.getinstance().mCheckstationId);
		// if (lastSyncTime == null)
		// {
		// lastSyncTime = "0000-00-00 00:00:00";
		// }
		// SendPacket(NetworkPacketFactory.make_sync_patient_info(lastSyncTime.getBytes()));
	}

	public void PacketHandle(NetworkPacket packet) {
		byte[] data = packet.GetData();
		byte res = 0;
		switch (packet.GetType()) {
		case NetworkPacketType.LOGIN: {
			int centerid = 0;
			int checkstationid = 0;
			if (packet.GetResult() == 1) {
				this.mLogin = true;
				centerid = Tools.bytesToInt(packet.GetData(), 0);
				checkstationid = Tools.bytesToInt(packet.GetData(), 4);
			} else {
				this.mLogin = false;
			}
			this.mCenterID = centerid;
			this.mCheckStationID = checkstationid;
			Message msg = mHandler.obtainMessage(MESSAGE.MESSAGE_LOGIN);
			Bundle bd = new Bundle();
			bd.putBoolean(MESSAGE.MESSAGE_KEY_LOGIN, mLogin);
			bd.putInt(MESSAGE.MESSAGE_KEY_CENTERID, centerid);
			bd.putInt(MESSAGE.MESSAGE_KEY_CHECKSTATIONID, checkstationid);
			msg.setData(bd);
			mHandler.sendMessage(msg);
			break;
		}
		case NetworkPacketType.SYNC_PATIENT_INFO: {
			if (packet.GetData() != null) {
				String strTime = Tools.bytesToString(packet.GetData(), 0, 20);
				Message msg = mHandler
						.obtainMessage(MESSAGE.MESSAGE_SYNC_PATIENT_INFO);
				Bundle bd = new Bundle();
				bd.putString(MESSAGE.MESSAGE_KEY_SYNC_PATIENT_INFO, strTime);
				msg.setData(bd);
				mHandler.sendMessage(msg);
			}
			break;
		}
		case NetworkPacketType.PATIENT_INFO: {
			Log.e(TAG, "PATIENT_INFO1");
			if (packet.GetData() != null) {
				switch (packet.GetParam()) {
				case 1:// 插入新的病人信息
				{
					Log.e(TAG, "PATIENT_INFO2");
					// 服务器同步下来的数据
					// DataRecord dr = new DataRecord();
					// dr.fromBytes(packet.GetData(), 0);
					break;
				}
				case 2:// 更新病人PERSONDATAID信息
				{
					Log.e(TAG, "PATIENT_INFO3");
					byte[] dataRecord = new byte[packet.GetData().length];
					System.arraycopy(packet.GetData(), 0, dataRecord, 0,
							packet.GetData().length);
					Message msg = mHandler
							.obtainMessage(MESSAGE.MESSAGE_PATIENT_INFO);
					Bundle bundle = new Bundle();
					bundle.putByteArray("DataRecord", dataRecord);
					msg.setData(bundle);
					mHandler.sendMessage(msg);
					break;
				}
				default:
					Log.e(TAG, "PATIENT_INFO4");
					break;
				}
			}
			break;
		}
		case NetworkPacketType.UPLOAD_DATAFILE: {
			Log.e(TAG, "UPLOAD_DATAFILE");
			switch (packet.GetParam()) {
			case 1: {
				Log.e(TAG, "UPLOAD_DATAFILE1");
				// UploadFileDesc desc = new UploadFileDesc();
				// desc.fromeBytes(data, 0);
				// String filename = Tools.bytesToString(desc.sFileName, 0,
				// desc.sFileName.length);
				// Message msg = mHandler
				// .obtainMessage(MESSAGE.MESSAGE_UPLOAD_FILE);
				// Bundle bundle = new Bundle();
				// bundle.putInt(MESSAGE.MESSAGE_KEY_UPLOAD_FILE, 1);
				// bundle.putInt(MESSAGE.MESSAGE_KEY_RESULT,
				// packet.GetResult());
				// bundle.putInt(MESSAGE.MESSAGE_KEY_UPLOAD_FILE_LEN,
				// desc.filelength);
				// msg.setData(bundle);
				// mHandler.sendMessage(msg);
				break;
			}
			case 2: {
				Log.e(TAG, "UPLOAD_DATAFILE2");
				Message msg = mHandler
						.obtainMessage(MESSAGE.MESSAGE_UPLOAD_FILE);
				Bundle bundle = new Bundle();
				bundle.putInt(MESSAGE.MESSAGE_KEY_UPLOAD_FILE, 2);
				bundle.putInt(MESSAGE.MESSAGE_KEY_RESULT, packet.GetResult());
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				break;
			}
			case 3: {
				Log.e(TAG, "UPLOAD_DATAFILE3");
				// Message msg =
				// mHandler.obtainMessage(Network.MESSAGE_UPLOAD_FILE);
				// Bundle bundle = new Bundle();
				// bundle.putInt(Network.MESSAGE_KEY_UPLOAD_FILE, 3);
				// msg.setData(bundle);
				// mHandler.sendMessage(msg);
				break;
			}
			case 4: {// 上传文件结束回复
				Log.e(TAG, "UPLOAD_DATAFILE4");
				int id = Tools.bytesToInt(data, 0);
				Message msg = mHandler
						.obtainMessage(MESSAGE.MESSAGE_UPLOAD_FILE);
				Bundle bundle = new Bundle();
				bundle.putInt(MESSAGE.MESSAGE_KEY_UPLOAD_FILE, 4);
				bundle.putInt(MESSAGE.MESSAGE_KEY_RESULT, packet.GetResult());
				bundle.putInt(MESSAGE.MESSAGE_KEY_DATA, id);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				break;
			}
			default:
				break;
			}
			break;
		}
		case NetworkPacketType.GETDATASTATE: {
			int id = 0, pos = 0;
			String synctime = Tools.bytesToString(data, pos, 20);
			pos += 20;
			int n = Tools.bytesToInt(data, pos);
			pos += 4;
			for (int i = 0; i < n; i++, pos += 4) {
				id = Tools.bytesToInt(data, pos);
				ContentValues cv = new ContentValues();
				cv.put(PatientTableCol.DATASTATE, 1);
				// StaticApp.getinstance().getDB().update(cv,
				// PatientTableCol.PERSONDATAID + "=" + id, null);
			}
			Message msg = mHandler
					.obtainMessage(MESSAGE.MESSAGE_SYNC_DATA_STATE);
			Bundle bundle = new Bundle();
			bundle.putString(MESSAGE.MESSAGE_KEY_SYNC_DATA_STATE, synctime);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			break;
		}
		case NetworkPacketType.SYNC_REPORT_STATE: {
			int id = 0, pos = 0;
			String synctime = Tools.bytesToString(data, pos, 20);
			String time = "";
			pos += 20;
			int n = Tools.bytesToInt(data, pos);
			pos += 4;
			for (int i = 0; i < n; i++) {
				id = Tools.bytesToInt(data, pos);
				pos += 4;
				time = Tools.bytesToString(data, pos, 20);
				pos += 20;
				ContentValues cv = new ContentValues();
				cv.put(PatientTableCol.REPORTCOLLECTDTM, time);
				// StaticApp.getinstance().getDB().update(cv,
				// PatientTableCol.PERSONDATAID + "=" + id, null);
			}
			Message msg = mHandler
					.obtainMessage(MESSAGE.MESSAGE_SYNC_REPORT_STATE);
			Bundle bundle = new Bundle();
			bundle.putString(MESSAGE.MESSAGE_KEY_SYNC_REPORT_STATE, synctime);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			break;
		}
		default:
			break;
		}
	}
}
