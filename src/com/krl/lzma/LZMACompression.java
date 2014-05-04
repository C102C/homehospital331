package com.krl.lzma;

import java.io.IOException;
public class LZMACompression
{
	public boolean Compress(String InFile, String OutFile, ICodeProgress progress) throws IOException
	{
		java.io.File inFile = new java.io.File(InFile);
		java.io.File outFile = new java.io.File(OutFile);
		java.io.BufferedInputStream inStream = new java.io.BufferedInputStream(new java.io.FileInputStream(inFile));
		java.io.BufferedOutputStream outStream = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outFile));
		{
			Encoder encoder = new Encoder();
			try
			{
				boolean eos = false;
				encoder.SetEndMarkerMode(eos);
				encoder.WriteCoderProperties(outStream);
				long fileSize = -1;
				if (eos)
					fileSize = -1;
				else
					fileSize = inFile.length();
				for (int i = 0; i < 8; i++)
					outStream.write((int) (fileSize >>> (8 * i)) & 0xFF);
				if (progress != null)
				{
					progress.SetRange(0, fileSize);
				}
				encoder.Code(inStream, outStream, -1, -1, progress);
			}
			catch (IOException e)
			{
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
