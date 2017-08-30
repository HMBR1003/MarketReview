package org.baseballbaedal.baseballbaedal.Order;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.baseballbaedal.baseballbaedal.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017-08-29.
 */

public class OrderListViewHolder extends RecyclerView.ViewHolder {
    private View.OnTouchListener onTouchListener;
    private ListItemClickListener listItemClickListener;
    private TextView orderState, orderTime, orderMenu, detailButton;


    //어댑터에 아이템 클릭 동작을 전달해주기 위한 리스너 선언
    protected interface ListItemClickListener {
        void onListItemClick(View v, int position);
    }

//
//    //마찬가지로 어댑터에 아이템 롱클릭 동작을 전달해주기 위한 리스너 선언
//    protected interface ListItemLongClickListener {
//        void onListItemLongClick(View v, int position);
//    }
//

//    //롱클릭리스너 세팅 함수
//    protected void setListItemLongClickListener(ListItemLongClickListener longClickListener) {
//        this.longClickListener = longClickListener;
//    }

    public void setListener(View.OnTouchListener onTouchListener, ListItemClickListener listItemClickListener) {
        this.onTouchListener = onTouchListener;
        this.listItemClickListener = listItemClickListener;
    }

    protected OrderListViewHolder(View itemView) {
        super(itemView);
        orderState = (TextView) itemView.findViewById(R.id.orderState);
        orderMenu = (TextView) itemView.findViewById(R.id.orderMenu);
        orderTime = (TextView) itemView.findViewById(R.id.orderTime);
        detailButton = (TextView) itemView.findViewById(R.id.detailButton);
        //주문정보 보기 온터치 리스너 설정
        detailButton.setOnTouchListener(onTouchListener);
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemClickListener.onListItemClick(v,getAdapterPosition());
            }
        });

    }

    public void onBindView(OrderData data) {
        switch (data.getOderState()) {
            case "0":
                orderState.setText("주문 신청");
                break;
            case "1":
                orderState.setText("주문 접수/배달중");
                break;
            case "2":
                orderState.setText("배달 완료");
                break;
            case "3":
                orderState.setText("주문 취소");
                break;
        }
        orderMenu.setText(data.getMenus());
        orderTime.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(data.getDate())));
    }

    ;
}