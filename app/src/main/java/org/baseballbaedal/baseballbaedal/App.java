package org.baseballbaedal.baseballbaedal;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by Administrator on 2017-07-19.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .add("CUSTOM",Typekit.createFromAsset(this, "BMDOHYEON_ttf.ttf"));
    }
}
