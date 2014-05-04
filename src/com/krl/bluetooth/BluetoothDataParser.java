package com.krl.bluetooth;

import com.krl.data.DataDescription;
import com.krl.data.DataProcess;
import com.krl.data.DataSegment;


public class BluetoothDataParser
{
	private static final String	TAG					= "BluetoothDataParser";
	private static final int	AVG_SIZE			= 10;
	private byte []				mSrcData			= null;
	private int					mMaxBuffer			= 0;
	private int					mLeadNum			= 0;
	private int					mInCurSize			= 0;
	private int					mCurIndex			= 0;
	private int [][]			mRealShowBuffer		= null;
	private int []				mOneSamplingPoint	= null;
	private double []			mTotal				= null;
	private int [][]			AdjustBaseline		= null;
	private int []				mFilterData			= null;
	private int [][]			mAvg				= null;
	private double [][]			mRealData50HzX		= null;
	private double [][]			mRealData50HzY		= null;
	private double [][]			mRealData50HzX_2	= null;
	private double [][]			mRealData50HzY_2	= null;
	private double []			mVCGAdjustFlag		= new double[3];
	public DataSegment			mDataSegment		= null;
	public boolean				mStartSampling		= false;
	public int					mScreenWidth		= 0;
	public int					mIndex				= 0;
	public int					mBatteryVol			= 0;
	private REAL_ECG []			pRealEcg			= new REAL_ECG[8];

	private class REAL_ECG
	{
		public int	High;	//
		public int	Mid;
		public int	Low;	//
	};

	// //////////////////////
	public BluetoothDataParser(int nBufferSize, int nPackSize, int leadNum, DataSegment segment)
	{
		mMaxBuffer = nBufferSize;
		mSrcData = new byte[mMaxBuffer];
		mLeadNum = leadNum;
		mDataSegment = segment;
		mOneSamplingPoint = new int[mLeadNum];
		mTotal = new double[mLeadNum];
		AdjustBaseline = new int[mLeadNum][1024];
		mFilterData = new int[mLeadNum];
		mAvg = new int[mLeadNum][AVG_SIZE];
		mRealData50HzX = new double[mLeadNum][3];
		mRealData50HzY = new double[mLeadNum][3];
		mRealData50HzX_2 = new double[mLeadNum][3];
		mRealData50HzY_2 = new double[mLeadNum][3];
		mVCGAdjustFlag[DataDescription.LeadIndex_X] = DataDescription.VCG_ADJUST_X;
		mVCGAdjustFlag[DataDescription.LeadIndex_Y] = DataDescription.VCG_ADJUST_Y;
		mVCGAdjustFlag[DataDescription.LeadIndex_Z] = DataDescription.VCG_ADJUST_Z;
		for (int i = 0; i < 8; i++)
		{
			pRealEcg[i] = new REAL_ECG();
		}
		mRealShowBuffer = new int[mLeadNum][segment.mDesc.nSamplingFrequency * 10];
		clear();
	}

	public int [][] GetShowBuffer()
	{
		return mRealShowBuffer;
	}

	public int push(byte [] in, int offset, int size, int maxsize)
	{
		synchronized (this.mSrcData)
		{
			int remain = Math.min(mMaxBuffer - mInCurSize, size);
			System.arraycopy(in, offset, mSrcData, mInCurSize, size);
			mInCurSize += size;
			mInCurSize %= mMaxBuffer;
			return (offset + remain) % maxsize;
		}
	}

	public void clear()
	{
		{
			mInCurSize = 0;
			mCurIndex = 0;
		}
	}

	public int GetCurSize()
	{
		return mInCurSize;
	}

	public int GetCurIndex()
	{
		return mCurIndex;
	}

	// 抑制基线漂移
	public int InhibitionBaselineDrift(final int nNewData, int nIndex, int [] pSrcData, double [] s, int pos)
	{
		final int m = 500;// 窗口宽度，可以作为参数调整
		int n = 0;
		nIndex = nIndex % (m * 2 + 1);
		s[pos] -= pSrcData[nIndex];
		pSrcData[nIndex] = nNewData;
		s[pos] += pSrcData[nIndex];
		nIndex = (nIndex - m + m * 2 + 1) % (m * 2 + 1);
		n = (int) (pSrcData[nIndex] - s[pos] / (m * 2 + 1));
		// */
		return n;
	}

