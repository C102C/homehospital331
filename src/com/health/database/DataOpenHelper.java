package com.health.database;

import java.io.File;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.health.util.L;

public class DataOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "health.db";
	private static final int DATABASVERSION = 1;
	private static final String TAG = "DataOpenHelper";
	public static final String SYS_ID = "sysId";

	public DataOpenHelper(Context context) {
		super(context, DATABASENAME, null, DATABASVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Tables tables = new Tables();
		// 建表
		db.execSQL(createSql(tables.pulseTable()));
		db.execSQL(createSql(tables.tempTable()));
		db.execSQL(createSql(tables.bpTable()));
		db.execSQL(createSql(tables.boTable()));
		db.execSQL(createSql(tables.gluTable()));
		db.execSQL(createSql(tables.uaTable()));
		db.execSQL(createSql(tables.cholTable()));
		db.execSQL(createSql(tables.urineTable()));
		db.execSQL(createSql(tables.vaccHeadTable()));
		db.execSQL(createSql(tables.vaccRecordTable()));
		db.execSQL(createSql(tables.babyVisitTable()));
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	/**
	 * 从map中解析出建表的sql
	 * 
	 * @param tableDesc
	 * @return
	 */
	private String createSql(Map<String, String> tableDesc) {
		StringBuilder builder = new StringBuilder();
		builder.append("create table ");
		builder.append(tableDesc.get(Tables.TABLE_NAME));
		tableDesc.remove(Tables.TABLE_NAME);// 删除表名
		builder.append("(");
		builder.append(SYS_ID);
		builder.append(" integer PRIMARY KEY autoincrement,");
		for (Map.Entry<String, String> entry : tableDesc.entrySet()) {
			builder.append(entry.getKey());
			builder.append(" ");
			builder.append(entry.getValue());
			builder.append(",");
		}
		String sql = builder.toString();
		sql = sql.substring(0, sql.length() - 1);// 删掉最后一个逗号
		return sql + ")";

	}

	/***
	 * 刪除整個数据库
	 */
	@SuppressLint("NewApi")
	public static void deleteDb() {
		boolean state = SQLiteDatabase.deleteDatabase(new File(DATABASENAME));
		L.i(TAG, "deleteDb:" + state);

	}
}
