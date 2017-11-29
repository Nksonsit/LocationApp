package com.myapp.locationapp.dbhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myapp.locationapp.model.Site;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by raghavthakkar on 10-10-2016.
 */
public class DBOpenHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "speed_catch.db";
    public static final int DB_VERSION = 1;
    private static DBOpenHelper instance;
    private static final String DATABASE_PATH = "/data/data/com.myapp.locationapp/databases/";
    public static final String DB_FullPATH = DATABASE_PATH + DB_NAME;

    public static DBOpenHelper getInstance(Context context) {
        if (null == instance) {
            instance = new DBOpenHelper(context);
        }

        return instance;
    }

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDataBase(Context context) {

        boolean dbExist = checkDataBase();
        Log.e("dbExist", dbExist + "");
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase(context);
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }

    }

    private void copyDataBase(Context context) throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DATABASE_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
            Log.e("coping", "db");
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDataBase() {

        File folder = new File(DATABASE_PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File dbFile = new File(DATABASE_PATH + DB_NAME);
        return dbFile.exists();
    }

    public static void addSite(Site site) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("INSERT INTO Site(UserId,Site,Description,Distance,Latitude,Longitude,Timestamp) " +
                "VALUES(" +
                "" + site.getUserId() + "," +
                "'" + site.getSite() + "'," +
                "'" + site.getDescription() + "'," +
                "'" + site.getDistance() + "'," +
                "'" + site.getLatitude() + "'," +
                "'" + site.getLongitude() + "'," +
                "'" + site.getTimestamp() + "'" +
                ")");
        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<Site> getSites() {
        List<Site> list=new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Site", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Site site=new Site();
                site.setId(cursor.getInt(0)+"");
                site.setUserId(cursor.getString(1));
                site.setSite(cursor.getString(2));
                site.setDescription(cursor.getString(3));
                site.setDistance(cursor.getString(4));
                site.setLatitude(cursor.getString(5));
                site.setLongitude(cursor.getString(6));
                site.setTimestamp(cursor.getString(7));
                list.add(site);
            } while (cursor.moveToNext());
        }
        DatabaseManager.getInstance().closeDatabase();
        return list;
    }
}
