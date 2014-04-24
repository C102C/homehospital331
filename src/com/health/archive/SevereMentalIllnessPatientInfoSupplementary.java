package com.health.archive;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.health.database.DataOpenHelper;
import com.health.database.Tables;
import com.health.util.DateEditText;

public class SevereMentalIllnessPatientInfoSupplementary extends Fragment {

	private EditText mNameET; // 姓名
	private TextView mSerialIDTV; // 编号
	private EditText mGuardianNameET; // 监护人姓名
	private EditText mRelationWithGuardianET; // 与患者关系
	private EditText mGuardianTelephoneET; // 监护人电话
	private EditText mGuardianAddressET; // 监护人住址
	private EditText mVillageCommitteeLinkmanET; // 辖区村（居）委会联系人
	private EditText mVillageCommitteeTelephonET; // 辖区村（居）委会电话
	private RadioGroup mInformedConsentRG; // 知情同意
	private RadioButton mInformedConsentRB0;
	private RadioButton mInformedConsentRB1;
	private EditText mInformedConsentSignatureET; // 知情同意签字
	private DateEditText informedConsentSignatureDateET; // 知情同意签字时间
	private DateEditText initialOnsetDate; // 初次发病时间

	// 既往主要症状
	private CheckBox mSymptomsOfPast0CB;
	private CheckBox mSymptomsOfPast1CB;
	private CheckBox mSymptomsOfPast2CB;
	private CheckBox mSymptomsOfPast3CB;
	private CheckBox mSymptomsOfPast4CB;
	private CheckBox mSymptomsOfPast5CB;
	private CheckBox mSymptomsOfPast6CB;
	private CheckBox mSymptomsOfPast7CB;
	private CheckBox mSymptomsOfPast8CB;
	private CheckBox mSymptomsOfPast9CB;
	private CheckBox mSymptomsOfPast10CB;
	private CheckBox mSymptomsOfPast11CB;
	private EditText mSymptomsOfPastOtherET;

	// 既往治疗情况
	private RadioGroup mOutpatientRG; // 门诊
	private RadioButton mOutPatientRB0;
	private RadioButton mOutPatientRB1;
	private RadioButton mOutPatientRB2;
	private DateEditText initialDrugTreatmentDateET; // 首次抗精神病药治疗时间
	private EditText mHospitalNumET; // 曾住院次数

	// 目前诊断情况
	private EditText mCurrentDiagnosisResultET; // 诊断结果
	private EditText mCurrentDiagnosisHospitalET; // 确诊医院
	private DateEditText currentDiagnosisDateET; // 确诊日期
	private RadioGroup mLastTreatmentRG;
	private RadioButton mLastTreatmentRB0;
	private RadioButton mLastTreatmentRB1;
	private RadioButton mLastTreatmentRB2;
	private RadioButton mLastTreatmentRB3;

	// 患病对家庭社会的影响
	private EditText mInfoMildDisturbancesNumET;
	private EditText mInfoMakeTroubleNumET;
	private EditText mInfoMakeDisasterNumET;
	private EditText mInfoHurtHimselfNumET;
	private EditText mInfoAttemptedSuicideNumET;
	private CheckBox mInfoNoImpactOnfamilyAndSocietyCB;

	private RadioGroup mInfoShutCaseRG; // 关锁情况
	private RadioButton mInfoShutCaseRB0;
	private RadioButton mInfoShutCaseRB1;
	private RadioButton mInfoShutCaseRB2;
	private RadioGroup mEconomicConditionRG; // 经济状况
	private RadioButton mEconomicConditionRB0;
	private RadioButton mEconomicConditionRB1;
	private RadioButton mEconomicConditionRB2;
	private EditText mSpecialistAdviceET; // 专科医生的意见
	private DateEditText infoFillDateET; // 填表日期
	private EditText mInfoDoctorSignatureET; // 医生签字

