package org.baseballbaedal.baseballbaedal.MainFragment.Delivery;

/**
 * Created by qwexo on 2017-06-16.
 */

public class MarketListItem {
    private String marketUserID;
    private String marketName;
    private String marketAdress;
    private String tell;
    private String minPrice;
    private String aTime;
    private boolean isTakeout;

    public String getMarketUserID() {
        return marketUserID;
    }

    public String getMarketAdress() {
        return marketAdress;
    }

    public String getMarketName() {
        return marketName;
    }

    public String getTell() {
        return tell;
    }

    public boolean getIsTakeout() {
        return isTakeout;
    }

    public void setMarketAdress(String marketAdress) {
        this.marketAdress = marketAdress;
    }

    public void setMarketUserID(String marketUserID) {
        this.marketUserID = marketUserID;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public void setMinPrice(String minPrice){
        this.minPrice = minPrice;
    }

    public String getMinPrice(){
        return minPrice;
    }

    public void setaTime(String aTime){
        this.aTime=aTime;
    }

    public String getaTime(){
        return aTime;
    }

    public void setIsTakeout(boolean isTakeout) {
        this.isTakeout = isTakeout;
    }
}
