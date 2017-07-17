package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Administrator on 2017-07-12.
 */

public class  MenuItemTouchHelperCallback extends ItemTouchHelper.Callback{
    public interface OnItemMoveListener{
        boolean onItemMove(int from, int to);
    }
    private final OnItemMoveListener itemMoveListener;

    public MenuItemTouchHelperCallback(OnItemMoveListener listener){
        itemMoveListener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        itemMoveListener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }
}