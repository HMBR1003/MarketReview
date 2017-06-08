package org.baseballbaedal.baseballbaedal.MainFragment;


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

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.baseballbaedal.baseballbaedal.MainActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.FragmentDeliveryBinding;
import org.baseballbaedal.baseballbaedal.databinding.FragmentHomeBinding;

/**
 * Created by Administrator on 2017-05-08.
 */

public class DeliveryFragment extends Fragment {

    private FragmentDeliveryBinding binding;
    MainActivity mainActivity;
    StorageReference ref;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentDeliveryBinding.bind(getView());

        binding.baedalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_delivery,container,false);
        mainActivity = (MainActivity)getActivity();
        return rootView;
    }
}
