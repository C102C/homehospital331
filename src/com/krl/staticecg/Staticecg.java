package com.krl.staticecg;

public class Staticecg
{
	public static final String	CONFIGFILE				= "staticecg.cfg";
	public static final String	CONFIG_LAST_SYNC_TIME	= "LastSyncTime";
	public static final String	CONFIG_DS_SYNC_TIME		= "DSSyncTime";	//数据状态同步时间
	public static final String	CONFIG_RS_SYNC_TIME		= "RSSyncTime";	//报告状态同步时间
	public static final String	CONFIG_ACCOUNT			= "Account";
	public static final String	CONFIG_PASSWORD			= "Password";
	public static final String	CONFIG_SAVEPASSWORD		= "SavePwd";
	public static final String	CONFIG_CENTERID			= "CenterId";
	public static final String	CONFIG_CHECKSTATIONID	= "CheckStationId";
	public static final String	CONFIG_IP				= "IP";
	public static final String	CONFIG_PORT				= "Port";
	public static final String	CONFIG_PAGENUMBER		= "PageNumber";
	public static final String	CONFIG_SAMPLINGGTIME	= "SamplingTime";
	public static final String	CONFIG_AUTOUPLOAD		= "AutoUpload";
	public static final int		MESSAGE_NEW				= 1;
	public static final int		MESSAGE_SAMPLING		= 2;
	public static final int		MESSAGE_SYNC			= 3;
	public static final int		MESSAGE_UPLOAD_FILE		= 4;
	public static final int		MESSAGE_PATIENT_INFO	= 5;
	public static final int		MESSAGE_SYNC_DATA_STATE	= 6;
	public static final int		REQUESTCODE_LOGIN		= 1;
	public static final int		REQUESTCODE_NEW			= 2;
	public static final int		REQUESTCODE_SEARCH		= 3;
	public static final int		REQUESTCODE_SAMPLING	= 4;
	public static final String	SEARCH_NAME				= "name";
	// ////////////////////////////////////////////////////
}
