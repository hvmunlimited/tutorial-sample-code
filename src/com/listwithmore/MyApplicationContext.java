package com.listwithmore;

import android.app.Application;
import android.content.Context;

public class MyApplicationContext extends Application {
	private static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplicationContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplicationContext.context;
    }
}

// this tutorial is according to http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android