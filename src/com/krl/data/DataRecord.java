package com.krl.data;

import java.util.Date;
import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;

import com.krl.tools.Tools;

public class DataRecord {
	private static final String TAG = "DataRecord";
	private static final int BUFFER_SIZE = 2048;
	public PatientInfo info = new PatientInfo();
	public byte[] nID = new byte[4]; // 本地数据库中的ID
	public byte[] sAccount = new byte[64];
	public byte[] sPersonRemark = new byte[256]; // 备注
	public byte[] sDataRemark = new byte[256]; // 备注
	public byte[] nCheckStationID = new byte[4]; // 该检测站对应ID
	public byte[] sFileName = new byte[128];
	public byte[] nFileLength = new byte[4];
	public byte[] sFileDesp = new byte[32]; // 文件描述，多少常规心电多少向量心电，单位秒
	public byte[] sDiagnose = new byte[256];
	public byte[] sPersonRecordDtm = new byte[32]; // 人员记录到服务器的时间
	public byte[] nPersonDataID = new byte[4]; // Mysql人员数据记录索引ID,可作为人员信息同步标识,0人员未同步,非零人员数据已经同步
	public byte nDataState; // 数据状态:0未采集,1已经采集
	public byte[] sReportCollectDtm = new byte[20]; // pdf报告上传时间
	public Date mSamplingTime = null;
	public String mFileName = "";
	public View mView = null;
	public int mID = -1;

	// ////////////////////////////////////////////
	public void reset() {
		info = new PatientInfo();
		nID = new byte[4]; // 本地数据库中的ID
		sAccount = new byte[64];
		sPersonRemark = new byte[256]; // 备注
		sDataRemark = new byte[256]; // 备注
		nCheckStationID = new byte[4]; // 该检测站对应ID
		sFileName = new byte[128];
		nFileLength = new byte[4];
		sFileDesp = new byte[32]; // 文件描述，多少常规心电多少向量心电，单位秒
		sDiagnose = new byte[256];
		sPersonRecordDtm = new byte[32]; // 人员记录到服务器的时间
		nPersonDataID = new byte[4]; // Mysql人员数据记录索引ID,可作为人员信息同步标识,0人员未同步,非零人员数据已经同步
		sReportCollectDtm = new byte[20]; // pdf报告上传时间
	}

	public byte[] toBytes() {
		byte[] buffer = new byte[BUFFER_SIZE];
		int pos = info.toBytes(buffer, 0);
		// pos = PatientInfo.MAX_BUFFER;
		System.arraycopy(nID, 0, buffer, pos, nID.length);
		pos += nID.length;
		System.arraycopy(sAccount, 0, buffer, pos, sAccount.length);
		pos += sAccount.length;
		System.arraycopy(sPersonRemark, 0, buffer, pos, sPersonRemark.length);
		pos += sPersonRemark.length;
		System.arraycopy(sDataRemark, 0, buffer, pos, sDataRemark.length);
		pos += sDataRemark.length;
		System.arraycopy(nCheckStationID, 0, buffer, pos,
				nCheckStationID.length);
		pos += nCheckStationID.length;
		System.arraycopy(sFileName, 0, buffer, pos, sFileName.length);
		pos += sFileName.length;
		System.arraycopy(nFileLength, 0, buffer, pos, nFileLength.length);
		pos += nFileLength.length;
		System.arraycopy(sFileDesp, 0, buffer, pos, sFileDesp.length);
		pos += sFileDesp.length;
		System.arraycopy(sDiagnose, 0, buffer, pos, sDiagnose.length);
		pos += sDiagnose.length;
		System.arraycopy(sPersonRecordDtm, 0, buffer, pos,
				sPersonRecordDtm.length);
		pos += sPersonRecordDtm.length;
		System.arraycopy(nPersonDataID, 0, buffer, pos, nPersonDataID.length);
		pos += nPersonDataID.length;
		buffer[pos] = nDataState;
		pos++;
		System.arraycopy(sReportCollectDtm, 0, buffer, pos,
				sReportCollectDtm.length);
		return buffer;
	}

