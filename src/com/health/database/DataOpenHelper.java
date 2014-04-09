package com.health.database;

import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "health.db";
	private static final int DATABASVERSION = 1;

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
		builder.append("(sysId integer PRIMARY KEY autoincrement,");
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
}
