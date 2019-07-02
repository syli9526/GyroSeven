package com.teamseven.gyroseven;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HighScore.db";
    private static final String NAME = "highest";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SaveHighScoreSQL = "create table tb_HighScore" +
                "(_id integer primary key autoincrement, " +
                "score)";
        sqLiteDatabase.execSQL(SaveHighScoreSQL);

        Log.v("DB","DB 생성");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            sqLiteDatabase.execSQL("drop table tb_HighScore");
            onCreate(sqLiteDatabase);
        }
        Log.v("DB","DB upgrade");
    }

    public void compareDBScore(int score){
        int DBScore = select();
        if(DBScore == -1) insert(score);
        else if(DBScore < score) insert(score);
    }

    private void insert(int score){
        SQLiteDatabase db = getWritableDatabase();

        //db.execSQL("insert into tb_Closet (img, name, season, color, value) values (?,?,?,?,?)", new String[]{photoURI.toString(), strName, strSeason, String.valueOf(nColor), strValue});
        db.execSQL("insert into tb_HighScore (score) values ("+score+")");
        db.close();

        Log.v("DB","DB insert : " + score);
    }

    private int select(){
        SQLiteDatabase db = getWritableDatabase();
        int score=0;
        String name;
        Cursor cursor = db.rawQuery("select * from tb_HighScore where _id = 1", null);
        while(cursor.moveToNext()) {
            score = cursor.getInt(1);

            Log.v("DB","DB select : " + score);
        }

        if (cursor == null)
            return -1;
        else
            return score;

    }
}
