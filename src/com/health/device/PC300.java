package com.health.device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.health.util.FileManger;
import com.health.util.L;

import android.util.Log;
import android.util.SparseArray;

public class PC300 {
	// 用于计算校验和的表
	public static final byte[] CRC_TABLE = { 0, 94, -68, -30, 97, 63, -35,
			-125, -62, -100, 126, 32, -93, -3, 31, 65, -99, -61, 33, 127, -4,
			-94, 64, 30, 95, 1, -29, -67, 62, 96, -126, -36, 35, 125, -97, -63,
			66, 28, -2, -96, -31, -65, 93, 3, -128, -34, 60, 98, -66, -32, 2,
			92, -33, -127, 99, 61, 124, 34, -64, -98, 29, 67, -95, -1, 70, 24,
			-6, -92, 39, 121, -101, -59, -124, -38, 56, 102, -27, -69, 89, 7,
			-37, -123, 103, 57, -70, -28, 6, 88, 25, 71, -91, -5, 120, 38, -60,
			-102, 101, 59, -39, -121, 4, 90, -72, -26, -89, -7, 27, 69, -58,
			-104, 122, 36, -8, -90, 68, 26, -103, -57, 37, 123, 58, 100, -122,
			-40, 91, 5, -25, -71, -116, -46, 48, 110, -19, -77, 81, 15, 78, 16,
			-14, -84, 47, 113, -109, -51, 17, 79, -83, -13, 112, 46, -52, -110,
			-45, -115, 111, 49, -78, -20, 14, 80, -81, -15, 19, 77, -50, -112,
			114, 44, 109, 51, -47, -113, 12, 82, -80, -18, 50, 108, -114, -48,
			83, 13, -17, -79, -16, -82, 76, 18, -111, -49, 45, 115, -54, -108,
			118, 40, -85, -11, 23, 73, 8, 86, -76, -22, 105, 55, -43, -117, 87,
			9, -21, -75, 54, 104, -118, -44, -107, -53, 41, 119, -12, -86, 72,
			22, -23, -73, 85, 11, -120, -42, 52, 106, 43, 117, -105, -55, 74,
			20, -10, -88, 116, 42, -56, -106, 21, 75, -87, -9, -74, -24, 10,
			84, -41, -119, 107, 53 };

	public static final byte[] COMMAND_BP_START = { -86, 85, 64, 2, 1, 41 };// 开始测量血压命令
	public static final byte[] COMMAND_BP_STOP = { -86, 85, 64, 2, 2, -53 };// 开始测量血压命令
	public static final byte[] COMMAND_BETTERY = { (byte) 0xaa, 0x55, 0x51,
			0x02, 0x01, -56 };// 电池电量命令
	public static final byte[] COMMAND_TEMP_START = { (byte) 0xaa, 0x55, 0x70,
			0x03, 0x01, (byte) 0x40, (byte) 0x1A };// 体温开始测量命令
	public static final byte[] COMMAND_ECG_START = { (byte) 0xaa, 0x55, 0x30,
			0x02, 0x01, -58 };// 心电开始测量命令
	public static final byte[] COMMAND_ECG_STOP = { (byte) 0xaa, 0x55, 0x30,
			0x02, 0x02, 36 };// 心电停止测量命令

	public static final byte TOKEN_BP_CURRENT = 0x42;// 当前血压令牌
	public static final byte TOKEN_BP_RESULT = 0x43;// 血压测量结果令牌
	public static final byte TOKEN_BO_WAVE = 0x52;// 血氧波形图令牌
	public static final byte TOKEN_POWER_OFF = (byte) 0xD0;// 测量仪关机令牌
	public static final byte TOKEN_BO_PAKAGE = (byte) 0x53;// 上传参数数据包令牌
	public static final byte TOKEN_TEMP = (byte) 0x72;// 上传体温令牌
	public static final byte TOKEN_ECG_WAVE = (byte) 0x32;// 心电波形图令牌
	public static final byte TOKEN_ECG_RESULT = (byte) 0x33;// 心电测量结果令牌

	public static final int PROBE_OFF = 0x02;// 探针脱落
	// 血压错误类型
	public static final int ERROR_RESULT = -1;// 测量结果错误
	public static final int ILLEGAL_PULSE = 0;// 测量不到有效的脉搏
	public static final int BAD_BOUND = 1;// 气袋没有绑好
	public static final int ERROR_VALUE = 2;// 测量数值结果有误
	// public static final int PRESSS_TOO_HIGH = 3;//
	// 气袋压力超过295mmHg .进入超压保护
	// public static final int MOVED_TOO_MANY = 4;//
	// 干预过多（测量中移动、说话等）
	private static final int MIN_DATA_SIZE = 6;// 最小字节数

	private static final byte[] HEAD = { (byte) 0xaa, 0x55 };

	private static final String TAG = "PC300.this";

