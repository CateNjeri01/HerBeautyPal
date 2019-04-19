package com.example.mercie.example;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mercie.example.models.Salon;
import com.example.mercie.example.models.SalonService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SalonActivity extends AppCompatActivity implements SalonServicesFragment.SalonServiceFragmentListener {

    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;

    private Salon salon = null;

    //Firebase Variables
    private StorageReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);

        mAuth = FirebaseAuth.getInstance();

        //OK SO far
        salon = (Salon) getIntent().getSerializableExtra("salon");
        mRef = FirebaseStorage.getInstance().getReference().child("cover_images");

        ImageView goBackIV = findViewById(R.id.go_back_iv);
        goBackIV.setOnClickListener(view -> {
            startActivity(new Intent(this, SalonsActivity.class));
            finish();
        });

        ImageView coverImageIV = findViewById(R.id.cover_image_iv);

        final long MB = 1024 * 1024;
        //Populate the cover image
        mRef.child(salon.getCoverImage())
                .getBytes(MB)
                .addOnSuccessListener(
                        bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            coverImageIV.setImageBitmap(bitmap);
                        }
                )
                .addOnFailureListener(e -> Toast.makeText(this, "Cover Image Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());

        viewPager = findViewById(R.id.container);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SalonInfoFragment.getInstance(salon), "Info");
        adapter.addFragment(SalonServicesFragment.getInstance(salon.getId()), "Services");
        adapter.addFragment(new SalonOffersFragment(), "Offers");
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void RequestSalonService(SalonService service) {

        //Getting Client Name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Service Request Dialog");
        builder.setMessage("Are you sure you want to request " + service.getServiceName() + " service from this salon?");

        builder
                .setPositiveButton("Sure", (DialogInterface dialog, int which) -> {
                    Intent intent = new Intent(this, ClientSendNotification.class);
                    intent.putExtra(ClientSendNotification.SALON_ID, salon.getId());
                    intent.putExtra(ClientSendNotification.SERVICE, service);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {
                    Toast.makeText(this, "Request Cancelled", Toast.LENGTH_SHORT).show();
                });

        builder.show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        }
    }
}



