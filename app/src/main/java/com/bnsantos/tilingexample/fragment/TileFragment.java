package com.bnsantos.tilingexample.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bnsantos.tilingexample.MyBitmapDecoder;
import com.bnsantos.tilingexample.MyDragListener;
import com.bnsantos.tilingexample.MyOnLongClickListener;
import com.bnsantos.tilingexample.MyTileView;
import com.bnsantos.tilingexample.R;
import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerEventListener;

/**
 * Created by bruno on 29/01/15.
 */
public class TileFragment extends Fragment{
    private static final String TAG = TileFragment.class.getSimpleName();
    private MyTileView mTileView;
    private String mPdf;
    private int mPage;
    private int mWidth;
    private int mHeight;
    private boolean mAddPin;
    private boolean mRemovePin;

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
        mTileView = new MyTileView(getActivity());
        mTileView.setSize(mWidth, mHeight);

        mTileView.setDecoder(new MyBitmapDecoder(mPdf, mPage));
        mTileView.addDetailLevel(0.125f, "12.5:%col%:%row%");
        mTileView.addDetailLevel(0.250f, "25:%col%:%row%");
        mTileView.addDetailLevel(0.500f, "50:%col%:%row%");
        mTileView.addDetailLevel(1.000f, "100:%col%:%row%");
        mTileView.setScale(0.125f);

        /*mTileView.setDecoder(new BitmapDecoderHttp());
        String endpoint = App.END_POINT + "/files/";
        mTileView.addDetailLevel(0.125f, endpoint + mPdf + "/" + mPage + "?zoom=12.5&col=%col%&row=%row%");
        mTileView.addDetailLevel(0.250f, endpoint + mPdf + "/" + mPage + "?zoom=25&col=%col%&row=%row%");
        mTileView.addDetailLevel(0.500f, endpoint + mPdf + "/" + mPage + "?zoom=50&col=%col%&row=%row%");
        mTileView.addDetailLevel(1.000f, endpoint + mPdf + "/" + mPage + "?zoom=100&col=%col%&row=%row%");
        mTileView.setCacheEnabled(false);*/

        // center markers along both axes
        mTileView.setMarkerAnchorPoints( -0.5f, -0.5f );

        mTileView.addMarkerEventListener(new MarkerEventListener() {
            @Override
            public void onMarkerTap(View view, int x, int y) {
                Toast.makeText(getActivity(), "You tapped a pin ["+x+","+y+"]", Toast.LENGTH_LONG).show();
                if(mRemovePin){
                    mTileView.removeMarker(view);
                    mRemovePin = false;
                }
            }
        });

        mTileView.addTileViewEventListener(new TileView.TileViewEventListener() {
            @Override
            public void onFingerDown(int i, int i2) {
                Log.i(TAG, "Finger down ["+i+","+i2+"]");
            }

            @Override
            public void onFingerUp(int i, int i2) {
                Log.i(TAG, "Finger up ["+i+","+i2+"]");
            }

            @Override
            public void onDrag(int i, int i2) {
                Log.i(TAG, "Drag ["+i+","+i2+"]");
            }

            @Override
            public void onDoubleTap(int i, int i2) {
                Log.i(TAG, "Double tap ["+i+","+i2+"]");
            }

            @Override
            public void onTap(int w, int h) {
                if(mAddPin){
                    addPin(w / mTileView.getScale(), h / mTileView.getScale());
                }
                Log.i(TAG, "Tap ["+w+","+h+"]");
            }

            @Override
            public void onPinch(int i, int i2) {
                Log.i(TAG, "Pinch ["+i+","+i2+"]");
            }

            @Override
            public void onPinchStart(int i, int i2) {
                Log.i(TAG, "Pinch start ["+i+","+i2+"]");
            }

            @Override
            public void onPinchComplete(int i, int i2) {
                Log.i(TAG, "Pinch complete ["+i+","+i2+"]");
            }

            @Override
            public void onFling(int i, int i2, int i3, int i4) {
                Log.i(TAG, "Fling ["+i+","+i2+"]");
            }

            @Override
            public void onFlingComplete(int i, int i2) {
                Log.i(TAG, "Fling complete ["+i+","+i2+"]");

            }

            @Override
            public void onScaleChanged(double v) {
            }

            @Override
            public void onScrollChanged(int i, int i2) {
                Log.i(TAG, "Scroll changed ["+i+","+i2+"]");

            }

            @Override
            public void onZoomStart(double v) {

            }

            @Override
            public void onZoomComplete(double v) {

            }

            @Override
            public void onDetailLevelChanged() {

            }

            @Override
            public void onRenderStart() {

            }

            @Override
            public void onRenderComplete() {

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

    public void addPin(){
        Toast.makeText(getActivity(), R.string.click_add_pin, Toast.LENGTH_SHORT).show();
        mAddPin = true;
    }

    public void removePin(){
        Toast.makeText(getActivity(), R.string.click_remove_pin, Toast.LENGTH_SHORT).show();
        mRemovePin = true;
    }

    private void addPin( double x, double y ) {
        ImageView imageView = new ImageView(getActivity());
        String key = x+":"+y;
        imageView.setTag(key);
        imageView.setImageResource(R.drawable.push_pin);
        imageView.setOnLongClickListener(new MyOnLongClickListener());
        mTileView.setOnDragListener(new MyDragListener(mTileView));
        mTileView.addMarker(imageView, x, y);
        mAddPin = false;
    }
}
