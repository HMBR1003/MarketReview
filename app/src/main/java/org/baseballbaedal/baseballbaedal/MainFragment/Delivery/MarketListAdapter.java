package org.baseballbaedal.baseballbaedal.MainFragment.Delivery;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.baseballbaedal.baseballbaedal.R;

import java.util.ArrayList;


/**
 * Created by qwexo on 2017-06-16.
 */

public class MarketListAdapter extends BaseAdapter {

    private ArrayList<MarketListItem> list = new ArrayList<>();
    Context context;
    StorageReference fireStorage;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();
        final MarketListView view = new MarketListView(context);

        MarketListItem item = list.get(position);

        String userID = item.getMarketUserID();
        String stamp = item.getaTime();
        fireStorage = FirebaseStorage.getInstance().getReference().child("market").child(userID).child(userID + ".jpg");

        try {
            Glide
                    .with(context)
                    .using(new FirebaseImageLoader())
                    .load(fireStorage)
                    .override(300, 300)
                    .signature(new StringSignature(stamp))
                    .placeholder(R.drawable.jamsil)
                    .thumbnail(0.1f)
                    .into(view.marketImage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.marketAdressText.setText(item.getMarketAdress());
        view.marketNameText.setText(item.getMarketName());
        view.tellText.setText(item.getTell());
        if(item.getIsTakeout()) {
           view.minPriceContainer.setVisibility(View.GONE);
        }
        else{
            view.minPriceText.setText(item.getMinPrice());
        }
        return view;
    }

    public void clear() {
        list.clear();
    }

    public void addItem(String marketUserID, String marketAdress, String marketName, String tell, String minPrice, String aTime,boolean isTakeout) {
        MarketListItem item = new MarketListItem();

        item.setMarketAdress(marketAdress);
        item.setMarketUserID(marketUserID);
        item.setMarketName(marketName);
        item.setMinPrice(minPrice);
        item.setTell(tell);
        item.setaTime(aTime);
        item.setIsTakeout(isTakeout);
        list.add(item);
    }
}
