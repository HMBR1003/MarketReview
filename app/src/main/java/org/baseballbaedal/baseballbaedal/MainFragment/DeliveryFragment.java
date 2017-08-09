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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.tsengvn.typekit.TypekitContextWrapper;

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

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(TypekitContextWrapper.wrap(context));
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_delivery,container,false);
        mainActivity = (MainActivity)getActivity();
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentDeliveryBinding.bind(getView());

        Listener listener = new Listener();
        binding.chickenButton.setOnClickListener(listener);
        binding.pizzaButton.setOnClickListener(listener);
        binding.hamButton.setOnClickListener(listener);
        binding.footButton.setOnClickListener(listener);
        binding.etcButton.setOnClickListener(listener);

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
