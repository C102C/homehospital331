package com.health.remoteask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RemoteDBHelper extends SQLiteOpenHelper{

	public final static String DB_NAME = "contact";
	public final static int VERSION = 1;
	public static int num = 0;
	
	private static RemoteDBHelper instance = null;
	
	private SQLiteDatabase db;
	
	public static RemoteDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new RemoteDBHelper(context);
		}
		return instance;
	}
	
	public void openDatabase() {
		if (db == null) {
			db = this.getWritableDatabase();
		}
	}
	
	public RemoteDBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	public void onCreate(SQLiteDatabase db) {
		StringBuffer tableCreate = new StringBuffer();
		tableCreate.append("create table user ( _id integer primary key autoincrement,")
					.append("name text,")
					.append("no integer,")
					.append("phone text )");
		db.execSQL(tableCreate.toString());
		
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists user";
		db.execSQL(sql);
		onCreate(db);
		
	}
	
	public long save(RemoteUser user) {
		openDatabase();
		ContentValues values = new ContentValues();
		values.put("name", user.name);
		values.put("phone", user.phone);
		return db.insert("user", null, values);
	}
	
	public void delete(String phone) {
		openDatabase();
		db.delete("user", "phone=?", new String[]{String.valueOf(phone)});
	}
	
	
	public ArrayList getUserList() {
		openDatabase();
		ArrayList userList = new ArrayList();
		Cursor cursor = db.query("user", null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			HashMap map = new HashMap();
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
			userList.add(map);
		}
		return userList;
	}

}
