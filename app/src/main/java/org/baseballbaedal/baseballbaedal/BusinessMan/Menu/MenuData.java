package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

/**
 * Created by Administrator on 2017-06-09.
 */

public class MenuData {
    private String menuDataName;
    private String menuDataPrice;
    private String menuDataExplain;
    private String uid;
    private String menuKey;
    private String aTime;
    private String option;
    private int aseq;
    private boolean isMain;

    public MenuData(){}

    public MenuData(String menuDataName, String menuDataPrice, String menuDataExplain, String option, boolean isMain,String uid,String menuKey, String aTime, int aseq){
        this.menuDataName=menuDataName;
        this.menuDataPrice=menuDataPrice;
        this.menuDataExplain=menuDataExplain;
        this.isMain = isMain;
        this.uid = uid;
        this.menuKey = menuKey;
        this.aTime = aTime;
        this.option = option;
        this.aseq = aseq;
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
    public String getATime(){
        return aTime;
    }
    public String getOption() {return option; }
    public int getAseq() {return aseq; }
}
