package org.baseballbaedal.baseballbaedal;

import android.app.Application;

import com.facebook.stetho.Stetho;

//import com.tsengvn.typekit.Typekit;

/**
 * Created by Administrator on 2017-07-19.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

//
//        Typekit.getInstance()
//                .addBold(Typekit.createFromAsset(this, "BMJUA_ttf.ttf"))
//                .addNormal(Typekit.createFromAsset(this, "BMJUA_ttf.ttf"));
    }
}
