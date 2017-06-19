package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

/**
 * Created by Administrator on 2017-06-09.
 */

public class MenuData {
    String menuDataName;
    String menuDataPrice;
    String menuDataExplain;
    String uid;
    String menuKey;
    boolean isMain;

    public MenuData(String menuDataName, String menuDataPrice, String menuDataExplain, boolean isMain,String uid,String menuKey){
        this.menuDataName=menuDataName;
        this.menuDataPrice=menuDataPrice;
        this.menuDataExplain=menuDataExplain;
        this.isMain = isMain;
        this.uid = uid;
        this.menuKey = menuKey;
    }

    public String getMenuDataName(){
        return menuDataName;
    }
    public String getMenuDataPrice(){
        return menuDataPrice;
    }
    public String getMenuDataExplain(){
        return menuDataExplain;
    }
    public boolean getIsMain(){
        return isMain;
    }
    public String getUid(){
        return uid;
    }
    public String getMenuKey(){
        return menuKey;
    }
}
