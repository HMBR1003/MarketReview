package org.baseballbaedal.baseballbaedal;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.baseballbaedal.baseballbaedal.MainFragment.DeliveryFragment;
import org.baseballbaedal.baseballbaedal.databinding.ActivityGlideTestBinding;

public class GlideTestActivity extends AppCompatActivity {

    StorageReference ref;
    ActivityGlideTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_glide_test);


        binding.glideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(GlideTestActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();

                ref = FirebaseStorage.getInstance().getReference().child("market").child(user.getUid()).child("market.jpg");
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("이미지 다운로드 경로",String.valueOf(uri));
                        Toast.makeText(GlideTestActivity.this, String.valueOf(uri), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        Toast.makeText(GlideTestActivity.this, "실패", Toast.LENGTH_SHORT).show();
                    }
                });
                Glide.with(GlideTestActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(ref)
                        .into(binding.glideImageView);
            }
        });

    }
}