	private boolean ParserECG()
	{
		//TODO:yangjiakun
		synchronized (mSrcData)
		{
			int i = 0, j = 0, index = 0;
			int inCurSize = GetCurSize();
			while ((i + 30) <= inCurSize)
			{
				if (((mSrcData[i] & 0xf0) == 0xc0 && (mSrcData[i + 2] & 0x0e) == 0x0e) && ((mSrcData[i + 27] & 0xf0) == 0xc0 && (mSrcData[i + 29] & 0x0e) == 0x0e))
				{
					// (mSrcData[i+2]&0x01) 起搏信号标识
					// ////////////////////////////////////////////////////////////////////////
					// 取得最新的原始采集心电数据
					i += 3;
					for (j = 0; j < 8; j++)
					{
						pRealEcg[j].High = (mSrcData[i] + 0x100) % 0x100;
						pRealEcg[j].Mid = (mSrcData[i + 1] + 0x100) % 0x100;
						pRealEcg[j].Low = (mSrcData[i + 2] + 0x100) % 0x100;
						i += 3;
					}
					for (j = 0; j < 2; j++)
					{// I、II
						mOneSamplingPoint[j] = (pRealEcg[j].High << 16) + (pRealEcg[j].Mid << 8) + pRealEcg[j].Low;
						if ((pRealEcg[j].High & 0x80) == 0x80)
						{
							mOneSamplingPoint[j] = -(0x1000000 - mOneSamplingPoint[j]);
						}
						// if(j==0)
						// Log.e(TAG,"DATA:"+mOneSamplingPoint[j]+" "+pRealEcg[j].High+" "+pRealEcg[j].Mid+" "+pRealEcg[j].Low);
					}
					for (j = 2; j < 8; j++)
					{// V1--V6
						mOneSamplingPoint[4 + j] = (pRealEcg[j].High << 16) + (pRealEcg[j].Mid << 8) + pRealEcg[j].Low;
						if ((pRealEcg[j].High & 0x80) == 0x80)
							mOneSamplingPoint[4 + j] = -(0x1000000 - mOneSamplingPoint[4 + j]);
					}
					// ////////////////////////////////////////////////////////////////////////
					// 抑制基线漂移
					{
						for (j = DataDescription.LeadIndex_I; j <= DataDescription.LeadIndex_V6; j++)
						{
							mFilterData[j] = InhibitionBaselineDrift(mOneSamplingPoint[j], mCurIndex, AdjustBaseline[j], mTotal, j);
						}
					}
					// ////////////////////////////////////////////////////////////////////////
					// 50Hz陷波
					{
						for (j = DataDescription.LeadIndex_I; j <= DataDescription.LeadIndex_V6; j++)
						{
							// ////////////////////////////////////////////////////////////////////////
							// 50Hz干扰滤波
							mRealData50HzX[j][0] = mRealData50HzX[j][1];
							mRealData50HzX[j][1] = mRealData50HzX[j][2];
							mRealData50HzX[j][2] = mFilterData[j];
							mRealData50HzY[j][0] = mRealData50HzY[j][1];
							mRealData50HzY[j][1] = mRealData50HzY[j][2];
							mFilterData[j] = (int) DataProcess.Filter50HzDisturbance(mRealData50HzX[j], mRealData50HzY[j]);
						}
						//						for (j = DataDescription.LeadIndex_I; j <= DataDescription.LeadIndex_V6; j++)
						//						{
						//							// ////////////////////////////////////////////////////////////////////////
						//							// 50Hz干扰滤波
						//							mRealData50HzX_2[j][0] = mRealData50HzX_2[j][1];
						//							mRealData50HzX_2[j][1] = mRealData50HzX_2[j][2];
						//							mRealData50HzX_2[j][2] = mFilterData[j];
						//
						//							mRealData50HzY_2[j][0] = mRealData50HzY_2[j][1];
						//							mRealData50HzY_2[j][1] = mRealData50HzY_2[j][2];
						//
						//							mFilterData[j] = (int) DataProcess.Filter50HzDisturbance(mRealData50HzX_2[j], mRealData50HzY_2[j]);
						//						}
					}
					// 5点平均
					{
						int k = 0;
						for (j = DataDescription.LeadIndex_I; j <= DataDescription.LeadIndex_V6; j++)
						{
							mAvg[j][mCurIndex % AVG_SIZE] = mFilterData[j];
							mFilterData[j] = 0;
							for (k = 0; k < AVG_SIZE; k++)
							{
								mFilterData[j] += mAvg[j][k];
							}
							mFilterData[j] /= AVG_SIZE;
						}
					}
					// ////////////////////////////////////////////////////////////////////////
					// 计算实时显示的合成通道数据
					// Ⅲ=Ⅱ-Ⅰ
					mFilterData[DataDescription.LeadIndex_III] = mFilterData[DataDescription.LeadIndex_II] - mFilterData[DataDescription.LeadIndex_I];
					// aVR=-(Ⅰ+Ⅱ) /2
					mFilterData[DataDescription.LeadIndex_aVR] = -(mFilterData[DataDescription.LeadIndex_I] + mFilterData[DataDescription.LeadIndex_II]) / 2;
					// aVL=Ⅰ-Ⅱ/2
					mFilterData[DataDescription.LeadIndex_aVL] = mFilterData[DataDescription.LeadIndex_I] - mFilterData[DataDescription.LeadIndex_II] / 2;
					// aVF=Ⅱ-Ⅰ/2
					mFilterData[DataDescription.LeadIndex_aVF] = mFilterData[DataDescription.LeadIndex_II] - mFilterData[DataDescription.LeadIndex_I] / 2;
					// ////////////////////////////////////////////////////////////////////////
					if (mStartSampling == true && mDataSegment.mDataSize < mDataSegment.mMaxBuffer)
					{
						index = mCurIndex % mScreenWidth;
						if (mDataSegment.mDataSize >= 0)
						{
							for (j = DataDescription.LeadIndex_I; j <= DataDescription.LeadIndex_V6; j++)
							{
								// 保存实时采集的原始数据
								mDataSegment.mBuffer[j][mDataSegment.mDataSize] = mOneSamplingPoint[j];
								mRealShowBuffer[j][index] = mFilterData[j];
							}
						}
						mDataSegment.mDataSize++;
					}
					else
					{
						index = mCurIndex % mScreenWidth;
						for (j = DataDescription.LeadIndex_I; j <= DataDescription.LeadIndex_V6; j++)
						{
							// 保存实时采集的原始数据
							mRealShowBuffer[j][index] = mFilterData[j];
						}
					}
					// ////////////////////////////////////////////////////////////////////////
					mCurIndex++;
				}
				else
				{
					i++;
				}
			}
			for (j = 0; i < mInCurSize; j++, i++)
			{
				mSrcData[j] = mSrcData[i];
			}
			mInCurSize = j;
		}
		return true;
	}

