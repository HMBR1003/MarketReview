package org.baseballbaedal.baseballbaedal.MainFragment.Weather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.baseballbaedal.baseballbaedal.R;

import java.util.ArrayList;

/**
 * Created by qwexo on 2017-06-16.
 */
//////////사용자정의 어뎁터
public class WeatherListViewAdapter extends BaseAdapter {

    private ArrayList<WeatherListItem> list = new ArrayList<>();
    Context context;

    public WeatherListViewAdapter() {

    }

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

        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.weather_listview_item, parent, false);
        }

        TextView dateText = (TextView) convertView.findViewById(R.id.dateText);
        TextView amTempText = (TextView) convertView.findViewById(R.id.amTempText);
        ImageView amSkyImage = (ImageView) convertView.findViewById(R.id.amSkyImage);
        TextView amSkyText = (TextView) convertView.findViewById(R.id.amSkyText);
        TextView pmTempText = (TextView) convertView.findViewById(R.id.pmTempText);
        ImageView pmSkyImage = (ImageView) convertView.findViewById(R.id.pmSkyImage);
        TextView pmSkyText = (TextView) convertView.findViewById(R.id.pmSkyText);

        WeatherListItem listItem = list.get(position);

        dateText.setText(listItem.getDateText());
        amTempText.setText(listItem.getAmTempText());
        Glide.with(context)
                .load(listItem.getAmSkyImage())
                .into(amSkyImage);

        amSkyText.setText(listItem.getAmSkyText());
        pmTempText.setText(listItem.getPmTempText());
        Glide.with(context)
                .load(listItem.getPmSkyImage())
                .into(pmSkyImage);

        pmSkyText.setText(listItem.getPmSkyText());

        return convertView;
    }

    public void clear() {
        list.clear();
    }

    //아이템 추가
    public void addItem(String dateText, String amTempText, int amSkyImage, String amSkyText,
                        String pmTempText, int pmSkyImage, String pmSkyText, Context context) {

        WeatherListItem item = new WeatherListItem();

        item.setDateText(dateText);
        item.setAmTempText(amTempText);
        item.setAmSkyImage(amSkyImage);
        item.setAmSkyText(amSkyText);
        item.setPmTempText(pmTempText);
        item.setPmSkyImage(pmSkyImage);
        item.setPmSkyText(pmSkyText);

        this.context = context;
        list.add(item);
    }
}
