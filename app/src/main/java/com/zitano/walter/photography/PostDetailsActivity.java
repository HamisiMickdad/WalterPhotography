package com.zitano.walter.photography;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostDetailsActivity extends AppCompatActivity {
    MaterialTextView mTitleTv, mDetailTv;
    AppCompatImageView mImageIv;
    Bitmap bitmap;

    Button mShareBtn;
    private AdView mAdView;


    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Walter Photography");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //initialize views
        mDetailTv = findViewById(R.id.descriptionTv);
        mTitleTv = findViewById(R.id.titleTv);
        mImageIv = findViewById(R.id.imageView);
        mShareBtn = findViewById(R.id.shareBtn);

        // Get Data from intent
        String image = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("description");

        //set data to views
        mTitleTv.setText(title);
        mDetailTv.setText(desc);
        //Picasso.get().load(image).into(mImageIv);
        Glide.with(getApplicationContext()).load(image).into(mImageIv);

        //Share button click handle
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //sharePost();
                shareImage();

            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void shareImage() {
        try {
            //get image from Image View as  bitmap
            bitmap = ((BitmapDrawable)mImageIv.getDrawable()).getBitmap();
            //get title and description and save to string s
            //String s = mTitleTv.getText().toString() + "\n" + mDetailTv.getText().toString();
            String s = "You can view more of Walter Photography photos from this app : " + " https://play.google.com/store/apps/details?id=" + getPackageName();

            File file = new File(getExternalCacheDir(), "sample.jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            //intent to share image & text
           /* Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, s);//put the text
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpg");
            startActivity(Intent.createChooser(intent, "Share via"));*/
            Intent share = new Intent("android.intent.action.SEND");
            share.putExtra(Intent.EXTRA_TEXT, s);//put the text
            //share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            share.setType("image/jpeg");
            Uri uri = FileProvider.getUriForFile(PostDetailsActivity.this, BuildConfig.APPLICATION_ID+ ".provider", (file));
            share.putExtra("android.intent.extra.STREAM", uri);

            startActivity(Intent.createChooser(share, "Share via"));


        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    // Handle back press go back to the previous activity

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //showInterstitials();
            finish();
        } else if (id == R.id.about) {
            showDialogAbout();
        } else if (id == R.id.feedback) {
            startActivity(new Intent(PostDetailsActivity.this, FeedbackActivity.class));
        } else if (id == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(PostDetailsActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.menu_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey check out this photography app that showcases the amazing work of Walter Photography. Get the app here for free: https://play.google.com/store/apps/details?id=" + getPackageName();
            //sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Best Football Predictions App on Play Store");
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
                    Toast.makeText(PostDetailsActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:{
                //if request is cancelled the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission is granted, save image
                    //saveImage();
                }
                else {
                    //Permission denied
                    Toast.makeText(this, "Allow Permission to save the image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
