package com.bnsantos.tilingexample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private static final String INTENT_EXTRA_FILE_ID = "INTENT_EXTRA_ID";
    private static final String INTENT_EXTRA_PDF = "INTENT_EXTRA_PDF";
    private static final String INTENT_EXTRA_PAGE = "INTENT_EXTRA_PAGE";

    public static void start(Activity activity, String id, String pdf, int page){
        Intent intent = new Intent(activity, TileActivity.class);
        intent.putExtra(INTENT_EXTRA_FILE_ID, id);
        intent.putExtra(INTENT_EXTRA_PDF, pdf);
        intent.putExtra(INTENT_EXTRA_PAGE, page);
        activity.startActivity(intent);
    }

    private String mId;
    private String mPdf;
    private int mPage;
    private Button mAddPin;
    private Button mRemovePin;
    private TextView mTtitle;
    private ProgressBar mProgressBar;
    private WeakReference<TileFragment> mTileFragment;
    private int mPictureWidth;
    private int mPictureHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile);
        initToolbar();

        extractData(getIntent());
        retrievePictureInfo();

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void extractData(Intent intent){
        if(intent!=null){
            if(!intent.hasExtra(INTENT_EXTRA_PAGE)||!intent.hasExtra(INTENT_EXTRA_PDF)){
                Toast.makeText(this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                finish();
            }
            mPage = intent.getIntExtra(INTENT_EXTRA_PAGE, 0);
            mPdf = intent.getStringExtra(INTENT_EXTRA_PDF);
            mId = intent.getStringExtra(INTENT_EXTRA_FILE_ID);
        }
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAddPin = (Button) toolbar.findViewById(R.id.addPinButton);
        mAddPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin();
            }
        });
        mRemovePin = (Button) toolbar.findViewById(R.id.removePinButton);
        mRemovePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePin();
            }
        });
        mTtitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
    }

    private void retrievePictureInfo(){
        App.getService().retrievePictureInfo(mId, mPage)
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
        mPictureWidth = width;
        mPictureHeight = height;
        mTileFragment = new WeakReference<>(TileFragment.newInstance(mId, mPage, width, height));
        mTtitle.setText(mPdf + " - " + mPage);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.tileFrame, mTileFragment.get(), "TILE_FRAGMENT")
                .commit();
        mProgressBar.setVisibility(View.GONE);
        findViewById(R.id.tileFrame).setVisibility(View.VISIBLE);
        mAddPin.setVisibility(View.VISIBLE);
        mRemovePin.setVisibility(View.VISIBLE);
    }

    private void addPin(){
        mTileFragment.get().addPin();
    }

    private void removePin(){
        mTileFragment.get().removePin();
    }

    public int getPictureWidth() {
        return mPictureWidth;
    }

    public int getPictureHeight() {
        return mPictureHeight;
    }
}
