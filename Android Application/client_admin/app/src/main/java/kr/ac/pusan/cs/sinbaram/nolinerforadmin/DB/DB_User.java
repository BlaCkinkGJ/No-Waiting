package kr.ac.pusan.cs.sinbaram.nolinerforadmin.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 참고 : http://blog.naver.com/PostView.nhn?blogId=hee072794&logNo=220619425456
public class DB_User extends SQLiteOpenHelper implements DB{

    public final int LIST_NAME = 1;
    public final int PUBLIC_KEY = 2;
    public final int CURRENT_TIME = 3;

    public DB_User(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        // 일반적으로 factory는 null이 됩니다.
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USER_INFO (_id INTEGER PRIMARY KEY, list_name TEXT, public_key TEXT, cur_time TEXT );");
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
        String currentTime = values[2];
        db.execSQL("INSERT INTO USER_INFO VALUES(null, '"+listName+"', '"+ publicKey + "', '"+ currentTime +"');");
        db.close();
    }

    @Override
    public void delete(String key) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM USER_INFO WHERE list_name='" + key +"';");
    }

    // use this like
    // String result = user.get("컴퓨터 공학부", DB_User.PUBLIC_KEY);
    @Override
    public String get(String listName, int what) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER_INFO", null);
        while(cursor.moveToNext()){
            if(cursor.getString(LIST_NAME).equals(listName)){
                return cursor.getString(what);
            }
        }
        return null;
    }
}
