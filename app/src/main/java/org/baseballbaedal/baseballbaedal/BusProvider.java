package org.baseballbaedal.baseballbaedal;

import com.squareup.otto.Bus;

/**
 * Created by Administrator on 2017-08-14.
 */

public class BusProvider {
    private static final Bus INSTANCE = new Bus();

    public static Bus getInstance() {
        return INSTANCE;
    }

    private BusProvider() {    }
}
