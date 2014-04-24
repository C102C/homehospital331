package com.health.database;

import java.util.HashMap;
import java.util.Map;

import com.health.archive.baby.BabyTable;
import com.health.archive.baby.oneold.OneOldChildTable;
import com.health.archive.vaccinate.VaccTables;

public class Tables {

	// 表名
	public static final String TABLE_NAME = "tableName";
	
	public static final String USER_ID = "user_id";
	public static final String NAME = "name";
	public static final String SERIAL_ID = "serial_id";

	// 每个表都有的四个属性
	public static final String CARDNO = "cardNo";
	public static final String TIME = "checkTime";
	public static final String DEVICENAME = "deviceName";
	public static final String DEVICEMAC = "deviceMac";
	// pc300 5项
	public static final String PULSE = "pulse";
	public static final String TEMP = "temp";
	public static final String DBP = "dbp";
	public static final String SBP = "sbp";
	public static final String BO = "so";
	// 百捷 3项
	public static final String GLU = "glu";
	public static final String UA = "ua";
	public static final String CHOL = "chol";
	// 白细胞
	public static final String WBC = "wbc";
	// 尿液11项
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
	
	// 老年人生活自理能力评估表
	public static final String MEAL = "meal";
	public static final String WASH = "wash";
	public static final String DRESS = "dress";
	public static final String TOILET = "toilet";
	public static final String EXERCISE = "exercise";
	public static final String TOTAL_ESTIMATE = "total_estimate";
	
	// 重性精神疾病患者个人信息补充表	
	public static final String GUARDIAN_NAME = "guardian_name";
	public static final String RELATION_WITH_GUARDIAN = "relation_with_guardian";
	public static final String GUARDIAN_ADDRESS = "guardian_address";
	public static final String GUARDIAN_TELEPHONE = "guardian_telephone";
	public static final String VILLAGE_COMMITTEE_LINKMAN = "village_committee_linkman";
	public static final String VILLAGE_COMMITTEE_TELEPHONE = "village_committee_telephone";
	public static final String INFORMED_CONSENT = "informed_consent";
	public static final String INFORMED_CONSENT_SIGNATURE = "informed_consent_signature";
	public static final String INFORMED_CONSENT_SIGNATURE_DATE = "informed_consent_signature_date";
	public static final String INITIAL_ONSET_DATE = "initial_onset_date";
	public static final String SYMPTOMS_OF_PAST = "symptoms_of_past";
	public static final String OUTPATIENT = "outpatient";
	public static final String INITIAL_DRUG_TREATMENT_DATE = "initial_drug_treatment_date";
	public static final String HOSPITAL_NUM = "hospital_num";
	public static final String CURRENT_DIAGNOSIS_RESULT = "current_diagnosis_result";
	public static final String CURRENT_DIAGNOSIS_HOSPITAL = "current_diagnosis_hospital";
	public static final String CURRENT_DIAGNOSIS_DATE = "current_diagnosis_date";
	public static final String LAST_TREATMENT = "last_treatment";
	public static final String INFO_MILD_DISTURBANCES_NUM = "info_mild_disturbances_num";
	public static final String INFO_MAKE_TROUBLE_NUM = "info_make_trouble_num";
	public static final String INFO_MAKE_DISASTER_NUM = "info_make_disaster_num";
	public static final String INFO_HURT_HIMSELF_NUM = "info_hurt_himself_num";
	public static final String INFO_ATTEMPTED_SUICIDE_NUM = "info_attempted_suicide_num";
	public static final String INFO_NO_IMPACT_ON_FAMILY_AND_SOCIETY = "info_no_impact_on_family_and_society";
	public static final String INFO_SHUT_CASE = "info_shut_case";
	public static final String ECONOMIC_CONDITION = "economic_condition";
	public static final String SPECIALIST_ADVICE = "specialist_advice";
	public static final String INFO_FILL_DATE = "info_fill_date";
	public static final String INFO_DOCTOR_SIGNATURE = "info_doctor_signature";
	
