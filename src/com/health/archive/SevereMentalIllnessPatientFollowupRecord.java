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

public class SevereMentalIllnessPatientFollowupRecord extends Fragment {
	private EditText mNameET;	// 姓名
	private TextView mSerialIdTV;
	private DateEditText followupDateET;	// 随访日期
	private RadioGroup mRiskRG;	// 危险性
	private RadioButton mRiskRB0;
	private RadioButton mRiskRB1;
	private RadioButton mRiskRB2;
	private RadioButton mRiskRB3;
	private RadioButton mRiskRB4;
	private RadioButton mRiskRB5;
	
	// 目前症状
	private CheckBox mCurrentSymptom0CB;
	private CheckBox mCurrentSymptom1CB;
	private CheckBox mCurrentSymptom2CB;
	private CheckBox mCurrentSymptom3CB;
	private CheckBox mCurrentSymptom4CB;
	private CheckBox mCurrentSymptom5CB;
	private CheckBox mCurrentSymptom6CB;
	private CheckBox mCurrentSymptom7CB;
	private CheckBox mCurrentSymptom8CB;
	private CheckBox mCurrentSymptom9CB;
	private CheckBox mCurrentSymptom10CB;
	private CheckBox mCurrentSymptom11CB;
	private EditText mCurrentSymptomOtherET;
	
	private RadioGroup mInsightRG;	// 自知力
	private RadioButton mInsightRB0;
	private RadioButton mInsightRB1;
	private RadioButton mInsightRB2;	
	private RadioGroup mSleepSituationRG;	// 睡眠情况
	private RadioButton mSleepSituationRB0;
	private RadioButton mSleepSituationRB1;
	private RadioButton mSleepSituationRB2;	
	private RadioGroup mDietSituationRG;	// 饮食情况
	private RadioButton mDietSituationRB0;
	private RadioButton mDietSituationRB1;
	private RadioButton mDietSituationRB2;
	
	// 社会功能情况 
	private RadioGroup mSelfcareRG;
	private RadioButton mSelfcareRB0;
	private RadioButton mSelfcareRB1;
	private RadioButton mSelfcareRB2;	
	private RadioGroup mHourseworkRG;
	private RadioButton mHourseworkRB0;
	private RadioButton mHourseworkRB1;
	private RadioButton mHourseworkRB2;
	private RadioGroup mProductiveWorkRG;
	private RadioButton mProductiveWorkRB0;
	private RadioButton mProductiveWorkRB1;
	private RadioButton mProductiveWorkRB2;
	private RadioButton mProductiveWorkRB3;	
	private RadioGroup mLearningAbilityRG;
	private RadioButton mLearningAbilityRB0;
	private RadioButton mLearningAbilityRB1;
	private RadioButton mLearningAbilityRB2;	
	private RadioGroup mInterpersonalRG;
	private RadioButton mInterpersonalRB0;
	private RadioButton mInterpersonalRB1;
	private RadioButton mInterpersonalRB2;	
	
	// 患病对家庭社会的影响 
	private EditText mFollowupMildDisturbancesNumET;
	private EditText mFollowupMakeTroubleNumET;
	private EditText mFollowupMakeDisasterNumET;
	private EditText mFollowupHurtHimselfNumET;
	private EditText mFollowupAttemptedSuicideNumET;
	private CheckBox mFollowupNoImpactOnfamilyAndSocietyCB;
	
	private RadioGroup mFollowupShutCaseRG;	// 关锁情况
	private RadioButton mFollowupShutCaseRB0;
	private RadioButton mFollowupShutCaseRB1;
	private RadioButton mFollowupShutCaseRB2;	
	private RadioGroup mFollowupHospitalizationRG;	// 住院情况
	private RadioButton mFollowupHospitalizationRB0;
	private RadioButton mFollowupHospitalizationRB1;
	private RadioButton mFollowupHospitalizationRB2;	
	private DateEditText lastDateToDischarge;	// 末次出院时间
	
	// 实验室检查
	private RadioGroup mLabTestRG;
	private RadioButton mLabTestRB0;
	private RadioButton mLabTestRB1;
	private EditText mLabTestContentET;
	
	private RadioGroup mMedicationComplianceRG;	// 服药依从性
	private RadioButton mMedicationComplianceRB0;
	private RadioButton mMedicationComplianceRB1;
	private RadioButton mMedicationComplianceRB2;	
	private RadioGroup mDrugAdverseReactionRG;	// 药物不良反应
	private RadioButton mDrugAdverseReactionRB0;
	private RadioButton mDrugAdverseReactionRB1;
	private EditText mDrugAdverseReactionContentET;
	private RadioGroup mTreatmentEffectRG;	// 治疗效果
	private RadioButton mTreatmentEffectRB0;
	private RadioButton mTreatmentEffectRB1;
	private RadioButton mTreatmentEffectRB2;
	private RadioButton mTreatmentEffectRB3;	
	private RadioGroup mWhetherToReferralRG;	// 是否转诊
	private RadioButton mWhetherToReferralRB0;
	private RadioButton mWhetherToReferralRB1;
	private EditText mReferralReasonET;	// 转诊原因
	private EditText mWhereToReferralET;	// 转诊至机构及科室
	
	// 用药情况
	private EditText mDrug0ET;
	private EditText mDrugUsage0ET;
	private EditText mDose0ET;
	private EditText mDrug1ET;
	private EditText mDrugUsage1ET;
	private EditText mDose1ET;
	private EditText mDrug2ET;
	private EditText mDrugUsage2ET;
	private EditText mDose2ET;
	
	// 康复措施
	private CheckBox mRehabilitationMeasureCB0;
	private CheckBox mRehabilitationMeasureCB1;
	private CheckBox mRehabilitationMeasureCB2;
	private CheckBox mRehabilitationMeasureCB3;
	private CheckBox mRehabilitationMeasureCB4;
	private EditText mRehabilitationMeasureOtherET;
	
	private RadioGroup mThisFollowupClassificationRG;	// 本次随访分类
	private RadioButton mThisFollowupClassificationRB0;
	private RadioButton mThisFollowupClassificationRB1;
	private RadioButton mThisFollowupClassificationRB2;
	private RadioButton mThisFollowupClassificationRB3;	
	private DateEditText nextFollowupDateET;	// 下次随访日期
	private EditText mFollowupDoctorSignatureET;	// 随访医生签名	
	
	// 
	private ContentValues cv = new ContentValues();
	private SQLiteDatabase db;
	private Cursor cursor;
	private String table = "followupRecordOfSevereMentalIllness";
	private String serialID;
	private Button mSaveTableBtn;

