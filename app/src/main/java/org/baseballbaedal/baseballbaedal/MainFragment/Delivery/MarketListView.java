package org.baseballbaedal.baseballbaedal.MainFragment.Delivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.baseballbaedal.baseballbaedal.R;
import org.w3c.dom.Text;

/**
 * Created by qwexo on 2017-06-19.
 */

public class MarketListView extends LinearLayout{
    ImageView marketImage;
    TextView marketNameText, marketAdressText, tellText, minPriceText;
    LinearLayout minPriceContainer;

    public MarketListView(Context context){
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.market_list_item,this,true);

        marketImage = (ImageView)findViewById(R.id.marketImage);
        marketNameText = (TextView)findViewById(R.id.marketNameText);
        marketAdressText = (TextView)findViewById(R.id.marketAdressText);
        tellText = (TextView)findViewById(R.id.tellText);
        minPriceText = (TextView)findViewById(R.id.minPriceText);
        minPriceContainer = (LinearLayout)findViewById(R.id.minPriceContainer);
    }
}