	// 重性精神疾病患者随访服务记录表
	public static final String FOLLOWUP_DATE = "followup_date";
	public static final String RISK = "risk";
	public static final String CURRENT_SYMPTOM = "current_symptom";
	public static final String INSIGHT = "insight";
	public static final String SLEEP_SITUATION = "sleep_situation";
	public static final String DIET_SITUATION = "diet_situation";
	public static final String SELF_CARE = "self_care";
	public static final String HOURSE_WORK = "hourse_wrok";
	public static final String PRODUCTIVE_WORK = "productive_work";
	public static final String LEARNING_ABILITY = "learning_ability";
	public static final String INTERPERSONAL = "interpersonal";
	public static final String FOLLOWUP_MILD_DISTURBANCES_NUM = "followup_mild_disturbances_num";
	public static final String FOLLOWUP_MAKE_TROUBLE_NUM = "followup_make_trouble_num";
	public static final String FOLLOWUP_MAKE_DISASTER_NUM = "followup_make_disaster_num";
	public static final String FOLLOWUP_HURT_HIMSELF_NUM = "followup_hurt_himself_num";
	public static final String FOLLOWUP_ATTEMPTED_SUICIDE_NUM = "followup_attempted_suicide_num";
	public static final String FOLLOWUP_NO_IMPACT_ON_FAMILY_AND_SOCIETY = "followup_no_impact_on_family_and_society";
	public static final String FOLLOWUP_SHUT_CASE = "followup_shut_case";
	public static final String FOLLOWUP_HOSPITALIZATION = "followup_hospitalization";
	public static final String LAST_DATE_TO_DISCHARGE = "last_date_to_discharge";
	public static final String LAB_TEST = "lab_test";
	public static final String MEDICATION_COMPLIANCE = "medication_compliance";
	public static final String DRUG_ADVERSE_REACTION = "drug_adverse_reaction";
	public static final String TREATMENT_EFFECT = "treatment_effect";
	public static final String WHETHER_TO_REFERRAL = "whether_to_referral";
	public static final String REFERRAL_REASON = "referral_reason";
	public static final String WHERE_TO_REFERRAL = "where_to_referral";
	public static final String DRUG_1 = "drug_1";
	public static final String DRUG_USAGE_1 = "drug_usage_1";
	public static final String DOSE_1 = "dose_1";
	public static final String DRUG_2 = "drug_2";
	public static final String DRUG_USAGE_2 = "drug_usage_2";
	public static final String DOSE_2 = "dose_2";
	public static final String DRUG_0 = "drug_0";
	public static final String DRUG_USAGE_0 = "drug_usage_0";
	public static final String DOSE_0 = "dose_0";
	public static final String REHABILITATION_MEASURE = "rehabilitation_measure";
	public static final String THIS_FOLLOWUP_CLASSIFICATION = "this_followup_classification";
	public static final String NEXT_FOLLOWUP_DATE = "next_followup_date";
	public static final String FOLLOWUP_DOCTOR_SIGNATURE = "followup_doctor_signature";
	
	

	public Map<String, String> pulseTable() {
		Map<String, String> pulseMap = defaultAttrs();
		pulseMap.put(TABLE_NAME, "PULSE");
		pulseMap.put(PULSE, "integer");// 脉率
		return pulseMap;
	}

	public Map<String, String> tempTable() {
		Map<String, String> tempMap = defaultAttrs();
		tempMap.put(TABLE_NAME, "TEMP");
		tempMap.put(TEMP, "integer");// 体温
		return tempMap;
	}

	public Map<String, String> bpTable() {
		Map<String, String> bpMap = defaultAttrs();
		bpMap.put(TABLE_NAME, "BP");
		bpMap.put(DBP, "integer");// 舒张压
		bpMap.put(SBP, "integer");// 收缩压
		bpMap.put(PULSE, "integer");// 脉率
		return bpMap;
	}

	public Map<String, String> boTable() {
		Map<String, String> boMap = defaultAttrs();
		boMap.put(TABLE_NAME, "BO");
		boMap.put(BO, "integer");// 血氧
		return boMap;
	}

	public Map<String, String> gluTable() {
		Map<String, String> gluMap = defaultAttrs();
		gluMap.put(TABLE_NAME, "GLU");
		gluMap.put(GLU, "integer");// 血糖
		return gluMap;
	}

	public Map<String, String> uaTable() {
		Map<String, String> uaMap = defaultAttrs();
		uaMap.put(TABLE_NAME, "UA");
		uaMap.put(UA, "integer");// 尿酸
		return uaMap;
	}

	public Map<String, String> cholTable() {
		Map<String, String> cholMap = defaultAttrs();
		cholMap.put(TABLE_NAME, "CHOL");
		cholMap.put(CHOL, "integer");// 总胆固醇
		return cholMap;
	}

	/**
	 * 尿液11项
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
	 * 每个表都有的属性
	 * 
	 * @return
	 */

	public Map<String, String> defaultAttrs() {
		Map<String, String> defaultMap = new HashMap<String, String>();
		defaultMap.put(CARDNO, "varchar(20)");// 账号
		// 2013-08-08 16:32:05
		defaultMap.put(TIME, "varchar(25)");
		defaultMap.put(DEVICENAME, "varchar(20)");// 数据来源设备名称
		defaultMap.put(DEVICEMAC, "varchar(20)");// 设备蓝牙mac地址
		return defaultMap;
	}

