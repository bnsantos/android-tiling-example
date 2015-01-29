package com.bnsantos.tilingexample;

import android.app.Application;

import com.bnsantos.tilingexample.service.TileService;

import retrofit.RestAdapter;

/**
 * Created by bruno on 26/01/15.
 */
public class App extends Application {
    public static final String END_POINT = "http://54.85.216.195:3000";

    private TileService mService;

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(END_POINT).build();
        mService = restAdapter.create(TileService.class);

        instance=this;
    }

    public static TileService getService() {
        return App.getInstance().mService;
    }

    public static App getInstance(){
        return instance;
    }
}