	public int fromBytes(byte[] buffer, int offset) {
		int pos = info.fromBytes(buffer, offset);
		System.arraycopy(buffer, pos, nID, 0, nID.length);
		pos += nID.length;
		System.arraycopy(buffer, pos, sAccount, 0, sAccount.length);
		pos += sAccount.length;
		System.arraycopy(buffer, pos, sPersonRemark, 0, sPersonRemark.length);
		pos += sPersonRemark.length;
		System.arraycopy(buffer, pos, sDataRemark, 0, sDataRemark.length);
		pos += sDataRemark.length;
		System.arraycopy(buffer, pos, nCheckStationID, 0,
				nCheckStationID.length);
		pos += nCheckStationID.length;
		System.arraycopy(buffer, pos, sFileName, 0, sFileName.length);
		pos += sFileName.length;
		System.arraycopy(buffer, pos, nFileLength, 0, nFileLength.length);
		pos += nFileLength.length;
		System.arraycopy(buffer, pos, sFileDesp, 0, sFileDesp.length);
		pos += sFileDesp.length;
		System.arraycopy(buffer, pos, sDiagnose, 0, sDiagnose.length);
		pos += sDiagnose.length;
		System.arraycopy(buffer, pos, sPersonRecordDtm, 0,
				sPersonRecordDtm.length);
		pos += sPersonRecordDtm.length;
		System.arraycopy(buffer, pos, nPersonDataID, 0, nPersonDataID.length);
		pos += nPersonDataID.length;
		nDataState = buffer[pos];
		pos++;
		System.arraycopy(buffer, pos, sReportCollectDtm, 0,
				sReportCollectDtm.length);
		pos += sReportCollectDtm.length;
		return pos;
	}

	public void fromCursor(Cursor cursor) {
		info.fromCursor(cursor);
		byte[] tmp = null;
		mID = cursor.getInt(PatientTableCol._ID_COL);
		this.nID = Tools.intToBytes(mID);
		if (cursor.getString(PatientTableCol.ACCOUNT_COL) != null) {
			Tools.stringToBytes(cursor.getString(PatientTableCol.ACCOUNT_COL),
					sAccount, 0, sAccount.length);
		}
		if (cursor.getString(PatientTableCol.PERSONREMARK_COL) != null) {
			Tools.stringToBytes(
					cursor.getString(PatientTableCol.PERSONREMARK_COL),
					sPersonRemark, 0, sPersonRemark.length);
		}
		if (cursor.getString(PatientTableCol.DATAREMARK_COL) != null) {
			Tools.stringToBytes(
					cursor.getString(PatientTableCol.DATAREMARK_COL),
					sDataRemark, 0, sDataRemark.length);
		}
		tmp = Tools.intToBytes(cursor
				.getInt(PatientTableCol.CHECKSTATIONID_COL));
		System.arraycopy(tmp, 0, this.nCheckStationID, 0, tmp.length);
		if (cursor.getString(PatientTableCol.FILENAME_COL) != null) {
			Tools.stringToBytes(cursor.getString(PatientTableCol.FILENAME_COL),
					sFileName, 0, sFileName.length);
		}
		tmp = Tools.intToBytes(cursor.getInt(PatientTableCol.FILELENGTH_COL));
		System.arraycopy(tmp, 0, this.nFileLength, 0, nFileLength.length);
		if (cursor.getString(PatientTableCol.FILEDESP_COL) != null) {
			Tools.stringToBytes(cursor.getString(PatientTableCol.FILEDESP_COL),
					sFileDesp, 0, sFileDesp.length);
		}
		if (cursor.getString(PatientTableCol.DIAGNOSE_COL) != null) {
			Tools.stringToBytes(cursor.getString(PatientTableCol.DIAGNOSE_COL),
					sDiagnose, 0, sDiagnose.length);
		}
		if (cursor.getString(PatientTableCol.PERSONRECORDDTM_COL) != null) {
			Tools.stringToBytes(
					cursor.getString(PatientTableCol.PERSONRECORDDTM_COL),
					sPersonRecordDtm, 0, sPersonRecordDtm.length);
		}
		tmp = Tools.intToBytes(cursor.getInt(PatientTableCol.PERSONDATAID_COL));
		System.arraycopy(tmp, 0, this.nPersonDataID, 0, tmp.length);
		nDataState = (byte) cursor.getInt(PatientTableCol.DATASTATE_COL);
		if (cursor.getString(PatientTableCol.PERSONRECORDDTM_COL) != null) {
			Tools.stringToBytes(
					cursor.getString(PatientTableCol.PERSONRECORDDTM_COL),
					sReportCollectDtm, 0, sReportCollectDtm.length);
		}
	}

