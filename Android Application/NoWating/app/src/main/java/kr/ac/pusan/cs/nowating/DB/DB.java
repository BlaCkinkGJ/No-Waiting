package kr.ac.pusan.cs.nowating.DB;

interface DB {
    void   insert(String[] values);
    void   delete(String key);
    String get(String listName, int what);
}
