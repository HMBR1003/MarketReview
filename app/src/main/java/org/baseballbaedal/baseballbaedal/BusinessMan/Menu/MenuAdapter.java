package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.baseballbaedal.baseballbaedal.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuManageActivity.MENU_ADD_REQUEST;
import static org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuManageActivity.MENU_EDIT_REQUEST;
import static org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuManageActivity.checkedItem;
import static org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuManageActivity.isDeleteMode;
import static org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuManageActivity.isMainSelect;
import static org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuManageActivity.isMoveMode;
import static org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuManageActivity.selectedPosition;


/**
 * Created by Administrator on 2017-07-15-015.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> implements MenuTouchCallback.OnItemMoveListener,MenuViewHolder.OnListItemClickListener {

    List<MenuData> items = new ArrayList<>();

    public void addItem(MenuData data){
        items.add(data);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
    }


    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_data,parent,false);

        MenuViewHolder holder = new MenuViewHolder(v);
        holder.setOnListItemClickListener(this);
        return holder;
    }


    public String numToWon(int num){
        String tmp = num+"";
        String won;
        if(tmp.length()>3){
            int a = tmp.length()%3;
            int b = tmp.length()/3;
            if(a!=0) {
                String first = tmp.substring(0, a);
                won = first;
                for(int i =0; i<b; i++){
                    won = won+","+ tmp.substring(a,a+3);
                    a=a+3;
                }
            }
            else{
                a=3;
                String first = tmp.substring(0, a);
                won = first;
                for(int i =0; i<b-1; i++){
                    won = won+","+ tmp.substring(a,a+3);
                    a=a+3;
                }
            }
        }
        else{
            won = tmp;
        }
        return won;
    }
    @Override
    public void onBindViewHolder(final MenuViewHolder holder, int position) {
        MenuData item = items.get(position);
        holder.itemView.setTag(item.getMenuKey());
        holder.menuDataName.setText(item.getMenuDataName());
        holder.menuDataPrice.setText(numToWon(Integer.parseInt(item.getMenuDataPrice()))+"원");
        holder.menuDataExplain.setText(item.getMenuDataExplain());
        holder.option.setText(item.getOption());
        if (item.getIsMain()) {
            holder.isMainText.setVisibility(View.VISIBLE);
        }
        else{
            holder.isMainText.setVisibility(View.INVISIBLE);
        }

        //이미지 url
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(item.getUid()).child("menu").child(item.getMenuKey() + ".jpg");

        try {
            Glide
                    .with(context)
                    .using(new FirebaseImageLoader())
                    .load(ref)
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .override(300, 300)
                    .signature(new StringSignature(item.getATime()))
                    .into(holder.menuDataImage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isDeleteMode) {
            if (selectedPosition[position]) {
                holder.itemView.setBackgroundColor(Color.rgb(100, 100, 100));
            } else {
                holder.itemView.setBackgroundColor(Color.rgb(255, 255, 255));
            }
        } else if (isMainSelect) {
            if (position == checkedItem) {
                holder.itemView.setBackgroundColor(Color.argb(100,255, 66, 66));
            }
            else{
                holder.itemView.setBackgroundColor(Color.rgb(255, 255, 255));
            }
        }
        else{
            holder.itemView.setBackgroundColor(Color.rgb(255, 255, 255));
        }


        holder.menuDataImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isMoveMode) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        startDragListener.onStartDrag(holder);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onItemMove(int fromPostion, int toPostion) {
        Collections.swap(items,fromPostion,toPostion);
        notifyItemMoved(fromPostion,toPostion);
        return false;
    }

    @Override
    public void onListItemClick(View view, int position) {

        if (isDeleteMode) {
            if (selectedPosition[position]) {
                selectedPosition[position] = false;
                view.setBackgroundColor(Color.rgb(255, 255, 255));
            } else {
                selectedPosition[position] = true;
                view.setBackgroundColor(Color.rgb(100, 100, 100));
            }
            int i = 0;
            for (boolean a : selectedPosition) {
                i++;
                Log.d("불리언", i + Boolean.toString(a));
            }
        } else if (isMainSelect) {
            checkedItem = position;
            notifyDataSetChanged();
            view.setBackgroundColor(Color.argb(100,255, 66, 66));
//                    if (oldPosition < parent.getChildCount())
//                        parent.getChildAt(oldPosition).setBackgroundColor(Color.rgb(255, 255, 255));
//                    oldPosition = position;

        } else {
            Intent intent = new Intent(context, MenuAddActivity.class);
            intent.putExtra("menuId", items.get(position).getMenuKey());
            intent.putExtra("isEdit", true);
            ((MenuManageActivity) context).startActivityForResult(intent,MENU_EDIT_REQUEST);
        }
    }

    public interface OnStartDragListener{
        void onStartDrag(MenuViewHolder viewHolder);
    }
    private final Context context;
    private final OnStartDragListener startDragListener;

    public MenuAdapter(Context context, OnStartDragListener startDragListener){
        this.context = context;
        this.startDragListener = startDragListener;
    }
}
