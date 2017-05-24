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
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    final String LOG_TAG = "myLogs";

    String name[] = { "Китай", "США", "Бразилия", "Россия", "Япония",
            "Германия", "Египет", "Италия", "Франция", "Канада" };
    int people[] = { 1400, 311, 195, 142, 128, 82, 80, 60, 66, 35 };
    String region[] = { "Азия", "Америка", "Америка", "Европа", "Азия",
            "Европа", "Африка", "Европа", "Европа", "Америка" };

    Button btnAll, btnFunc, btnPeople, btnSort, btnGroup, btnHaving;
    EditText etFunc, etPeople, etRegionPeople;
    RadioGroup rgSort;

    DBHelper dbHelper;
    SQLiteDatabase db;

    /** Called when the activity is first created. */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAll = (Button) findViewById(R.id.btnAll);
        btnAll.setOnClickListener(this);

        btnFunc = (Button) findViewById(R.id.btnFunc);
        btnFunc.setOnClickListener(this);

        btnPeople = (Button) findViewById(R.id.btnPeople);
        btnPeople.setOnClickListener(this);

        btnSort = (Button) findViewById(R.id.btnSort);
        btnSort.setOnClickListener(this);

        btnGroup = (Button) findViewById(R.id.btnGroup);
        btnGroup.setOnClickListener(this);

        btnHaving = (Button) findViewById(R.id.btnHaving);
        btnHaving.setOnClickListener(this);

        etFunc = (EditText) findViewById(R.id.etFunc);
        etPeople = (EditText) findViewById(R.id.etPeople);
        etRegionPeople = (EditText) findViewById(R.id.etRegionPeople);

        rgSort = (RadioGroup) findViewById(R.id.rgSort);

        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();

        // проверка существования записей
        Cursor c = db.query("countrytable", null, null, null,
                null, null, null);
        if (c.getCount() == 0) {
            ContentValues cv = new ContentValues();
            // заполним таблицу
            for (int i = 0; i < 10; i++) {
                cv.put("name", name[i]);
                cv.put("people", people[i]);
                cv.put("region", region[i]);
                Log.d(LOG_TAG, "id = " + db.insert("countrytable", null, cv));
            }
        }
        c.close();
        dbHelper.close();
        // эмулируем нажатие кнопки btnAll
        onClick(btnAll);

    }

    @Override
    public void onClick(View v) {

        // подключаемся к базе
        db = dbHelper.getWritableDatabase();

        // данные с экрана
        String sFunc = etFunc.getText().toString();
        String sPeople = etPeople.getText().toString();
        String sRegionPeople = etRegionPeople.getText().toString();

        // переменные для query
        String[] columns = null;
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        // курсор
        Cursor c = null;

        // определяем нажатую кнопку
        switch (v.getId()) {
            // Все записи
            case R.id.btnAll:
                Log.d(LOG_TAG, "--- Все записи ---");
                c = db.query("countrytable", null, null, null,
                        null, null, null);
                break;
            // Функция
            /**
             * Вывод значения агрегатной функции (или любого поля). Использую параметр columns,
             * в который надо записать поля, которые я хотел бы получить из таблицы, т.е. то,
             * что обычно перечисляется после слова SELECT в SQL-запросе. columns имеет тип String[] – массив строк.
             * Создаем массив из одного значения, которое считано с поля etFunc на экране. Запускаем query.
             */
            case R.id.btnFunc:
                Log.d(LOG_TAG, "--- Функция " + sFunc + " ---");
                columns = new String[] { sFunc };
                c = db.query("countrytable", columns, null, null,
                        null, null, null);
                break;
            // Население больше, чем
            case R.id.btnPeople:
                Log.d(LOG_TAG, "--- Население больше " + sPeople + " ---");
                selection = "people > ?";
                selectionArgs = new String[] { sPeople };
                c = db.query("countrytable", null, selection, selectionArgs, null, null,
                        null);
                break;
            // Население по региону
            case R.id.btnGroup:
                Log.d(LOG_TAG, "--- Население по региону ---");
                columns = new String[] { "region", "sum(people) as people" };
                groupBy = "region";
                c = db.query("countrytable", columns, null, null,
                        groupBy, null, null);
                break;
            // Население по региону больше чем
            case R.id.btnHaving:
                Log.d(LOG_TAG, "--- Регионы с населением больше " + sRegionPeople
                        + " ---");
                columns = new String[] { "region", "sum(people) as people" };
                groupBy = "region";
                having = "sum(people) > " + sRegionPeople;
                c = db.query("countrytable", columns, null, null,
                        groupBy, having, null);
                break;
            // Сортировка
            case R.id.btnSort:
                // сортировка по
                switch (rgSort.getCheckedRadioButtonId()) {
                    // наименование
                    case R.id.rName:
                        Log.d(LOG_TAG, "--- Сортировка по наименованию ---");
                        orderBy = "name";
                        break;
                    // население
                    case R.id.rPeople:
                        Log.d(LOG_TAG, "--- Сортировка по населению ---");
                        orderBy = "people";
                        break;
                    // регион
                    case R.id.rRegion:
                        Log.d(LOG_TAG, "--- Сортировка по региону ---");
                        orderBy = "region";
                        break;
                }
                c = db.query("countrytable", null, null, null,
                        null, null, orderBy);
                break;
        }

        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = "
                                + c.getString(c.getColumnIndex(cn)) + "; \n");
                    }
                    Log.d(LOG_TAG, str);
                    Toast.makeText(this, str, Toast.LENGTH_LONG);
                } while (c.moveToNext());
            }
            c.close();
        } else
            Log.d(LOG_TAG, "Cursor is null");

        dbHelper.close();
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

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myCountryDB", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table countrytable ("
                    + "id integer primary key autoincrement," + "name text,"
                    + "people integer," + "region text" + ");");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
