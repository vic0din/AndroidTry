package com.study.vic0din.functionalapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getName();
    TextView textView;
    Button buttonOk;
    Button buttonCancel;
    Button buttonReset;
    CheckBox chb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "creating action for buttons");
        textView = (TextView) findViewById(R.id.tvOut);
        buttonOk = ((Button) findViewById(R.id.btnOk));
        buttonOk.setOnClickListener(this);
        buttonCancel = (Button) findViewById(R.id.btnCancel);
        buttonCancel.setOnClickListener(this);
        buttonReset = (Button) findViewById(R.id.btnReset);
        buttonReset.setOnClickListener(this);
        chb = (CheckBox) findViewById(R.id.chbExtMenu);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                Log.i(TAG, "button \" ok \" was clicked");
                textView.setText("Ok was pushed");
                Toast.makeText(this, "Нажата кнопка ОК", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnCancel:
                Log.i(TAG, "button \" cancel \" was clicked");
                textView.setText("Cancel was pushed");
                Toast.makeText(this, "Нажата кнопка Cancel", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnReset:
                Log.i(TAG, "button \" reset \" was clicked");
                textView.setText(R.string.hello);
                Toast.makeText(this, "Нажата кнопка Reset", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "ClickerCounter");
        menu.add(1, 2, 2, "menu2");
        menu.add(1, 3, 3, "menu3");
        menu.add(1, 4, 4, "menu4");
//        getMenuInflater().inflate(R.menu.menu, menu);//способ с подгрузкой из xml файла
        return super.onCreateOptionsMenu(menu);
    }

    // обновление меню
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(1, false);
//        menu.setGroupVisible(R.id.group1, chb.isChecked());
        return super.onPrepareOptionsMenu(menu);
    }

    // обработка нажатий
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        // Выведем в TextView информацию о нажатом пункте меню
        sb.append("Item Menu");
        sb.append("\r\n groupId: " + String.valueOf(item.getGroupId()));
        sb.append("\r\n itemId: " + String.valueOf(item.getItemId()));
        sb.append("\r\n order: " + String.valueOf(item.getOrder()));
        sb.append("\r\n title: " + item.getTitle());
        textView.setText(sb.toString());

        return super.onOptionsItemSelected(item);
    }
}
