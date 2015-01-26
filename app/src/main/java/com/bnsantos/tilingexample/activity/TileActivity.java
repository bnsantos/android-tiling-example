package com.bnsantos.tilingexample.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bnsantos.tilingexample.App;
import com.bnsantos.tilingexample.R;
import com.bnsantos.tilingexample.model.PictureInfo;
import com.qozix.tileview.TileView;
import com.qozix.tileview.graphics.BitmapDecoder;
import com.qozix.tileview.markers.MarkerEventListener;

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
        setContentView(R.layout.activity_tile);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.tileActivityLayout);
        initTileView();
        relativeLayout.addView(mTileView);
        extractData(getIntent());
        retrievePictureInfo();

        findViewById(R.id.addPinButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin();
            }
        });
    }

    private void initTileView(){
        mTileView = new TileView(this);
        mTileView.setDecoder(new BitmapDecoder() {
            @Override
            public Bitmap decode(String s, Context context) {
                String[] split = s.split(":");
                Response r = App.getService().retrieveTile(mPdf, mPage, Integer.parseInt(split[0]), Integer.parseInt(split[2]), Integer.parseInt(split[1]));
                byte[] bitmap = ((TypedByteArray) r.getBody()).getBytes();
                return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTileView.setLayoutParams(params);

        mTileView.addMarkerEventListener(new MarkerEventListener() {
            @Override
            public void onMarkerTap(View view, int x, int y) {
                Toast.makeText( getApplicationContext(), "You tapped a pin", Toast.LENGTH_LONG ).show();
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
                        mTileView.setSize(pictureInfo.width, pictureInfo.height);
                        mTileView.addDetailLevel(1.000f, "100:%col%:%row%");
                        mTileView.addDetailLevel(0.750f, "75:%col%:%row%");
                        mTileView.addDetailLevel(0.500f, "50:%col%:%row%");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "Error retrieving picture info");
                        Toast.makeText(TileActivity.this, R.string.error_picture, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addPin(){
        addPin(0, 0);
    }

    private void addPin( double x, double y ) {
        ImageView imageView = new ImageView( this );
        imageView.setImageResource( R.drawable.maps_marker_blue );
        mTileView.addMarker(imageView, x, y);
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
