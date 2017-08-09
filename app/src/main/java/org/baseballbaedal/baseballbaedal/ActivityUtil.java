package org.baseballbaedal.baseballbaedal;

import android.app.Activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017-08-06-006.
 */
//액티비티를 리플렉션하여 메소드를 호출하기 위한 유틸 클래스
public class ActivityUtil<T> {

    //매개변수가 없는 메소드를 가져와서 부를 경우
    public void callMethod(Activity activity, String method) {
        try {
            Class myClass = activity.getClass();
            Method invokeMethod = myClass.getMethod(method);
            invokeMethod.invoke(activity);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //매개변수가 있는 메소드를 가져와서부를 경우
    public void callMethod(Activity activity, String method, Class[] paramClass, T[] param) {
        try {
            Class myClass = activity.getClass();
            Method invokeMethod = myClass.getMethod(method, paramClass);
            invokeMethod.invoke(activity, param);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
