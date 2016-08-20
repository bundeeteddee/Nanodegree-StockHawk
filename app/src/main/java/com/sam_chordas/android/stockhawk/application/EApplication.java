package com.sam_chordas.android.stockhawk.application;

import android.app.Application;

/**
 * Created by bundee on 8/19/16.
 */
public class EApplication extends Application{

    private static EApplication _instance;

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
    }

    /**
     * Get application instance
     * @return
     */
    public static EApplication getInstance(){
        return _instance; //This wont be null when application starts
    }
}
