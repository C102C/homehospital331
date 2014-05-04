package com.krl.staticecg;

import java.io.File;

import android.database.Cursor;

import com.krl.data.PatientTableCol;
import com.krl.tools.Tools;




public class UploadFileDesc {
	private static final String TAG = "UploadFileDesc";
	private static final int BUFFER_SIZE = 201;
	public int nID = 0;
	public int nDataID = 0;
	public byte[] sOccurDtm = new byte[32];
	public byte[] sFileName = new byte[128];
	public byte[] sFileDesp = new byte[32];
	public int filelength = 0;
	public byte flag = 0;
	public String filePath = "";
	public String filePath_ecg = "";
	public String filePath_7z = "";
	public String patientname = "";
	public String filename = "";
	public int mStartPos = 0;

	public void fromCursor(Cursor cursor) {
		nID = cursor.getInt(PatientTableCol._ID_COL);
		nDataID = cursor.getInt(PatientTableCol.PERSONDATAID_COL);
		String path = "";
		byte[] tmp = null;
		if (cursor.getString(PatientTableCol.NAME_COL) != null) {
			patientname = cursor.getString(PatientTableCol.NAME_COL);
		}
		if (cursor.getString(PatientTableCol.OCCURDTM_COL) != null) {
			Tools.stringToBytes(cursor.getString(PatientTableCol.OCCURDTM_COL),
					sOccurDtm, 0, sOccurDtm.length);
		}
		if (cursor.getString(PatientTableCol.FILEDESP_COL) != null) {
			Tools.stringToBytes(cursor.getString(PatientTableCol.FILEDESP_COL),
					sFileDesp, 0, sFileDesp.length);
		}
		if (cursor.getString(PatientTableCol.FILENAME_COL) != null) {
			path = cursor.getString(PatientTableCol.FILENAME_COL);
			Tools.stringToBytes(path, sFileName, 0, sFileName.length);
		}
		filename = path;
		filelength = cursor.getInt(PatientTableCol.FILELENGTH_COL);
		filePath_ecg = StaticApp.getinstance().mDataPath + path + ".12necg";
		filePath_7z = StaticApp.getinstance().mDataPath + path + ".12necg.7z";
		File file7z = new File(filePath_7z);
		File fileSrc = new File(filePath_ecg);
		if (file7z.isFile())// 存在压缩文件
		{
			filePath = StaticApp.getinstance().mDataPath + path + ".12necg.7z";
			flag = 1;
			filelength = (int) file7z.length();
		} else {
			filePath = StaticApp.getinstance().mDataPath + path + ".12necg";
			if (fileSrc.isFile())// 存在压缩文件
			{
				filelength = (int) fileSrc.length();
			} else {
				filelength = 0;
			}
			flag = 0;
		}
	}

	public byte[] toBytes() {
		byte[] buffer = new byte[BUFFER_SIZE];
		int pos = 0;
		byte[] tmp = Tools.intToBytes(nDataID);
		System.arraycopy(tmp, 0, buffer, pos, tmp.length);
		pos += tmp.length;
		System.arraycopy(sOccurDtm, 0, buffer, pos, sOccurDtm.length);
		pos += sOccurDtm.length;
		System.arraycopy(sFileName, 0, buffer, pos, sFileName.length);
		pos += sFileName.length;
		System.arraycopy(sFileDesp, 0, buffer, pos, sFileDesp.length);
		pos += sFileDesp.length;
		tmp = Tools.intToBytes(filelength);
		System.arraycopy(tmp, 0, buffer, pos, tmp.length);
		pos += tmp.length;
		buffer[pos] = flag;
		return buffer;
	}

	public int fromeBytes(byte[] buffer, int offset) {
		int pos = offset;
		nDataID = Tools.bytesToInt(buffer, pos);
		pos += 4;
		System.arraycopy(buffer, pos, sOccurDtm, 0, sOccurDtm.length);
		pos += sOccurDtm.length;
		System.arraycopy(buffer, pos, sFileName, 0, sFileName.length);
		pos += sFileName.length;
		System.arraycopy(buffer, pos, sFileDesp, 0, sFileDesp.length);
		pos += sFileDesp.length;
		filelength = Tools.bytesToInt(buffer, pos);
		pos += 4;
		flag = buffer[pos];
		pos += 1;
		return pos;
	}

	public UploadFileDesc() {

	}

	public UploadFileDesc(int nID, int nDataID, String patientname,
			String occurdtm, String filedesp, String filename, int filelength) {
		this.nID = nID;
		this.nDataID = nDataID;
		this.patientname = patientname;
		if (occurdtm != null) {
			Tools.stringToBytes(occurdtm, sOccurDtm, 0, sOccurDtm.length);
		}
		if (filedesp != null) {
			Tools.stringToBytes(filedesp, sFileDesp, 0, sFileDesp.length);
		}
		if (filename != null) {
			this.filename = filename;
			Tools.stringToBytes(filename, sFileName, 0, sFileName.length);
		}
		this.filelength = filelength;
		filePath_ecg = StaticApp.getinstance().mDataPath + filename + ".12necg";
		filePath_7z = StaticApp.getinstance().mDataPath + filename
				+ ".12necg.7z";
		File file7z = new File(filePath_7z);
		File fileSrc = new File(filePath_ecg);
		if (file7z.isFile())// 存在压缩文件
		{
			filePath = StaticApp.getinstance().mDataPath + filename
					+ ".12necg.7z";
			flag = 1;
			filelength = (int) file7z.length();
		} else {
			filePath = StaticApp.getinstance().mDataPath + filename + ".12necg";
			if (fileSrc.isFile())// 存在压缩文件
			{
				filelength = (int) fileSrc.length();
			} else {
				filelength = 0;
			}
			flag = 0;
		}

	}
}
