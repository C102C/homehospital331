package com.health.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseService {
	private static final String TAG = "DatabaseService";
	private DataOpenHelper dbOpenHelper;

	public DatabaseService(Context context) {
		super();
		this.dbOpenHelper = new DataOpenHelper(context);
	}

	/**
	 * 根据id号码和表描述查询记录
	 * 
	 * @param idCard
	 * @param tableDesc
	 * @return
	 */
	public List<Map<String, String>> select(String idCard,
			Map<String, String> tableDesc) {
		String tableName = tableDesc.get(Tables.TABLE_NAME);
		tableDesc.remove(Tables.TABLE_NAME);
		StringBuilder selectBuilder = new StringBuilder("select * from ");
		selectBuilder.append(tableName);
		selectBuilder.append(" where ");
		selectBuilder.append(Tables.CARDNO);
		selectBuilder.append(" = ?");
		Set<String> attrSet = tableDesc.keySet();
		String[] selectAttrs = attrSet.toArray(new String[0]);
		String[] keys = { idCard };
		return select(selectBuilder.toString(), keys, selectAttrs);
	}

	/**
	 * 插入记录
	 * 
	 * @param tableDesc表描述
	 * @param ValueMap插入值
	 */
	public void insert(Map<String, String> tableDesc,
			Map<String, String> ValueMap) {
		String tableName = tableDesc.get(Tables.TABLE_NAME);
		tableDesc.remove(Tables.TABLE_NAME);
		Set<String> attrSet = tableDesc.keySet();
		String[] insertAttrs = attrSet.toArray(new String[0]);
		String[] insertValue = new String[insertAttrs.length];
		StringBuilder askBuilder = new StringBuilder("values(");// 问号构造，占位符
		StringBuilder sqlBuilder = new StringBuilder("insert into ");
		sqlBuilder.append(tableName);
		sqlBuilder.append("(");
		for (int i = 0; i < insertAttrs.length; i++) {
			sqlBuilder.append(insertAttrs[i]);
			sqlBuilder.append(",");
			askBuilder.append("?,");
			insertValue[i] = ValueMap.get(insertAttrs[i]);
		}
		String insertSql = sqlBuilder.toString();
		insertSql = insertSql.substring(0, insertSql.length() - 1);// 删除最后一个逗号
		insertSql += ")";
		String asks = askBuilder.toString();
		asks = asks.substring(0, asks.length() - 1);// 删除最后一个逗号
		insertSql += asks + ")";
		Log.i(TAG, insertSql);
		insert(insertSql, insertValue);
	}

	/**
	 * 根据条件从数据库中取数据
	 * 
	 * @param selectSql
	 * @param keys
	 * @param attrs
	 * @return
	 */
	private List<Map<String, String>> select(String selectSql, String[] keys,
			String[] attrs) {
		Log.i(TAG, selectSql + Arrays.toString(keys));
		SQLiteDatabase findDb = dbOpenHelper.getReadableDatabase();
		Cursor cursor = findDb.rawQuery(selectSql, keys);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			for (String attr : attrs)
				map.put(attr, cursor.getString(cursor.getColumnIndex(attr)));
			result.add(map);
		}
		return result;
	}

	/**
	 * 根据插入语句和插入值往数据库中添加数据
	 * 
	 * @param insertSql
	 * @param insertValue
	 */
	private void insert(String insertSql, String[] insertValue) {
		Log.i(TAG, insertSql + Arrays.toString(insertValue));
		SQLiteDatabase insertDb = dbOpenHelper.getWritableDatabase();
		try {
			insertDb.execSQL(insertSql, insertValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		insertDb.close();
	}

}
