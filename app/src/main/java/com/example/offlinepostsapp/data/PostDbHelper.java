package com.example.offlinepostsapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "posts.db";
    private static final int DB_VER = 1;

    public PostDbHelper(Context c) {
        super(c, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + PostContract.PostEntry.TABLE + " (" +
                PostContract.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PostContract.PostEntry.COL_POST_ID + " INTEGER UNIQUE, " +
                PostContract.PostEntry.COL_USER_ID + " INTEGER, " +
                PostContract.PostEntry.COL_TITLE + " TEXT, " +
                PostContract.PostEntry.COL_BODY + " TEXT" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    // CRUD
    public long insertOrReplace(int postId, int userId, String title, String body) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PostContract.PostEntry.COL_POST_ID, postId);
        cv.put(PostContract.PostEntry.COL_USER_ID, userId);
        cv.put(PostContract.PostEntry.COL_TITLE, title);
        cv.put(PostContract.PostEntry.COL_BODY, body);
        return db.insertWithOnConflict(PostContract.PostEntry.TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Cursor getAllCursor() {
        return getReadableDatabase().query(
                PostContract.PostEntry.TABLE,
                null, null, null, null, null,
                PostContract.PostEntry.COL_POST_ID + " ASC"
        );
    }

    public int deleteByPostId(int postId) {
        return getWritableDatabase().delete(
                PostContract.PostEntry.TABLE,
                PostContract.PostEntry.COL_POST_ID + "=?",
                new String[]{String.valueOf(postId)}
        );
    }

    public int updateTitleBody(int postId, String title, String body) {
        ContentValues cv = new ContentValues();
        cv.put(PostContract.PostEntry.COL_TITLE, title);
        cv.put(PostContract.PostEntry.COL_BODY, body);
        return getWritableDatabase().update(
                PostContract.PostEntry.TABLE,
                cv,
                PostContract.PostEntry.COL_POST_ID + "=?",
                new String[]{String.valueOf(postId)}
        );
    }
}
