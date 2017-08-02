package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market;

/**
 * Created by Administrator on 2017-07-26-026.
 */

public class MenuItem {
    public String aTime;
    public long aseq;
    public String menuName;
    public String menuPrice;
    public String menuKey;
    public String marketId;
    public boolean isMain;

    public MenuItem(String aTime, String menuName, String menuPrice, String menuKey, String marketId, boolean isMain, long aseq){
        this.aTime = aTime;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuKey = menuKey;
        this.marketId = marketId;
        this.isMain = isMain;
        this.aseq = aseq;
    }
}