	public static final String[] ECG_RESULT = { "波形未见异常", "波形疑似心跳稍快请注意休息",
			"波形疑似心跳过快请注意休息", "波形疑似阵发性心跳过快请咨询医生", "波形疑似心跳稍缓请注意休息",
			"波形疑似心跳过缓请注意休息", "波形疑似偶发心跳间期缩短请咨询医生", "波形疑似心跳间期不规则请咨询医生",
			"波形疑似心跳稍快伴有偶发心跳间期缩短请咨询医生", "波形疑似心跳稍缓伴有偶发心跳间期缩短请咨询医生",
			"波形疑似心跳稍缓伴有心跳间期不规则请咨询医生", "波形有漂移请重新测量", "波形疑似心跳过快伴有波形漂移请咨询医生",
			"波形疑似心跳过缓伴有波形漂移请咨询医生", "波形疑似偶发心跳间期缩短伴有波形漂移请咨询医生",
			"波形疑似心跳间期不规则伴有波形漂移请咨询医生", "信号较差请重新测量" };

	/**
	 * 计算字节序列的校验和
	 * 
	 * @param data
	 * @return
	 */
	public static byte getCRC(byte[] data) {
		byte crc = 0;
		for (int j = 0; j < data.length; ++j) {
			crc = CRC_TABLE[(0xFF & (crc ^ data[j]))];
		}
		return crc;
	}

	/**
	 * 检验数据的校验和是否正确
	 * 
	 * @param data
	 * @return
	 */
	public static boolean check(byte[] data) {
		if (data.length < MIN_DATA_SIZE)
			return false;
		byte[] content = new byte[data.length - 1];
		for (int i = 0; i < content.length; i++)
			content[i] = data[i];
		byte crc = getCRC(content);
		boolean result = crc == data[data.length - 1];
		// Log.i(TAG, "check:" + Arrays.toString(data)
		// + "->" + result);
		return result;

	}

	private void printDataWithCrc(byte[] data, String dataName) {
		byte crc = getCRC(data);
		System.out.print(dataName + " = {");
		for (byte d : data) {
			System.out.print(d + ",");
		}
		System.out.println(crc + "}");
	}

	/**
	 * 获取数据的令牌
	 * 
	 * @param data
	 * @return
	 */
	public byte getToken(byte[] data) {
		byte token = (byte) (data[2] & 0xff);
		Log.i(TAG + " token", new Byte(token).toString());
		return token;
		// return (byte) (data[2] & 0xff);// 第3个字节表示令牌
	}

	/**
	 * 
	 * 获取当前血压值
	 * 
	 * @param data
	 * @return
	 */
	public int getCurrentBp(byte[] data) {
		// 包头（2） 令牌（1） 长度（1） 类型（1） 数据（2） 校验和 （1）
		return ((data[5] & 0xf) << 8) + (data[6] & 0xff);
	}

	/**
	 * 解析血压测量结果,返回脉率判断结果（正常[0]与否[1]、收缩压、舒张压、脉率
	 * 
	 * @param data
	 * @return
	 */
	public int[] getResultBp(byte[] data) {
		if (data.length == 11) {// 仪器测量正常
			// 包头（2） 令牌（1） 长度（1） 类型（1） 数据（5) 校验和（1）
			// -86, 85, 67, 7, 1, 0, 87, 0, 64, 68, -27
			int pulseTag = (0xff & data[5]) >> 7;// 测量结果，1表示心率不齐，0表示正常
			int sys = ((0x7f & data[5]) << 8) + (data[6] & 0xff);// 收缩压,第5个字节的后7为与第6个字节
			// int map = 0xff&data[7];//平均压
			int dia = 0xff & data[8];// 舒张压
			int pulse = 0xff & data[9];// 脉率
			int[] bpResult = { pulseTag, sys, dia, pulse };
			return bpResult;
		} else {// 测量结果错误
			int error = 0x01 & data[5];
			int[] errorResult = { ERROR_RESULT, error };
			return errorResult;
		}
	}

	/**
	 * 获取血氧波形图的数据，一次数据一般包含两个点
	 * 
	 * @param data
	 * @return
	 */
	public int[] getBoWave(byte[] data) {
		if (data[4] == 0x01) {
			int[] value = { data[5] & 0x7f, data[6] & 0x7f };
			return value;
		}
		return new int[] {};
	}

	/**
	 * 血氧含量\脉率\探头状态
	 * 
	 * @param data
	 * @return
	 */
	public int[] getSpO2(byte[] data) {
		return new int[] { 0xff & data[5], (data[7] << 8) + data[6],
				data[9] & PROBE_OFF };
	}

	/**
	 * 获取体温
	 * 
	 * @param bs
	 * @return
	 */
	public Float getTemp(byte[] data) {
		int fDegree = ((data[6] & 0xff) << 8) | (data[7] & 0xff);
		return (float) (30.00 + (1.0 * fDegree / 100));
	}

	// -86, 85, 83, 7, 1, 98, 59, 0, 85, 0, -107,

