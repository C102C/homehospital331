package com.krl.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import android.content.ContentValues;
import android.database.Cursor;

import com.krl.tools.Tools;

public class PatientInfo {
	private static final String TAG = "PatientInfo";
	public static final int MAX_BUFFER = 2048; // bytes
	public static final int BUFFER_SIZE = 987; // bytes
	public byte tVer = 0; // 版本，初始值为1
	public byte[] sName = new byte[32]; // 姓名
	public byte[] sAge = new byte[4]; // 年龄
	public byte[] sSex = new byte[4]; // 性别
	public byte[] sPhone = new byte[32]; // 电话
	public byte[] sHeight = new byte[8]; // 身高
	public byte[] sWeight = new byte[8]; // 体重
	public byte[] sAddress = new byte[128]; // 地址
	public byte[] sPostcode = new byte[8]; // 邮编
	public byte[] sOutPatientNumber = new byte[16]; // 门诊号
	public byte[] sHospitalNumber = new byte[16]; // 住院号
	public byte[] sBedNumber = new byte[16]; // 床号
	public byte[] sDepartment = new byte[16]; // 科室
	public byte[] sSymptom = new byte[256]; // 症状
	public byte[] sMedicalHistory = new byte[256]; // 病史
	public byte[] sHospitalName = new byte[128]; // 医院名称
	public byte[] sCreateDtm = new byte[20]; // 病员创建时刻
	public byte[] sOccurDtm = new byte[32]; // 数据采集时刻
	public byte[] wFlag = new byte[2]; //
	public byte[] nDel = new byte[4];

	// //////////////////////////////////////////////
	public byte[] toBytes() {
		byte[] bytes = new byte[BUFFER_SIZE];
		toBytes(bytes, 0);
		return bytes;
	}

	public int toBytes(byte[] buffer, int offset) {
		int pos = offset;
		buffer[pos] = tVer;
		pos++;
		System.arraycopy(sName, 0, buffer, pos, sName.length);
		pos += sName.length;
		System.arraycopy(sAge, 0, buffer, pos, sAge.length);
		pos += sAge.length;
		System.arraycopy(sSex, 0, buffer, pos, sSex.length);
		pos += sSex.length;
		System.arraycopy(sPhone, 0, buffer, pos, sPhone.length);
		pos += sPhone.length;
		System.arraycopy(sHeight, 0, buffer, pos, sHeight.length);
		pos += sHeight.length;
		System.arraycopy(sWeight, 0, buffer, pos, sWeight.length);
		pos += sWeight.length;
		System.arraycopy(sAddress, 0, buffer, pos, sAddress.length);
		pos += sAddress.length;
		System.arraycopy(sPostcode, 0, buffer, pos, sPostcode.length);
		pos += sPostcode.length;
		System.arraycopy(sOutPatientNumber, 0, buffer, pos,
				sOutPatientNumber.length);
		pos += sOutPatientNumber.length;
		System.arraycopy(sHospitalNumber, 0, buffer, pos,
				sHospitalNumber.length);
		pos += sHospitalNumber.length;
		System.arraycopy(sBedNumber, 0, buffer, pos, sBedNumber.length);
		pos += sBedNumber.length;
		System.arraycopy(sDepartment, 0, buffer, pos, sDepartment.length);
		pos += sDepartment.length;
		System.arraycopy(sSymptom, 0, buffer, pos, sSymptom.length);
		pos += sSymptom.length;
		System.arraycopy(sMedicalHistory, 0, buffer, pos,
				sMedicalHistory.length);
		pos += sMedicalHistory.length;
		System.arraycopy(sHospitalName, 0, buffer, pos, sHospitalName.length);
		pos += sHospitalName.length;
		System.arraycopy(sCreateDtm, 0, buffer, pos, sCreateDtm.length);
		pos += sCreateDtm.length;
		System.arraycopy(sOccurDtm, 0, buffer, pos, sOccurDtm.length);
		pos += sOccurDtm.length;
		System.arraycopy(wFlag, 0, buffer, pos, wFlag.length);
		pos += wFlag.length;
		System.arraycopy(nDel, 0, buffer, pos, nDel.length);
		pos += nDel.length;
		return pos;
	}

