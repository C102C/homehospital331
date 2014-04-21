package com.health.database;

import java.util.HashMap;
import java.util.Map;

import com.health.archive.baby.BabyTable;
import com.health.archive.vaccinate.VaccTables;

public class Tables {

	// ����
	public static final String TABLE_NAME = "tableName";

	// ÿ�����е��ĸ�����
	public static final String CARDNO = "cardNo";
	public static final String TIME = "checkTime";
	public static final String DEVICENAME = "deviceName";
	public static final String DEVICEMAC = "deviceMac";
	// pc300 5��
	public static final String PULSE = "pulse";
	public static final String TEMP = "temp";
	public static final String DBP = "dbp";
	public static final String SBP = "sbp";
	public static final String BO = "so";
	// �ٽ� 3��
	public static final String GLU = "glu";
	public static final String UA = "ua";
	public static final String CHOL = "chol";
	// ��ϸ��
	public static final String WBC = "wbc";
	// ��Һ11��
	public static final String LEU = "leu";
	public static final String BLD = "bld";
	public static final String PH = "ph";
	public static final String PRO = "pro";
	public static final String UBG = "ubg";
	public static final String NIT = "nit";
	public static final String SG = "sg";
	public static final String KET = "ket";
	public static final String BIL = "bil";
	public static final String UGLU = "glu";
	public static final String VC = "vc";

	private static final String ECG = "ecg";

	public Map<String, String> pulseTable() {
		Map<String, String> pulseMap = defaultAttrs();
		pulseMap.put(TABLE_NAME, "PULSE");
		pulseMap.put(PULSE, "integer");// ����
		return pulseMap;
	}

	public Map<String, String> tempTable() {
		Map<String, String> tempMap = defaultAttrs();
		tempMap.put(TABLE_NAME, "TEMP");
		tempMap.put(TEMP, "integer");// ����
		return tempMap;
	}

	public Map<String, String> bpTable() {
		Map<String, String> bpMap = defaultAttrs();
		bpMap.put(TABLE_NAME, "BP");
		bpMap.put(DBP, "integer");// ����ѹ
		bpMap.put(SBP, "integer");// ����ѹ
		bpMap.put(PULSE, "integer");// ����
		return bpMap;
	}

	public Map<String, String> boTable() {
		Map<String, String> boMap = defaultAttrs();
		boMap.put(TABLE_NAME, "BO");
		boMap.put(BO, "integer");// Ѫ��
		return boMap;
	}

	public Map<String, String> gluTable() {
		Map<String, String> gluMap = defaultAttrs();
		gluMap.put(TABLE_NAME, "GLU");
		gluMap.put(GLU, "integer");// Ѫ��
		return gluMap;
	}

	public Map<String, String> uaTable() {
		Map<String, String> uaMap = defaultAttrs();
		uaMap.put(TABLE_NAME, "UA");
		uaMap.put(UA, "integer");// ����
		return uaMap;
	}

	public Map<String, String> cholTable() {
		Map<String, String> cholMap = defaultAttrs();
		cholMap.put(TABLE_NAME, "CHOL");
		cholMap.put(CHOL, "integer");// �ܵ��̴�
		return cholMap;
	}

	/**
	 * ��Һ11��
	 * 
	 * @return
	 */
	public Map<String, String> urineTable() {
		Map<String, String> urineMap = defaultAttrs();
		urineMap.put(TABLE_NAME, "URINE");
		urineMap.put(LEU, "integer");//
		urineMap.put(BLD, "integer");//
		urineMap.put(PH, "integer");//
		urineMap.put(PRO, "integer");//
		urineMap.put(UBG, "integer");//
		urineMap.put(NIT, "integer");//
		urineMap.put(SG, "integer");//
		urineMap.put(KET, "integer");//
		urineMap.put(BIL, "integer");//
		urineMap.put(GLU, "integer");//
		urineMap.put(VC, "integer");//
		return urineMap;
	}

	/**
	 * ÿ�����е�����
	 * 
	 * @return
	 */

	public Map<String, String> defaultAttrs() {
		Map<String, String> defaultMap = new HashMap<String, String>();
		defaultMap.put(CARDNO, "varchar(20)");// �˺�
		// 2013-08-08 16:32:05
		defaultMap.put(TIME, "varchar(25)");
		defaultMap.put(DEVICENAME, "varchar(20)");// ������Դ�豸����
		defaultMap.put(DEVICEMAC, "varchar(20)");// �豸����mac��ַ
		return defaultMap;
	}

	public Map<String, String> ecgTable() {
		Map<String, String> ecgMap = defaultAttrs();
		ecgMap.put(TABLE_NAME, "ECG");
		ecgMap.put(ECG, "integer");// �ܵ��̴�
		return ecgMap;
	}

	/***
	 * ������ֿ���¼��Ϣ��
	 * 
	 * @return
	 */
	public Map<String, String> vaccRecordTable() {
		return VaccTables.vaccRecordTable();
	}

	/***
	 * ������ֿ���ͷ��Ϣ
	 * 
	 * @return
	 */
	public Map<String, String> vaccHeadTable() {
		return VaccTables.vaccHeadTable();
	}

	/***
	 * ��������ͥ��ü�¼��
	 * 
	 * @return
	 */
	public Map<String, String> babyVisitTable() {
		return BabyTable.babyVisitTable();
	}
	
	public static String getSerialId() {
		return "400800-1878";
	}
}
