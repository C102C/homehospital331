package com.krl.data;

import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Log;

import com.krl.lzma.ICodeProgress;
import com.krl.tools.Tools;

public class DataSegment
{
	private static final String	TAG			= "DataSegment";
	public static final int		MAX_BUFFER	= 1024;			// bytes
	public String				mStartTime	= "";
	public DataDescription		mDesc		= null;
	public int [][]				mBuffer		= null;			// 原始数据
	public int					mMaxBuffer;
	public int					mMaxLead;
	public int					mDataSize;

	public Boolean Init(DataDescription desc, int MaxLead, int MaxBuffer)
	{
		if (mBuffer != null || MaxBuffer <= 0 || MaxBuffer <= 0)
		{
			return false;
		}
		mMaxLead = MaxLead;
		mMaxBuffer = MaxBuffer;
		try
		{
			mBuffer = new int[mMaxLead][mMaxBuffer];
		}
		catch (Exception e)
		{
			Log.v(TAG, e.toString());
			return false;
		}
		mStartTime = Tools.GetCurrentTime();
		mDesc = desc;
		return true;
	}

	public void SetSampling(int seconds)
	{
		try
		{
			mDataSize = 0;
			mMaxBuffer = mDesc.nSamplingFrequency * seconds;
			mBuffer = new int[mMaxLead][mMaxBuffer];
		}
		catch (Exception e)
		{
			Log.v(TAG, e.toString());
		}
	}

	public boolean IsCompleted()
	{
		return mDataSize >= mMaxBuffer;
	}

	public int RemainTime()
	{
		return (mMaxBuffer - mDataSize) / mDesc.nSamplingFrequency;
	}

	public Boolean Push(int [] data)
	{
		if (mBuffer == null || mDataSize >= mMaxBuffer)
			return false;
		for (int i = 0; i < mMaxLead; i++)
		{
			mBuffer[i][mDataSize] = data[i];
		}
		mDataSize++;
		return true;
	}

	public Boolean store(RandomAccessFile file, ICodeProgress progress) throws IOException
	{
		if (mDesc == null)
			return false;
		byte [] startTime = Tools.stringToBytes(mStartTime, 20);
		int size = mDataSize;
		file.write(startTime);
		file.write(Tools.intToBytes(size));
		mDesc.store(file);
		file.write(new byte[488]);
		if (progress != null)
		{
			progress.SetRange(0, mMaxLead * mDataSize);
		}
		byte [] tmpbuffer = new byte[mDataSize*4];
		for (int i = 0; i < mMaxLead; i++)
		{
			if (mDesc.bLeadEnable[i] == true)
			{
				if (progress != null)
				{
					for (int j = 0; j < mDataSize; j++)
					{
						Tools.intToBytes(mBuffer[i][j], tmpbuffer, j*4);
					}
					file.write(tmpbuffer);
					progress.SetProgress(i * mDataSize + mDataSize, 0);
				}
				else
				{
					for (int j = 0; j < mDataSize; j++)
					{
						Tools.intToBytes(mBuffer[i][j], tmpbuffer, j*4);
					}
					file.write(tmpbuffer);
				}
			}
		}
		return true;
	}
}
