package com.krl.lzma;

public interface ICodeProgress {
	public void SetRange(long low, long hight);

	public void SetProgress(long inSize, long outSize);

	public void finished();

	public void onSaveSuccess(String occurdtm, String fileName,
			String filedesc, int fileLength, int dataState, int samplingstate);
}
