package com.qualifies.app.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class dbHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME="qualifies";
	private final static int DATABASE_VERSION=1;
	private final static String TABLE_NAME="search_history";
	public final static String FIELD_ID="_id"; 
	public final static String FIELD_ITEM="item_Title";
	
	
	public dbHelper(Context context)
	{
		super(context, DATABASE_NAME,null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql="Create table "+TABLE_NAME+"("+FIELD_ID+" integer primary key autoincrement,"
		+FIELD_ITEM+" text );";
		db.execSQL(sql); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql=" DROP TABLE IF EXISTS "+TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	public Cursor select()
	{
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.query(TABLE_NAME, null, null, null, null, null,  " _id desc");
		return cursor;
	}
	
	public long insert(String Title)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cv=new ContentValues(); 
		cv.put(FIELD_ITEM, Title);
		long row=db.insert(TABLE_NAME, null, cv);
		return row;
	}
	
	public void delete(int id)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		String where=FIELD_ID+"=?";
		String[] whereValue={Integer.toString(id)};
		db.delete(TABLE_NAME, where, whereValue);
	}
	public void clear(){
		SQLiteDatabase db=this.getWritableDatabase();
		String sqlString = "DELETE FROM "+TABLE_NAME;
		db.execSQL(sqlString);
	}
	public void update(int id,String Title)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		String where=FIELD_ID+"=?";
		String[] whereValue={Integer.toString(id)};
		ContentValues cv=new ContentValues(); 
		cv.put(FIELD_ITEM, Title);
		db.update(TABLE_NAME, cv, where, whereValue);
	}
}
