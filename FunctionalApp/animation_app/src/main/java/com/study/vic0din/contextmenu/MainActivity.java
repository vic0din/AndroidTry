package com.study.vic0din.contextmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

/**
 * Created by BBPax on 22.05.17.
 */
public class MainActivity extends AppCompatActivity {

    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    TextView tv;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        // регистрируем контекстное меню для компонента tv
        registerForContextMenu(tv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    // создание контекстного меню
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                       ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        Toast.makeText(this, "menu was created", Toast.LENGTH_LONG).show();
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    // обработка нажатий на пункты меню
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Animation anim = null;
        switch (item.getItemId()) {
            case R.id.menu_alpha:
                // создаем объект анимации из файла anim/myalpha
                Toast.makeText(this, "Нажата кнопка Alpha", Toast.LENGTH_LONG).show();
                anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
                break;
            case R.id.menu_scale:
                Toast.makeText(this, "Нажата кнопка Scale", Toast.LENGTH_LONG).show();
                anim = AnimationUtils.loadAnimation(this, R.anim.myscale);
                break;
            case R.id.menu_trans:
                Toast.makeText(this, "Нажата кнопка Translate", Toast.LENGTH_LONG).show();
                anim = AnimationUtils.loadAnimation(this, R.anim.mytrans);
                break;
            case R.id.menu_rotate:
                Toast.makeText(this, "Нажата кнопка Rotate", Toast.LENGTH_LONG).show();
                anim = AnimationUtils.loadAnimation(this, R.anim.myrotate);
                break;
            case R.id.menu_combo:
                Toast.makeText(this, "Нажата кнопка Combo", Toast.LENGTH_LONG).show();
                anim = AnimationUtils.loadAnimation(this, R.anim.combo);
                break;
        }
        // запускаем анимацию для компонента tv
        tv.startAnimation(anim);
        return super.onContextItemSelected(item);
    }
}
