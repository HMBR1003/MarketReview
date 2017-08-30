package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.baseballbaedal.baseballbaedal.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017-07-26-026.
 */

public class ReviewAdapter extends BaseAdapter {

    ArrayList<MarketReviewListItem> list;
    Context context;
    StorageReference storageReference;

    public ReviewAdapter(ArrayList<MarketReviewListItem> list) {
        if (list != null)
            this.list = list;
        else
            this.list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void clear() {
        list.clear();
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
        MarketReviewView view = new MarketReviewView(context);

        MarketReviewListItem item = list.get(position);

        storageReference = FirebaseStorage.getInstance().getReference().child("market").child("review").child(item.getReviewID() + ".jpg");
        if (item.getImageBool().equals("true")) {
            view.imageView.setVisibility(View.VISIBLE);
            try {
                Glide
                        .with(context)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(Glide.with(context).load(R.drawable.loading))
                        .override(300, 300)
                        .signature(new StringSignature(item.getTime()))
                        .into(view.imageView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            view.imageView.setVisibility(View.GONE);
        }

        view.userNameText.setText(item.getUserName());
        String date = new SimpleDateFormat("yyyy년MM월dd일 HH:mm").format(new Date(Long.parseLong(item.getTime())));
        view.timeText.setText(date);
        view.bodyText.setText(item.getBody());

        switch (Integer.parseInt(item.getScore())) {     //평점 표시
            case 5:
                view.scoreImage5.setImageDrawable(view.getResources().getDrawable(R.drawable.fillstar));
            case 4:
                view.scoreImage4.setImageDrawable(view.getResources().getDrawable(R.drawable.fillstar));
            case 3:
                view.scoreImage3.setImageDrawable(view.getResources().getDrawable(R.drawable.fillstar));
            case 2:
                view.scoreImage2.setImageDrawable(view.getResources().getDrawable(R.drawable.fillstar));
            case 1:
                view.scoreImage1.setImageDrawable(view.getResources().getDrawable(R.drawable.fillstar));
                break;
        }

        return view;
    }

    public void addItem(String userName, String body, String score, String reviewID, String imageBool,String time) {
        MarketReviewListItem item = new MarketReviewListItem();

        item.setUserName(userName);
        item.setBody(body);
        item.setScore(score);
        item.setReviewID(reviewID);
        item.setImageBool(imageBool);
        item.setTime(time);

        list.add(item);
    }
}
