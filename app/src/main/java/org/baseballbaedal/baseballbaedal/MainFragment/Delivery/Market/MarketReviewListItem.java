package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market;

/**
 * Created by qwexo on 2017-08-30.
 */

public class MarketReviewListItem {
    private String userName;
    private String body;
    private String score;
    private String reviewID;
    private String imageBool;
    private String time;

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setImageBool(String imageBool) {
        this.imageBool = imageBool;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageBool() {
        return imageBool;
    }

    public String getScore() {
        return score;
    }

    public String getReviewID() {
        return reviewID;
    }

    public String getBody() {
        return body;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
