package com.zitano.walter.photography;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    AdapterMain AdapterMain;
    private AdView mAdView;
    private InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AudienceNetworkAds.initialize(this);
       // AdSettings.setDebugBuild(true);

        FirebaseMessaging.getInstance().subscribeToTopic("walter");

        //recyclerview
        mRecyclerView = findViewById(R.id.recycler_view);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        AdapterMain = new AdapterMain(this, getPlayers());
        mRecyclerView.setAdapter(AdapterMain);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    //add model to arraylist
    private ArrayList<Model_Main> getPlayers(){
        ArrayList<Model_Main> models = new ArrayList<>();

        Model_Main P = new Model_Main();
        P.setTitle("Portraits");
        P.setDescription("Portraits");
        P.setImg(R.drawable.portraits);
        models.add(P);

        P = new Model_Main();
        P.setTitle("Outdoor");
        P.setDescription("Outdoor");
        P.setImg(R.drawable.outdoor);
        models.add(P);

        P = new Model_Main();
        P.setTitle("Studio/Indoors");
        P.setDescription("Studio/Indoors");
        P.setImg(R.drawable.studio);
        models.add(P);

        P = new Model_Main();
        P.setTitle("Fashion Photography");
        P.setDescription("Fashion Photography");
        P.setImg(R.drawable.fashion);
        models.add(P);

        P = new Model_Main();
        P.setTitle("Baby Bump");
        P.setDescription("Baby Bump");
        P.setImg(R.drawable.baby_bump);
        models.add(P);

        P = new Model_Main();
        P.setTitle("Traditional");
        P.setDescription("Traditional");
        P.setImg(R.drawable.traditional);
        models.add(P);

        return models;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_main, menu);
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
        } else if (id == R.id.privacy) {
            startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
        } else if (id == R.id.feedback) {
            startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
        } else if (id == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
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

    public class AdapterMain extends RecyclerView.Adapter<Holder_Main> {
        Context c;
        ArrayList<Model_Main> models;

        public AdapterMain(Context c, ArrayList<Model_Main> models) {
            this.c = c;
            this.models = models;
        }

        @NonNull
        @Override
        public Holder_Main onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //convert xml to view obj
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_main, null);
            return new Holder_Main(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder_Main holder, int position) {
            //bind data to our views
            holder.mTitleTv.setText(models.get(position).getTitle());
            holder.mDescrTv.setText(models.get(position).getDescription());
            holder.mImageIv.setImageResource(models.get(position).getImg());

            //animation
            Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    if (models.get(pos).getTitle().equals("Portraits")){
                        Intent intent = new Intent(c, Portraits.class);
                        c.startActivity(intent);
                        showInterstitial();
                    }
                    if (models.get(pos).getTitle().equals("Outdoor")){
                        Intent intent = new Intent(c, Outdoor.class);
                        c.startActivity(intent);
                        showInterstitial();
                    }
                    if (models.get(pos).getTitle().equals("Studio/Indoors")){
                        Intent intent = new Intent(c, Studio_Indoors.class);
                        c.startActivity(intent);
                        showInterstitial();
                    }
                    if (models.get(pos).getTitle().equals("Fashion Photography")){
                        Intent intent = new Intent(c, Fashion_Photography.class);
                        c.startActivity(intent);
                        showInterstitial();
                    }
                    if (models.get(pos).getTitle().equals("Baby Bump")){
                        Intent intent = new Intent(c, Baby_Bump.class);
                        c.startActivity(intent);
                        showInterstitial();
                    }
                    if (models.get(pos).getTitle().equals("Traditional")){
                        Intent intent = new Intent(c, Traditional.class);
                        c.startActivity(intent);
                        showInterstitial();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return models.size();
        }
    }

    @Override
    protected void onStart() {
        loadAd();
        super.onStart();
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getString(R.string.admob_interstitial_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MainActivity.this.interstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        //Toast.makeText(AndroidTricks.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;

                     /*   String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Toast.makeText(
                                AndroidTricks.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
                                .show();*/
                    }
                });
    }
    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null) {
            interstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
            //startGame();
        }
    }

}
