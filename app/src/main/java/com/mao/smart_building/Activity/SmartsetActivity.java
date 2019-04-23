package com.mao.smart_building.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.mao.smart_building.R;

/**
 * Created by Mingpeidev on 2019/4/22.
 */

public class SmartsetActivity extends AppCompatActivity {

    private ImageButton cancel_Btn = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.smartset_layout);

        cancel_Btn = (ImageButton) findViewById(R.id.cancel_Btn);

        cancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
