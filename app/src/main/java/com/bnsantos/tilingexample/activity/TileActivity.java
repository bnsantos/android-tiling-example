package com.bnsantos.tilingexample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bnsantos.tilingexample.App;
import com.bnsantos.tilingexample.R;
import com.bnsantos.tilingexample.fragment.TileFragment;
import com.bnsantos.tilingexample.model.PictureInfo;

import java.lang.ref.WeakReference;

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
    private Button mAddPin;
    private Button mRemovePin;
    private ProgressBar mProgressBar;
    private WeakReference<TileFragment> mTileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile);
        setTheme(R.style.FullscreenTheme);

        extractData(getIntent());
        retrievePictureInfo();

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAddPin = (Button) findViewById(R.id.addPinButton);
        mAddPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin();
            }
        });
        mRemovePin = (Button) findViewById(R.id.removePinButton);
        mRemovePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePin();
            }
        });
    }

    private void extractData(Intent intent){
        if(intent!=null){
            if(!intent.hasExtra(INTENT_EXTRA_PAGE)||!intent.hasExtra(INTENT_EXTRA_PDF)){
                Toast.makeText(this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                finish();
            }
            mPage = intent.getIntExtra(INTENT_EXTRA_PAGE, 0);
            mPdf = intent.getStringExtra(INTENT_EXTRA_PDF);
        }
    }

    private void retrievePictureInfo(){
        App.getService().retrievePictureInfo(mPdf, mPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PictureInfo>() {
                    @Override
                    public void call(PictureInfo pictureInfo) {
                        initFragment(pictureInfo.width, pictureInfo.height);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "Error retrieving picture info");
                        Toast.makeText(TileActivity.this, R.string.error_picture, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initFragment(int width, int height){
        mTileFragment = new WeakReference<>(TileFragment.newInstance(mPdf, mPage, width, height));
        getFragmentManager()
                .beginTransaction()
                .add(R.id.tileFrame, mTileFragment.get(), "TILE_FRAGMENT")
                .commit();
        mProgressBar.setVisibility(View.GONE);
        mAddPin.setVisibility(View.VISIBLE);
        mRemovePin.setVisibility(View.VISIBLE);
    }

    private void addPin(){
        mTileFragment.get().addPin();
    }

    private void removePin(){
        mTileFragment.get().removePin();
    }
}