	public int fromBytes(byte[] buffer, int offset) {
		int pos = offset;
		tVer = buffer[pos];
		pos++;
		System.arraycopy(buffer, pos, sName, 0, sName.length);
		pos += sName.length;
		System.arraycopy(buffer, pos, sAge, 0, sAge.length);
		pos += sAge.length;
		System.arraycopy(buffer, pos, sSex, 0, sSex.length);
		pos += sSex.length;
		System.arraycopy(buffer, pos, sPhone, 0, sPhone.length);
		pos += sPhone.length;
		System.arraycopy(buffer, pos, sHeight, 0, sHeight.length);
		pos += sHeight.length;
		System.arraycopy(buffer, pos, sWeight, 0, sWeight.length);
		pos += sWeight.length;
		System.arraycopy(buffer, pos, sAddress, 0, sAddress.length);
		pos += sAddress.length;
		System.arraycopy(buffer, pos, sPostcode, 0, sPostcode.length);
		pos += sPostcode.length;
		System.arraycopy(buffer, pos, sOutPatientNumber, 0,
				sOutPatientNumber.length);
		pos += sOutPatientNumber.length;
		System.arraycopy(buffer, pos, sHospitalNumber, 0,
				sHospitalNumber.length);
		pos += sHospitalNumber.length;
		System.arraycopy(buffer, pos, sBedNumber, 0, sBedNumber.length);
		pos += sBedNumber.length;
		System.arraycopy(buffer, pos, sDepartment, 0, sDepartment.length);
		pos += sDepartment.length;
		System.arraycopy(buffer, pos, sSymptom, 0, sSymptom.length);
		pos += sSymptom.length;
		System.arraycopy(buffer, pos, sMedicalHistory, 0,
				sMedicalHistory.length);
		pos += sMedicalHistory.length;
		System.arraycopy(buffer, pos, sHospitalName, 0, sHospitalName.length);
		pos += sHospitalName.length;
		System.arraycopy(buffer, pos, sCreateDtm, 0, sCreateDtm.length);
		pos += sCreateDtm.length;
		System.arraycopy(buffer, pos, sOccurDtm, 0, sOccurDtm.length);
		pos += sOccurDtm.length;
		System.arraycopy(buffer, pos, wFlag, 0, wFlag.length);
		pos += wFlag.length;
		System.arraycopy(buffer, pos, nDel, 0, nDel.length);
		pos += nDel.length;
		return pos;
	}

	// //////////////////////////////////////////////
	public boolean store(RandomAccessFile file) throws IOException {
		byte[] bytes = new byte[MAX_BUFFER];
		toBytes(bytes, 0);
		file.write(bytes);
		return true;
	}

	public boolean load(RandomAccessFile file) throws IOException {
		byte[] bytes = new byte[MAX_BUFFER];
		file.read(bytes);
		fromBytes(bytes, 0);
		return true;
	}

