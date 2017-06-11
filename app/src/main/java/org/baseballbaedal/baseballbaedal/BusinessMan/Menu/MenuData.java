package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

/**
 * Created by Administrator on 2017-06-09.
 */

public class MenuData {
    String menuDataName;
    String menuDataPrice;
    String menuDataExplain;
    String menuDataImage;
    boolean isMain;

    public MenuData(String menuDataName, String menuDataPrice, String menuDataExplain, String menuDataImage, boolean isMain){
        this.menuDataName=menuDataName;
        this.menuDataPrice=menuDataPrice;
        this.menuDataExplain=menuDataExplain;
        this.menuDataImage=menuDataImage;
        this.isMain = isMain;
    }

    public String getMenuDataName(){
        return menuDataName;
    }
    public void setMenuDataName(String menuDataName){
        this.menuDataName=menuDataName;
    }
    public String getMenuDataPrice(){
        return menuDataPrice;
    }
    public void setMenuDataPrice(String menuDataPrice){
        this.menuDataPrice=menuDataPrice;
    }
    public String getMenuDataExplain(){
        return menuDataExplain;
    }
    public void setMenuDataExplain(String menuDataExplain){
        this.menuDataExplain=menuDataExplain;
    }
    public String getMenuDataImage(){
        return menuDataImage;
    }
    public void setMenuDataImage(String menuDataImage){
        this.menuDataImage=menuDataImage;
    }
    public boolean getIsMain(){
        return isMain;
    }
}
