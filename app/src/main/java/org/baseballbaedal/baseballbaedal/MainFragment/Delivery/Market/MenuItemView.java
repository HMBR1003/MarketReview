package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.baseballbaedal.baseballbaedal.R;

/**
 * Created by Administrator on 2017-07-26-026.
 */

public class MenuItemView extends LinearLayout{

    private TextView isMainText;
    private TextView menuName;
    private TextView menuPrice;
    private ImageView menuImage;
    private LinearLayout itemContainer;
    public MenuItemView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_item,this,true);

        isMainText = (TextView)findViewById(R.id.isMainText);
        menuName=(TextView)findViewById(R.id.menuName);
        menuPrice=(TextView)findViewById(R.id.menuPrice);
        menuImage=(ImageView)findViewById(R.id.menuImage);
        itemContainer=(LinearLayout)findViewById(R.id.itemContainer);
    }

    public void setMenuName(String menuName){
        this.menuName.setText(menuName);
    }

    public void setMenuPrice(String menuPrice){
        this.menuPrice.setText(menuPrice);
    }

    public ImageView getMenuImage(){
        return menuImage;
    }

    public LinearLayout getItemContainer(){
        return itemContainer;
    }

    public TextView getMenuName(){
        return menuName;
    }

    public TextView getMenuPrice(){
        return menuPrice;
    }

    public void setIsMainText(){
        isMainText.setText("대표 메뉴");
    }
}
