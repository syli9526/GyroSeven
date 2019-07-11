package com.teamseven.gyroseven;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GyroSevenScore.db";


    // 초기화
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 해당 테이블이 없을 경우 만들어줌
        String SaveHighScoreSQL = "create table tb_bestScore" + "(_id integer primary key autoincrement, " + "score)";
        sqLiteDatabase.execSQL(SaveHighScoreSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 버전이 바뀌면 테이블 삭제하고 다시 만듬
        if (newVersion == DATABASE_VERSION) {
            sqLiteDatabase.execSQL("drop table tb_bestScore");
            onCreate(sqLiteDatabase);
        }
        Log.v("DB", "DB upgrade");
    }

    // 현재 스코어랑 저장된 스코어를 비교해 최고 점수 저장.
    public int compareDBScore(int score) {
        int DBScore = select();
        Log.v("DB", String.valueOf(DBScore));
        if (DBScore == -1) {
            // 자료가 없을경우는 그냥 현재 점수 저장
            Log.v("DB", "insert_>");
            insert(score);
            return score;
        } else if (DBScore < score) {
            Log.v("DB", "update_>");
            update(score);
            return score;
        } else {
            return DBScore;
        }
    }

    // 삽입
    private void insert(int score) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("insert into tb_bestScore (score) values (" + score + ")");
        db.close();

    }

    // 업데이트
    private void update(int bestscore) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("update tb_bestScore set score = " + bestscore + " where _id = 1 ");
        db.close();

    }

    // 검색
    private int select() {
        SQLiteDatabase db = getWritableDatabase();
        int score = -1;
        Cursor cursor = db.rawQuery("select * from tb_bestScore where _id = 1", null);
        while (cursor.moveToNext()) {
            score = cursor.getInt(1);
        }

        if (cursor == null) return -1;
        else return score;

    }
}
