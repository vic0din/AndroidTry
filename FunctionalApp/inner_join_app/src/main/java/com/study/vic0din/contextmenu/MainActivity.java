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

    // данные для таблицы должностей
    int[] position_id = { 1, 2, 3, 4 };
    String[] position_name = { "Директор", "Программер", "Бухгалтер", "Охранник" };
    int[] position_salary = { 15000, 13000, 10000, 8000 };

    // данные для таблицы людей
    String[] people_name = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис", "Костя", "Игорь" };
    int[] people_posid = { 2, 3, 2, 2, 3, 1, 2, 4 };

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Подключаемся к БД
        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getWritableDatabase();

        // Описание курсора
        Cursor c;

        // выводим в лог данные по должностям
        Log.d(LOG_TAG, "--- Table position ---");
        c = db.query("position", null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим в лог данные по людям
        Log.d(LOG_TAG, "--- Table people ---");
        c = db.query("people", null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим результат объединения
        // используем rawQuery
        Log.d(LOG_TAG, "--- INNER JOIN with rawQuery---");
        String sqlQuery = "select PL.name as Name, PS.name as Position, salary as Salary "
                + "from people as PL "
                + "inner join position as PS "
                + "on PL.posid = PS.id "
                + "where salary > ?";
        c = db.rawQuery(sqlQuery, new String[] {"12000"});
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим результат объединения
        // используем query
        Log.d(LOG_TAG, "--- INNER JOIN with query---");
        String table = "people as PL inner join position as PS on PL.posid = PS.id";
        String columns[] = { "PL.name as Name", "PS.name as Position", "salary as Salary" };
        String selection = "salary < ?";
        String[] selectionArgs = {"12000"};
        c = db.query(table, columns, selection, selectionArgs, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // закрываем БД
        dbh.close();
    }

    // вывод в лог данных из курсора
    void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, "Cursor is null");
    }

    // класс для работы с БД
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "myDB", null, 2);
        }

        public void onCreate(SQLiteDatabase db) {
            //it already exists, but older version
        }

        /**
         * way to upgrade BD:
         * db.beginTransaction();
         * try {
         * // создаем таблицу должностей
         * db.execSQL("create table position ("
         * + "id integer primary key,"
         * + "name text, salary integer);");
         *
         * // заполняем ее
         * for (int i = 0; i < position_id.length; i++) {
         * cv.clear();
         * cv.put("id", position_id[i]);
         * cv.put("name", position_name[i]);
         * cv.put("salary", position_salary[i]);
         * db.insert("position", null, cv);
         * }
         *
         * db.execSQL("alter table people add column posid integer;");
         *
         * for (int i = 0; i < position_id.length; i++) {
         * cv.clear();
         * cv.put("posid", position_id[i]);
         * db.update("people", cv, "position = ?",
         * new String[] { position_name[i] });
         * }
         *
         * db.execSQL("create temporary table people_tmp ("
         * + "id integer, name text, position text, posid integer);");
         *
         * db.execSQL("insert into people_tmp select id, name, position, posid from people;");
         * db.execSQL("drop table people;");
         *
         * db.execSQL("create table people ("
         * + "id integer primary key autoincrement,"
         * + "name text, posid integer);");
         *
         * db.execSQL("insert into people select id, name, posid from people_tmp;");
         * db.execSQL("drop table people_tmp;");
         *
         * db.setTransactionSuccessful();
         * } finally {
         * db.endTransaction();
         * }
         * @param db
         * @param oldVersion
         * @param newVersion
         */

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, "--- onUpgrade database ---");

            ContentValues cv = new ContentValues();

            // создаем таблицу должностей
            db.execSQL("create table position ("
                    + "id integer primary key,"
                    + "name text," + "salary integer"
                    + ");");

            // заполняем ее
            for (int i = 0; i < position_id.length; i++) {
                cv.clear();
                cv.put("id", position_id[i]);
                cv.put("name", position_name[i]);
                cv.put("salary", position_salary[i]);
                db.insert("position", null, cv);
            }

            // создаем таблицу людей
            db.execSQL("create table people ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "posid integer"
                    + ");");

            // заполняем ее
            for (int i = 0; i < people_name.length; i++) {
                cv.clear();
                cv.put("name", people_name[i]);
                cv.put("posid", people_posid[i]);
                db.insert("people", null, cv);
            }
        }
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
}
