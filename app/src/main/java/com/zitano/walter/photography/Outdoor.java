package com.zitano.walter.photography;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zitano.walter.photography.models.Model;

import java.util.ArrayList;
import java.util.List;

public class Outdoor extends AppCompatActivity {
    GridLayoutManager mLayoutManager; //for sorting
    SharedPreferences mSharedPref; //For saving sort settings
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Model> options;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braids);

        //Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Outdoor");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mSharedPref = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "newest"); //where if no setting is selected newest is default

        if (mSorting.equals("newest")) {
            int numberOfColumns = 3;
            mLayoutManager = new GridLayoutManager(this, numberOfColumns);
            //this will load the items from bottom means newest first
            mLayoutManager.setReverseLayout(false);
            //mLayoutManager.setStackFromEnd(true);
        } else if (mSorting.equals("oldest")) {
            int numberOfColumns = 3;
            mLayoutManager = new GridLayoutManager(this, numberOfColumns);
            //this will load the items from top means oldest first
            mLayoutManager.setReverseLayout(true);
            //mLayoutManager.setStackFromEnd(false);
        }


        //recyclerview
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);


        //send query to firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference().child("Data").child("outdoor");
        showData();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    //show data
    private void showData() {
        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(mRef, Model.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Model model) {
                viewHolder.setDetails(getApplicationContext(), model.getTitle(), model.getDescription(), model.getImage());


            }

            @Override
            public Model getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //inflating layout row.xml
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
                ViewHolder viewHolder = new ViewHolder(itemView);

                //item click listener
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //get data from firebase at the position clicked
                        String mTitle = getItem(position).getTitle();
                        String mDesc = getItem(position).getDescription();
                        String mImage = getItem(position).getImage();


                        //Pass data to the post detail activity
                        Intent intent = new Intent(view.getContext(), PostDetailsActivity.class);
                        intent.putExtra("title", mTitle);// put title
                        intent.putExtra("description", mDesc);// put description
                        intent.putExtra("image", mImage);//put image url
                        startActivity(intent);//start activity

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                });

                return viewHolder;
            }
        };

        //set as linearlayout
        mRecyclerView.setLayoutManager(mLayoutManager);
        firebaseRecyclerAdapter.startListening();
        //set adapter to firebase recycler view
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    //Load data into recyclerview on start
    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.about) {
            showDialogAbout();
        } else if (id == R.id.feedback) {
            startActivity(new Intent(Outdoor.this, FeedbackActivity.class));
        } else if (id == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(Outdoor.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.menu_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Download the best hairstyles app on playstore . Download here https://play.google.com/store/apps/details?id=" + getPackageName();
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Best Hairstyles App on Play Store");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(sharingIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogAbout() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.about);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_version)).setText("Version " + BuildConfig.VERSION_NAME);
        ((View) dialog.findViewById(R.id.bt_getcode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Outdoor.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


}