	public SevereMentalIllnessPatientFollowupRecord() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		View v = inflater.inflate(
				R.layout.table_followup_record_of_patient_with_severe_mental_illness, null);
		findView(v);
		db = new DataOpenHelper(getActivity()).getWritableDatabase();
		cursor = db.rawQuery("select * from followupRecordOfSevereMentalIllness " +
				"where user_id = ?", new String[] {"123"});
		fillTable(cursor);
		
		// 危险性
		mRiskRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.followup_risk_rg0:
					cv.put(Tables.RISK, mRiskRB0.getText().toString());
					break;
				case R.id.followup_risk_rg1:
					cv.put(Tables.RISK, mRiskRB1.getText().toString());
					break;
				case R.id.followup_risk_rg2:
					cv.put(Tables.RISK, mRiskRB2.getText().toString());
					break;
				case R.id.followup_risk_rg3:
					cv.put(Tables.RISK, mRiskRB3.getText().toString());
					break;
				case R.id.followup_risk_rg4:
					cv.put(Tables.RISK, mRiskRB4.getText().toString());
					break;
				case R.id.followup_risk_rg5:
					cv.put(Tables.RISK, mRiskRB5.getText().toString());
					break;				
				default:
					break;
				}
			}
		});
		
		// 自知力
		mInsightRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.insight_rg0:
					cv.put(Tables.INSIGHT, mInsightRB0.getText().toString());
					break;
				case R.id.insight_rg1:
					cv.put(Tables.INSIGHT, mInsightRB1.getText().toString());
					break;
				case R.id.insight_rg2:
					cv.put(Tables.INSIGHT, mInsightRB2.getText().toString());
					break;			
				default:
					break;
				}				
			}
		});
		
		// 睡眠情况
		mSleepSituationRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.sleep_situation_rg0:
					cv.put(Tables.SLEEP_SITUATION, mSleepSituationRB0.getText().toString());
					break;
				case R.id.sleep_situation_rg1:
					cv.put(Tables.SLEEP_SITUATION, mSleepSituationRB1.getText().toString());
					break;
				case R.id.sleep_situation_rg2:
					cv.put(Tables.SLEEP_SITUATION, mSleepSituationRB2.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 饮食情况
		mDietSituationRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.diet_situation_rg0:
					cv.put(Tables.DIET_SITUATION, mDietSituationRB0.getText().toString());
					break;
				case R.id.diet_situation_rg1:
					cv.put(Tables.DIET_SITUATION, mDietSituationRB1.getText().toString());
					break;
				case R.id.diet_situation_rg2:
					cv.put(Tables.DIET_SITUATION, mDietSituationRB2.getText().toString());
					break;
				default:
					break;
				}				
			}
		});
		
		// 个人生活料理
		mSelfcareRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.selfcare_rg0:
					cv.put(Tables.SELF_CARE, mSelfcareRB0.getText().toString());
					break;
				case R.id.selfcare_rg1:
					cv.put(Tables.SELF_CARE, mSelfcareRB1.getText().toString());
					break;
				case R.id.selfcare_rg2:
					cv.put(Tables.SELF_CARE, mSelfcareRB2.getText().toString());
					break;				

				default:
					break;
				}
			}
		});
		
		// 家务劳动
		mHourseworkRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.hoursework_rg0:
					cv.put(Tables.HOURSE_WORK, mHourseworkRB0.getText().toString());
					break;
				case R.id.hoursework_rg1:
					cv.put(Tables.HOURSE_WORK, mHourseworkRB1.getText().toString());
					break;
				case R.id.hoursework_rg2:
					cv.put(Tables.HOURSE_WORK, mHourseworkRB2.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 生产劳动及工作
		mProductiveWorkRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.productive_work_rg0:
					cv.put(Tables.PRODUCTIVE_WORK, mProductiveWorkRB0.getText().toString());
					break;
				case R.id.productive_work_rg1:
					cv.put(Tables.PRODUCTIVE_WORK, mProductiveWorkRB1.getText().toString());
					break;
				case R.id.productive_work_rg2:
					cv.put(Tables.PRODUCTIVE_WORK, mProductiveWorkRB2.getText().toString());
					break;
				case R.id.productive_work_rg3:
					cv.put(Tables.PRODUCTIVE_WORK, mProductiveWorkRB3.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 学习能力
		mLearningAbilityRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.learning_ability_rg0:
					cv.put(Tables.LEARNING_ABILITY, mLearningAbilityRB0.getText().toString());
					break;
				case R.id.learning_ability_rg1:
					cv.put(Tables.LEARNING_ABILITY, mLearningAbilityRB1.getText().toString());
					break;
				case R.id.learning_ability_rg2:
					cv.put(Tables.LEARNING_ABILITY, mLearningAbilityRB2.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 社会人际交往
		mInterpersonalRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.interpersonal_rg0:
					cv.put(Tables.INTERPERSONAL, mInterpersonalRB0.getText().toString());
					break;
				case R.id.interpersonal_rg1:
					cv.put(Tables.INTERPERSONAL, mInterpersonalRB1.getText().toString());
					break;
				case R.id.interpersonal_rg2:
					cv.put(Tables.INTERPERSONAL, mInterpersonalRB2.getText().toString());
					break;				
				default:
					break;
				}	
			}
		});
		
		// 关锁情况
		mFollowupShutCaseRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.followup_shut_case_rg0:
					cv.put(Tables.FOLLOWUP_SHUT_CASE, mFollowupShutCaseRB0.getText().toString());
					break;
				case R.id.followup_shut_case_rg1:
					cv.put(Tables.FOLLOWUP_SHUT_CASE, mFollowupShutCaseRB1.getText().toString());
					break;
				case R.id.followup_shut_case_rg2:
					cv.put(Tables.FOLLOWUP_SHUT_CASE, mFollowupShutCaseRB2.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 住院情况
		mFollowupHospitalizationRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.followup_hospitalization_rg0:
					cv.put(Tables.FOLLOWUP_HOSPITALIZATION, mFollowupHospitalizationRB0.getText().toString());
					break;
				case R.id.followup_hospitalization_rg1:
					cv.put(Tables.FOLLOWUP_HOSPITALIZATION, mFollowupHospitalizationRB1.getText().toString());
					break;
				case R.id.followup_hospitalization_rg2:
					cv.put(Tables.FOLLOWUP_HOSPITALIZATION, mFollowupHospitalizationRB2.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 实验室检查
		mLabTestRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.lab_test_rg0:
					cv.put(Tables.LAB_TEST, mLabTestRB0.getText().toString());
					break;
				case R.id.lab_test_rg1:
					cv.put(Tables.LAB_TEST, mLabTestRB1.getText().toString() + ":" + mLabTestContentET.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 服药依从性 
		mMedicationComplianceRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.medication_compliance_rg0:
					cv.put(Tables.MEDICATION_COMPLIANCE, mMedicationComplianceRB0.getText().toString());
					break;
				case R.id.medication_compliance_rg1:
					cv.put(Tables.MEDICATION_COMPLIANCE, mMedicationComplianceRB1.getText().toString());
					break;
				case R.id.medication_compliance_rg2:
					cv.put(Tables.MEDICATION_COMPLIANCE, mMedicationComplianceRB2.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 药物不良反应
		mDrugAdverseReactionRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.drug_adverse_reaction_rg0:
					cv.put(Tables.DRUG_ADVERSE_REACTION, mDrugAdverseReactionRB0.getText().toString());
					break;
				case R.id.drug_adverse_reaction_rg1:
					cv.put(Tables.DRUG_ADVERSE_REACTION, mDrugAdverseReactionRB1.getText().toString() + ":" + mDrugAdverseReactionContentET.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 治疗效果
		mTreatmentEffectRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.treatment_effect_rg0:
					cv.put(Tables.TREATMENT_EFFECT, mTreatmentEffectRB0.getText().toString());
					break;
				case R.id.treatment_effect_rg1:
					cv.put(Tables.TREATMENT_EFFECT, mTreatmentEffectRB1.getText().toString());
					break;
				case R.id.treatment_effect_rg2:
					cv.put(Tables.TREATMENT_EFFECT, mTreatmentEffectRB2.getText().toString());
					break;
				case R.id.treatment_effect_rg3:
					cv.put(Tables.TREATMENT_EFFECT, mTreatmentEffectRB3.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 是否转诊
		mWhetherToReferralRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.whether_to_referral_rg0:
					cv.put(Tables.WHETHER_TO_REFERRAL, mWhetherToReferralRB0.getText().toString());
					break;
				case R.id.whether_to_referral_rg1:
					cv.put(Tables.WHETHER_TO_REFERRAL, mWhetherToReferralRB1.getText().toString());
					cv.put(Tables.REFERRAL_REASON, mReferralReasonET.getText().toString());
					cv.put(Tables.WHERE_TO_REFERRAL, mWhereToReferralET.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		// 本次随访分类
		mThisFollowupClassificationRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.this_followup_classification_rg0:
					cv.put(Tables.THIS_FOLLOWUP_CLASSIFICATION, mThisFollowupClassificationRB0.getText().toString());
					break;
				case R.id.this_followup_classification_rg1:
					cv.put(Tables.THIS_FOLLOWUP_CLASSIFICATION, mThisFollowupClassificationRB1.getText().toString());
					break;
				case R.id.this_followup_classification_rg2:
					cv.put(Tables.THIS_FOLLOWUP_CLASSIFICATION, mThisFollowupClassificationRB2.getText().toString());
					break;
				case R.id.this_followup_classification_rg3:
					cv.put(Tables.THIS_FOLLOWUP_CLASSIFICATION, mThisFollowupClassificationRB3.getText().toString());
					break;
				default:
					break;
				}
			}
		});
		
		mSaveTableBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				cv.put(Tables.USER_ID, "123"); // ************ need to modify ***********
				cv.put(Tables.NAME, mNameET.getText().toString());
				cv.put(Tables.SERIAL_ID, mSerialIdTV.getText().toString().trim());
				cv.put(Tables.FOLLOWUP_DATE, followupDateET.getText().toString());

				// 危险性在别处
				
				// 目前症状
				StringBuilder sb = new StringBuilder();
				if (mCurrentSymptom0CB.isChecked()) 
					sb.append(mCurrentSymptom0CB.getText().toString() + ';');
				if (mCurrentSymptom1CB.isChecked()) 
					sb.append(mCurrentSymptom1CB.getText().toString() + ';');
				if (mCurrentSymptom2CB.isChecked()) 
					sb.append(mCurrentSymptom2CB.getText().toString() + ';');
				if (mCurrentSymptom3CB.isChecked()) 
					sb.append(mCurrentSymptom3CB.getText().toString() + ';');
				if (mCurrentSymptom4CB.isChecked()) 
					sb.append(mCurrentSymptom4CB.getText().toString() + ';');
				if (mCurrentSymptom5CB.isChecked()) 
					sb.append(mCurrentSymptom5CB.getText().toString() + ';');
				if (mCurrentSymptom6CB.isChecked()) 
					sb.append(mCurrentSymptom6CB.getText().toString() + ';');
				if (mCurrentSymptom7CB.isChecked()) 
					sb.append(mCurrentSymptom7CB.getText().toString() + ';');
				if (mCurrentSymptom8CB.isChecked()) 
					sb.append(mCurrentSymptom8CB.getText().toString() + ';');
				if (mCurrentSymptom9CB.isChecked()) 
					sb.append(mCurrentSymptom9CB.getText().toString() + ';');
				if (mCurrentSymptom10CB.isChecked()) 
					sb.append(mCurrentSymptom10CB.getText().toString() + ';');
				if (mCurrentSymptom11CB.isChecked()) 
					sb.append(mCurrentSymptom11CB.getText().toString() + ':' + mCurrentSymptomOtherET.getText().toString() + ';');
				if (sb.length() >= 1)
					sb.deleteCharAt(sb.length() - 1); // remove last ';'
				cv.put(Tables.CURRENT_SYMPTOM, sb.toString());
				
				// 自知力在别处
				// 睡眠情况在别处
				// 饮食情况在别处
				// 个人生活料理在别处
				// 家务劳动在别处
				// 生产劳动及工作在别处
				// 学习能力在别处
				// 社会人际交往在别处
				
				cv.put(Tables.REFERRAL_REASON, mReferralReasonET.getText().toString());
				cv.put(Tables.WHERE_TO_REFERRAL, mWhereToReferralET.getText().toString());
				
				cv.put(Tables.FOLLOWUP_MILD_DISTURBANCES_NUM, mFollowupMildDisturbancesNumET.getText().toString());
				cv.put(Tables.FOLLOWUP_MAKE_TROUBLE_NUM, mFollowupMakeTroubleNumET.getText().toString());
				cv.put(Tables.FOLLOWUP_MAKE_DISASTER_NUM, mFollowupMakeDisasterNumET.getText().toString());
				cv.put(Tables.FOLLOWUP_HURT_HIMSELF_NUM, mFollowupHurtHimselfNumET.getText().toString());
				cv.put(Tables.FOLLOWUP_ATTEMPTED_SUICIDE_NUM, mFollowupAttemptedSuicideNumET.getText().toString());
				if (mFollowupNoImpactOnfamilyAndSocietyCB.isChecked())
					cv.put(Tables.FOLLOWUP_NO_IMPACT_ON_FAMILY_AND_SOCIETY, "1");
				else cv.put(Tables.FOLLOWUP_NO_IMPACT_ON_FAMILY_AND_SOCIETY, "0");
				
				// 关锁情况在别处
				// 住院情况在别处
				
				cv.put(Tables.LAST_DATE_TO_DISCHARGE, lastDateToDischarge.getText().toString());
				
				// 实验室检查在别处
				// 服药依从性在别处
				// 药物不良反应在别处
				// 治疗效果在别处
				// 是否转诊在别处
				
				cv.put(Tables.DRUG_0, mDrug0ET.getText().toString());
				cv.put(Tables.DRUG_USAGE_0, mDrugUsage0ET.getText().toString());
				cv.put(Tables.DOSE_0, mDose0ET.getText().toString());
				cv.put(Tables.DRUG_1, mDrug1ET.getText().toString());
				cv.put(Tables.DRUG_USAGE_1, mDrugUsage1ET.getText().toString());
				cv.put(Tables.DOSE_1, mDose1ET.getText().toString());
				cv.put(Tables.DRUG_2, mDrug2ET.getText().toString());
				cv.put(Tables.DRUG_USAGE_2, mDrugUsage2ET.getText().toString());
				cv.put(Tables.DOSE_2, mDose2ET.getText().toString());
				
				StringBuilder rehaSB = new StringBuilder();
				if (mRehabilitationMeasureCB0.isChecked())
					rehaSB.append(mRehabilitationMeasureCB0.getText().toString() + ";");
				if (mRehabilitationMeasureCB1.isChecked())
					rehaSB.append(mRehabilitationMeasureCB1.getText().toString() + ";");
				if (mRehabilitationMeasureCB2.isChecked())
					rehaSB.append(mRehabilitationMeasureCB2.getText().toString() + ";");
				if (mRehabilitationMeasureCB3.isChecked())
					rehaSB.append(mRehabilitationMeasureCB3.getText().toString() + ";");
				if (mRehabilitationMeasureCB4.isChecked())
					rehaSB.append(mRehabilitationMeasureCB4.getText().toString() + ":" + mRehabilitationMeasureOtherET.getText().toString() +  ";");
				if (rehaSB.length() >= 1)
					rehaSB.deleteCharAt(rehaSB.length() - 1); // delete the last ';'
				cv.put(Tables.REHABILITATION_MEASURE, rehaSB.toString());
				
				// 本次随访分类在别处
				
				cv.put(Tables.NEXT_FOLLOWUP_DATE, nextFollowupDateET.getText().toString());
				cv.put(Tables.FOLLOWUP_DOCTOR_SIGNATURE, mFollowupDoctorSignatureET.getText().toString());
				
				// 查看表中是否存在该entry，如有则更新，若没有则插入
				cursor = db.rawQuery("select * from followupRecordOfSevereMentalIllness " +
						"where serial_id = ?", new String[] {mSerialIdTV.getText().toString().trim()});
				if (cursor.moveToNext()) {
					if (db.update(table, cv, "serial_id = ?", 
							new String[] {mSerialIdTV.getText().toString().trim()}) == -1)
						Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_LONG).show();
					else {
						Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_LONG).show();
						Log.i("插入", cv.valueSet().toString());
					}
				} else {
					if (db.insert(table, null, cv) == -1)
						Toast.makeText(getActivity(), "插入失败", Toast.LENGTH_LONG).show();
					else {
						Toast.makeText(getActivity(), "插入成功", Toast.LENGTH_LONG).show();
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
		mNameET = (EditText) v.findViewById(R.id.mental_illness_patient_name_followup_et);
		mSerialIdTV = (TextView) v.findViewById(R.id.followup_serial_id);
		followupDateET = (DateEditText) v.findViewById(R.id.followup_date);
		mRiskRG = (RadioGroup) v.findViewById(R.id.followup_risk_rg);
		mRiskRB0 = (RadioButton) v.findViewById(R.id.followup_risk_rg0);
		mRiskRB1 = (RadioButton) v.findViewById(R.id.followup_risk_rg1);
		mRiskRB2 = (RadioButton) v.findViewById(R.id.followup_risk_rg2);
		mRiskRB3 = (RadioButton) v.findViewById(R.id.followup_risk_rg3);
		mRiskRB4 = (RadioButton) v.findViewById(R.id.followup_risk_rg4);
		mRiskRB5 = (RadioButton) v.findViewById(R.id.followup_risk_rg5);		
		mCurrentSymptom0CB = (CheckBox) v.findViewById(R.id.current_symptom_0_cb);
		mCurrentSymptom1CB = (CheckBox) v.findViewById(R.id.current_symptom_1_cb);
		mCurrentSymptom2CB = (CheckBox) v.findViewById(R.id.current_symptom_2_cb);
		mCurrentSymptom3CB = (CheckBox) v.findViewById(R.id.current_symptom_3_cb);
		mCurrentSymptom4CB = (CheckBox) v.findViewById(R.id.current_symptom_4_cb);
		mCurrentSymptom5CB = (CheckBox) v.findViewById(R.id.current_symptom_5_cb);
		mCurrentSymptom6CB = (CheckBox) v.findViewById(R.id.current_symptom_6_cb);
		mCurrentSymptom7CB = (CheckBox) v.findViewById(R.id.current_symptom_7_cb);
		mCurrentSymptom8CB = (CheckBox) v.findViewById(R.id.current_symptom_8_cb);
		mCurrentSymptom9CB = (CheckBox) v.findViewById(R.id.current_symptom_9_cb);
		mCurrentSymptom10CB = (CheckBox) v.findViewById(R.id.current_symptom_10_cb);
		mCurrentSymptom11CB = (CheckBox) v.findViewById(R.id.current_symptom_11_cb);
		mCurrentSymptomOtherET = (EditText) v.findViewById(R.id.current_symptom_other_et);
		mInsightRG = (RadioGroup) v.findViewById(R.id.insight_rg);
		mInsightRB0 = (RadioButton) v.findViewById(R.id.insight_rg0);
		mInsightRB1 = (RadioButton) v.findViewById(R.id.insight_rg1);
		mInsightRB2 = (RadioButton) v.findViewById(R.id.insight_rg2);		
		mSleepSituationRG = (RadioGroup) v.findViewById(R.id.sleep_situation_rg);
		mSleepSituationRB0 = (RadioButton) v.findViewById(R.id.sleep_situation_rg0);
		mSleepSituationRB1 = (RadioButton) v.findViewById(R.id.sleep_situation_rg1);
		mSleepSituationRB2 = (RadioButton) v.findViewById(R.id.sleep_situation_rg2);		
		mDietSituationRG = (RadioGroup) v.findViewById(R.id.diet_situation_rg);
		mDietSituationRB0 = (RadioButton) v.findViewById(R.id.diet_situation_rg0);
		mDietSituationRB1 = (RadioButton) v.findViewById(R.id.diet_situation_rg1);
		mDietSituationRB2 = (RadioButton) v.findViewById(R.id.diet_situation_rg2);		
		mSelfcareRG = (RadioGroup) v.findViewById(R.id.selfcare_rg);
		mSelfcareRB0 = (RadioButton) v.findViewById(R.id.selfcare_rg0);
		mSelfcareRB1 = (RadioButton) v.findViewById(R.id.selfcare_rg1);
		mSelfcareRB2 = (RadioButton) v.findViewById(R.id.selfcare_rg2);		
		mHourseworkRG = (RadioGroup) v.findViewById(R.id.hoursework_rg);
		mHourseworkRB0 = (RadioButton) v.findViewById(R.id.hoursework_rg0);
		mHourseworkRB1 = (RadioButton) v.findViewById(R.id.hoursework_rg1);
		mHourseworkRB2 = (RadioButton) v.findViewById(R.id.hoursework_rg2);		
		mProductiveWorkRG = (RadioGroup) v.findViewById(R.id.productive_work_rg);
		mProductiveWorkRB0 = (RadioButton) v.findViewById(R.id.productive_work_rg0);
		mProductiveWorkRB1 = (RadioButton) v.findViewById(R.id.productive_work_rg1);
		mProductiveWorkRB2 = (RadioButton) v.findViewById(R.id.productive_work_rg2);
		mProductiveWorkRB3 = (RadioButton) v.findViewById(R.id.productive_work_rg3);
		mLearningAbilityRG = (RadioGroup) v.findViewById(R.id.learning_ability_rg);
		mLearningAbilityRB0 = (RadioButton) v.findViewById(R.id.learning_ability_rg0);
		mLearningAbilityRB1 = (RadioButton) v.findViewById(R.id.learning_ability_rg1);
		mLearningAbilityRB2 = (RadioButton) v.findViewById(R.id.learning_ability_rg2);		
		mInterpersonalRG = (RadioGroup) v.findViewById(R.id.interpersonal_rg);
		mInterpersonalRB0 = (RadioButton) v.findViewById(R.id.interpersonal_rg0);
		mInterpersonalRB1 = (RadioButton) v.findViewById(R.id.interpersonal_rg1);
		mInterpersonalRB2 = (RadioButton) v.findViewById(R.id.interpersonal_rg2);		
		mFollowupMildDisturbancesNumET = (EditText) v.findViewById(R.id.followup_mild_disturbances_num_et);
		mFollowupMakeTroubleNumET = (EditText) v.findViewById(R.id.followup_make_trouble_num_et);
		mFollowupMakeDisasterNumET = (EditText) v.findViewById(R.id.followup_make_disaster_num_et);
		mFollowupHurtHimselfNumET = (EditText) v.findViewById(R.id.followup_hurt_himself_num_et);
		mFollowupAttemptedSuicideNumET = (EditText) v.findViewById(R.id.followup_attempted_suicide_num_et);
		mFollowupNoImpactOnfamilyAndSocietyCB = (CheckBox) v.findViewById(R.id.followup_no_impact_on_family_and_society_cb);
		mFollowupShutCaseRG = (RadioGroup) v.findViewById(R.id.followup_shut_case_rg);
		mFollowupShutCaseRB0 = (RadioButton) v.findViewById(R.id.followup_shut_case_rg0);
		mFollowupShutCaseRB1 = (RadioButton) v.findViewById(R.id.followup_shut_case_rg1);
		mFollowupShutCaseRB2 = (RadioButton) v.findViewById(R.id.followup_shut_case_rg2);		
		mFollowupHospitalizationRG = (RadioGroup) v.findViewById(R.id.followup_hospitalization_rg);
		mFollowupHospitalizationRB0 = (RadioButton) v.findViewById(R.id.followup_hospitalization_rg0);
		mFollowupHospitalizationRB1 = (RadioButton) v.findViewById(R.id.followup_hospitalization_rg1);
		mFollowupHospitalizationRB2 = (RadioButton) v.findViewById(R.id.followup_hospitalization_rg2);		
		lastDateToDischarge = (DateEditText) v.findViewById(R.id.last_time_to_discharge);
		mLabTestRG = (RadioGroup) v.findViewById(R.id.lab_test_rg);
		mLabTestRB0 = (RadioButton) v.findViewById(R.id.lab_test_rg0);
		mLabTestRB1 = (RadioButton) v.findViewById(R.id.lab_test_rg1);		
		mLabTestContentET = (EditText) v.findViewById(R.id.lab_test_content_et);
		mMedicationComplianceRG = (RadioGroup) v.findViewById(R.id.medication_compliance_rg);
		mMedicationComplianceRB0 = (RadioButton) v.findViewById(R.id.medication_compliance_rg0);
		mMedicationComplianceRB1 = (RadioButton) v.findViewById(R.id.medication_compliance_rg1);
		mMedicationComplianceRB2 = (RadioButton) v.findViewById(R.id.medication_compliance_rg2);		
		mDrugAdverseReactionRG = (RadioGroup) v.findViewById(R.id.drug_adverse_reaction_rg);
		mDrugAdverseReactionRB0 = (RadioButton) v.findViewById(R.id.drug_adverse_reaction_rg0);
		mDrugAdverseReactionRB1 = (RadioButton) v.findViewById(R.id.drug_adverse_reaction_rg1);		
		mDrugAdverseReactionContentET = (EditText) v.findViewById(R.id.drug_adverse_reaction_content_et);
		mTreatmentEffectRG = (RadioGroup) v.findViewById(R.id.treatment_effect_rg);
		mTreatmentEffectRB0 = (RadioButton) v.findViewById(R.id.treatment_effect_rg0);
		mTreatmentEffectRB1 = (RadioButton) v.findViewById(R.id.treatment_effect_rg1);
		mTreatmentEffectRB2 = (RadioButton) v.findViewById(R.id.treatment_effect_rg2);
		mTreatmentEffectRB3 = (RadioButton) v.findViewById(R.id.treatment_effect_rg3);		
		mWhetherToReferralRG = (RadioGroup) v.findViewById(R.id.whether_to_referral_rg);
		mWhetherToReferralRB0 = (RadioButton) v.findViewById(R.id.whether_to_referral_rg0);
		mWhetherToReferralRB1 = (RadioButton) v.findViewById(R.id.whether_to_referral_rg1);
		mReferralReasonET = (EditText) v.findViewById(R.id.referral_reason_et);
		mWhereToReferralET = (EditText) v.findViewById(R.id.where_to_referral_et);
		mDrug0ET = (EditText) v.findViewById(R.id.drug_0_et);
		mDrugUsage0ET = (EditText) v.findViewById(R.id.drug_usage_0_et);
		mDose0ET = (EditText) v.findViewById(R.id.dose_0_et);
		mDrug1ET = (EditText) v.findViewById(R.id.drug_1_et);
		mDrugUsage1ET = (EditText) v.findViewById(R.id.drug_usage_1_et);
		mDose1ET = (EditText) v.findViewById(R.id.dose_1_et);
		mDrug2ET = (EditText) v.findViewById(R.id.drug_2_et);
		mDrugUsage2ET = (EditText) v.findViewById(R.id.drug_usage_2_et);
		mDose2ET = (EditText) v.findViewById(R.id.dose_2_et);
		mRehabilitationMeasureCB0 = (CheckBox) v.findViewById(R.id.rehabilitation_measure_0_cb);
		mRehabilitationMeasureCB1 = (CheckBox) v.findViewById(R.id.rehabilitation_measure_1_cb);
		mRehabilitationMeasureCB2 = (CheckBox) v.findViewById(R.id.rehabilitation_measure_2_cb);
		mRehabilitationMeasureCB3 = (CheckBox) v.findViewById(R.id.rehabilitation_measure_3_cb);
		mRehabilitationMeasureCB4 = (CheckBox) v.findViewById(R.id.rehabilitation_measure_4_cb);
		mRehabilitationMeasureOtherET = (EditText) v.findViewById(R.id.rehabilitation_measure_other_et);
		mThisFollowupClassificationRG = (RadioGroup) v.findViewById(R.id.this_followup_classification_rg);
		mThisFollowupClassificationRB0 = (RadioButton) v.findViewById(R.id.this_followup_classification_rg0);
		mThisFollowupClassificationRB1 = (RadioButton) v.findViewById(R.id.this_followup_classification_rg1);
		mThisFollowupClassificationRB2 = (RadioButton) v.findViewById(R.id.this_followup_classification_rg2);
		mThisFollowupClassificationRB3 = (RadioButton) v.findViewById(R.id.this_followup_classification_rg3);		
		nextFollowupDateET = (DateEditText) v.findViewById(R.id.next_followup_date);
		mFollowupDoctorSignatureET = (EditText) v.findViewById(R.id.followup_doctor_signature_et);
		mSaveTableBtn = (Button) v.findViewById(R.id.followup_save_table_btn);
	}
	
	private void fillTable(Cursor cursor) {
		Log.i("填表", "here");
		if (cursor.moveToNext()) {
			Log.i("cursor", "have one");
			int nameCol = cursor.getColumnIndex(Tables.NAME);
			int serialIdCol = cursor.getColumnIndex(Tables.SERIAL_ID);
			int followupDateCol = cursor.getColumnIndex(Tables.FOLLOWUP_DATE);
			int riskCol = cursor.getColumnIndex(Tables.RISK);
			int currentSymptomCol = cursor.getColumnIndex(Tables.CURRENT_SYMPTOM);
			int insightCol = cursor.getColumnIndex(Tables.INSIGHT);
			int sleepSituationCol = cursor.getColumnIndex(Tables.SLEEP_SITUATION);
			int dietSituationCol = cursor.getColumnIndex(Tables.DIET_SITUATION);
			int selfcareCol = cursor.getColumnIndex(Tables.SELF_CARE);
			int hourseworkCol = cursor.getColumnIndex(Tables.HOURSE_WORK);
			int productiveWorkCol = cursor.getColumnIndex(Tables.PRODUCTIVE_WORK);
			int learningAbilityCol = cursor.getColumnIndex(Tables.LEARNING_ABILITY);
			int interpersonalCol = cursor.getColumnIndex(Tables.INTERPERSONAL);
			int followupMildDisturbancesNumCol = cursor.getColumnIndex(Tables.FOLLOWUP_MILD_DISTURBANCES_NUM);
			int followupMakeTroubleNumCol = cursor.getColumnIndex(Tables.FOLLOWUP_MAKE_TROUBLE_NUM);
			int followupMakeDisasterNumCol = cursor.getColumnIndex(Tables.FOLLOWUP_MAKE_DISASTER_NUM);
			int followupHurtHimselfNumCol = cursor.getColumnIndex(Tables.FOLLOWUP_HURT_HIMSELF_NUM);
			int followupAttemptedSuicideNumCol = cursor.getColumnIndex(Tables.FOLLOWUP_ATTEMPTED_SUICIDE_NUM);
			int followupNoImpactOnfamilyAndSocietyCol = cursor.getColumnIndex(Tables.FOLLOWUP_NO_IMPACT_ON_FAMILY_AND_SOCIETY);
			int followupShutCaseCol = cursor.getColumnIndex(Tables.FOLLOWUP_SHUT_CASE);
			int followupHospitalizationCol = cursor.getColumnIndex(Tables.FOLLOWUP_HOSPITALIZATION);
			int lastDateToDischargeCol = cursor.getColumnIndex(Tables.LAST_DATE_TO_DISCHARGE);
			int labTestCol = cursor.getColumnIndex(Tables.LAB_TEST);
			int medicationComplianceCol = cursor.getColumnIndex(Tables.MEDICATION_COMPLIANCE);
			int drugAdverseReactionCol = cursor.getColumnIndex(Tables.DRUG_ADVERSE_REACTION);
			int treatmentEffectCol = cursor.getColumnIndex(Tables.TREATMENT_EFFECT);
			int whetherToReferralCol = cursor.getColumnIndex(Tables.WHETHER_TO_REFERRAL);
			int referralReasonCol = cursor.getColumnIndex(Tables.REFERRAL_REASON);
			int whereToReferralCol = cursor.getColumnIndex(Tables.WHERE_TO_REFERRAL);
			int drug0Col = cursor.getColumnIndex(Tables.DRUG_0);
			int drugUsage0Col = cursor.getColumnIndex(Tables.DRUG_USAGE_0);
			int dose0Col = cursor.getColumnIndex(Tables.DOSE_0);
			int drug1Col = cursor.getColumnIndex(Tables.DRUG_1);
			int drugUsage1Col = cursor.getColumnIndex(Tables.DRUG_USAGE_1);
			int dose1Col = cursor.getColumnIndex(Tables.DOSE_1);
			int drug2Col = cursor.getColumnIndex(Tables.DRUG_2);
			int drugUsage2Col = cursor.getColumnIndex(Tables.DRUG_USAGE_2);
			int dose2Col = cursor.getColumnIndex(Tables.DOSE_2);			
			int rehabilitationMeasureCol = cursor.getColumnIndex(Tables.REHABILITATION_MEASURE);
			int thisFollowupClassificationCol = cursor.getColumnIndex(Tables.THIS_FOLLOWUP_CLASSIFICATION);
			int nextFollowupDateCol = cursor.getColumnIndex(Tables.NEXT_FOLLOWUP_DATE);
			int followupDoctorSignatureCol = cursor.getColumnIndex(Tables.FOLLOWUP_DOCTOR_SIGNATURE);
			
			if (nameCol != -1) mNameET.setText(cursor.getString(nameCol));
			if (serialIdCol != -1) mSerialIdTV.setText(cursor.getString(serialIdCol));
			if (followupDateCol != -1) followupDateET.setText(cursor.getString(followupDateCol));
			if (riskCol != -1 && cursor.getString(riskCol) != null) {
				switch (cursor.getString(riskCol).charAt(0) - '0') {
				case 0:
					mRiskRB0.setChecked(true);
					break;
				case 1:
					mRiskRB1.setChecked(true);
					break;
				case 2:
					mRiskRB2.setChecked(true);
					break;
				case 3:
					mRiskRB3.setChecked(true);
					break;
				case 4:
					mRiskRB4.setChecked(true);
					break;
				case 5:
					mRiskRB5.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (currentSymptomCol != -1 && cursor.getString(currentSymptomCol).length() > 0) {
				String[] symptoms = cursor.getString(currentSymptomCol).split(";");	
				for (int i = 0; i < symptoms.length; ++i) {
					switch (Integer.valueOf(symptoms[i].substring(0, 2).trim())) {
					case 1:
						mCurrentSymptom0CB.setChecked(true);
						break;
					case 2:
						mCurrentSymptom1CB.setChecked(true);
						break;
					case 3:
						mCurrentSymptom2CB.setChecked(true);
						break;
					case 4:
						mCurrentSymptom3CB.setChecked(true);
						break;
					case 5:
						mCurrentSymptom4CB.setChecked(true);
						break;
					case 6:
						mCurrentSymptom5CB.setChecked(true);
						break;
					case 7:
						mCurrentSymptom6CB.setChecked(true);
						break;
					case 8:
						mCurrentSymptom7CB.setChecked(true);
						break;
					case 9:
						mCurrentSymptom8CB.setChecked(true);
						break;
					case 10:
						mCurrentSymptom9CB.setChecked(true);
						break;
					case 11:
						mCurrentSymptom10CB.setChecked(true);
						break;
					case 12:
						mCurrentSymptom11CB.setChecked(true);
						String[] otherArr = symptoms[i].trim().split(":");
						if (otherArr.length > 1) 
							mCurrentSymptomOtherET.setText(symptoms[i].substring(otherArr[0].length() + 1));
						break;					
					default:
						break;
					}
				}	
			}
			if (insightCol != -1 && cursor.getString(insightCol) != null) {
				switch (cursor.getString(insightCol).charAt(0) - '1') {
				case 0:
					mInsightRB0.setChecked(true);
					break;
				case 1:
					mInsightRB1.setChecked(true);
					break;
				case 2:
					mInsightRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (sleepSituationCol != -1 && cursor.getString(sleepSituationCol) != null) {
				switch (cursor.getString(sleepSituationCol).charAt(0) - '1') {
				case 0:
					mSleepSituationRB0.setChecked(true);
					break;
				case 1:
					mSleepSituationRB1.setChecked(true);
					break;
				case 2:
					mSleepSituationRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (dietSituationCol != -1 && cursor.getString(dietSituationCol) != null) {
				switch (cursor.getString(dietSituationCol).charAt(0) - '1') {
				case 0:
					mDietSituationRB0.setChecked(true);
					break;
				case 1:
					mDietSituationRB1.setChecked(true);
					break;
				case 2:
					mDietSituationRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (selfcareCol != -1 && cursor.getString(selfcareCol) != null) {
				switch (cursor.getString(selfcareCol).charAt(0) - '1') {
				case 0:
					mSelfcareRB0.setChecked(true);
					break;
				case 1:
					mSelfcareRB1.setChecked(true);
					break;
				case 2:
					mSelfcareRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (hourseworkCol != -1 && cursor.getString(hourseworkCol) != null) {
				switch (cursor.getString(hourseworkCol).charAt(0) - '1') {
				case 0:
					mHourseworkRB0.setChecked(true);
					break;
				case 1:
					mHourseworkRB1.setChecked(true);
					break;
				case 2:
					mHourseworkRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (productiveWorkCol != -1 && cursor.getString(productiveWorkCol) != null) {
				switch (cursor.getString(productiveWorkCol).charAt(0) - '1') {
				case 0:
					mProductiveWorkRB0.setChecked(true);
					break;
				case 1:
					mProductiveWorkRB1.setChecked(true);
					break;
				case 2:
					mProductiveWorkRB2.setChecked(true);
					break;
				case 3:
					mProductiveWorkRB3.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (learningAbilityCol != -1 && cursor.getString(learningAbilityCol) != null) {
				switch (cursor.getString(learningAbilityCol).charAt(0) - '1') {
				case 0:
					mLearningAbilityRB0.setChecked(true);
					break;
				case 1:
					mLearningAbilityRB1.setChecked(true);
					break;
				case 2:
					mLearningAbilityRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (interpersonalCol != -1 && cursor.getString(interpersonalCol) != null) {
				switch (cursor.getString(interpersonalCol).charAt(0) - '1') {
				case 0:
					mInterpersonalRB0.setChecked(true);
					break;
				case 1:
					mInterpersonalRB1.setChecked(true);
					break;
				case 2:
					mInterpersonalRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (followupMildDisturbancesNumCol != -1)
				mFollowupMildDisturbancesNumET.setText(cursor.getString(followupMildDisturbancesNumCol));
			if (followupMakeTroubleNumCol != -1)
				mFollowupMakeTroubleNumET.setText(cursor.getString(followupMakeTroubleNumCol));
			if (followupMakeDisasterNumCol != -1)
				mFollowupMakeDisasterNumET.setText(cursor.getString(followupMakeDisasterNumCol));
			if (followupHurtHimselfNumCol != -1)
				mFollowupHurtHimselfNumET.setText(cursor.getString(followupHurtHimselfNumCol));
			if (followupAttemptedSuicideNumCol != -1)
				mFollowupAttemptedSuicideNumET.setText(cursor.getString(followupAttemptedSuicideNumCol));
			if (followupNoImpactOnfamilyAndSocietyCol != -1 && cursor.getString(followupNoImpactOnfamilyAndSocietyCol) != null) {
				if (cursor.getString(followupNoImpactOnfamilyAndSocietyCol).charAt(0) == '1') {
					mFollowupNoImpactOnfamilyAndSocietyCB.setChecked(true);
				}
			}
			if (followupShutCaseCol != -1 && cursor.getString(followupShutCaseCol) != null) {
				switch (cursor.getString(followupShutCaseCol).charAt(0) - '1') {
				case 0:
					mFollowupShutCaseRB0.setChecked(true);
					break;
				case 1:
					mFollowupShutCaseRB1.setChecked(true);
					break;
				case 2:
					mFollowupShutCaseRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (followupHospitalizationCol != -1 && cursor.getString(followupHospitalizationCol) != null) {
				switch (cursor.getString(followupHospitalizationCol).charAt(0) - '0') {
				case 0:
					mFollowupHospitalizationRB0.setChecked(true);
					break;
				case 1:
					mFollowupHospitalizationRB1.setChecked(true);
					break;
				case 2:
					mFollowupHospitalizationRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (lastDateToDischargeCol != -1) 
				lastDateToDischarge.setText(cursor.getString(lastDateToDischargeCol));
			if (labTestCol != -1 && cursor.getString(labTestCol) != null) {
				String labTest = cursor.getString(labTestCol);
				switch (labTest.charAt(0) - '1') {
				case 0:
					mLabTestRB0.setChecked(true);
					break;
				case 1:
					mLabTestRB1.setChecked(true);
					String[] haveArr = labTest.split(":");
					if (haveArr.length > 1)
						mLabTestContentET.setText(labTest.substring(haveArr[0].length() + 1));
					break;
				default:
					break;
				}
			}
			if (medicationComplianceCol != -1 && cursor.getString(medicationComplianceCol) != null) {
				switch (cursor.getString(medicationComplianceCol).charAt(0) - '1') {
				case 0:
					mMedicationComplianceRB0.setChecked(true);
					break;
				case 1:
					mMedicationComplianceRB1.setChecked(true);
					break;
				case 2:
					mMedicationComplianceRB2.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (drugAdverseReactionCol != -1 && cursor.getString(drugAdverseReactionCol) != null) {
				String drugAdverseReaction = cursor.getString(drugAdverseReactionCol);
				switch (drugAdverseReaction.charAt(0) - '1') {
				case 0:
					mDrugAdverseReactionRB0.setChecked(true);
					break;
				case 1:
					mDrugAdverseReactionRB1.setChecked(true);
					String[] haveArr = drugAdverseReaction.split(":");
					if (haveArr.length > 1) 
						mDrugAdverseReactionContentET.setText(
								drugAdverseReaction.substring(haveArr[0].length() + 1));
					break;
				default:
					break;
				}
			}
			if (treatmentEffectCol != -1 && cursor.getString(treatmentEffectCol) != null) {
				switch (cursor.getString(treatmentEffectCol).charAt(0) - '1') {
				case 0:
					mTreatmentEffectRB0.setChecked(true);
					break;
				case 1:
					mTreatmentEffectRB1.setChecked(true);
					break;
				case 2:
					mTreatmentEffectRB2.setChecked(true);
					break;
				case 3:
					mTreatmentEffectRB3.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (whetherToReferralCol != -1 && cursor.getString(whetherToReferralCol) != null) {
				switch (cursor.getString(whetherToReferralCol).charAt(0) - '1') {
				case 0:
					mWhetherToReferralRB0.setChecked(true);
					break;
				case 1:
					mWhetherToReferralRB1.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (referralReasonCol != -1) 
				mReferralReasonET.setText(cursor.getString(referralReasonCol));
			if (whereToReferralCol != -1)
				mWhereToReferralET.setText(cursor.getString(whereToReferralCol));
			if (drug0Col != -1)
				mDrug0ET.setText(cursor.getString(drug0Col));
			if (drugUsage0Col != -1)
				mDrugUsage0ET.setText(cursor.getString(drugUsage0Col));
			if (dose0Col != -1)
				mDose0ET.setText(cursor.getString(dose0Col));
			if (drug1Col != -1)
				mDrug1ET.setText(cursor.getString(drug1Col));
			if (drugUsage1Col != -1)
				mDrugUsage1ET.setText(cursor.getString(drugUsage1Col));
			if (dose1Col != -1)
				mDose1ET.setText(cursor.getString(dose1Col));
			if (drug2Col != -1)
				mDrug2ET.setText(cursor.getString(drug2Col));
			if (drugUsage2Col != -1)
				mDrugUsage2ET.setText(cursor.getString(drugUsage2Col));
			if (dose2Col != -1)
				mDose2ET.setText(cursor.getString(dose2Col));
			if (rehabilitationMeasureCol != -1 && cursor.getString(rehabilitationMeasureCol).length() > 0) {
				String[] rehabilitations = cursor.getString(rehabilitationMeasureCol).split(";");	
				for (int i = 0; i < rehabilitations.length; ++i) {
					switch (rehabilitations[i].trim().charAt(0) - '1') {
					case 0:
						mRehabilitationMeasureCB0.setChecked(true);
						break;
					case 1:
						mRehabilitationMeasureCB1.setChecked(true);
						break;
					case 2:
						mRehabilitationMeasureCB2.setChecked(true);
						break;
					case 3:
						mRehabilitationMeasureCB3.setChecked(true);
						break;
					case 4:
						mRehabilitationMeasureCB4.setChecked(true);
						String[] otherArr = rehabilitations[i].trim().split(":");
						mRehabilitationMeasureOtherET.setText(
								rehabilitations[i].trim().substring(otherArr[0].length() + 1));
						break;
					
					default:
						break;
					}
				}	
			}
			if (thisFollowupClassificationCol != -1 && cursor.getString(thisFollowupClassificationCol) != null) {
				switch (cursor.getString(thisFollowupClassificationCol).charAt(0) - '0') {
				case 0:
					mThisFollowupClassificationRB0.setChecked(true);
					break;
				case 1:
					mThisFollowupClassificationRB1.setChecked(true);
					break;
				case 2:
					mThisFollowupClassificationRB2.setChecked(true);
					break;
				case 3:
					mThisFollowupClassificationRB3.setChecked(true);
					break;
				default:
					break;
				}
			}
			if (nextFollowupDateCol != -1)
				nextFollowupDateET.setText(cursor.getString(nextFollowupDateCol));
			if (followupDoctorSignatureCol != -1)
				mFollowupDoctorSignatureET.setText(cursor.getString(followupDoctorSignatureCol));
		}
	}
}