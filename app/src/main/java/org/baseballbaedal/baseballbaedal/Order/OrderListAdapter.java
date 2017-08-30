package org.baseballbaedal.baseballbaedal.Order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.baseballbaedal.baseballbaedal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-29.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListViewHolder> implements View.OnTouchListener {

    Context context;
    private List<OrderData> items;

    public OrderListAdapter() {
        items = new ArrayList<OrderData>();
    }

    public void add(OrderData data) {
        items.add(data);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            // if pressed
            case MotionEvent.ACTION_DOWN: {
                 /* 터치하고 있는 상태 */
                v.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                ((TextView)v).setTextColor(context.getResources().getColor(R.color.white));
                break;
            }

            // if released
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                /* 터치가 안 되고 있는 상태 */
                v.setBackgroundColor(context.getResources().getColor(R.color.white));
                ((TextView)v).setTextColor(context.getResources().getColor(R.color.black));
                break;
            }

            default: {
                break;
            }
        }
        return false;
    }

    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_finish_list_item, parent, false);

        OrderListViewHolder holder = new OrderListViewHolder(v);
        holder.setOnTouchListener(this);
        return holder;

    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(OrderListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
