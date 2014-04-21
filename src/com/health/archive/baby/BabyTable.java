package com.health.archive.baby;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.younext.R;

import com.health.database.Tables;

/**
 * 新生儿表
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-16 下午5:14:08
 */
public class BabyTable {
	public static final String serial_id = "serial_id";
	public static final String sex = "sex";
	public static final String birthday = "birthday";
	public static final String id_card = "id_card";
	public static final String addr = "addr";
	public static final String f_name = "f_name";
	public static final String f_job = "f_job";
	public static final String f_phone = "f_phone";
	public static final String f_birthday = "f_birthday";
	public static final String m_name = "m_name";
	public static final String m_job = "m_job";
	public static final String m_phone = "m_phone";
	public static final String m_birthday = "m_birthday";
	public static final String birth_week = "birth_week";
	public static final String m_health = "m_health";
	public static final String birth_organize = "birth_organize";
	public static final String birth_state = "birth_state";
	public static final String apnea = "apnea";
	public static final String apgar_score = "apgar_score";
	public static final String malformation = "malformation";
	public static final String hearing_check = "hearing_check";
	public static final String sick_check = "sick_check";
	public static final String birth_weight = "birth_weight";
	public static final String c_weight = "c_weight";
	public static final String birth_height = "birth_height";
	public static final String nurse_pattern = "nurse_pattern";
	public static final String milk_amount = "milk_amount";
	public static final String milk_times = "milk_times";
	public static final String emesis = "emesis";
	public static final String excrement = "excrement";
	public static final String excrement_time = "excrement_time";
	public static final String temp = "temp";
	public static final String pulse = "pulse";
	public static final String frequence = "frequence";
	public static final String complexion = "complexion";
	public static final String jaundice_part = "jaundice_part";
	public static final String bregma_size_a = "bregma_size_a";
	public static final String bregma_size_b = "bregma_size_b";
	public static final String bregma_state = "bregma_state";
	public static final String eye = "eye";
	public static final String limbs = "limbs";
	public static final String ear = "ear";
	public static final String neck_block = "neck_block";
	public static final String nose = "nose";
	public static final String skin = "skin";
	public static final String mouth = "mouth";
	public static final String anus = "anus";
	public static final String heart_hear = "heart_hear";
	public static final String externalia = "externalia";
	public static final String abdomen_touch = "abdomen_touch";
	public static final String spine = "spine";
	public static final String funicle = "funicle";
	public static final String transfer_advise = "transfer_advise";
	public static final String transfer_reason = "transfer_reason";
	public static final String transfer_org = "transfer_org";
	public static final String guide = "guide";
	public static final String visit_date = "visit_date";
	public static final String next_place = "next_place";
	public static final String next_date = "next_date";
	public static final String visite_doctor = "visite_doctor";
	public static final String baby_table = "baby_table";

	public static final Map<String, Integer> cloumIdmap = Collections
			.unmodifiableMap(new HashMap<String, Integer>() {

				private static final long serialVersionUID = -8896139986212203908L;
				{
					put(serial_id, R.id.serial_id);
					put(sex, R.id.baby_sex);
					put(birthday, R.id.baby_birthday);
					put(id_card, R.id.baby_idcard);
					put(addr, R.id.baby_addr);
					put(f_name, R.id.baby_f_name);
					put(f_job, R.id.baby_f_job);
					put(f_phone, R.id.baby_f_phone);
					put(f_birthday, R.id.baby_f_birthday);
					put(m_name, R.id.baby_m_name);
					put(m_job, R.id.baby_m_job);
					put(m_phone, R.id.baby_m_phone);
					put(m_birthday, R.id.baby_m_birthday);
					put(birth_week, R.id.baby_birth_week);
					put(m_health, R.id.baby_m_health);
					put(birth_organize, R.id.baby_birth_organize);
					put(birth_state, R.id.baby_birth_state);
					put(apnea, R.id.baby_apnea);
					put(apgar_score, R.id.baby_apgar_score);
					put(malformation, R.id.baby_malformation);
					put(hearing_check, R.id.baby_hearing_check);
					put(sick_check, R.id.baby_sick_check);
					put(birth_weight, R.id.baby_birth_weight);
					put(c_weight, R.id.baby_c_weight);
					put(birth_height, R.id.baby_birth_height);
					put(nurse_pattern, R.id.baby_nurse_pattern);
					put(milk_amount, R.id.baby_milk_amount);
					put(milk_times, R.id.baby_milk_times);
					put(emesis, R.id.baby_emesis);
					put(excrement, R.id.baby_excrement);
					put(excrement_time, R.id.baby_excrement_time);
					put(temp, R.id.baby_temp);
					put(pulse, R.id.baby_pulse);
					put(frequence, R.id.baby_frequence);
					put(complexion, R.id.baby_complexion);
					put(jaundice_part, R.id.baby_jaundice_part);
					put(bregma_size_a, R.id.baby_bregma_size_a);
					put(bregma_size_b, R.id.baby_bregma_size_b);
					put(bregma_state, R.id.baby_bregma_state);
					put(eye, R.id.baby_eye);
					put(limbs, R.id.baby_limbs);
					put(ear, R.id.baby_ear);
					put(neck_block, R.id.baby_neck_block);
					put(nose, R.id.baby_nose);
					put(skin, R.id.baby_skin);
					put(mouth, R.id.baby_mouth);
					put(anus, R.id.baby_anus);
					put(heart_hear, R.id.baby_heart_hear);
					put(externalia, R.id.baby_externalia);
					put(abdomen_touch, R.id.baby_abdomen_touch);
					put(spine, R.id.baby_spine);
					put(funicle, R.id.baby_funicle);
					put(transfer_advise, R.id.baby_transfer_advise);
					put(transfer_reason, R.id.baby_transfer_reason);
					put(transfer_org, R.id.baby_transfer_org);
					put(guide, R.id.baby_guide);
					put(visit_date, R.id.baby_visit_date);
					put(next_place, R.id.baby_next_place);
					put(next_date, R.id.baby_next_date);
					put(visite_doctor, R.id.baby_visite_doctor);
				}
			});

