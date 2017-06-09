package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.baseballbaedal.baseballbaedal.R;

/**
 * Created by Administrator on 2017-06-09.
 */

public class MenuDataView extends LinearLayout{
    TextView menuDataName;
    TextView menuDataPrice;
    TextView menuDataExplain;
    ImageView menuDataImage;
    public MenuDataView(Context context) {
        super(context);
        init(context);
    }

    public MenuDataView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_data,this,true);

        menuDataName=(TextView)findViewById(R.id.menuDataName);
        menuDataPrice=(TextView)findViewById(R.id.menuDataPrice);
        menuDataExplain=(TextView)findViewById(R.id.menuDataExplain);
        menuDataImage=(ImageView)findViewById(R.id.menuDataImage);
    }

    public void setMenuDataName(String menuDataName){
        this.menuDataName.setText(menuDataName);
    }

    public void setMenuDataPrice(String menuDataPrice){
        this.menuDataPrice.setText(menuDataPrice);
    }

    public void setMenuDataExplain(String menuDataExplain){
        this.menuDataExplain.setText(menuDataExplain);
    }
}
