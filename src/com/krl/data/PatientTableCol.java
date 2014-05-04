package com.krl.data;

import android.provider.BaseColumns;
public class PatientTableCol implements BaseColumns
{
	// 字段名
	public static final String	ACCOUNT					= "account";			// 帐号
	public static final String	CHECKSTATIONID			= "checkstationid";	// 检测站ID
	public static final String	DEPARTMENT				= "department";		// 部门科室
	public static final String	NAME					= "name";				// 姓名
	public static final String	SEX						= "sex";				// 性别
	public static final String	AGE						= "age";				// 年龄
	public static final String	WEIGHT					= "weight";			// 体重
	public static final String	HEIGHT					= "height";			// 身高
	public static final String	OUTPATIENTNUMBER		= "outpatientnumber";	// 门诊号
	public static final String	HOSPITALNUMBER			= "hospitalnumber";	// 住院号
	public static final String	BEDNUMBER				= "bednumber";			// 床号
	public static final String	SYMPTOM					= "symptom";			// 症状
	public static final String	PHONE					= "phone";				// 电话
	public static final String	ADDRESS					= "address";			// 地址
	public static final String	POSTCODE				= "postcode";			// 邮编
	public static final String	CREATEDTM				= "createdtm";			// 创建时刻
	public static final String	PERSONREMARK			= "personremark";		// 人员备注
	public static final String	RELATIVEPATH			= "relativepath";		// 相对路径
	public static final String	FILENAME				= "filename";			// 文件名称
	public static final String	FILELENGTH				= "filelength";		// 文件长度
	public static final String	FILEDESP				= "filedesp";			// 文件描述
	public static final String	OCCURDTM				= "occurdtm";			// 发生时刻
	public static final String	HANDLESTATE				= "handlestate";		// 处理状态
	public static final String	STARTHANDLEDTM			= "starthandledtm";	// 报告处理开始时刻
	public static final String	ENDHANDLEDTM			= "endhandledtm";		// 报告处理结束时刻
	public static final String	DIAGNOSE				= "diagnose";			// 诊断
	public static final String	DATAPATH				= "datapath";			// 保存全路径
	public static final String	DATAREMARK				= "dataremark";		// 备注
	public static final String	HANDLEID				= "handleid";			// 处理ID
	public static final String	DEL						= "del";				// 删除标识
	public static final String	PERSONDATAID			= "persondataid";		// 服务器人员数据ID,可以作为人员数据同步标识
	public static final String	DATASYNCFLAG			= "datasyncflag";		// 数据同步标识,0未同步,1已经同步
	public static final String	STATESYNCFLAG			= "statesyncflag";		// 状态同步标识,0该记录有离线操作未提交,1已经提交
	public static final String	DATASTATE				= "datastate";			// 数据状态:0未采集,1已经采集,2需要重采,3已经重采
	public static final String	PERSONRECORDDTM			= "personrecorddtm";	// 信息同步时间
	public static final String	SAMPLINGSATE			= "samplingstate";		// 数据采集状态
	public static final String	REPORTCOLLECTDTM		= "reportcollectdtm";	// 上传报告文件的时间
																				// 字段索引
	public static final int		_ID_COL					= 0;
	public static final int		ACCOUNT_COL				= 1;
	public static final int		CHECKSTATIONID_COL		= 2;
	public static final int		DEPARTMENT_COL			= 3;
	public static final int		NAME_COL				= 4;
	public static final int		SEX_COL					= 5;
	public static final int		AGE_COL					= 6;
	public static final int		WEIGHT_COL				= 7;
	public static final int		HEIGHT_COL				= 8;
	public static final int		OUTPATIENTNUMBER_COL	= 9;
	public static final int		HOSPITALNUMBER_COL		= 10;
	public static final int		BEDNUMBER_COL			= 11;
	public static final int		SYMPTOM_COL				= 12;
	public static final int		PHONE_COL				= 13;
	public static final int		ADDRESS_COL				= 14;
	public static final int		POSTCODE_COL			= 15;
	public static final int		CREATEDTM_COL			= 16;
	public static final int		PERSONREMARK_COL		= 17;
	public static final int		RELATIVEPATH_COL		= 18;
	public static final int		DATAPATH_COL			= 19;
	public static final int		FILENAME_COL			= 20;
	public static final int		FILELENGTH_COL			= 21;
	public static final int		FILEDESP_COL			= 22;
	public static final int		OCCURDTM_COL			= 23;
	public static final int		HANDLESTATE_COL			= 24;
	public static final int		STARTHANDLEDTM_COL		= 25;
	public static final int		ENDHANDLEDTM_COL		= 26;
	public static final int		DIAGNOSE_COL			= 27;
	public static final int		DATAREMARK_COL			= 28;
	public static final int		HANDLEID_COL			= 29;
	public static final int		DEL_COL					= 30;
	public static final int		PERSONDATAID_COL		= 31;
	public static final int		DATASYNCFLAG_COL		= 32;
	public static final int		STATESYNCFLAG_COL		= 33;
	public static final int		DATASTATE_COL			= 34;
	public static final int		PERSONRECORDDTM_COL		= 35;
	public static final int		SAMPLINGSATE_COL		= 36;
	public static final int		REPORTCOLLECTDTM_COL	= 37;
}
