package com.krl.network;

public class NetworkPacketType
{
	private static final String	TAG					= "NetworkPacketType";
	public static final byte	LOGIN				= 1;
	public static final byte	SYNC_PATIENT_INFO	= 2;
	public static final byte	PATIENT_INFO		= 3;
	public static final byte	UPLOAD_DATAFILE		= 4;
	public static final byte	GETDATASTATE		= 5;
	public static final byte	SYNC_REPORT_STATE	= 6;
}
