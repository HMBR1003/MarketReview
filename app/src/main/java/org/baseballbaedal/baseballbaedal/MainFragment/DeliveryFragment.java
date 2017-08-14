package org.baseballbaedal.baseballbaedal.MainFragment;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.otto.Subscribe;
//import com.tsengvn.typekit.TypekitContextWrapper;

import org.baseballbaedal.baseballbaedal.BusProvider;
import org.baseballbaedal.baseballbaedal.HeightEvent;
import org.baseballbaedal.baseballbaedal.MainActivity;
import org.baseballbaedal.baseballbaedal.MainFragment.Delivery.MarketListActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.FragmentDeliveryBinding;
import org.baseballbaedal.baseballbaedal.databinding.FragmentHomeBinding;

/**
 * Created by Administrator on 2017-05-08.
 */

public class DeliveryFragment extends Fragment {

    private FragmentDeliveryBinding binding;
    MainActivity mainActivity;
    int height;
    int width;
    int containerHeight;
    int imageWidth;
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(TypekitContextWrapper.wrap(context));
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_delivery, container, false);
        mainActivity = (MainActivity) getActivity();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        Log.d("리슘", "d");
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("퓨즈", "d");
        BusProvider.getInstance().unregister(this);
    }

    public void init() {
        binding.chickenContainer.setLayoutParams(new LinearLayout.LayoutParams(0, containerHeight, 1));
        binding.pizzaContainer.setLayoutParams(new LinearLayout.LayoutParams(0, containerHeight, 1));
        binding.footContainer.setLayoutParams(new LinearLayout.LayoutParams(0, containerHeight, 1));
        binding.hamContainer.setLayoutParams(new LinearLayout.LayoutParams(0, containerHeight, 1));
        binding.etcContainer.setLayoutParams(new LinearLayout.LayoutParams(0, containerHeight, 1));

        binding.chickenButton.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
        binding.pizzaButton.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
        binding.footButton.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
        binding.hamButton.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
        binding.etcButton.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
    }

    @Subscribe
    public void onPushEvent(HeightEvent heightEvent) {
        height = heightEvent.getHeight();
        width = heightEvent.getWidth();
        containerHeight = height / 3;
        imageWidth = (width / 100) * 37;
        init();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentDeliveryBinding.bind(getView());

        Listener listener = new Listener();
        TouchListener touchListener = new TouchListener();
        binding.chickenButton.setOnClickListener(listener);
        binding.pizzaButton.setOnClickListener(listener);
        binding.hamButton.setOnClickListener(listener);
        binding.footButton.setOnClickListener(listener);
        binding.etcButton.setOnClickListener(listener);
        binding.chickenButton.setOnTouchListener(touchListener);
        binding.pizzaButton.setOnTouchListener(touchListener);
        binding.hamButton.setOnTouchListener(touchListener);
        binding.footButton.setOnTouchListener(touchListener);
        binding.etcButton.setOnTouchListener(touchListener);
        Glide.with(this)
                .load(R.drawable.chicken_button1)
                .into(binding.chickenButton);
        Glide.with(this)
                .load(R.drawable.pizza_button1)
                .into(binding.pizzaButton);
        Glide.with(this)
                .load(R.drawable.ham_button1)
                .into(binding.hamButton);
        Glide.with(this)
                .load(R.drawable.foot_button1)
                .into(binding.footButton);
        Glide.with(this)
                .load(R.drawable.etc_button1)
                .into(binding.etcButton);
        ;

    }

    public class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                // if pressed
                case MotionEvent.ACTION_DOWN: {
                        /* 터치하고 있는 상태 */
                    if (v.equals(binding.chickenButton)) {
                        binding.chickenButton.setImageResource(R.drawable.chicken_button2);
                    } else if (v.equals(binding.pizzaButton)) {
                        binding.pizzaButton.setImageResource(R.drawable.pizza_button2);
                    } else if (v.equals(binding.hamButton)) {
                        binding.hamButton.setImageResource(R.drawable.ham_button2);
                    } else if (v.equals(binding.footButton)) {
                        binding.footButton.setImageResource(R.drawable.foot_button2);
                    } else if (v.equals(binding.etcButton)) {
                        binding.etcButton.setImageResource(R.drawable.etc_button2);
                    }

                    break;
                }

                // if released
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: {
                        /* 터치가 안 되고 있는 상태 */
                    if (v.equals(binding.chickenButton)) {
                        binding.chickenButton.setImageResource(R.drawable.chicken_button1);
                    } else if (v.equals(binding.pizzaButton)) {
                        binding.pizzaButton.setImageResource(R.drawable.pizza_button1);
                    } else if (v.equals(binding.hamButton)) {
                        binding.hamButton.setImageResource(R.drawable.ham_button1);
                    } else if (v.equals(binding.footButton)) {
                        binding.footButton.setImageResource(R.drawable.foot_button1);
                    } else if (v.equals(binding.etcButton)) {
                        binding.etcButton.setImageResource(R.drawable.etc_button1);
                    }

                    break;
                }

                default: {
                    break;
                }
            }
            return false;
        }
    }

    public class Listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MarketListActivity.class);
            if (v == binding.chickenButton) {
                intent.putExtra("menu", "치킨");
                startActivity(intent);
            } else if (v == binding.pizzaButton) {
                intent.putExtra("menu", "피자");
                startActivity(intent);
            } else if (v == binding.hamButton) {
                intent.putExtra("menu", "햄버거");
                startActivity(intent);
            } else if (v == binding.footButton) {
                intent.putExtra("menu", "족발");
                startActivity(intent);
            } else if (v == binding.etcButton) {
                intent.putExtra("menu", "기타음식");
                startActivity(intent);
            }
        }
    }

}
