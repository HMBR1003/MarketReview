package org.baseballbaedal.baseballbaedal.Order;

/**
 * Created by Administrator on 2017-08-29.
 */

public class OrderData {
    private String block;
    private long count;
    private long date;
    private String marketId;
    private String menus;
    private String oderState;
    private String pay;
    private String row;
    private String seatNum;
    private long selectedCol;
    private String totalPrice;
    private String userId;
    private String userMemo;
    private String userName;
    private String userTel;

    public long getCount() {
        return count;
    }

    public long getDate() {
        return date;
    }

    public long getSelectedCol() {
        return selectedCol;
    }

    public String getBlock() {
        return block;
    }

    public String getMarketId() {
        return marketId;
    }

    public String getMenus() {
        return menus;
    }

    public String getOderState() {
        return oderState;
    }

    public String getPay() {
        return pay;
    }

    public String getRow() {
        return row;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserMemo() {
        return userMemo;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserTel() {
        return userTel;
    }
}


