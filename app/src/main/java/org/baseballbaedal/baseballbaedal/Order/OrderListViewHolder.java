package org.baseballbaedal.baseballbaedal.Order;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.baseballbaedal.baseballbaedal.R;

/**
 * Created by Administrator on 2017-08-29.
 */

public class OrderListViewHolder extends RecyclerView.ViewHolder {
    private View.OnTouchListener onTouchListener;
    private TextView orderState, orderTime, orderMenu, detailButton;

//    //어댑터에 아이템 클릭 동작을 전달해주기 위한 리스너 선언
//    protected interface ListItemClickListener {
//        void onListItemClick(View v, int position);
//    }
//
//    //마찬가지로 어댑터에 아이템 롱클릭 동작을 전달해주기 위한 리스너 선언
//    protected interface ListItemLongClickListener {
//        void onListItemLongClick(View v, int position);
//    }
//
//    //클릭리스너 세팅 함수
//    protected void setListItemClickListener(ListItemClickListener clickListener) {
//        this.clickListener = clickListener;
//    }
//    //롱클릭리스너 세팅 함수
//    protected void setListItemLongClickListener(ListItemLongClickListener longClickListener) {
//        this.longClickListener = longClickListener;
//    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    protected OrderListViewHolder(View itemView) {
        super(itemView);
        orderState = (TextView)itemView.findViewById(R.id.orderState);
        orderMenu = (TextView)itemView.findViewById(R.id.orderMenu);
        orderTime = (TextView)itemView.findViewById(R.id.orderTime);
        detailButton = (TextView)itemView.findViewById(R.id.detailButton);
        //주문정보 보기 온터치 리스너 설정
        detailButton.setOnTouchListener(onTouchListener);
    }

    public void onBindView(int data){

    };
}