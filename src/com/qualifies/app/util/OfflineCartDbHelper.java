package com.qualifies.app.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OfflineCartDbHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "qualifies";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "offline_cart";
    public final static String FIELD_ID = "_id";
    public final static String FIELD_ITEM1 = "goodsId";
    public final static String FIELD_ITEM2 = "goodsAttr";
    public final static String FIELD_ITEM3 = "goodsNum";
    private static OfflineCartDbHelper inst;

    public static OfflineCartDbHelper getInstance(Context context) {
        if (inst == null) {
            inst = new OfflineCartDbHelper(context);
        }
        return inst;
    }

    public OfflineCartDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table " + TABLE_NAME + "(" + FIELD_ID + " integer primary key autoincrement,"
                + FIELD_ITEM1 + " text,"
                + FIELD_ITEM2 + "text,"
                + FIELD_ITEM3 + "text );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = " DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, " _id desc");
        return cursor;
    }

    public long insert(String goodsId, String goodsAttr, String goodNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_ITEM1, goodsId);
        cv.put(FIELD_ITEM2, goodsAttr);
        cv.put(FIELD_ITEM3, goodNum);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = FIELD_ID + "=?";
        String[] whereValue = {Integer.toString(id)};
        db.delete(TABLE_NAME, where, whereValue);
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlString = "DELETE FROM " + TABLE_NAME;
        db.execSQL(sqlString);
    }

    public void update(int id, String goodsId, String goodsAttr, String goodNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = FIELD_ID + "=?";
        String[] whereValue = {Integer.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put(FIELD_ITEM1, goodsId);
        cv.put(FIELD_ITEM2, goodsAttr);
        cv.put(FIELD_ITEM3, goodNum);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

}
