package com.songhk.user.keyword;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBController extends SQLiteOpenHelper {
    String sql;

    public DBController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //테이블 생성
        /* 이름은 KEYWORD이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과 word 문자열 컬럼*/
        db.execSQL("CREATE TABLE KEYWORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT, favorite INTEGER DEFAULT 0);");
        db.execSQL("CREATE TABLE MEMO (_id INTEGER PRIMARY KEY, memo TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String word) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO KEYWORD(word) VALUES('" + word +"');");
        db.close();
    }

    public String select(int id){
        SQLiteDatabase db = getReadableDatabase();
        String word = null;

        sql="select word from KEYWORD where _id="+id;
        Cursor resultset=db.rawQuery(sql,null);

        int count = resultset.getCount();

        for(int i=0;i<count;i++){
            resultset.moveToNext();
            word=resultset.getString(0);
        }
        return word;
    }

    public void addMark(int id){
        SQLiteDatabase db = getWritableDatabase();

        if(isMarked(id)){
            db.execSQL("UPDATE KEYWORD SET favorite = 0 WHERE _id = "+id);
            deleteMemo(id);
        }else{ //즐겨찾기 되어있지 않을 때 추가
            db.execSQL("UPDATE KEYWORD SET favorite = 1 WHERE _id = "+id);
        }

    }

    //즐겨찾기 추가 되어있는지 확인
    public boolean isMarked(int id){
        SQLiteDatabase db = getReadableDatabase();
        int isMark = 0;

        sql = "select favorite from KEYWORD where _id="+id;
        Cursor resultset = db.rawQuery(sql,null);

        int count = resultset.getCount();

        for(int i=0;i<count;i++){
            resultset.moveToNext();
            isMark = resultset.getInt(0);
        }

        if(isMark==1)
            return true;
        else
            return false;
    }

    //즐겨찾기 목록 가져오기
    public ArrayList<ListWordItem> getFavorite(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ListWordItem> favorites = new ArrayList<ListWordItem>();

        sql = "select _id,word from KEYWORD where favorite=1";
        Cursor resultset = db.rawQuery(sql,null);

        int count = resultset.getCount();

        for(int i=0;i<count;i++){
            resultset.moveToNext();
            ListWordItem item = new ListWordItem();
            item.setId(resultset.getInt(0));
            item.setWord(resultset.getString(1));

            favorites.add(item);
        }

        return favorites;
    }

    public void insertMemo(int id, String memo) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO MEMO VALUES('"+id+"','"+memo+"')");
        db.close();
    }

    public String selectMemo(int id){
        SQLiteDatabase db = getReadableDatabase();
        String memo = null;

        sql="select memo from MEMO where _id="+id;
        Cursor resultset=db.rawQuery(sql,null);

        int count = resultset.getCount();

        for(int i=0;i<count;i++){
            resultset.moveToNext();
            memo=resultset.getString(0);
        }
        return memo;
    }

    public void updateMemo(int id, String memo){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE MEMO SET memo = '"+memo+"' WHERE _id = "+id);
        db.close();
    }

    public void deleteMemo(int id){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM MEMO WHERE _id ="+id);
        db.close();
    }
}
