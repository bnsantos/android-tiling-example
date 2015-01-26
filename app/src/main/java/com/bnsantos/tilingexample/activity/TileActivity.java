package com.bnsantos.tilingexample.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.bnsantos.tilingexample.App;
import com.bnsantos.tilingexample.model.PictureInfo;
import com.qozix.tileview.TileView;
import com.qozix.tileview.graphics.BitmapDecoder;

import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class TileActivity extends ActionBarActivity {
    private static final String TAG = TileActivity.class.getSimpleName();
    private static final String INTENT_EXTRA_PDF = "INTENT_EXTRA_PDF";
    private static final String INTENT_EXTRA_PAGE = "INTENT_EXTRA_PAGE";

    public static void start(Activity activity, String pdf, int page){
        Intent intent = new Intent(activity, TileActivity.class);
        intent.putExtra(INTENT_EXTRA_PDF, pdf);
        intent.putExtra(INTENT_EXTRA_PAGE, page);
        activity.startActivity(intent);
    }

    private String mPdf;
    private int mPage;
    private TileView mTileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTileView = new TileView(this);
        mTileView.setDecoder(new BitmapDecoder() {
            @Override
            public Bitmap decode(String s, Context context) {
                String[] split = s.split(":");
                Response r = App.getService().retrieveTile(mPdf, mPage, Integer.parseInt(split[0]), Integer.parseInt(split[2]), Integer.parseInt(split[1]));
                byte[] bitmap = ((TypedByteArray)r.getBody()).getBytes();
                return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            }
        });
        setContentView(mTileView);
        extractData(getIntent());

        App.getService().retrievePictureInfo(mPdf, mPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PictureInfo>() {
                    @Override
                    public void call(PictureInfo pictureInfo) {
                        mTileView.setSize(pictureInfo.width, pictureInfo.height);
                        mTileView.addDetailLevel(1.000f, "100:%col%:%row%");
                        mTileView.addDetailLevel(0.750f, "75:%col%:%row%");
                        mTileView.addDetailLevel(0.500f, "50:%col%:%row%");

                        //TODO set tiles
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "Error retrieving picture info");
                    }
                });

    }

    private void extractData(Intent intent){
        if(intent!=null){
            if(!intent.hasExtra(INTENT_EXTRA_PAGE)||!intent.hasExtra(INTENT_EXTRA_PDF)){
                finish();//TODO
            }
            mPage = intent.getIntExtra(INTENT_EXTRA_PAGE, 0);
            mPdf = intent.getStringExtra(INTENT_EXTRA_PDF);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mTileView!=null) {
            mTileView.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTileView.destroy();
        mTileView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mTileView!=null){
            mTileView.resume();
        }
    }
}
