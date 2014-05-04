package com.krl.network;

import com.krl.tools.Tools;



public class NetworkPacket
{
	public static final int	HEADER_SIZE		= 8;
	private byte []			FLAG			= { 0xa, 0xb };
	private byte []			HEADER			= new byte[HEADER_SIZE];
	private byte			VERSION_INDEX	= 2;
	private byte			TYPE_INDEX		= 3;
	private byte			RESULT_INDEX	= 4;
	private byte			PARAM_INDEX		= 5;
	private byte			LENGTH_INDEX	= 6;
	private short			mLength			= 0;
	private int				mParser_size	= 0;
	private boolean			mInit_header	= false;
	private byte []			mData			= null;

	public NetworkPacket()
	{
		HEADER[0] = FLAG[0];
		HEADER[1] = FLAG[1];
		HEADER[VERSION_INDEX] = 0x01;
	}

	public int GetType()
	{
		return HEADER[TYPE_INDEX];
	}

	public int GetResult()
	{
		return HEADER[RESULT_INDEX];
	}

	public int GetParam()
	{
		return HEADER[PARAM_INDEX];
	}

	public byte [] GetData()
	{
		return mData;
	}

	public boolean IsCompleted()
	{
		return mLength == mParser_size;
	}

	public boolean IsValidPacket(byte [] buffer, int offset)
	{
		return (buffer[offset] == FLAG[0] && buffer[offset + 1] == FLAG[1]);
	}

	public boolean IsInited()
	{
		return mInit_header;
	}

	public byte [] Packet(byte type, byte res, byte param, byte [] data)
	{
		byte [] buffer = null;
		if (data != null)
		{
			byte [] len = Tools.shortToBytes((short) data.length);
			buffer = new byte[HEADER_SIZE + data.length];
			buffer[0] = FLAG[0];
			buffer[1] = FLAG[1];
			buffer[VERSION_INDEX] = 0x01;
			buffer[TYPE_INDEX] = type;
			buffer[RESULT_INDEX] = res;
			buffer[PARAM_INDEX] = param;
			buffer[LENGTH_INDEX] = len[0];
			buffer[LENGTH_INDEX + 1] = len[1];
			System.arraycopy(data, 0, buffer, HEADER_SIZE, data.length);
		}
		else
		{
			buffer = new byte[HEADER_SIZE];
			buffer[0] = FLAG[0];
			buffer[1] = FLAG[1];
			buffer[VERSION_INDEX] = 0x01;
			buffer[TYPE_INDEX] = type;
			buffer[RESULT_INDEX] = res;
			buffer[PARAM_INDEX] = param;
		}
		return buffer;
	}

	public byte [] Packet(byte type, byte res, byte param, byte [] data, int offset, int size)
	{
		byte [] buffer = null;
		if (data != null)
		{
			byte [] len = Tools.shortToBytes((short) data.length);
			buffer = new byte[HEADER_SIZE + data.length];
			buffer[0] = FLAG[0];
			buffer[1] = FLAG[1];
			buffer[VERSION_INDEX] = 0x01;
			buffer[TYPE_INDEX] = type;
			buffer[RESULT_INDEX] = res;
			buffer[PARAM_INDEX] = param;
			buffer[LENGTH_INDEX] = len[0];
			buffer[LENGTH_INDEX + 1] = len[1];
			System.arraycopy(data, offset, buffer, HEADER_SIZE, size);
		}
		else
		{
			buffer = new byte[HEADER_SIZE];
			buffer[0] = FLAG[0];
			buffer[1] = FLAG[1];
			buffer[VERSION_INDEX] = 0x01;
			buffer[TYPE_INDEX] = type;
			buffer[RESULT_INDEX] = res;
			buffer[PARAM_INDEX] = param;
		}
		return buffer;
	}

	public byte [] Packet(byte type, byte res, byte param, byte [] data, int offset, int size, byte [] buffer, int [] length)
	{
		length[0] = 0;
		if (data != null)
		{
			byte [] len = Tools.shortToBytes((short) data.length);
			length[0] = HEADER_SIZE + data.length;
			buffer[0] = FLAG[0];
			buffer[1] = FLAG[1];
			buffer[VERSION_INDEX] = 0x01;
			buffer[TYPE_INDEX] = type;
			buffer[RESULT_INDEX] = res;
			buffer[PARAM_INDEX] = param;
			buffer[LENGTH_INDEX] = len[0];
			buffer[LENGTH_INDEX + 1] = len[1];
			System.arraycopy(data, offset, buffer, HEADER_SIZE, size);
		}
		else
		{
			length[0] = HEADER_SIZE;
			buffer[0] = FLAG[0];
			buffer[1] = FLAG[1];
			buffer[VERSION_INDEX] = 0x01;
			buffer[TYPE_INDEX] = type;
			buffer[RESULT_INDEX] = res;
			buffer[PARAM_INDEX] = param;
		}
		return buffer;
	}

	public boolean Parse(byte [] buffer, int offset, int size, int [] parser_len)
	{
		int remain = 0, i = 0;
		parser_len[0] = 0;
		if (mInit_header != true)
		{
			for (i = 0; i < size; i++)
			{
				if (this.IsValidPacket(buffer, i + offset))
				{
					parser_len[0] += i;
					break;
				}
			}
			if ((size - i) < HEADER_SIZE)
			{
				parser_len[0] = 0;
				return false;
			}
			System.arraycopy(buffer, offset + parser_len[0], HEADER, 0, HEADER_SIZE);
			parser_len[0] += HEADER_SIZE;
			mParser_size = 0;
			mInit_header = true;
			mLength = Tools.bytesToShort(HEADER, 6);
			if (mLength == 0)
			{
				return true;
			}
			mData = new byte[mLength];
		}
		if (mData != null)
		{
			// 解析剩下的数据
			remain = Math.min(size - parser_len[0], mLength - mParser_size);
			System.arraycopy(buffer, parser_len[0] + offset, mData, mParser_size, remain);
			mParser_size += remain;
			parser_len[0] += remain;
		}
		return IsCompleted();
	}

	public void Reset()
	{
		mParser_size = 0;
		mInit_header = false;
		mData = null;
	}
}