	/**
	 * 检验分割后的多条数据的校验和，对于一个类型的数据有多条的情况， 只保存最后且校验码正确的一条
	 * 结果保存在一个map中，键为数据的令牌，值为一条完整的数据
	 * 
	 * @param datas
	 * @return
	 */
	public SparseArray<byte[]> checkCrcAndRetainSinglePattern(List<byte[]> datas) {
		SparseArray<byte[]> map = new SparseArray<byte[]>();
		for (byte[] data : datas) {
			if (check(data) == true) {
				int token = getToken(data);
				map.put(token, data);
			}
		}
		return map;
	}

	/**
	 * 将读到的多条数据分割，返回每个类型最新的合法的数据，如有多条血压当前值得数据，
	 * 只保留最后一条合法的当前血压数据
	 * 
	 * @param buffer
	 * @return
	 */
	public SparseArray<byte[]> getSingleLegalPatternFromBuffer(byte[] buffer) {
		return checkCrcAndRetainSinglePattern(HealthDevice.splitBufferData(
				buffer, HEAD));
	}

	/**
	 * 将读到数据按类型汇总
	 * 
	 * @param buffer
	 * @return
	 */
	public SparseArray<List<byte[]>> getLegalPatternsFromBuffer(byte[] buffer) {
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

	public static void main(String[] args) {
		PC300 testCrc = new PC300();
		testCrc.printDataWithCrc(COMMAND_BP_START, "COMMAND_BP_START");
		testCrc.printDataWithCrc(COMMAND_BP_STOP, "COMMAND_BP_STOP");
		testCrc.printDataWithCrc(COMMAND_BETTERY, "COMMAND_BETTERY");
		testCrc.printDataWithCrc(COMMAND_TEMP_START, "COMMAND_YEMP");
	}

	/***
	 * 获取心电的波形数据
	 * 
	 * @param each
	 * @return
	 */
	public EcgFrame getEcgFram(byte[] data) {
		if (data.length != 59)// 每一帧的数据长为59
			return null;
		int framNum = data[5] & 0xff;
		int[] ecg = new int[25];
		int index = 0;
		for (int i = 7; i < 56; i += 2)
			ecg[index++] = data[i] & 0xff;
		// data[56]为实时心率值，暂时保留
		boolean flag = (data[57] & 0x80) == 1 ? true : false;
		return new EcgFrame(framNum, ecg, flag);
	}

	/***
	 * 心电数据的帧
	 * 
	 * @author jiqunpeng
	 * 
	 *         创建时间：2014-3-17 下午8:45:26
	 */
	public class EcgFrame implements Comparable<EcgFrame> {
		private int framNum;// 帧号
		private int[] ecg;// 心电值
		private boolean isDeviceOff;// 导联脱落
		private long time;// 测试时间

		public EcgFrame(int framNum, int[] ecg, boolean isDeviceOff) {
			super();
			this.framNum = framNum;
			this.ecg = ecg;
			this.isDeviceOff = isDeviceOff;
			this.time = System.currentTimeMillis();
		}

		public int getFramNum() {
			return framNum;
		}

		public int[] getEcg() {
			return ecg;
		}

		public boolean isDeviceOff() {
			return isDeviceOff;
		}
		public long getTime() {
			return time;
		}

		@Override
		public int compareTo(EcgFrame other) {
			return framNum - other.framNum;
		}

	}

	// 保存心电的所有帧
	public static List<EcgFrame> ecgFrames = new ArrayList<EcgFrame>();

	/***
	 * 添加一个帧
	 * 
	 * @param frame
	 */
	public void addEcgFrameAndSort(EcgFrame frame) {
		// saveEcgFrams(frame);
		L.i("addEcgFrameAndSort",
				frame.getFramNum() + ":" + Arrays.toString(frame.ecg));
		ecgFrames.add(frame);
		Collections.sort(ecgFrames);
	}

	/***
	 * 查找一个帧所在下标
	 * 
	 * @param frameNum
	 * @return
	 */
	private int findEcgFrameIndex(int frameNum) {
		for (int i = 0; i < ecgFrames.size(); i++)
			if (ecgFrames.get(i).getFramNum() == frameNum)
				return i;
		return -1;
	}

	public EcgFrame getNextFrame(int curFrameNum) {
		int index = findEcgFrameIndex(curFrameNum);
		if (index != -1) {
			if (index < ecgFrames.size() - 1
					&& ecgFrames.get(index + 1).getFramNum() == curFrameNum + 1)
				return ecgFrames.get(index + 1);
		}
		return null;
	}

	/**
	 * 心电测量分析结果与心率值
	 * 
	 * @param bs
	 * @return
	 */
	public int[] getEcgResult(byte[] data) {
		return new int[] { data[5] & 0xff, data[7] & 0xff };
	}

	public void saveEcgFrams(EcgFrame frame) {
		StringBuilder sb = new StringBuilder();
		sb.append(frame.framNum);
		sb.append(":");
		int[] ecg = frame.getEcg();
		for (int j = 0; j < ecg.length; j++) {
			sb.append(ecg[j]);
		}
		sb.append("\n");
		FileManger.getInstance().append(sb.toString());
	}
}