	public boolean Parser()
	{
		if (mDataSegment == null)
			return false;
		if (mLeadNum == 12)
		{
			return ParserECG();
		}
		if (mLeadNum == 3)
		{
			return ParserVCG();
		}
		return false;
	}

	private boolean ParserVCG()
	{
		// ////////////////////////////////////////////////////////////////////////
		int i = 0, j;
		while ((i + 6) <= mInCurSize)
		{
			if ((mSrcData[i + 1] & 0xf0) == 0xc0 && (mSrcData[i + 3] & 0xf0) == 0xd0 && (mSrcData[i + 5] & 0xf0) == 0xe0)
			{
				mOneSamplingPoint[DataDescription.LeadIndex_X] = mSrcData[i] + ((mSrcData[i + 1] & 0x0f) << 8);
				mOneSamplingPoint[DataDescription.LeadIndex_Y] = mSrcData[i + 2] + ((mSrcData[i + 3] & 0x0f) << 8);
				mOneSamplingPoint[DataDescription.LeadIndex_Z] = 0x1000 - (mSrcData[i + 4] + ((mSrcData[i + 5] & 0x0f) << 8));// Z导反转一次
				i += 6;
				// ////////////////////////////////////////////////////////////////////////
				{
					for (j = DataDescription.LeadIndex_X; j <= DataDescription.LeadIndex_Z; j++)
					{
						mFilterData[j] = InhibitionBaselineDrift(mOneSamplingPoint[j], mCurIndex, AdjustBaseline[j], mTotal, j);
					}
				}
				// 开启50Hz滤波，替换最新一个点数据
				{
					for (j = DataDescription.LeadIndex_X; j <= DataDescription.LeadIndex_Z; j++)
					{
						mRealData50HzX[j][0] = mRealData50HzX[j][1];
						mRealData50HzX[j][1] = mRealData50HzX[j][2];
						mRealData50HzX[j][2] = mFilterData[j];
						mRealData50HzY[j][0] = mRealData50HzY[j][1];
						mRealData50HzY[j][1] = mRealData50HzY[j][2];
						mFilterData[j] = (int) DataProcess.Filter50HzDisturbance(mRealData50HzX[j], mRealData50HzY[j]);
					}
				}
				// ////////////////////////////////////////////////////////////////////////
				// 给实时显示数据赋值
				{
					if (mDataSegment.mDataSize >= 0)
					{
						for (j = DataDescription.LeadIndex_X; j <= DataDescription.LeadIndex_Z; j++)
						{
							// 保存实时采集的原始数据
							mDataSegment.mBuffer[j][mDataSegment.mDataSize] = mOneSamplingPoint[j];
						}
					}
					mDataSegment.mDataSize++;
				}
				// ////////////////////////////////////////////////////////////////////////
				mCurIndex++;
			}
			else
			{
				i++;
			}
		}
		for (j = 0; i < mInCurSize; j++, i++)
		{
			mSrcData[j] = mSrcData[i];
		}
		mInCurSize = j;
		return true;
	}
}
