package com.krl.data;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Log;

import com.krl.lzma.ICodeProgress;
import com.krl.lzma.LZMACompression;
import com.krl.staticecg.StaticApp;
import com.krl.tools.Tools;






public class DataManager {
	private static final String TAG = "DataManager";
	public DataSegment mECG = null;
	// public DataSegment mVCG = null;
	public DataRecord mDataRecord = null;
	public boolean mSaving = false;

	public DataManager() {
		mECG = new DataSegment();
		// mVCG = new DataSegment();
		DataDescription desc = new DataDescription();
		for (int i = DataDescription.LeadIndex_I; i <= DataDescription.LeadIndex_V6; i++)
			desc.bLeadEnable[i] = true;
		desc.bLeadEnable[DataDescription.LeadIndex_III] = false;
		desc.bLeadEnable[DataDescription.LeadIndex_aVR] = false;
		desc.bLeadEnable[DataDescription.LeadIndex_aVL] = false;
		desc.bLeadEnable[DataDescription.LeadIndex_aVF] = false;
		mECG.Init(desc, 12, desc.nSamplingFrequency * 90);
		// desc = new DataDescription();
		// for (int i = DataDescription.LeadIndex_X; i <=
		// DataDescription.LeadIndex_Z; i++)
		// desc.bLeadEnable[i] = true;
		// desc.fScale = DataDescription.VCG_SCALE;
		// desc.tType = 1;
		// desc.nLeadCount = 3;
		// desc.fVoltageLow = DataDescription.VOLTAGELOW_VCG;
		// desc.fVoltageHigh = DataDescription.VOLTAGEHIGH_VCG;
		// desc.nDigvoltageLow = DataDescription.DIGVOLTAGELOW_VCG;
		// desc.nDigvoltageHigh = DataDescription.DIGVOLTAGEHIGH_VCG;
		// mVCG.Init(desc, 12, desc.nLeadCount * desc.nSamplingFrequency * 1);
		mDataRecord = new DataRecord();
	}

	public boolean store(DataRecord dr, ICodeProgress progress)
			throws IOException {
		File dir = new File(StaticApp.getinstance().mDataPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		mSaving = true;
		String strFileTemp = make_file_name(dr);
		String strFileName = StaticApp.getinstance().mDataPath + strFileTemp
				+ ".12necg";
		String strFileName7z = strFileName + ".7z";
		RandomAccessFile file = new RandomAccessFile(strFileName, "rw");
		dr.info.store(file);
		byte[] segment_main = new byte[32];
		byte[] sNum = Tools.intToBytes(1);
		System.arraycopy(sNum, 0, segment_main, 0, sNum.length);
		file.write(segment_main);
		if (!mECG.store(file, null)) {
			mSaving = false;
			return false;
		}
		int len = (int) file.length();
		dr.nFileLength = Tools.intToBytes(len);
		file.close();
		dr.sFileName = Tools.stringToBytes(strFileTemp, dr.sFileName.length);

		LZMACompression compr = new LZMACompression();
		try {
			compr.Compress(strFileName, strFileName7z, progress);

			// TODO:johnny yang
			// 通知
			String filedesc = "常规：" + mECG.mDataSize
					/ mECG.mDesc.nSamplingFrequency + "秒" + "向量0秒";
			if (null != progress) {
				progress.onSaveSuccess(mECG.mStartTime, strFileTemp, filedesc,
						len, 1, 1);
			}
		} catch (IOException e2) {
			// TODO 自动生成的 catch 块
			e2.printStackTrace();
			Log.e(TAG, e2.toString());
			compr = null;
			mSaving = false;
			return false;
		}
		compr = null;
		mSaving = false;
		return true;
	}

	public String make_file_name(DataRecord dr) {
		String mac = StaticApp.getinstance().mMAC.replaceAll(":", "");
		String starttime = mECG.mStartTime.replaceAll("-", "");
		starttime = starttime.replaceAll(":", "");
		starttime = starttime.replaceAll(" ", "");
		String FileName = starttime
				+ "-"
				+ String.format("%04d", StaticApp.getinstance().mCheckstationId)
				+ "-" + String.format("%06d", Tools.bytesToInt(dr.nID, 0))
				+ "-" + mac;
		return FileName;
	}

	public boolean save(ICodeProgress progress) {
		try {
			byte[] tmp = mECG.mStartTime.getBytes();
			System.arraycopy(tmp, 0, mDataRecord.info.sOccurDtm, 0, tmp.length);
			tmp = null;
			return store(mDataRecord, progress);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
	}
}