	public Map<String, String> ecgTable() {
		Map<String, String> ecgMap = defaultAttrs();
		ecgMap.put(TABLE_NAME, "ECG");
		ecgMap.put(ECG, "integer");// 总胆固醇
		return ecgMap;
	}

	public Map<String, String> oldPeopleSelfCareEstimateTable() {		
		Map<String, String> oldPeopleMap = new HashMap<String, String>();
		oldPeopleMap.put(TABLE_NAME, "oldPeopleSelfCareEstimate");
		oldPeopleMap.put(USER_ID, "char(18)");
		oldPeopleMap.put(MEAL, "varchar(255)");
		oldPeopleMap.put(WASH, "varchar(255)");
		oldPeopleMap.put(DRESS, "varchar(255)");
		oldPeopleMap.put(TOILET, "varchar(255)");
		oldPeopleMap.put(EXERCISE, "varchar(255)");
		oldPeopleMap.put(TOTAL_ESTIMATE, "varchar(255)");
		return oldPeopleMap;
	}
	
	/**
	 * 重性精神疾病患者个人信息补充表
	 * @return
	 */
	public Map<String, String> infoSupplementaryOfSevereMentalIllness() {
		Map<String, String> infoSuppMap = new HashMap<String, String>();
		infoSuppMap.put(TABLE_NAME, "infoSuppOfSevereMentalIllness");
		infoSuppMap.put(USER_ID, "char(18)");
		infoSuppMap.put(NAME, "varchar(8)");
		infoSuppMap.put(SERIAL_ID, "char(20) UNIQUE");
		infoSuppMap.put(GUARDIAN_NAME, "varchar(8)");
		infoSuppMap.put(RELATION_WITH_GUARDIAN, "varchar(8)");
		infoSuppMap.put(GUARDIAN_ADDRESS, "varchar(255)");
		infoSuppMap.put(GUARDIAN_TELEPHONE, "varchar(20)");
		infoSuppMap.put(VILLAGE_COMMITTEE_LINKMAN, "varchar(8)");
		infoSuppMap.put(VILLAGE_COMMITTEE_TELEPHONE, "varchar(20)");
		infoSuppMap.put(INFORMED_CONSENT, "varchar(16)");
		infoSuppMap.put(INFORMED_CONSENT_SIGNATURE, "varchar(8)");
		infoSuppMap.put(INFORMED_CONSENT_SIGNATURE_DATE, "varchar(15)");
		infoSuppMap.put(INITIAL_ONSET_DATE, "varchar(15)");
		infoSuppMap.put(SYMPTOMS_OF_PAST, "varchar(255)");
		infoSuppMap.put(OUTPATIENT, "varchar(20)");
		infoSuppMap.put(INITIAL_DRUG_TREATMENT_DATE, "varchar(15)");
		infoSuppMap.put(HOSPITAL_NUM, "varchar(3)");
		infoSuppMap.put(CURRENT_DIAGNOSIS_RESULT, "varchar(255)");
		infoSuppMap.put(CURRENT_DIAGNOSIS_HOSPITAL, "varchar(255)");
		infoSuppMap.put(CURRENT_DIAGNOSIS_DATE, "varchar(15)");
		infoSuppMap.put(LAST_TREATMENT, "varchar(10)");
		infoSuppMap.put(INFO_MILD_DISTURBANCES_NUM, "varchar(3)");
		infoSuppMap.put(INFO_MAKE_TROUBLE_NUM, "varchar(3)");
		infoSuppMap.put(INFO_MAKE_DISASTER_NUM, "varchar(3)");
		infoSuppMap.put(INFO_HURT_HIMSELF_NUM, "varchar(3)");
		infoSuppMap.put(INFO_ATTEMPTED_SUICIDE_NUM, "varchar(3)");
		infoSuppMap.put(INFO_NO_IMPACT_ON_FAMILY_AND_SOCIETY, "varchar(1)");
		infoSuppMap.put(INFO_SHUT_CASE, "varchar(20)");
		infoSuppMap.put(ECONOMIC_CONDITION, "varchar(30)");
		infoSuppMap.put(SPECIALIST_ADVICE, "varchar(255)");
		infoSuppMap.put(INFO_FILL_DATE, "varchar(15)");
		infoSuppMap.put(INFO_DOCTOR_SIGNATURE, "varcahr(8)");
		return infoSuppMap;
	}
	
