package com.bnsantos.tilingexample.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bnsantos.tilingexample.MyBitmapDecoder;
import com.qozix.tileview.TileView;
import com.qozix.tileview.graphics.BitmapDecoderHttp;
import com.qozix.tileview.markers.MarkerEventListener;

/**
 * Created by bruno on 29/01/15.
 */
public class TileFragment extends Fragment{
    private static final String TAG = TileFragment.class.getSimpleName();
    private TileView mTileView;
    private String mPdf;
    private int mPage;
    private int mWidth;
    private int mHeight;

    public static TileFragment newInstance(String pdf, int page, int width, int height){
        TileFragment tileFragment = new TileFragment();
        tileFragment.mPdf = pdf;
        tileFragment.mPage = page;
        tileFragment.mWidth = width;
        tileFragment.mHeight = height;
        return tileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initTileView();
        return mTileView;
    }

    private void initTileView(){
        mTileView = new TileView(getActivity());
        mTileView.setSize(mWidth, mHeight);

        mTileView.setDecoder(new MyBitmapDecoder(mPdf, mPage));
        mTileView.addDetailLevel(1.000f, "100:%col%:%row%");
        mTileView.addDetailLevel(0.500f, "50:%col%:%row%");
        mTileView.addDetailLevel(0.250f, "25:%col%:%row%");
        mTileView.addDetailLevel(0.125f, "12.5:%col%:%row%");

        /*mTileView.setDecoder(new BitmapDecoderHttp());
        String endpoint = "http://54.85.216.195:3000/files/";
        mTileView.addDetailLevel(0.125f, endpoint + mPdf + "/" + mPage + "?zoom=12.5&col=%col%&row=%row%");
        mTileView.addDetailLevel(0.125f, endpoint + mPdf + "/" + mPage + "?zoom=25&col=%col%&row=%row%");
        mTileView.addDetailLevel(0.125f, endpoint + mPdf + "/" + mPage + "?zoom=50&col=%col%&row=%row%");
        mTileView.addDetailLevel(0.125f, endpoint + mPdf + "/" + mPage + "?zoom=100&col=%col%&row=%row%");
        mTileView.setCacheEnabled(true);
        */

        mTileView.addMarkerEventListener(new MarkerEventListener() {
            @Override
            public void onMarkerTap(View view, int x, int y) {
                Toast.makeText(getActivity(), "You tapped a pin", Toast.LENGTH_LONG).show();
            }
        });

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
