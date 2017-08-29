package org.baseballbaedal.baseballbaedal.Order;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017-08-29.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListViewHolder> implements View.OnTouchListener {

    private List<OrderData> items;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            // if pressed
            case MotionEvent.ACTION_DOWN: {
                        /* 터치하고 있는 상태 */
                //TODO
                break;
            }

            // if released
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                        /* 터치가 안 되고 있는 상태 */
                //TODO
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
        return null;
    }

    @Override
    public void onBindViewHolder(OrderListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
