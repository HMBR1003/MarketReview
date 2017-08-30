package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.baseballbaedal.baseballbaedal.R;

/**
 * Created by qwexo on 2017-08-30.
 */

public class MarketReviewView extends LinearLayout {
    ImageView imageView,scoreImage1,scoreImage2,scoreImage3,scoreImage4,scoreImage5;
    TextView timeText,bodyText,userNameText;
    public MarketReviewView(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.market_review_list_item,this,true);

        imageView = (ImageView)findViewById(R.id.imageView);
        scoreImage1 = (ImageView)findViewById(R.id.scoreImage1);
        scoreImage2 = (ImageView)findViewById(R.id.scoreImage2);
        scoreImage3 = (ImageView)findViewById(R.id.scoreImage3);
        scoreImage4 = (ImageView)findViewById(R.id.scoreImage4);
        scoreImage5 = (ImageView)findViewById(R.id.scoreImage5);
        timeText = (TextView)findViewById(R.id.timeText);
        bodyText = (TextView)findViewById(R.id.bodyText);
        userNameText = (TextView)findViewById(R.id.userNameText);
    }
}
