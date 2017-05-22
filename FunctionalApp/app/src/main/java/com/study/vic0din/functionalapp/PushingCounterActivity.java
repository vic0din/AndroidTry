package com.study.vic0din.functionalapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by BBPax on 21.05.17.
 */
public class PushingCounterActivity extends AppCompatActivity implements View.OnClickListener {
    TextView clickNumberView;
    Button buttonClick;
    Button buttonReset;
    Button buttonBack;
    private static int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushing_counter);
        clickNumberView = (TextView) findViewById(R.id.tvOut);
        buttonClick = ((Button) findViewById(R.id.btnClick));
        buttonClick.setOnClickListener(this);
        buttonBack = (Button) findViewById(R.id.btnCancel);
        buttonBack.setOnClickListener(this);
        buttonReset = (Button) findViewById(R.id.btnResetCount);
        buttonReset.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClick:
                count++;
                clickNumberView.setText(count);
                break;
            case R.id.btnBack:
                //need to return back
                break;
            case R.id.btnReset:
                count = 0;
                clickNumberView.setText(count);
                break;
        }
    }
}
