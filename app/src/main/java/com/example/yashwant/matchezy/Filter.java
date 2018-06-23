package com.example.yashwant.matchezy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;

public class Filter extends AppCompatActivity {

    RangeBar rangebar_height;
    RangeBar rangebar_age;

    TextView height_start,height_end,age_start,age_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        rangebar_height = (RangeBar) findViewById(R.id.rangebar_height);
        rangebar_age = (RangeBar) findViewById(R.id.rangebar_age);

        height_start=(TextView)findViewById(R.id.height_start);
        height_end=(TextView)findViewById(R.id.height_end);

        age_start=(TextView)findViewById(R.id.age_start);
        age_end=(TextView)findViewById(R.id.age_end);

        rangebar_height.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {

               height_start.setText(String.valueOf(leftPinValue));
                height_end.setText(String.valueOf(rightPinIndex));
            }

        });

        rangebar_age.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {

               age_start.setText(String.valueOf(leftPinIndex));
               age_end.setText(String.valueOf(rightPinIndex));
            }

        });
    }
}
