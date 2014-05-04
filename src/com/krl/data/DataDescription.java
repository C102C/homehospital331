package com.krl.data;

import java.io.IOException;
import java.io.RandomAccessFile;

import com.krl.tools.Tools;
// 数据描述
public class DataDescription
{
	public static final int		BUFFER_SIZE			= 512;
	public static final int		LeadIndex_I			= 0;
	public static final int		LeadIndex_II		= 1;
	public static final int		LeadIndex_III		= 2;
	public static final int		LeadIndex_aVR		= 3;
	public static final int		LeadIndex_aVL		= 4;
	public static final int		LeadIndex_aVF		= 5;
	public static final int		LeadIndex_V1		= 6;
	public static final int		LeadIndex_V2		= 7;
	public static final int		LeadIndex_V3		= 8;
	public static final int		LeadIndex_V4		= 9;
	public static final int		LeadIndex_V5		= 10;
	public static final int		LeadIndex_V6		= 11;
	public static final int		LeadIndex_X			= 0;
	public static final int		LeadIndex_Y			= 1;
	public static final int		LeadIndex_Z			= 2;
	public static final int		LeadIndex_XY		= 3;
	public static final int		LeadIndex_XZ		= 4;
	public static final int		LeadIndex_ZY		= 5;
	public static final int		MAX_ECG_LEAD_COUNT	= 12;
	public static final int		MAX_VCG_LEAD_COUNT	= 6;
	// 1298的普通心电数据
	public static final int		SAMPLE_FREQUENCY	= 1000;
	// 放大倍数
	// 1298的普通心电数据
	public static final float	ECG_SCALE			= 0.50895f;
	// 心电向量放大倍数
	public static final float	VCG_SCALE			= 500.0f;
	//
	// 向量数据较正系数
	public static final float	VCG_ADJUST_X		= 1.432f;
	public static final float	VCG_ADJUST_Y		= 1.184f;
	public static final float	VCG_ADJUST_Z		= 1.808f;
	// 数据类型
	// 常规心电数据
	public static final byte	TYPE_ECG			= 0;
	// 心电向量数据
	public static final byte	TYPE_VCG			= 1;
	// 常规心电数据范围
	public static final float	VOLTAGELOW_ECG		= (-2500.0f);
	public static final float	VOLTAGEHIGH_ECG		= 2500.0f;
	public static final int		DIGVOLTAGELOW_ECG	= (-0x007fffff);
	public static final int		DIGVOLTAGEHIGH_ECG	= 0x007fffff;
	// 向量心电数据范围
	public static final float	VOLTAGELOW_VCG		= 0.0f;
	public static final float	VOLTAGEHIGH_VCG		= 3300.0f;
	public static final int		DIGVOLTAGELOW_VCG	= 0x0000;
	public static final int		DIGVOLTAGEHIGH_VCG	= 0x0fff;
	// 描述所占内在空间的大小
	public static final int		MAX_BUFFER			= 512;								// bytes
	byte []						wVersion			= new byte[] { 0, 1 };				// 版本号,目前为1
	public int					nSamplingFrequency	= SAMPLE_FREQUENCY;				// 采样频率
	public int					nLeadCount			= MAX_ECG_LEAD_COUNT;				// 导联数
	public byte					tType				= 0;								// 数据类型
																						// 0普通心电
																						// 1向量心电
	public float				fVoltageLow			= VOLTAGELOW_ECG;					// 最低电压值
	public float				fVoltageHigh		= VOLTAGEHIGH_ECG;					// 最高电压值
	public int					nDigvoltageLow		= DIGVOLTAGELOW_ECG;				// 最低数值电压值
	public int					nDigvoltageHigh		= DIGVOLTAGEHIGH_ECG;				// 最高数值电压值
	public float				fScale				= ECG_SCALE;						// 放大倍数
	public boolean				bBP					= false;							// 是否有起搏器信号
																						// 采集数据时所做的滤波使能，故在加载时，需要按采集选择进行滤波操作
																						// 0x01(基线调整) | 0x02(50Hz干扰) | 0x04(10平均)
	public byte					tFilterType;
	public boolean []			bLeadEnable			= new boolean[MAX_ECG_LEAD_COUNT];

	//
	//
	public double GetDataValuePerMv()
	{
		return 1.0 / GetMvPerSample();
	}

	public double GetMvPerSample()
	{
		return ((fVoltageHigh - fVoltageLow) / fScale) / (double) (nDigvoltageHigh - nDigvoltageLow);
	}

	public boolean store(RandomAccessFile file) throws IOException
	{
		file.write(wVersion);
		file.write(Tools.intToBytes(nSamplingFrequency));
		file.write(Tools.intToBytes(nLeadCount));
		file.writeByte(tType);
		file.write(Tools.floatToBytes(fVoltageLow));
		file.write(Tools.floatToBytes(fVoltageHigh));
		file.write(Tools.intToBytes(nDigvoltageLow));
		file.write(Tools.intToBytes(nDigvoltageHigh));
		file.write(Tools.floatToBytes(fScale));
		file.writeBoolean(bBP);
		file.writeByte(tFilterType);
		for (int i = 0; i < MAX_ECG_LEAD_COUNT; i++)
		{
			file.writeBoolean(bLeadEnable[i]);
		}
		file.write(new byte[BUFFER_SIZE - 45]);
		return true;
	}

	public boolean load(RandomAccessFile file) throws IOException
	{
		file.read(wVersion, 0, 2);
		nSamplingFrequency = file.readInt();
		nLeadCount = file.readInt();
		tType = file.readByte();
		fVoltageLow = file.readFloat();
		fVoltageHigh = file.readFloat();
		nDigvoltageLow = file.readInt();
		nDigvoltageHigh = file.readInt();
		fScale = file.readFloat();
		bBP = file.readBoolean();
		tFilterType = file.readByte();
		for (int i = 0; i < MAX_ECG_LEAD_COUNT; i++)
		{
			bLeadEnable[i] = file.readBoolean();
		}
		return true;
	}
}