	public ContentValues toContentValues(ContentValues srcCV) {
		ContentValues cv = srcCV;
		if (cv == null) {
			cv = new ContentValues();
		}
		info.toContentValues(cv);
		cv.put(PatientTableCol.ACCOUNT,
				Tools.bytesToString(sAccount, 0, sAccount.length));
		cv.put(PatientTableCol.PERSONREMARK,
				Tools.bytesToString(sPersonRemark, 0, sPersonRemark.length));
		cv.put(PatientTableCol.DATAREMARK,
				Tools.bytesToString(sDataRemark, 0, sDataRemark.length));
		cv.put(PatientTableCol.CHECKSTATIONID,
				Tools.bytesToString(nCheckStationID, 0, nCheckStationID.length));
		cv.put(PatientTableCol.DIAGNOSE,
				Tools.bytesToString(sDiagnose, 0, sDiagnose.length));
		cv.put(PatientTableCol.PERSONRECORDDTM, Tools.bytesToString(
				sPersonRecordDtm, 0, sPersonRecordDtm.length));
		cv.put(PatientTableCol.PERSONDATAID, Tools.bytesToInt(nPersonDataID, 0));
		cv.put(PatientTableCol.DATASTATE, nDataState);
		// if (Integer.valueOf(nDataState) == 1)
		{
			cv.put(PatientTableCol.FILENAME,
					Tools.bytesToString(sFileName, 0, sFileName.length));
			cv.put(PatientTableCol.FILELENGTH, Tools.bytesToInt(nFileLength, 0));
			cv.put(PatientTableCol.FILEDESP,
					Tools.bytesToString(sFileDesp, 0, sFileDesp.length));
		}
		cv.put(PatientTableCol.REPORTCOLLECTDTM, Tools.bytesToString(
				sReportCollectDtm, 0, sReportCollectDtm.length));
		return cv;
	}

	public void setPatientInfo(PatientInfo info) {
		this.info = info;
	}

	public PatientInfo getPatientInfo() {
		return info;
	}

	public String getPersonRecordDtm() {
		return Tools
				.bytesToString(sPersonRecordDtm, 0, sPersonRecordDtm.length);
	}

	public String getFileDesp() {
		return Tools.bytesToString(sFileDesp, 0, sFileDesp.length);
	}

	public String getFileName() {
		return Tools.bytesToString(sFileName, 0, sFileName.length);
	}

	public int getFileLength() {
		return Tools.bytesToInt(nFileLength, 0);
	}

	// personRecordDtm
	public DataRecord(int mID, String account, String personRemark,
			String dataRemark, int checkStationID, String fileName,
			int fileLength, String filedesp, String diagnose,
			String personRecordDtm, int personDataId, int dataState,
			String reportCollectDtm) {
		this.mID = mID;
		this.nID = Tools.intToBytes(mID);
		if (account != null) {
			Tools.stringToBytes(account, sAccount, 0, sAccount.length);
		}
		if (personRemark != null) {
			Tools.stringToBytes(personRemark, sPersonRemark, 0,
					sPersonRemark.length);
		}
		if (dataRemark != null) {
			Tools.stringToBytes(dataRemark, sDataRemark, 0, sDataRemark.length);
		}
		byte[] tmp = Tools.intToBytes(checkStationID);
		System.arraycopy(tmp, 0, this.nCheckStationID, 0, tmp.length);
		if (fileName != null) {
			Tools.stringToBytes(fileName, sFileName, 0, sFileName.length);
		}
		tmp = Tools.intToBytes(fileLength);
		System.arraycopy(tmp, 0, this.nFileLength, 0, nFileLength.length);
		if (filedesp != null) {
			Tools.stringToBytes(filedesp, sFileDesp, 0, sFileDesp.length);
		}
		if (diagnose != null) {
			Tools.stringToBytes(diagnose, sDiagnose, 0, sDiagnose.length);
		}
		if (personRecordDtm != null) {
			Tools.stringToBytes(personRecordDtm, sPersonRecordDtm, 0,
					sPersonRecordDtm.length);
		}
		tmp = Tools.intToBytes(personDataId);
		System.arraycopy(tmp, 0, this.nPersonDataID, 0, tmp.length);
		nDataState = (byte) dataState;
		if (reportCollectDtm != null) {
			Tools.stringToBytes(reportCollectDtm, sReportCollectDtm, 0,
					sReportCollectDtm.length);
		}

	}

	public DataRecord() {

	}
}
