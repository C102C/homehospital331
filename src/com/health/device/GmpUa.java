package com.health.device;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

import com.health.util.MyArrays;

/**
 * GMP urine analyzer GMP尿液分析仪
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2013-10-30 上午10:14:50
 */
public class GmpUa {
	// 0x93 0x8e 0x4 0x00 0x08 0x04 无 0x0c
	// 获取单条数据的命令
	public static final byte[] COMMAND_SINGLE_DATA = { (byte) 0x93,
			(byte) 0x8e, 0x04, 0x00, 0x08, 0x04, 0x10 };
	// 确认命令
	public static final byte[] CONFIRM = { (byte) 0x93, (byte) 0x8e, 0x08,
			0x00, 0x08, 0x01, 0x43, 0x4f, 0x4e, 0x54, 0x45 };
	private static final byte[] HEAD = { (byte) 0x93, (byte) 0x8e };
	// 单条数据标志
	public static final byte TOKEN_SINGLE_DATA = 0x04;

	private GmpUa() {// 避免实例化

	}

	/**
	 * 获取数据标志
	 * 
	 * @param data
	 * @return
	 */
	public static byte getToken(byte[] data) {
		return data[5];
	}

	/**
	 * 获取数据的校验和
	 * 
	 * @param data
	 * @return
	 */
	private static byte getCheckSum(byte[] data) {
		byte sum = 0;
		for (int i = 2; i < data.length - 1; i++)
			sum += data[i];
		return sum;
	}

	/**
	 * 检验数据的校验和是否正确
	 * 
	 * @param data
	 * @return
	 */
	public static boolean check(byte[] data) {
		byte checkSum = getCheckSum(data);
		boolean result = checkSum == data[data.length - 1];
		return result;
	}

	/**
	 * 解析数据记录
	 * 
	 * @param data
	 */
	public static UaRecord parseRecord(byte[] data) {
		// 数据记录从第6（以0为起点）个字节开始[6,7]表示序列号
		int effective = ((data[8] & 0x07) << 8) | (data[9] & 0xff);// [8,9]表示有效位
		// [10,11]表示年、月、日
		int year = data[11] & 0x7f;
		int month = ((data[10] & 0x07) << 1) | ((data[11] & 0x80) >> 7);
		int day = (data[10] & 0xf8) >> 3;
		// [12,13]表示时、分、LEU
		int hour = data[13] & 0x1f;
		int minute = ((data[12] & 0x07) << 3) | ((data[13] & 0xe0) >> 5);
		UaRecord.Builder builder = new UaRecord.Builder(year, month, day, hour,
				minute);
		if ((effective & 0x01) != 0) {
			int leu = (data[12] & 0x38) >> 3;
			builder.leu(leu);
		}
		// [14,15]表示NIT UBG PRO PH BLD
		if ((effective & (0x01 << 1)) != 0) {
			int bld = (data[14] & 0x70) >> 4;
			builder.bld(bld);
		}
		if ((effective & (0x01 << 2)) != 0) {
			int ph = (data[14] & 0x0e) >> 1;
			builder.ph(ph);
		}
		if ((effective & (0x01 << 3)) != 0) {
			int pro = ((data[14] & 0x01) << 2) | ((data[15] & 0xc0) >> 6);
			builder.pro(pro);
		}
		if ((effective & (0x01 << 4)) != 0) {
			int ubg = (data[15] & 0x38) >> 3;
			builder.ubg(ubg);
		}
		if ((effective & (0x01 << 5)) != 0) {
			int nit = data[15] & 0x7;
			builder.nit(nit);
		}
		// [16,17]表示VC GLU BIL KET SG
		if ((effective & (0x01 << 6)) != 0) {
			int sg = data[17] & 0x7;
			builder.sg(sg);
		}
		if ((effective & (0x01 << 7)) != 0) {
			int ket = (data[17] & 0x38) >> 3;
			builder.ket(ket);
		}
		if ((effective & (0x01 << 8)) != 0) {
			int bil = ((data[16] & 0x01) << 2) | ((data[17] & 0xc0) >> 6);
			builder.bil(bil);
		}
		if ((effective & (0x01 << 9)) != 0) {
			int glu = (data[16] & 0x0e) >> 1;
			builder.glu(glu);
		}
		if ((effective & (0x01 << 10)) != 0) {
			int vc = (data[16] & 0x70) >> 4;
			builder.vc(vc);
		}
		return builder.build();
	}

