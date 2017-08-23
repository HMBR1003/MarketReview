package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuInfo;
import org.baseballbaedal.baseballbaedal.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-07-26-026.
 */

public class MenuAdapter extends BaseAdapter {

    ArrayList<MenuItem> list = new ArrayList<MenuItem>();
    ArrayList<MenuInfo> infoList = new ArrayList<MenuInfo>();
    Context context;
    int imageWidth;

    public MenuAdapter(Context context, int imageWidth) {
        this.context = context;
        this.imageWidth = imageWidth;
    }

    public void add(MenuItem item) {
        list.add(item);
    }
    public void addInfo(MenuInfo item) {
        infoList.add(item);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void clear() {
        list.clear();
        infoList.clear();
    }

    public MenuInfo getInfo(int position){
        return infoList.get(position);
    }

    public String getMenuKey(int position){
        return list.get(position).menuKey;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuItem item = list.get(position);
        MenuItemView view = new MenuItemView(context);
        view.setMenuName(item.menuName);
        view.setMenuPrice(item.menuPrice);
        if (item.isMain) {
            view.setIsMainText();
        }
        view.getItemContainer().setLayoutParams(new LinearLayout.LayoutParams(imageWidth - 50, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.getMenuImage().setLayoutParams(new LinearLayout.LayoutParams(imageWidth - 50, imageWidth - 50));

        //이미지 url
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(item.marketId).child("menu").child(item.menuKey + ".jpg");

        try {
            Glide
                    .with(context)
                    .using(new FirebaseImageLoader())
                    .load(ref)
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .override(300, 300)
                    .signature(new StringSignature(item.aTime))
                    .into(view.getMenuImage());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}

