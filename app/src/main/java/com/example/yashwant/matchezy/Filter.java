package com.example.yashwant.matchezy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;

public class Filter extends AppCompatActivity {

    RangeBar rangebar_height;
    RangeBar rangebar_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        rangebar_height = (RangeBar) findViewById(R.id.rangebar_height);
        rangebar_age = (RangeBar) findViewById(R.id.rangebar_age);

        rangebar_height.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {

                Toast.makeText(Filter.this, String.valueOf(leftPinIndex), Toast.LENGTH_SHORT).show();
            }

        });

        rangebar_age.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {

                Toast.makeText(Filter.this, String.valueOf(rightPinIndex), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
