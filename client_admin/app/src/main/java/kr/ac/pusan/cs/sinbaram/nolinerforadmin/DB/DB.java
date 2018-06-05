package kr.ac.pusan.cs.sinbaram.nolinerforadmin.DB;

import android.database.Cursor;

interface DB {
    void   insert(String[] values);
    void   delete(String key);
    String get(String listName, int what);
}