	// 表中属性
	private ContentValues cv = new ContentValues();
	private SQLiteDatabase db;
	private Cursor cursor;
	private String table = "infoSuppOfSevereMentalIllness";
	private Button saveTableBtn;
	private String serialID;

	public SevereMentalIllnessPatientInfoSupplementary() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(
						R.layout.table_info_supplementary_of_patient_with_severe_mental_illness,
						null);
		findView(v);

		db = new DataOpenHelper(getActivity()).getWritableDatabase();

		// 查询以往记录，若有则填充表格
		cursor = db.rawQuery("select * from infoSuppOfSevereMentalIllness "
				+ "where user_id = ?", new String[] { "123" }); // ****************need
																// to
																// modify****************
		fillTable(cursor);

		// 知情同意
		mInformedConsentRG
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.informed_consent_rg0:
							cv.put(Tables.INFORMED_CONSENT, mInformedConsentRB0
									.getText().toString());
							break;
						case R.id.informed_consent_rg1:
							cv.put(Tables.INFORMED_CONSENT, mInformedConsentRB1
									.getText().toString());
							break;
						default:
							break;
						}

					}
				});

		// 门诊
		mOutpatientRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.outpatient_rg0:
					cv.put(Tables.OUTPATIENT, mOutPatientRB0.getText()
							.toString());
					break;
				case R.id.outpatient_rg1:
					cv.put(Tables.OUTPATIENT, mOutPatientRB1.getText()
							.toString());
					break;
				case R.id.outpatient_rg2:
					cv.put(Tables.OUTPATIENT, mOutPatientRB2.getText()
							.toString());
					break;
				default:
					break;
				}

			}
		});

		// 最近一次治疗效果
		mLastTreatmentRG
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.last_treatment_rg0:
							cv.put(Tables.LAST_TREATMENT, mLastTreatmentRB0
									.getText().toString());
							break;
						case R.id.last_treatment_rg1:
							cv.put(Tables.LAST_TREATMENT, mLastTreatmentRB1
									.getText().toString());
							break;
						case R.id.last_treatment_rg2:
							cv.put(Tables.LAST_TREATMENT, mLastTreatmentRB2
									.getText().toString());
							break;
						case R.id.last_treatment_rg3:
							cv.put(Tables.LAST_TREATMENT, mLastTreatmentRB3
									.getText().toString());
							break;
						default:
							break;
						}
					}
				});

		// 关锁情况
		mInfoShutCaseRG
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.info_shut_case_rg0:
							cv.put(Tables.INFO_SHUT_CASE, mInfoShutCaseRB0
									.getText().toString());
							break;
						case R.id.info_shut_case_rg1:
							cv.put(Tables.INFO_SHUT_CASE, mInfoShutCaseRB1
									.getText().toString());
							break;
						case R.id.info_shut_case_rg2:
							cv.put(Tables.INFO_SHUT_CASE, mInfoShutCaseRB2
									.getText().toString());
							break;
						default:
							break;
						}
					}
				});

		// 经济状况
		mEconomicConditionRG
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.economic_condition_rg0:
							cv.put(Tables.ECONOMIC_CONDITION,
									mEconomicConditionRB0.getText().toString());
							break;
						case R.id.economic_condition_rg1:
							cv.put(Tables.ECONOMIC_CONDITION,
									mEconomicConditionRB1.getText().toString());
							break;
						case R.id.economic_condition_rg2:
							cv.put(Tables.ECONOMIC_CONDITION,
									mEconomicConditionRB2.getText().toString());
							break;
						default:
							break;
						}
					}
				});

		// 把表格插入数据库
		saveTableBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cv.put(Tables.USER_ID, "123"); // ***************
												// need
												// to
												// change
												// ************
				cv.put(Tables.NAME, mNameET.getText().toString());
				cv.put(Tables.SERIAL_ID, mSerialIDTV.getText().toString()
						.trim());
				cv.put(Tables.GUARDIAN_NAME, mGuardianNameET.getText()
						.toString());
				cv.put(Tables.RELATION_WITH_GUARDIAN, mRelationWithGuardianET
						.getText().toString());
				cv.put(Tables.GUARDIAN_ADDRESS, mGuardianAddressET.getText()
						.toString());
				cv.put(Tables.GUARDIAN_TELEPHONE, mGuardianTelephoneET
						.getText().toString());
				cv.put(Tables.VILLAGE_COMMITTEE_LINKMAN,
						mVillageCommitteeLinkmanET.getText().toString());
				cv.put(Tables.VILLAGE_COMMITTEE_TELEPHONE,
						mVillageCommitteeTelephonET.getText().toString());

				// 知情同意与否在别处

				cv.put(Tables.INFORMED_CONSENT_SIGNATURE,
						mInformedConsentSignatureET.getText().toString());
				cv.put(Tables.INFORMED_CONSENT_SIGNATURE_DATE,
						informedConsentSignatureDateET.getText().toString());
				cv.put(Tables.INITIAL_ONSET_DATE, initialOnsetDate.getText()
						.toString());

				// 既往主要症状
				StringBuilder sb = new StringBuilder();
				if (mSymptomsOfPast0CB.isChecked())
					sb.append(mSymptomsOfPast0CB.getText().toString() + ";");
				if (mSymptomsOfPast1CB.isChecked())
					sb.append(mSymptomsOfPast1CB.getText().toString() + ";");
				if (mSymptomsOfPast2CB.isChecked())
					sb.append(mSymptomsOfPast2CB.getText().toString() + ";");
				if (mSymptomsOfPast3CB.isChecked())
					sb.append(mSymptomsOfPast3CB.getText().toString() + ";");
				if (mSymptomsOfPast4CB.isChecked())
					sb.append(mSymptomsOfPast4CB.getText().toString() + ";");
				if (mSymptomsOfPast5CB.isChecked())
					sb.append(mSymptomsOfPast5CB.getText().toString() + ";");
				if (mSymptomsOfPast6CB.isChecked())
					sb.append(mSymptomsOfPast6CB.getText().toString() + ";");
				if (mSymptomsOfPast7CB.isChecked())
					sb.append(mSymptomsOfPast7CB.getText().toString() + ";");
				if (mSymptomsOfPast8CB.isChecked())
					sb.append(mSymptomsOfPast8CB.getText().toString() + ";");
				if (mSymptomsOfPast9CB.isChecked())
					sb.append(mSymptomsOfPast9CB.getText().toString() + ";");
				if (mSymptomsOfPast10CB.isChecked())
					sb.append(mSymptomsOfPast10CB.getText().toString() + ";");
				if (mSymptomsOfPast11CB.isChecked())
					sb.append(mSymptomsOfPast11CB.getText().toString() + ":"
							+ mSymptomsOfPastOtherET.getText().toString() + ";");
				if (sb.length() > 1) {
					sb = sb.deleteCharAt(sb.length() - 1);
				}
				cv.put(Tables.SYMPTOMS_OF_PAST, sb.toString());

				// 门诊选项在别处

				cv.put(Tables.INITIAL_DRUG_TREATMENT_DATE,
						initialDrugTreatmentDateET.getText().toString());
				cv.put(Tables.HOSPITAL_NUM, mHospitalNumET.getText().toString());
				cv.put(Tables.CURRENT_DIAGNOSIS_RESULT,
						mCurrentDiagnosisResultET.getText().toString());
				cv.put(Tables.CURRENT_DIAGNOSIS_HOSPITAL,
						mCurrentDiagnosisHospitalET.getText().toString());
				cv.put(Tables.CURRENT_DIAGNOSIS_DATE, currentDiagnosisDateET
						.getText().toString());

				// 最近一次治疗效果在别处

				cv.put(Tables.INFO_MILD_DISTURBANCES_NUM,
						mInfoMildDisturbancesNumET.getText().toString());
				cv.put(Tables.INFO_MAKE_TROUBLE_NUM, mInfoMakeTroubleNumET
						.getText().toString());
				cv.put(Tables.INFO_MAKE_DISASTER_NUM, mInfoMakeDisasterNumET
						.getText().toString());
				cv.put(Tables.INFO_HURT_HIMSELF_NUM, mInfoHurtHimselfNumET
						.getText().toString());
				cv.put(Tables.INFO_ATTEMPTED_SUICIDE_NUM,
						mInfoAttemptedSuicideNumET.getText().toString());
				if (mInfoNoImpactOnfamilyAndSocietyCB.isChecked())
					cv.put(Tables.INFO_NO_IMPACT_ON_FAMILY_AND_SOCIETY, "1");
				else
					cv.put(Tables.INFO_NO_IMPACT_ON_FAMILY_AND_SOCIETY, "0");

				// 关锁情况在别处
				// 经济状况在别处

				cv.put(Tables.SPECIALIST_ADVICE, mSpecialistAdviceET.getText()
						.toString());
				cv.put(Tables.INFO_FILL_DATE, infoFillDateET.getText()
						.toString());
				cv.put(Tables.INFO_DOCTOR_SIGNATURE, mInfoDoctorSignatureET
						.getText().toString());

				// 查询表中是否已经存在该编号的entry，如果有则更新，若没有则插入
				cursor = db
						.rawQuery(
								"select * from infoSuppOfSevereMentalIllness "
										+ "where serial_id = ?",
								new String[] { mSerialIDTV.getText().toString()
										.trim() });
				if (cursor.moveToNext()) {
					if (db.update(table, cv, "serial_id = ?",
							new String[] { mSerialIDTV.getText().toString()
									.trim() }) == -1) {
						Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_LONG)
								.show();
					} else {
						Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_LONG)
								.show();
						Log.i("更新", cv.valueSet().toString());
					}
				} else {
					if (db.insert(table, null, cv) == -1)
						Toast.makeText(getActivity(), "插入失败", Toast.LENGTH_LONG)
								.show();
					else {
						Toast.makeText(getActivity(), "插入成功", Toast.LENGTH_LONG)
								.show();
						Log.i("插入", cv.valueSet().toString());
					}
				}
			}
		});
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", 4);
	}

	private void findView(View v) {
		mNameET = (EditText) v
				.findViewById(R.id.mental_illness_patient_name_info_et);
		mSerialIDTV = (TextView) v.findViewById(R.id.serial_id);
		mGuardianNameET = (EditText) v.findViewById(R.id.guardian_name_et);
		mRelationWithGuardianET = (EditText) v
				.findViewById(R.id.guardian_address_et);
		mGuardianTelephoneET = (EditText) v
				.findViewById(R.id.guardian_telephone_et);
		mGuardianAddressET = (EditText) v
				.findViewById(R.id.guardian_address_et);
		mVillageCommitteeLinkmanET = (EditText) v
				.findViewById(R.id.village_committee_linkman_et);
		mVillageCommitteeTelephonET = (EditText) v
				.findViewById(R.id.village_committee_telephone_et);
		mInformedConsentRG = (RadioGroup) v
				.findViewById(R.id.informed_consent_rg);
		mInformedConsentRB0 = (RadioButton) v
				.findViewById(R.id.informed_consent_rg0);
		mInformedConsentRB1 = (RadioButton) v
				.findViewById(R.id.informed_consent_rg1);
		mInformedConsentSignatureET = (EditText) v
				.findViewById(R.id.informed_consent_signature_et);
		informedConsentSignatureDateET = (DateEditText) v
				.findViewById(R.id.informed_consent_signature_date);
		initialOnsetDate = (DateEditText) v
				.findViewById(R.id.initial_onset_date);
		mSymptomsOfPast0CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_0_cb);
		mSymptomsOfPast1CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_1_cb);
		mSymptomsOfPast2CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_2_cb);
		mSymptomsOfPast3CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_3_cb);
		mSymptomsOfPast4CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_4_cb);
		mSymptomsOfPast5CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_5_cb);
		mSymptomsOfPast6CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_6_cb);
		mSymptomsOfPast7CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_7_cb);
		mSymptomsOfPast8CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_8_cb);
		mSymptomsOfPast9CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_9_cb);
		mSymptomsOfPast10CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_10_cb);
		mSymptomsOfPast11CB = (CheckBox) v
				.findViewById(R.id.symptoms_of_past_11_cb);
		mSymptomsOfPastOtherET = (EditText) v
				.findViewById(R.id.symptoms_of_past_other_et);
		mOutpatientRG = (RadioGroup) v.findViewById(R.id.outpatient_rg);
		mOutPatientRB0 = (RadioButton) v.findViewById(R.id.outpatient_rg0);
		mOutPatientRB1 = (RadioButton) v.findViewById(R.id.outpatient_rg1);
		mOutPatientRB2 = (RadioButton) v.findViewById(R.id.outpatient_rg2);
		initialDrugTreatmentDateET = (DateEditText) v
				.findViewById(R.id.initial_drug_treatment_date);
		mHospitalNumET = (EditText) v.findViewById(R.id.hospital_num_et);
		mCurrentDiagnosisResultET = (EditText) v
				.findViewById(R.id.current_diagnosis_result_et);
		mCurrentDiagnosisHospitalET = (EditText) v
				.findViewById(R.id.current_diagnosis_hospital_et);
		currentDiagnosisDateET = (DateEditText) v
				.findViewById(R.id.current_diagnosis_date);
		mLastTreatmentRG = (RadioGroup) v.findViewById(R.id.last_treatment_rg);
		mLastTreatmentRB0 = (RadioButton) v
				.findViewById(R.id.last_treatment_rg0);
		mLastTreatmentRB1 = (RadioButton) v
				.findViewById(R.id.last_treatment_rg1);
		mLastTreatmentRB2 = (RadioButton) v
				.findViewById(R.id.last_treatment_rg2);
		mLastTreatmentRB3 = (RadioButton) v
				.findViewById(R.id.last_treatment_rg3);
		mInfoMildDisturbancesNumET = (EditText) v
				.findViewById(R.id.info_mild_disturbances_num_et);
		mInfoMakeTroubleNumET = (EditText) v
				.findViewById(R.id.info_make_trouble_num_et);
		mInfoMakeDisasterNumET = (EditText) v
				.findViewById(R.id.info_make_disaster_num_et);
		mInfoHurtHimselfNumET = (EditText) v
				.findViewById(R.id.info_hurt_himself_num_et);
		mInfoAttemptedSuicideNumET = (EditText) v
				.findViewById(R.id.info_attempted_suicide_num_et);
		mInfoNoImpactOnfamilyAndSocietyCB = (CheckBox) v
				.findViewById(R.id.info_no_impact_on_family_and_society_cb);
		mInfoShutCaseRG = (RadioGroup) v.findViewById(R.id.info_shut_case_rg);
		mInfoShutCaseRB0 = (RadioButton) v
				.findViewById(R.id.info_shut_case_rg0);
		mInfoShutCaseRB1 = (RadioButton) v
				.findViewById(R.id.info_shut_case_rg1);
		mInfoShutCaseRB2 = (RadioButton) v
				.findViewById(R.id.info_shut_case_rg2);
		mEconomicConditionRG = (RadioGroup) v
				.findViewById(R.id.economic_condition_rg);
		mEconomicConditionRB0 = (RadioButton) v
				.findViewById(R.id.economic_condition_rg0);
		mEconomicConditionRB1 = (RadioButton) v
				.findViewById(R.id.economic_condition_rg1);
		mEconomicConditionRB2 = (RadioButton) v
				.findViewById(R.id.economic_condition_rg2);
		mSpecialistAdviceET = (EditText) v
				.findViewById(R.id.specialist_advice_et);
		infoFillDateET = (DateEditText) v.findViewById(R.id.info_fill_date);
		mInfoDoctorSignatureET = (EditText) v
				.findViewById(R.id.info_doctor_signature_et);
		saveTableBtn = (Button) v.findViewById(R.id.info_save_table_btn);

	}

	private void fillTable(Cursor cursor) {
		if (cursor.moveToNext()) {
			// column indices
			int nameCol = cursor.getColumnIndex(Tables.NAME);
			int serialIDCol = cursor.getColumnIndex(Tables.SERIAL_ID);
			int guardianNameCol = cursor.getColumnIndex(Tables.GUARDIAN_NAME);
			int relationWithGuardianCol = cursor
					.getColumnIndex(Tables.RELATION_WITH_GUARDIAN);
			int guardianAddressCol = cursor
					.getColumnIndex(Tables.GUARDIAN_ADDRESS);
			int guardianTelephoneCol = cursor
					.getColumnIndex(Tables.GUARDIAN_TELEPHONE);
			int villageCommitteeLinkmanCol = cursor
					.getColumnIndex(Tables.VILLAGE_COMMITTEE_LINKMAN);
			int villageCommitteeTelephonCol = cursor
					.getColumnIndex(Tables.VILLAGE_COMMITTEE_TELEPHONE);
			int informedConsentCol = cursor
					.getColumnIndex(Tables.INFORMED_CONSENT);
			int informedConsentSignatureCol = cursor
					.getColumnIndex(Tables.INFORMED_CONSENT_SIGNATURE);
			int informedConsentSignatureDateCol = cursor
					.getColumnIndex(Tables.INFORMED_CONSENT_SIGNATURE_DATE);
			int initialOnsetDateCol = cursor
					.getColumnIndex(Tables.INITIAL_ONSET_DATE);
			int symptomsOfPastCol = cursor
					.getColumnIndex(Tables.SYMPTOMS_OF_PAST);
			int outPatientCol = cursor.getColumnIndex(Tables.OUTPATIENT);
			int initialDrugTreatmentDateCol = cursor
					.getColumnIndex(Tables.INITIAL_DRUG_TREATMENT_DATE);
			int hospitalNumCol = cursor.getColumnIndex(Tables.HOSPITAL_NUM);
			int currentDiagnosisResultCol = cursor
					.getColumnIndex(Tables.CURRENT_DIAGNOSIS_RESULT);
			int currentDiagnosisHospitalCol = cursor
					.getColumnIndex(Tables.CURRENT_DIAGNOSIS_HOSPITAL);
			int currentDiagnosisDateCol = cursor
					.getColumnIndex(Tables.CURRENT_DIAGNOSIS_DATE);
			int lastTreatmentCol = cursor.getColumnIndex(Tables.LAST_TREATMENT);
			int infoMildDisturbancesNumCol = cursor
					.getColumnIndex(Tables.INFO_MILD_DISTURBANCES_NUM);
			int infoMakeTroubleNumCol = cursor
					.getColumnIndex(Tables.INFO_MAKE_TROUBLE_NUM);
			int infoMakeDisasterNumCol = cursor
					.getColumnIndex(Tables.INFO_MAKE_DISASTER_NUM);
			int infoHurtHimselfNumCol = cursor
					.getColumnIndex(Tables.INFO_HURT_HIMSELF_NUM);
			int infoAttemptedSuicideNumCol = cursor
					.getColumnIndex(Tables.INFO_ATTEMPTED_SUICIDE_NUM);
			int infoNoImpactOnfamilyAndSocietyCol = cursor
					.getColumnIndex(Tables.INFO_NO_IMPACT_ON_FAMILY_AND_SOCIETY);
			int infoShutCaseCol = cursor.getColumnIndex(Tables.INFO_SHUT_CASE);
			int economicConditionCol = cursor
					.getColumnIndex(Tables.ECONOMIC_CONDITION);
			int specialistAdviceCol = cursor
					.getColumnIndex(Tables.SPECIALIST_ADVICE);
			int infoFillDateCol = cursor.getColumnIndex(Tables.INFO_FILL_DATE);
			int infoDoctorSignatureCol = cursor
					.getColumnIndex(Tables.INFO_DOCTOR_SIGNATURE);

			if (nameCol != -1)
				mNameET.setText(cursor.getString(nameCol));
			if (serialIDCol != -1)
				mSerialIDTV.setText(cursor.getString(serialIDCol));
			if (guardianNameCol != -1)
				mGuardianNameET.setText(cursor.getString(guardianNameCol));
			if (relationWithGuardianCol != -1)
				mRelationWithGuardianET.setText(cursor
						.getString(relationWithGuardianCol));
			if (guardianAddressCol != -1)
				mGuardianAddressET
						.setText(cursor.getString(guardianAddressCol));
			if (guardianTelephoneCol != -1)
				mGuardianTelephoneET.setText(cursor
						.getString(guardianTelephoneCol));
			if (villageCommitteeLinkmanCol != -1)
				mVillageCommitteeLinkmanET.setText(cursor
						.getString(villageCommitteeLinkmanCol));
			if (villageCommitteeTelephonCol != -1)
				mVillageCommitteeTelephonET.setText(cursor
						.getString(villageCommitteeTelephonCol));
			if (informedConsentCol != -1
					&& cursor.getString(informedConsentCol) != null) {
				switch (cursor.getString(informedConsentCol).charAt(0) - '0') {
				case 0:
					mInformedConsentRB0.setChecked(true);
					break;
				case 1:
					mInformedConsentRB1.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (informedConsentSignatureCol != -1)
				mInformedConsentSignatureET.setText(cursor
						.getString(informedConsentSignatureCol));
			if (informedConsentSignatureDateCol != -1)
				informedConsentSignatureDateET.setText(cursor
						.getString(informedConsentSignatureDateCol));
			if (initialOnsetDateCol != -1)
				initialOnsetDate.setText(cursor.getString(initialOnsetDateCol));
			if (symptomsOfPastCol != -1
					&& cursor.getString(symptomsOfPastCol).length() > 0) {
				String[] symptoms = cursor.getString(symptomsOfPastCol).split(
						";");
				for (int i = 0; i < symptoms.length; ++i) {
					switch (Integer.valueOf(symptoms[i].substring(0, 2).trim())) {
					case 1:
						mSymptomsOfPast0CB.setChecked(true);
						break;
					case 2:
						mSymptomsOfPast1CB.setChecked(true);
						break;
					case 3:
						mSymptomsOfPast2CB.setChecked(true);
						break;
					case 4:
						mSymptomsOfPast3CB.setChecked(true);
						break;
					case 5:
						mSymptomsOfPast4CB.setChecked(true);
						break;
					case 6:
						mSymptomsOfPast5CB.setChecked(true);
						break;
					case 7:
						mSymptomsOfPast6CB.setChecked(true);
						break;
					case 8:
						mSymptomsOfPast7CB.setChecked(true);
						break;
					case 9:
						mSymptomsOfPast8CB.setChecked(true);
						break;
					case 10:
						mSymptomsOfPast9CB.setChecked(true);
						break;
					case 11:
						mSymptomsOfPast10CB.setChecked(true);
						break;
					case 12:
						mSymptomsOfPast11CB.setChecked(true);
						String[] otherArr = symptoms[i].split(":");
						if (otherArr.length > 1)
							mSymptomsOfPastOtherET.setText(symptoms[i]
									.substring(otherArr[0].length() + 1));
						break;
					default:
						break;
					}
				}
			}
			if (outPatientCol != -1 && cursor.getString(outPatientCol) != null) {
				switch (cursor.getString(outPatientCol).charAt(0) - '1') {
				case 0:
					mOutPatientRB0.setChecked(true);
					break;
				case 1:
					mOutPatientRB1.setChecked(true);
					break;
				case 2:
					mOutPatientRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (initialDrugTreatmentDateCol != -1)
				initialDrugTreatmentDateET.setText(cursor
						.getString(initialDrugTreatmentDateCol));
			if (hospitalNumCol != -1)
				mHospitalNumET.setText(cursor.getString(hospitalNumCol));
			if (currentDiagnosisResultCol != -1)
				mCurrentDiagnosisResultET.setText(cursor
						.getString(currentDiagnosisResultCol));
			if (currentDiagnosisHospitalCol != -1)
				mCurrentDiagnosisHospitalET.setText(cursor
						.getString(currentDiagnosisHospitalCol));
			if (currentDiagnosisDateCol != -1)
				currentDiagnosisDateET.setText(cursor
						.getString(currentDiagnosisDateCol));
			if (lastTreatmentCol != -1
					&& cursor.getString(lastTreatmentCol) != null) {
				switch (cursor.getString(lastTreatmentCol).trim().charAt(0) - '1') {
				case 0:
					mLastTreatmentRB0.setChecked(true);
					break;
				case 1:
					mLastTreatmentRB1.setChecked(true);
					break;
				case 2:
					mLastTreatmentRB2.setChecked(true);
					break;
				case 3:
					mLastTreatmentRB3.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (infoMildDisturbancesNumCol != -1)
				mInfoMildDisturbancesNumET.setText(cursor
						.getString(infoMildDisturbancesNumCol));
			if (infoMakeTroubleNumCol != -1)
				mInfoMakeTroubleNumET.setText(cursor
						.getString(infoMakeTroubleNumCol));
			if (infoMakeDisasterNumCol != -1)
				mInfoMakeDisasterNumET.setText(cursor
						.getString(infoMakeDisasterNumCol));
			if (infoHurtHimselfNumCol != -1)
				mInfoHurtHimselfNumET.setText(cursor
						.getString(infoHurtHimselfNumCol));
			if (infoAttemptedSuicideNumCol != -1)
				mInfoAttemptedSuicideNumET.setText(cursor
						.getString(infoAttemptedSuicideNumCol));
			if (infoNoImpactOnfamilyAndSocietyCol != -1
					&& cursor.getString(infoNoImpactOnfamilyAndSocietyCol)
							.length() > 0) {
				if (cursor.getString(infoNoImpactOnfamilyAndSocietyCol).charAt(
						0) == '1') {
					mInfoNoImpactOnfamilyAndSocietyCB.setChecked(true);
				}
			}
			if (infoShutCaseCol != -1
					&& cursor.getString(infoShutCaseCol) != null) {
				switch (cursor.getString(infoShutCaseCol).charAt(0) - '1') {
				case 0:
					mInfoShutCaseRB0.setChecked(true);
					break;
				case 1:
					mInfoShutCaseRB1.setChecked(true);
					break;
				case 2:
					mInfoShutCaseRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (economicConditionCol != -1
					&& cursor.getString(economicConditionCol) != null) {
				switch (cursor.getString(economicConditionCol).charAt(0) - '1') {
				case 0:
					mEconomicConditionRB0.setChecked(true);
					break;
				case 1:
					mEconomicConditionRB1.setChecked(true);
					break;
				case 2:
					mEconomicConditionRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (specialistAdviceCol != -1)
				mSpecialistAdviceET.setText(cursor
						.getString(specialistAdviceCol));
			if (infoFillDateCol != -1)
				infoFillDateET.setText(cursor.getString(infoFillDateCol));
			if (infoDoctorSignatureCol != -1)
				mInfoDoctorSignatureET.setText(cursor
						.getString(infoDoctorSignatureCol));
		}
	}
}