	/***
	 * 疫苗接种卡表头信息
	 * 
	 * @return
	 */
	public static Map<String, String> babyVisitTable() {

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tables.TABLE_NAME, baby_table);
		map.put(serial_id, "varchar(18)");
		map.put(sex, "varchar(4)");
		map.put(birthday, "date");
		map.put(id_card, "varchar(20)");
		map.put(addr, "varchar(40)");
		map.put(f_name, "varchar(20)");
		map.put(f_job, "varchar(20)");
		map.put(f_phone, "integer");
		map.put(f_birthday, "date");
		map.put(m_name, "varchar(20)");
		map.put(m_job, "varchar(20)");
		map.put(m_phone, "integer");
		map.put(m_birthday, "date");
		map.put(birth_week, "integer");
		map.put(m_health, "varchar(20)");
		map.put(birth_organize, "varchar(20)");
		map.put(birth_state, "varchar(20)");
		map.put(apnea, "varchar(20)");
		map.put(apgar_score, "varchar(20)");
		map.put(malformation, "varchar(20)");
		map.put(hearing_check, "varchar(20)");
		map.put(sick_check, "varchar(20)");
		map.put(birth_weight, "varchar(20)");
		map.put(c_weight, "varchar(20)");
		map.put(birth_height, "varchar(20)");
		map.put(nurse_pattern, "varchar(20)");
		map.put(milk_amount, "varchar(20)");
		map.put(milk_times, "varchar(20)");
		map.put(emesis, "varchar(20)");
		map.put(excrement, "varchar(20)");
		map.put(excrement_time, "varchar(20)");
		map.put(temp, "varchar(20)");
		map.put(pulse, "varchar(20)");
		map.put(frequence, "varchar(20)");
		map.put(complexion, "varchar(20)");
		map.put(jaundice_part, "varchar(20)");
		map.put(bregma_size_a, "varchar(20)");
		map.put(bregma_size_b, "varchar(20)");
		map.put(bregma_state, "varchar(20)");
		map.put(eye, "varchar(20)");
		map.put(limbs, "varchar(20)");
		map.put(ear, "varchar(20)");
		map.put(neck_block, "varchar(20)");
		map.put(nose, "varchar(20)");
		map.put(skin, "varchar(20)");
		map.put(mouth, "varchar(20)");
		map.put(anus, "varchar(20)");
		map.put(heart_hear, "varchar(20)");
		map.put(externalia, "varchar(20)");
		map.put(abdomen_touch, "varchar(20)");
		map.put(spine, "varchar(20)");
		map.put(funicle, "varchar(20)");
		map.put(transfer_advise, "varchar(20)");
		map.put(transfer_reason, "varchar(20)");
		map.put(transfer_org, "varchar(20)");
		map.put(guide, "varchar(20)");
		map.put(visit_date, "date");
		map.put(next_place, "varchar(20)");
		map.put(next_date, "date");
		map.put(visite_doctor, "varchar(20)");
		return map;
	}
}
