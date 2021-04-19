package com.example.mars.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mars.data.CourseContentData;
import com.example.mars.data.CourseData;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "caurseApp";
    private static final String TABLE_COURSE = "course";
    private static final String TABLE_CONTENT = "course_content";

    private static final String KEY_ID = "id";
    private static final String KEY_COURSE_NAME = "name";
    private static final String KEY_COURSE_IS_PAID = "isPaid";
    private static final String KEY_COURSE_FEE = "fee";


    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_VIDEO_NAME = "video_name";
    private static final String KEY_VIDEO_URL = "video_url";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSE_TABLE = "CREATE TABLE " + TABLE_COURSE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COURSE_NAME + " TEXT," + KEY_COURSE_IS_PAID +" BOOLEN," +KEY_COURSE_FEE +" TEXT" +")";

        String CREATE_COURSE_CONTENT_TABLE = "CREATE TABLE " + TABLE_CONTENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_VIDEO_NAME + " TEXT," + KEY_VIDEO_URL +" TEXT," +KEY_COURSE_ID +" TEXT" +")";

        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(CREATE_COURSE_CONTENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);

        // Create tables again
        onCreate(db);
    }

    //Add Course
    public void addCourse(CourseData data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COURSE_NAME, data.getCourseName());
        values.put(KEY_COURSE_IS_PAID, String.valueOf(data.isPaid()));
        values.put(KEY_COURSE_FEE, data.getCourseFee());

        // Inserting Row
        db.insert(TABLE_COURSE, null, values);
        db.close();
    }

    //Add Course
    public void addCourseContent(CourseContentData data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_VIDEO_NAME, data.getName());
        values.put(KEY_VIDEO_URL, String.valueOf(data.getUrl()));
        values.put(KEY_COURSE_ID, String.valueOf(data.getCourseId()));

        // Inserting Row
        db.insert(TABLE_CONTENT, null, values);
        db.close();
    }

    // code to get all contacts in a list view
    public List<CourseData> getAllCourse() {
        List<CourseData> data = new ArrayList<CourseData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COURSE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CourseData courseData = new CourseData(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Boolean.parseBoolean(cursor.getString(2)),cursor.getString(3));

                // Adding category to list
                data.add(courseData);
            } while (cursor.moveToNext());
        }

        // return category list
        return data;
    }

    // code to update the single note
    public int updateCourseData(CourseData data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_NAME, data.getCourseName());
        values.put(KEY_COURSE_IS_PAID, String.valueOf(data.isPaid()));
        values.put(KEY_COURSE_FEE, data.getCourseFee());

        // updating row
        return db.update(TABLE_COURSE, values, KEY_COURSE_NAME + " = ?",
                new String[] { data.getCourseName() });
    }


    public List<CourseContentData> getAllCourseContent(String courseId) {
        List<CourseContentData> data = new ArrayList<CourseContentData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTENT+" WHERE "+KEY_COURSE_ID+"="+courseId;;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CourseContentData courseContentData=new CourseContentData(Integer.parseInt(cursor.getString(3)),cursor.getString(1),cursor.getString(2));
                // Adding category to list
                data.add(courseContentData);
            } while (cursor.moveToNext());
        }

        // return category list
        return data;
    }
}
