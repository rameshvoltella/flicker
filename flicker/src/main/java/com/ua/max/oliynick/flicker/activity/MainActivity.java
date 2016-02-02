package com.ua.max.oliynick.flicker.activity;

import android.os.Bundle;
import android.util.Log;

import com.ua.max.oliynick.flicker.interfaces.IMainController;

import oliynick.max.ua.com.flicker.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

/**
 * Created by Максим on 02.02.2016.
 */

@ContentView(R.layout.main)
public class MainActivity extends RoboActivity implements IMainController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("MainActivity", "Main activity started");
    }

}
