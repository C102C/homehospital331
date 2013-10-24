package cn.younext;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseHelper extends SQLiteOpenHelper{
	public static final String DATABASE_NAME="testrecord";
	public static final int Version = 1;
	
	public static final String XUEYATABLE="xueya";
	public static final String GAOYA="gaoya";
	public static final String DIYA ="diya";
	public static final String XINLV="xinlv";
	
	public static final String MAILVTABLE="mailv";
	public static final String MAILV="mailv";
	
	public static final String ZHIFANGTABLE="zhifang";
	public static final String ZHIFANGLV="zhifanglv";
	
	public static final String XUETANGTABLE="xuetang";
	public static final String XUETANG="xuetang";
	
	public static final String TIWENTABLE="tiwen";
	public static final String TIWEN="tiwen";
	
	public static final String TAIXINTABLE="taixin";
	public static final String TAIXIN="taixin";
	
	public static final String TIZHONGTABLE="tizhong";
	public static final String TIZHONG="tizhong";
	
	public static final String XUEYANGTABLE="xueyang";
	public static final String XUEYANG="xueyang";
	
	public static final String ID="_id";	
	public static final String YEAR="year";
	public static final String MONTH="month";
	public static final String DAY="day";
	public static final String HOUR="hour";
	public static final String MINUTE="minute";
	
	
	public static final String USER_NAME="username";
	public static final String USERPID="userpid";
	public static final String USER_AGE="user_age";
	public static final String USER_SEX="user_sex";
	public static final String USER_PIC="user_pic";
	public static final String USER_MANAGE="usermanage";
	
	public static final String BLUETOOTH_MACTABLE="bluetooth_mac";
	public static final String BLUETOOTH_MAC="mac";
	
	public static final String PHONETABLE="phone";
	public static final String CONTECT_NAME="name";
	public static final String CONTECT_PHONENUMBER="phonenumber";
	public static final String USER_ID="userid";
	
	public static final String ALARMTABLE="alarm";
	public static final String XUANZHONG="xuanzhong";
	public static final String TEXT="text";
	public static final String SPINNER="spinner";
	
	public static final String ALARMZHOUQITABLE="alarmzhouqi";
	public static final String ZHOUQI="zhouqi";
	
	public static final String HEALTHINFO_CHANGSHITABLE="healthinfo_changshi";
	public static final String HEALTHINFO_BANGBANGTABLE="healthinfo_bangbang";
	public static final String HEALTHINFOTABLE_TEXTTITLE="texttitle";
	public static final String HEALTHINFOTABLE_TEXTCONTENT="textcontent";
	public static final String HEALTHINFOTABLE_TAG="tag";
	
	
	public static final String HISTORY_REPORT="history_report";
	public static final String HISTORY_REPORT_GAOYA="gaoya";
	public static final String HISTORY_REPORT_DIYA="diya";
	public static final String HISTORY_REPORT_MAILV="mailv";
	public static final String HISTORY_REPORT_XUETANG="xuetang";
	public static final String HISTORY_REPORT_ZHIFANGLV="zhifanglv";
	public static final String HISTORY_REPORT_TIWEN="tiwen";
	public static final String HISTORY_REPORT_TAIXIN="taixin";
	public static final String HISTORY_REPORT_TIZHONG="tizhong";
	public static final String HISTORY_REPORT_XUEYANG="xueyang";
	public static final String HISTORY_REPORT_TAG="tag";
	public static final String HISTORY_REPORT_USERID="userid";
	public static final String HISTORY_REPORT_EXPERT_XUEYA="expert_xueya";
	public static final String HISTORY_REPORT_EXPERT_MAILV="expert_mailv";
	public static final String HISTORY_REPORT_EXPERT_XUETANG="expert_xuetang";
	public static final String HISTORY_REPORT_EXPERT_ZHIFANGLV="expert_zhifanglv";
	public static final String HISTORY_REPORT_EXPERT_TIWEN="expert_tiwen";
	public static final String HISTORY_REPORT_EXPERT_TAIXIN="expert_taixin";
	public static final String HISTORY_REPORT_EXPERT_TIZHONG="expert_tizhong";
	public static final String HISTORY_REPORT_EXPERT_XUEYANG="expert_xueyang";
	
	
	public static final String ECGFILE_TABLE="ecgfile";
	public static final String ECGFILE_FILENAME="filename";
	public static final String ECGFILE_USERID="userid";


	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}


}
