package com.example.yunzou.connection;

import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by Simon on 2017/10/27.
 */

public class test {

    Switch aSwitch;

    @Override
    public String toString() {
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        return super.toString();
    }
}
