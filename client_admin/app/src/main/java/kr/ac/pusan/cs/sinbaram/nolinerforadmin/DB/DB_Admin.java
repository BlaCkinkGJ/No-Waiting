package kr.ac.pusan.cs.sinbaram.nolinerforadmin.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Admin extends SQLiteOpenHelper implements DB{

    public final int LIST_NAME = 1;
    public final int PUBLIC_KEY = 2;
    public final int PUBLIC_ID = 3;

    public DB_Admin(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        // 일반적으로 factory는 null이 됩니다.
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ADMIN_INFO (_id INTEGER PRIMARY KEY, list_name TEXT, public_key TEXT, public_id TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // version 업그레이드 시에만 사용하도록 합니다.
    }


    @Override
    public void insert(String[] values){
        if(values.length != 3) throw new IllegalArgumentException();
        SQLiteDatabase db = getWritableDatabase();
        String listName = values[0];
        String publicKey = values[1];
        String publicID = values[2];
        db.execSQL("INSERT INTO ADMIN_INFO VALUES(null, '"+listName+"', '"+ publicKey + "', '"+ publicID +"');");
        db.close();
    }

    @Override
    public void delete(String key) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM ADMIN_INFO WHERE list_name='" + key +"';");
    }

    // use this like
    // String result = user.get("컴퓨터 공학부", DB_User.PUBLIC_KEY);
    @Override
    public String get(String listName, int what) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ADMIN_INFO", null);
        while(cursor.moveToNext()){
            if(cursor.getString(LIST_NAME).equals(listName)){
                return cursor.getString(what);
            }
        }
        return null;
    }
}
