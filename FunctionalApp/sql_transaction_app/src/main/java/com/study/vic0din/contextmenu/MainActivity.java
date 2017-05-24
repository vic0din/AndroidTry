package com.study.vic0din.contextmenu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

/**
 * Created by BBPax on 22.05.17.
 */
public class MainActivity extends AppCompatActivity {

    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    final String LOG_TAG = "myLogs";

    DBHelper dbh;
    SQLiteDatabase db;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "--- onCreate Activity ---");
        dbh = new DBHelper(this);
        myActions();
    }

//    void myActions() {
//        db = dbh.getWritableDatabase();
//        delete(db, "mytable");
//        db.beginTransaction();
//        insert(db, "mytable", "val1");
//        db.setTransactionSuccessful();
//        insert(db, "mytable", "val2");
//        db.endTransaction();
//        insert(db, "mytable", "val3");
//        read(db, "mytable");
//        dbh.close();
//    }

    /**
     * recomended usage:
     * db.beginTransaction();
     * try {
     * ...
     * db.setTransactionSuccessful();
     * } finally {
     * db.endTransaction();
     * }
     */
    void myActions() {
        db = dbh.getWritableDatabase();
        SQLiteDatabase db2 = dbh.getWritableDatabase();
        Log.d(LOG_TAG, "db = db2 - " + db.equals(db2));
        Log.d(LOG_TAG, "db open - " + db.isOpen() + ", db2 open - " + db2.isOpen());
        db2.close();
        Log.d(LOG_TAG, "db open - " + db.isOpen() + ", db2 open - " + db2.isOpen());
    }

    void insert(SQLiteDatabase db, String table, String value) {
        Log.d(LOG_TAG, "Insert in table " + table + " value = " + value);
        ContentValues cv = new ContentValues();
        cv.put("val", value);
        db.insert(table, null, cv);
    }

    void read(SQLiteDatabase db, String table) {
        Log.d(LOG_TAG, "Read table " + table);
        Cursor c = db.query(table, null, null, null,
                null, null, null);
        if (c != null) {
            Log.d(LOG_TAG, "Records count = " + c.getCount());
            if (c.moveToFirst()) {
                do {
                    Log.d(LOG_TAG, c.getString(c.getColumnIndex("val")));
                } while (c.moveToNext());
            }
            c.close();
        }
    }

    void delete(SQLiteDatabase db, String table) {
        Log.d(LOG_TAG, "Delete all from table " + table);
        db.delete(table, null, null);
    }

    // создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // обработка нажатий на пункты меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                // выход из приложения
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // класс для работы с БД
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "myDB", null, 3);
        }

        public void onCreate(SQLiteDatabase db) {

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, "--- onUpgrade database ---");
            db.execSQL("drop table mytable");
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "val text"
                    + ");");
        }
    }
}