	public boolean fromCursor(Cursor cursor) {
		try {
			byte[] tmp = null;
			if (cursor.getString(PatientTableCol.NAME_COL) != null) {
				Tools.stringToBytes(cursor.getString(PatientTableCol.NAME_COL),
						sName, 0, sName.length);
			}
			if (cursor.getString(PatientTableCol.AGE_COL) != null) {
				Tools.stringToBytes(cursor.getString(PatientTableCol.AGE_COL),
						sAge, 0, sAge.length);
			}
			if (cursor.getString(PatientTableCol.SEX_COL) != null) {
				Tools.stringToBytes(cursor.getString(PatientTableCol.SEX_COL),
						sSex, 0, sSex.length);
			}
			if (cursor.getString(PatientTableCol.PHONE_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.PHONE_COL), sPhone, 0,
						sPhone.length);
			}
			if (cursor.getString(PatientTableCol.WEIGHT_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.WEIGHT_COL), sWeight,
						0, sWeight.length);
			}
			if (cursor.getString(PatientTableCol.ADDRESS_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.ADDRESS_COL),
						sAddress, 0, sAddress.length);
			}
			if (cursor.getString(PatientTableCol.POSTCODE_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.POSTCODE_COL),
						sPostcode, 0, sPostcode.length);
			}
			if (cursor.getString(PatientTableCol.OUTPATIENTNUMBER_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.OUTPATIENTNUMBER_COL),
						sOutPatientNumber, 0, sOutPatientNumber.length);
			}
			if (cursor.getString(PatientTableCol.HOSPITALNUMBER_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.HOSPITALNUMBER_COL),
						sHospitalNumber, 0, sHospitalNumber.length);
			}
			if (cursor.getString(PatientTableCol.CREATEDTM_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.CREATEDTM_COL),
						sCreateDtm, 0, sCreateDtm.length);
			}
			if (cursor.getString(PatientTableCol.BEDNUMBER_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.BEDNUMBER_COL),
						sBedNumber, 0, sBedNumber.length);
			}
			if (cursor.getString(PatientTableCol.DEPARTMENT_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.DEPARTMENT_COL),
						sDepartment, 0, sDepartment.length);
			}
			if (cursor.getString(PatientTableCol.SYMPTOM_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.SYMPTOM_COL),
						sSymptom, 0, sSymptom.length);
			}
			if (cursor.getString(PatientTableCol.OCCURDTM_COL) != null) {
				Tools.stringToBytes(
						cursor.getString(PatientTableCol.OCCURDTM_COL),
						sOccurDtm, 0, sOccurDtm.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public ContentValues toContentValues(ContentValues srcCV) {
		ContentValues cv = srcCV;
		if (cv == null) {
			cv = new ContentValues();
		}
		cv.put(PatientTableCol.NAME,
				Tools.bytesToString(sName, 0, sName.length));
		cv.put(PatientTableCol.AGE, Tools.bytesToString(sAge, 0, sAge.length));
		cv.put(PatientTableCol.SEX, Tools.bytesToString(sSex, 0, sSex.length));
		cv.put(PatientTableCol.PHONE,
				Tools.bytesToString(sPhone, 0, sPhone.length));
		cv.put(PatientTableCol.HEIGHT,
				Tools.bytesToString(sHeight, 0, sHeight.length));
		cv.put(PatientTableCol.WEIGHT,
				Tools.bytesToString(sWeight, 0, sWeight.length));
		cv.put(PatientTableCol.ADDRESS,
				Tools.bytesToString(sAddress, 0, sAddress.length));
		cv.put(PatientTableCol.POSTCODE,
				Tools.bytesToString(sPostcode, 0, sPostcode.length));
		cv.put(PatientTableCol.OUTPATIENTNUMBER, Tools.bytesToString(
				sOutPatientNumber, 0, sOutPatientNumber.length));
		cv.put(PatientTableCol.HOSPITALNUMBER,
				Tools.bytesToString(sHospitalNumber, 0, sHospitalNumber.length));
		cv.put(PatientTableCol.BEDNUMBER,
				Tools.bytesToString(sBedNumber, 0, sBedNumber.length));
		cv.put(PatientTableCol.DEPARTMENT,
				Tools.bytesToString(sDepartment, 0, sDepartment.length));
		cv.put(PatientTableCol.SYMPTOM,
				Tools.bytesToString(sSymptom, 0, sSymptom.length));
		// cv.put(PatientTableCol.sMedicalHistory ,
		// Tools.bytesToString(sMedicalHistory , 0, sMedicalHistory.length));
		// cv.put(PatientTableCol.sHospitalName ,
		// Tools.bytesToString(sHospitalName , 0, sHospitalName.length));
		cv.put(PatientTableCol.CREATEDTM,
				Tools.bytesToString(sCreateDtm, 0, sCreateDtm.length));
		cv.put(PatientTableCol.OCCURDTM,
				Tools.bytesToString(sOccurDtm, 0, sOccurDtm.length));
		// cv.put(PatientTableCol.wFlag , Tools.bytesToString(wFlag , 0,
		// wFlag.length));
		cv.put(PatientTableCol.DEL, Tools.bytesToString(nDel, 0, nDel.length));
		return cv;
	}

	public void setUserName(String userName) {
		if (null != userName) {
			byte[] name = userName.getBytes();
			System.arraycopy(name, 0, sName, 0, name.length < 32 ? name.length
					: 32);
		}
	}

	public String getName() {
		return Tools.bytesToString(sName, 0, sName.length);
	}

	public PatientInfo() {

	}

	public PatientInfo(String name, String cardNO, String birthDay, String sex,
			String nickName, String customerGuid, String userGuid,
			String mobile, String email, String createDtm, String occurDtm) {
		byte[] tmp = null;
		if (name != null) {
			Tools.stringToBytes(name, sName, 0, sName.length);
		}
		// if (cursor.getString(PatientTableCol.AGE_COL) != null) {
		// Tools.stringToBytes(cursor.getString(PatientTableCol.AGE_COL),
		// sAge, 0, sAge.length);
		// }
		if (sex != null) {
			Tools.stringToBytes(sex, sSex, 0, sSex.length);
		}
		if (mobile != null) {
			Tools.stringToBytes(mobile, sPhone, 0, sPhone.length);
		}
		// if (cursor.getString(PatientTableCol.WEIGHT_COL) != null) {
		// Tools.stringToBytes(cursor.getString(PatientTableCol.WEIGHT_COL),
		// sWeight, 0, sWeight.length);
		// }
		// if (cursor.getString(PatientTableCol.ADDRESS_COL) != null) {
		// Tools.stringToBytes(cursor.getString(PatientTableCol.ADDRESS_COL),
		// sAddress, 0, sAddress.length);
		// }
		// if (cursor.getString(PatientTableCol.POSTCODE_COL) != null) {
		// Tools.stringToBytes(cursor.getString(PatientTableCol.POSTCODE_COL),
		// sPostcode, 0, sPostcode.length);
		// }
		// if (cursor.getString(PatientTableCol.OUTPATIENTNUMBER_COL) != null) {
		// Tools.stringToBytes(
		// cursor.getString(PatientTableCol.OUTPATIENTNUMBER_COL),
		// sOutPatientNumber, 0, sOutPatientNumber.length);
		// }
		// if (cursor.getString(PatientTableCol.HOSPITALNUMBER_COL) != null) {
		// Tools.stringToBytes(
		// cursor.getString(PatientTableCol.HOSPITALNUMBER_COL),
		// sHospitalNumber, 0, sHospitalNumber.length);
		// }
		if (createDtm != null) {
			Tools.stringToBytes(createDtm, sCreateDtm, 0, sCreateDtm.length);
		}
		// if (cursor.getString(PatientTableCol.BEDNUMBER_COL) != null) {
		// Tools.stringToBytes(
		// cursor.getString(PatientTableCol.BEDNUMBER_COL),
		// sBedNumber, 0, sBedNumber.length);
		// }
		// if (cursor.getString(PatientTableCol.DEPARTMENT_COL) != null) {
		// Tools.stringToBytes(
		// cursor.getString(PatientTableCol.DEPARTMENT_COL),
		// sDepartment, 0, sDepartment.length);
		// }
		// if (cursor.getString(PatientTableCol.SYMPTOM_COL) != null) {
		// Tools.stringToBytes(cursor.getString(PatientTableCol.SYMPTOM_COL),
		// sSymptom, 0, sSymptom.length);
		// }
		if (occurDtm != null) {
			Tools.stringToBytes(occurDtm, sOccurDtm, 0, sOccurDtm.length);
		}
	}
}
