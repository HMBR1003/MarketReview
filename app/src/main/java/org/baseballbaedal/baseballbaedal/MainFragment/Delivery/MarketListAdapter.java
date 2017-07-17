package org.baseballbaedal.baseballbaedal.MainFragment.Delivery;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
        fireStorage = FirebaseStorage.getInstance().getReference().child("market").child(userID).child(userID + ".jpg");

                


//        fireStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Log.d("URI",String.valueOf(uri));
//                Picasso.with(context)
//                        .load(uri)
//                        .into(view.marketImage, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError() {
//                                Toast.makeText(context, "이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show();
//            }
//        });

        view.marketAdressText.setText(item.getMarketAdress());
        view.marketNameText.setText(item.getMarketName());
        view.tellText.setText(item.getTell());

        return view;
    }

    public void clear() {
        list.clear();
    }

    public void addItem(String marketUserID, String marketAdress, String marketName, String tell) {
        MarketListItem item = new MarketListItem();

        item.setMarketAdress(marketAdress);
        item.setMarketUserID(marketUserID);
        item.setMarketName(marketName);
        item.setTell(tell);

        list.add(item);
    }
}
