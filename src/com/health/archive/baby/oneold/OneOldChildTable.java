package com.health.archive.baby.oneold;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.younext.R;

import com.health.database.Tables;

/***
 * 一岁以内的小屁股
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-22 下午3:02:19
 */
public class OneOldChildTable {
	public static final String oneold_table = "oneold_table";
	public static final String id_card = "id_card";
	public static final String serial_id = "serial_id";
	public static final String age = "age";
	public static final String visit_date = "visite_date";
	public static final String weight = "weight";
	public static final String height = "height";
	public static final String head_circum = "head_circum";
	public static final String face = "face";
	public static final String skin = "skin";
	public static final String bregma_size_a = "bregma_size_a";
	public static final String bregma_size_b = "bregma_size_b";
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
	public static final String transfer_reason = "transfer_reason";
	public static final String transfer_org = "transfer_org";
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
		map.put(visit_date, "varchar20");
		map.put(weight, "varchar20");
		map.put(height, "varchar20");
		map.put(head_circum, "varchar20");
		map.put(face, "varchar20");
		map.put(skin, "varchar20");
		map.put(bregma_size_a, "varchar20");
		map.put(bregma_size_b, "varchar20");
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
		map.put(transfer_reason, "varchar20");
		map.put(transfer_org, "varchar20");
		map.put(guide, "varchar20");
		map.put(TCM, "varchar20");
		map.put(next_date, "varchar20");
		map.put(visit_doctor, "varchar20");
		return map;
	}

	public static final Map<String, Integer> cloumIdmap = Collections
			.unmodifiableMap(new HashMap<String, Integer>() {

				private static final long serialVersionUID = -8896139986212203908L;
				{
					// put(id_card,
					// R.id.oneold_id_card);
					put(serial_id, R.id.oneold_serial_id);
					put(age, R.id.oneold_age);
					put(visit_date, R.id.oneold_visit_date);
					put(weight, R.id.oneold_weight);
					put(height, R.id.oneold_height);
					put(head_circum, R.id.oneold_head_circum);
					put(face, R.id.oneold_face);
					put(skin, R.id.oneold_skin);
					put(bregma_size_a, R.id.oneold_bregma_size_a);
					put(bregma_size_b, R.id.oneold_bregma_size_b);
					put(bregma_state, R.id.oneold_bregma_state);
					put(neck_block, R.id.oneold_neck_block);
					put(eye, R.id.oneold_eye);
					put(ear, R.id.oneold_ear);
					put(hear, R.id.oneold_hear);
					put(mouth, R.id.oneold_mouth);
					put(heart_hear, R.id.oneold_heart_hear);
					put(abdomen_touch, R.id.oneold_abdomen_touch);
					put(funicle, R.id.oneold_funicle);
					put(limbs, R.id.oneold_limbs);
					put(rickets_sign, R.id.oneold_rickets_sign);
					put(rickets_feature, R.id.oneold_rickets_feature);
					put(externalia, R.id.oneold_externalia);
					put(HGB, R.id.oneold_HGB);
					put(v_d, R.id.oneold_v_d);
					put(growth_assess, R.id.oneold_growth_assess);
					put(seak_state, R.id.oneold_seak_state);
					put(transfer_advise, R.id.oneold_transfer_advise);
					put(guide, R.id.oneold_guide);
					put(TCM, R.id.oneold_TCM);
					put(next_date, R.id.oneold_next_date);
					put(visit_doctor, R.id.oneold_visit_doctor);
				}

			});

}
