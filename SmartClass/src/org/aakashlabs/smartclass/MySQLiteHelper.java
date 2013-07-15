package org.aakashlabs.smartclass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String create_query="create table messages(ID integer primary key autoincrement," +
											 "SENDER text not null, MESSAGE text not null," +
											 "DATE text not null, RECIPIENT text );";
	
	private static final String DATABASE_NAME = "chat.db";
	static final String TABLE_NAME = "messages";
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(create_query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS messages");
		onCreate(db);
	}
}
