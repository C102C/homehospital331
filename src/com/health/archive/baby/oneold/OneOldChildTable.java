package com.health.archive.baby.oneold;

import java.util.HashMap;
import java.util.Map;

import com.health.database.Tables;

/***
 * 一岁以内的小屁股
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-22 下午3:02:19
 */
public class OneOldChildTable {
	public static final String oneold_table =  "oneold_table";
	public static final String id_card = "id_card";
	public static final String serial_id = "serial_id";
	public static final String age = "age";
	public static final String visite_date = "visite_date";
	public static final String weight = "weight";
	public static final String height = "height";
	public static final String head_circum = "head_circum";
	public static final String face = "face";
	public static final String skin = "skin";
	public static final String bregma_size = "bregma_size";
	public static final String bregma_state = "bregma_state";
	public static final String neck_block = "neck_block";
	public static final String eye = "eye";
	public static final String ear = "ear";
	public static final String hear = "hear";
	public static final String mouth = "mouth";
	public static final String heart_hear = "heart_hear";
	public static final String abdomen_touch = "abdomen_touch";
	public static final String funicle = "funicle";
	public static final String limbs = "limbs";
	public static final String rickets_sign = "rickets_sign";
	public static final String rickets_feature = "rickets_feature";
	public static final String externalia = "externalia";
	public static final String HGB = "HGB";
	public static final String v_d = "v_d";
	public static final String growth_assess = "growth_assess";
	public static final String seak_state = "seak_state";
	public static final String transfer_advise = "transfer_advise";
	public static final String guide = "guide";
	public static final String TCM = "TCM";
	public static final String next_date = "next_date";
	public static final String visit_doctor = "visit_doctor";

	public static Map<String, String> oneOldChildTable() {

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tables.TABLE_NAME, oneold_table);
		map.put(id_card, "varchar20");
		map.put(serial_id, "varchar20");
		map.put(age, "varchar20");
		map.put(visite_date, "varchar20");
		map.put(weight, "varchar20");
		map.put(height, "varchar20");
		map.put(head_circum, "varchar20");
		map.put(face, "varchar20");
		map.put(skin, "varchar20");
		map.put(bregma_size, "varchar20");
		map.put(bregma_state, "varchar20");
		map.put(neck_block, "varchar20");
		map.put(eye, "varchar20");
		map.put(ear, "varchar20");
		map.put(hear, "varchar20");
		map.put(mouth, "varchar20");
		map.put(heart_hear, "varchar20");
		map.put(abdomen_touch, "varchar20");
		map.put(funicle, "varchar20");
		map.put(limbs, "varchar20");
		map.put(rickets_sign, "varchar20");
		map.put(rickets_feature, "varchar20");
		map.put(externalia, "varchar20");
		map.put(HGB, "varchar20");
		map.put(v_d, "varchar20");
		map.put(growth_assess, "varchar20");
		map.put(seak_state, "varchar20");
		map.put(transfer_advise, "varchar20");
		map.put(guide, "varchar20");
		map.put(TCM, "varchar20");
		map.put(next_date, "varchar20");
		map.put(visit_doctor, "varchar20");
		return map;
	}
}
