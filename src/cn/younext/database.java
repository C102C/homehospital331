package cn.younext;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import cn.younext.DatabaseHelper;

public class database {
	/* 可以所有东西在这个类里执行，也可以给外界返回一个DB的实例，让外界去操作 */
	private SQLiteDatabase db;
	private static DatabaseHelper dbhelper;

	public database(Context ctx, String dbname) {
		dbhelper = new DatabaseHelper(ctx, dbname, null, 1);
		db = dbhelper.getWritableDatabase();
	}

	public void close() {
		db.close();
	}

	/* 返回得到的数据库的实例 */
	public SQLiteDatabase getDatabase() {
		return db;
	}

	/* 接下来包括对表操作，如添加删除表，添加删除记录，更新记录 */

	/* 添加数据表 */
	public void creatTable(String TABLE_NAME, String createSQL) {
		String strend = "DROP TABLE IF EXISTS " + TABLE_NAME;
		try {
			db.execSQL(strend);
			db.execSQL(createSQL);
		} catch (SQLException e) {

		}
	}

	/* 删除数据表 */
	public void deleteTable(String TABLE_NAME) {
		String strend = "DROP TABLE IF EXISTS " + TABLE_NAME;
		try {
			db.execSQL(strend);
		} catch (SQLException e) {

		}
	}

	/* 添加一条记录,正确添加返回1，否则返回-1 */
	public int insertRecord(String SQL) {
		int i = 1;
		try {
			db.execSQL(SQL);
		} catch (SQLException e) {
			i = -1;
		}
		return i;
	}

	/* 正确删除记录就返回1，否则返回-1 */
	public int deleteRecord(String SQL) {
		int i = 1;
		try {
			db.execSQL(SQL);
		} catch (SQLException e) {
			i = -1;
		}
		return i;
	}

	/* 查询记录语句 */
	public Cursor queryRecord(String SQL) {
		Cursor c;
		c = db.rawQuery(SQL, null);
		return c;
	}

	/* 更新记录,成功返回1，否则返回-1 */
	public int updateTable(String SQL) {
		int i = 1;
		
		try {
			db.execSQL(SQL);
		} catch (SQLException e) {
			i = -1;
		}
		return i;
	}


}
