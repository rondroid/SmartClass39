package org.aakashlabs.smartclass;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSource {
	private SQLiteDatabase database;	 
	private MySQLiteHelper dbHelper;
	public DataSource(Context context) {
		dbHelper  = new MySQLiteHelper (context);
	}
public void add (String sender,String message,String date,String recipient) {  
	//Link is the article link and url is a identifier to identify sport
		
		ContentValues values = new ContentValues();
			values.put("SENDER",sender);
			values.put("MESSAGE",message);
			values.put("DATE",date);
			values.put("RECIPIENT",recipient);
			
			
			
			database .insert(MySQLiteHelper.TABLE_NAME, null,values);
			Log.d("sqlcheck", "added");
			
			
	}

public void open() throws SQLException {
	database  = dbHelper .getWritableDatabase();
}
public void read() {
	database =dbHelper .getReadableDatabase();
}
public void cleardata(String mRec)
{
	String[] dummy={};
	if(mRec.equals("public"))
		database.delete(MySQLiteHelper.TABLE_NAME,"RECIPIENT='"+mRec+"'",dummy);
	else
		database.delete(MySQLiteHelper.TABLE_NAME,"(RECIPIENT='"+mRec+"' and SENDER='"+CommonUtilities.nickname+"') or (RECIPIENT='"+MainActivity.sIP+"' and SENDER='"+CommonUtilities.name+"')",dummy);

}

public void view (String mRecipient)
{
	read();
	Cursor cursor2 = null;
	Log.d("rec,send",mRecipient +" "+CommonUtilities.nickname);
	if(mRecipient.equals("public"))
		cursor2 = database.query(MySQLiteHelper .TABLE_NAME,null,"RECIPIENT='"+mRecipient+"'", null,null, null, null);
	else
		cursor2 = database.query(MySQLiteHelper .TABLE_NAME,null,"(RECIPIENT='"+mRecipient+"' and SENDER='"+CommonUtilities.nickname+"') or (RECIPIENT='"+MainActivity.sIP+"' and SENDER='"+CommonUtilities.name+"')", null,null, null, null);
	String sender=null,message=null,date=null;
	
	if((cursor2.getCount()>50)&&!CommonUtilities.message_show)
		cursor2.moveToPosition(cursor2.getCount()-50);
	else
		cursor2.moveToFirst();
	while(!cursor2.isAfterLast())
	{
		sender=cursor2.getString(cursor2.getColumnIndex("SENDER"));
		message=cursor2.getString(cursor2.getColumnIndex("MESSAGE"));
		date=cursor2.getString(cursor2.getColumnIndex("DATE"));
		
	
		ChatActivity.initiateList(sender,message,date);
		cursor2.moveToNext();
		
	}
	cursor2.close();
	


}

public String getLastMessage(String mRecipient){
	
	read();
	Cursor cursor2 = null;
	cursor2 = database.query(MySQLiteHelper .TABLE_NAME,null,"RECIPIENT='"+mRecipient+"'", null,null, null, null);
	String message=null;
	if (!cursor2.moveToFirst())
		return "";
		
		cursor2.moveToLast();
		message=cursor2.getString(cursor2.getColumnIndex("MESSAGE"));
	return message;
}

public String getLastDate(String mRecipient){
	
	read();
	Cursor cursor2 = null;
	cursor2 = database.query(MySQLiteHelper .TABLE_NAME,null,"RECIPIENT='"+mRecipient+"'", null,null, null, null);
	String date=null;
	if (!cursor2.moveToFirst())
		return "0";
	
	cursor2.moveToLast();
	date=cursor2.getString(cursor2.getColumnIndex("DATE"));
	return date;
}

public void close() {
	dbHelper.close();
}
}
