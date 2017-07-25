package org.baseballbaedal.baseballbaedal.MainFragment.Weather;


import android.graphics.drawable.Drawable;

/**
 * Created by qwexo on 2017-06-13.
 */
////////////사용자정의 아이템
public class WeatherListItem {
    private String dateText;
    private String amTempText;
    private int amSkyImage;
    private String amSkyText;
    private String pmTempText;
    private int pmSkyImage;
    private String pmSkyText;

    public void setAmSkyImage(int amSkyImage) {
        this.amSkyImage = amSkyImage;
    }

    public void setAmSkyText(String amSkyText) {
        this.amSkyText = amSkyText;
    }

    public void setAmTempText(String amTempText) {
        this.amTempText = amTempText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public void setPmSkyImage(int pmSkyImage) {
        this.pmSkyImage = pmSkyImage;
    }

    public void setPmSkyText(String pmSkyText) {
        this.pmSkyText = pmSkyText;
    }

    public void setPmTempText(String pmTempText) {
        this.pmTempText = pmTempText;
    }

    public int getAmSkyImage() {
        return amSkyImage;
    }

    public int getPmSkyImage() {
        return pmSkyImage;
    }

    public String getAmSkyText() {
        return amSkyText;
    }

    public String getAmTempText() {
        return amTempText;
    }

    public String getDateText() {
        return dateText;
    }

    public String getPmSkyText() {
        return pmSkyText;
    }

    public String getPmTempText() {
        return pmTempText;
    }
}
