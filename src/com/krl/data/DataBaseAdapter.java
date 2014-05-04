package com.krl.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.krl.staticecg.StaticApp;

public class DataBaseAdapter {
	private static final String TAG = "DataBaseAdapter";
	private Context mContext = null;
	private SQLiteDatabase mSQLiteDataBase = null;
	private DBHelper mDBHelper = null;
	private String mTableName = "persondata";

	public DataBaseAdapter(Context context) {
		mContext = context;
	}

	public String getTableName() {
		return mTableName;
	}

	// 打开数据库
	public void open(String tablename) throws SQLException {
		if (mSQLiteDataBase == null) {
			this.mDBHelper = new DBHelper(mContext, tablename);
			this.mSQLiteDataBase = mDBHelper.getWritableDatabase();
		}
		String sql = "select count(*) from Sqlite_master where type='table' and name='"
				+ tablename.trim() + "'";
		Cursor s = mSQLiteDataBase.rawQuery(sql, null);
		if (s.moveToLast()) {
			int n = s.getInt(0);
			if (n < 1) {
				mSQLiteDataBase.execSQL(DBHelper.getCreateTableSQL(tablename));
			}
		}
		mTableName = tablename;
		StaticApp.getinstance().mTableName = mTableName;
	}

	// 关闭数据库
	public void close() {
		mSQLiteDataBase = null;
		if (mDBHelper != null)
			mDBHelper.close();
	}

	// 插入一条数据
	public long insert(ContentValues cv) {
		return this.mSQLiteDataBase.insert(mTableName, null, cv);
	}

	// 删除一条数据
	public int delete(String[] account) {
		return this.mSQLiteDataBase.delete(mTableName, PatientTableCol.ACCOUNT
				+ "=?", account);
	}

	// 修改一条数据
	public int update(ContentValues cv, String whereClause, String[] whereArgs) {
		return this.mSQLiteDataBase.update(mTableName, cv, whereClause,
				whereArgs);
	}

	// 查询所有数据
	public Cursor QueryAll() {
		return this.mSQLiteDataBase.query(mTableName, null, null, null, null,
				null, PatientTableCol._ID);
	}

	// 查询指定数据
	public Cursor Query(String selection, String[] selectionArgs, String orderBy) {
		return this.mSQLiteDataBase.query(mTableName, null, selection,
				selectionArgs, null, null, orderBy);
	}

	//
	public Cursor Query(String sql, String[] selectionArgs) {
		return this.mSQLiteDataBase.rawQuery(sql, null);
	}

	public Cursor Query(int id) {
		return this.mSQLiteDataBase.query(mTableName, null, PatientTableCol._ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null);
	}
}
