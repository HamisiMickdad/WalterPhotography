package com.zitano.walter.photography;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class Hair extends MultiDexApplication {
    @Override
    public void onCreate() {

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //AudienceNetworkAds.initialize(this);
        //AdSettings.addTestDevice("53571afe-9459-4e6f-ab34-43d6e6e274d5");
        FirebaseDatabase.getInstance();


        super.onCreate();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
