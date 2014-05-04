package com.krl.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper
{
	private static final String	TAG					= "DBHelper";
	public static final String	DATABASE_NAME		= "staticecgnet.db";
	public static final int		DATABASE_VERSION	= 1;
	public static final String	PATIENT_TABLE		= "persondata";
	// 创建表的SQL语句
	public static final String	DATABASE_CREATE		= "CREATE TABLE " + PATIENT_TABLE + " (" + PatientTableCol._ID + " integer primary key autoincrement," + PatientTableCol.ACCOUNT + " text," + PatientTableCol.CHECKSTATIONID + " integer," + PatientTableCol.DEPARTMENT + " text," + PatientTableCol.NAME + " text," + PatientTableCol.SEX + " text," + PatientTableCol.AGE + " text," + PatientTableCol.WEIGHT + " text," + PatientTableCol.HEIGHT + " text," + PatientTableCol.OUTPATIENTNUMBER + " text," + PatientTableCol.HOSPITALNUMBER + " text," + PatientTableCol.BEDNUMBER + " text," + PatientTableCol.SYMPTOM + " text," + PatientTableCol.PHONE + " text," + PatientTableCol.ADDRESS + " text," + PatientTableCol.POSTCODE + " text," + PatientTableCol.CREATEDTM + " text," + PatientTableCol.PERSONREMARK + " text," + PatientTableCol.DATAPATH + " text," + PatientTableCol.RELATIVEPATH + " text," + PatientTableCol.FILENAME + " text," + PatientTableCol.FILELENGTH + " integer," + PatientTableCol.FILEDESP + " text," + PatientTableCol.OCCURDTM + " text," + PatientTableCol.HANDLESTATE + " integer," + PatientTableCol.STARTHANDLEDTM + " text," + PatientTableCol.ENDHANDLEDTM + " text," + PatientTableCol.DIAGNOSE + " text," + PatientTableCol.DATAREMARK + " text," + PatientTableCol.HANDLEID + " integer," + PatientTableCol.DEL + " integer," + PatientTableCol.PERSONDATAID + " integer," + PatientTableCol.DATASYNCFLAG + " integer," + PatientTableCol.STATESYNCFLAG + " integer," + PatientTableCol.DATASTATE + " integer," + PatientTableCol.PERSONRECORDDTM + " text," + PatientTableCol.SAMPLINGSATE + " integer," + PatientTableCol.REPORTCOLLECTDTM + " text" + ")";
	public String				mTableName			= PATIENT_TABLE;

	public DBHelper(Context context, String tablename)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO 自动生成的构造函数存根
		mTableName = tablename;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO 自动生成的方法存根
		db.execSQL(getCreateTableSQL(mTableName));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO 自动生成的方法存根
		db.execSQL("DROP TABLE IF EXISTS " + mTableName);
		onCreate(db);
	}

	public static String getCreateTableSQL(String tablename)
	{
		return "CREATE TABLE " + tablename + " (" + PatientTableCol._ID + " integer primary key autoincrement," + PatientTableCol.ACCOUNT + " text," + PatientTableCol.CHECKSTATIONID + " integer," + PatientTableCol.DEPARTMENT + " text," + PatientTableCol.NAME + " text," + PatientTableCol.SEX + " text," + PatientTableCol.AGE + " text," + PatientTableCol.WEIGHT + " text," + PatientTableCol.HEIGHT + " text," + PatientTableCol.OUTPATIENTNUMBER + " text," + PatientTableCol.HOSPITALNUMBER + " text," + PatientTableCol.BEDNUMBER + " text," + PatientTableCol.SYMPTOM + " text," + PatientTableCol.PHONE + " text," + PatientTableCol.ADDRESS + " text," + PatientTableCol.POSTCODE + " text," + PatientTableCol.CREATEDTM + " text," + PatientTableCol.PERSONREMARK + " text," + PatientTableCol.DATAPATH + " text," + PatientTableCol.RELATIVEPATH + " text," + PatientTableCol.FILENAME + " text," + PatientTableCol.FILELENGTH + " integer," + PatientTableCol.FILEDESP + " text," + PatientTableCol.OCCURDTM + " text," + PatientTableCol.HANDLESTATE + " integer," + PatientTableCol.STARTHANDLEDTM + " text," + PatientTableCol.ENDHANDLEDTM + " text," + PatientTableCol.DIAGNOSE + " text," + PatientTableCol.DATAREMARK + " text," + PatientTableCol.HANDLEID + " integer," + PatientTableCol.DEL + " integer," + PatientTableCol.PERSONDATAID + " integer," + PatientTableCol.DATASYNCFLAG + " integer," + PatientTableCol.STATESYNCFLAG + " integer," + PatientTableCol.DATASTATE + " integer," + PatientTableCol.PERSONRECORDDTM + " text," + PatientTableCol.SAMPLINGSATE + " integer," + PatientTableCol.REPORTCOLLECTDTM + " text" + ")";
	}
}