	/**
	 * 将读到数据按类型汇总
	 * 
	 * @param buffer
	 * @return
	 */
	public static SparseArray<List<byte[]>> getLegalPatternsFromBuffer(
			byte[] buffer) {
		List<byte[]> datas = HealthDevice.splitBufferData(buffer, HEAD);
		SparseArray<List<byte[]>> map = new SparseArray<List<byte[]>>();
		for (byte[] data : datas) {
			if (check(data) == true) {
				int token = getToken(data);
				if (map.get(token) == null) {
					List<byte[]> pattern = new ArrayList<byte[]>();
					pattern.add(data);
					map.put(token, pattern);
				} else {
					map.get(token).add(data);
				}
			}
		}
		return map;
	}

	/**
	 * 尿液分析仪记录结构
	 * 
	 * @author jiqunpeng
	 * 
	 *         创建时间：2013-10-30 下午4:44:30
	 */
	public static class UaRecord {
		public final String date;
		public final String leu;// 白细胞
		public final String bld;// 潜血
		public final String ph;// pH值
		public final String pro;// 蛋白质
		public final String ubg;// 尿胆原
		public final String nit;// 亚硝酸盐
		public final String sg;// 比重
		public final String ket;// 酮体
		public final String bil;// 胆红素
		public final String glu;// 葡萄糖
		public final String vc;// 维生素C

		private UaRecord(Builder builder) {
			this.date = builder.date;
			this.leu = builder.leu;
			this.bld = builder.bld;
			this.ph = builder.ph;
			this.pro = builder.pro;
			this.ubg = builder.ubg;
			this.nit = builder.nit;
			this.sg = builder.sg;
			this.ket = builder.ket;
			this.bil = builder.bil;
			this.glu = builder.glu;
			this.vc = builder.vc;
		}

		public static class Builder {
			private static final String NEGTIVE = "-";// 阴性
			private static final String NORMAL = "+-";// 正常？
			private static final String[] PH = { "0-5.0", "1-6.0", "2-6.5",
					"3-7.0", "4-7.5", "5-8.0", "6-8.5" };
			private String date = null;
			private String leu = null;
			private String bld = null;
			private String ph = null;
			private String pro = null;
			private String ubg = null;
			private String nit = null;
			private String sg = null;
			private String ket = null;
			private String bil = null;
			private String glu = null;
			private String vc = null;

			public Builder(int year, int month, int day, int hour, int minute) {
				StringBuilder sb = new StringBuilder();
				sb.append("20");
				sb.append(year);
				sb.append(" ");
				sb.append(month);
				sb.append(" ");
				sb.append(day);
				sb.append(" ");
				sb.append(hour);
				sb.append(":");
				sb.append(minute);
				this.date = sb.toString();
			}

			public UaRecord build() {
				return new UaRecord(this);
			}

			public Builder leu(int leu) {
				this.leu = getResult(leu);
				return this;
			}

			public Builder bld(int bld) {
				this.bld = getResult(bld);
				return this;
			}

			public Builder ph(int ph) {
				this.ph = PH[ph];
				return this;
			}

			public Builder pro(int pro) {
				this.pro = getResult(pro);
				return this;
			}

			public Builder ubg(int ubg) {
				if (ubg == 0)
					this.ubg = NEGTIVE;
				else
					this.ubg = "+" + ubg;
				return this;
			}

			public Builder nit(int nit) {
				if (nit == 0)
					this.nit = NEGTIVE;
				else
					this.nit = "+";
				return this;
			}

			public Builder sg(int sg) {
				this.sg = (sg * 0.005 + 1) + "";
				return this;
			}

			public Builder ket(int ket) {
				this.ket = getResult(ket);
				return this;

			}

			public Builder bil(int bil) {
				if (bil == 0)
					this.bil = NEGTIVE;
				else
					this.bil = "+" + bil;
				return this;

			}

			public Builder glu(int glu) {
				this.glu = getResult(glu);
				return this;

			}

			public Builder vc(int vc) {
				this.vc = getResult(vc);
				return this;

			}

			/**
			 * 结构体值到结果的转换
			 * 
			 * @param code
			 * @return
			 */
			private String getResult(int code) {
				if (code == 0)
					return NEGTIVE;
				if (code == 1)
					return NORMAL;
				return "+" + (code - 1);
			}
		}
	}
}