	public Map<String, String> followupRecordOfSevereMentalIllness() {
		Map<String, String> follRecoMap = new HashMap<String, String>();
		follRecoMap.put(TABLE_NAME, "followupRecordOfSevereMentalIllness");
		follRecoMap.put(USER_ID, "char(18)");
		follRecoMap.put(NAME, "varchar(8)");
		follRecoMap.put(SERIAL_ID, "char(20) UNIQUE");
		follRecoMap.put(FOLLOWUP_DATE, "varchar(15)");
		follRecoMap.put(RISK, "char(1)");
		follRecoMap.put(CURRENT_SYMPTOM, "varchar(255)");
		follRecoMap.put(INSIGHT, "varchar(15)");
		follRecoMap.put(SLEEP_SITUATION, "varchar(10)");
		follRecoMap.put(DIET_SITUATION, "varchar(10)");
		follRecoMap.put(SELF_CARE, "varchar(10)");
		follRecoMap.put(HOURSE_WORK, "varchar(10)");
		follRecoMap.put(PRODUCTIVE_WORK, "varchar(12)");
		follRecoMap.put(LEARNING_ABILITY, "varchar(10)");
		follRecoMap.put(INTERPERSONAL, "varchar(10)");
		follRecoMap.put(FOLLOWUP_MILD_DISTURBANCES_NUM, "varchar(3)");
		follRecoMap.put(FOLLOWUP_MAKE_TROUBLE_NUM, "varchar(3)");
		follRecoMap.put(FOLLOWUP_MAKE_DISASTER_NUM, "varchar(3)");
		follRecoMap.put(FOLLOWUP_HURT_HIMSELF_NUM, "varchar(3)");
		follRecoMap.put(FOLLOWUP_ATTEMPTED_SUICIDE_NUM, "varchar(3)");
		follRecoMap.put(FOLLOWUP_NO_IMPACT_ON_FAMILY_AND_SOCIETY, "char(1)");
		follRecoMap.put(FOLLOWUP_SHUT_CASE, "varchar(15)");
		follRecoMap.put(FOLLOWUP_HOSPITALIZATION, "varchar(30)");
		follRecoMap.put(LAST_DATE_TO_DISCHARGE, "varchar(20)");
		follRecoMap.put(LAB_TEST, "varchar(255)");
		follRecoMap.put(MEDICATION_COMPLIANCE, "varchar(10)");
		follRecoMap.put(DRUG_ADVERSE_REACTION, "varchar(255)");
		follRecoMap.put(TREATMENT_EFFECT, "varchar(10)");
		follRecoMap.put(WHETHER_TO_REFERRAL, "varchar(5)");
		follRecoMap.put(REFERRAL_REASON, "varchar(255)");
		follRecoMap.put(WHERE_TO_REFERRAL, "varchar(255)");
		follRecoMap.put(DRUG_1, "varchar(255)");
		follRecoMap.put(DRUG_USAGE_1, "varchar(255)");
		follRecoMap.put(DOSE_1, "varchar(255)");
		follRecoMap.put(DRUG_2, "varchar(255)");
		follRecoMap.put(DRUG_USAGE_2, "varchar(255)");
		follRecoMap.put(DOSE_2, "varchar(255)");
		follRecoMap.put(DRUG_0, "varchar(255)");
		follRecoMap.put(DRUG_USAGE_0, "varchar(255)");
		follRecoMap.put(DOSE_0, "varchar(255)");
		follRecoMap.put(REHABILITATION_MEASURE, "varchar(255)");
		follRecoMap.put(THIS_FOLLOWUP_CLASSIFICATION, "varchar(15)");
		follRecoMap.put(NEXT_FOLLOWUP_DATE, "varchar(20)");
		follRecoMap.put(FOLLOWUP_DOCTOR_SIGNATURE, "varchar(8)");
		return follRecoMap;
	}


	/***
	 * 疫苗接种卡记录信息表
	 * 
	 * @return
	 */
	public Map<String, String> vaccRecordTable() {
		return VaccTables.vaccRecordTable();
	}

	/***
	 * 疫苗接种卡表头信息
	 * 
	 * @return
	 */
	public Map<String, String> vaccHeadTable() {
		return VaccTables.vaccHeadTable();
	}

	/***
	 * 新生儿家庭随访记录表
	 * 
	 * @return
	 */
	public Map<String, String> babyVisitTable() {
		return BabyTable.babyVisitTable();
	}

	/***
	 * 一岁一下儿童访问表
	 * 
	 * @return
	 */
	public Map<String, String> oneOldChileTalbe() {
		return OneOldChildTable.oneOldChildTable();
	}

	public static String getSerialId() {
		return "400800-1878";
	}
}
