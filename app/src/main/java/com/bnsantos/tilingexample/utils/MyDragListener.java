package com.bnsantos.tilingexample.utils;

import android.graphics.Point;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bnsantos.tilingexample.R;
import com.qozix.tileview.TileView;

/**
 * Created by bruno on 10/02/15.
 */
public class MyDragListener implements View.OnDragListener {
    private final String TAG = MyDragListener.class.getSimpleName();

    private final TileView mTileView;
    private int enterShape = R.drawable.shape_droptarget;
    private int normalShape = R.drawable.shape;

    private double[] mStart;
    private double[] mFinal;
    private final int mWidth;
    private final int mHeight;
    private final int mToolbarH;

    public MyDragListener(TileView mTileView, int width, int height, int toolbarHeight) {
        this.mTileView = mTileView;
        mWidth = width;
        mHeight = height;
        mToolbarH = toolbarHeight;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                mStart = new double[]{event.getX(), event.getY()};
                Log.i(TAG, "ACTION_DRAG_STARTED");
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.i(TAG, "ACTION_DRAG_ENTERED");
                v.setBackgroundResource(enterShape);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.i(TAG, "ACTION_DRAG_EXITED");
                v.setBackgroundResource(normalShape);
                break;
            case DragEvent.ACTION_DROP:
                Log.i(TAG, "ACTION_DROP");
                // Dropped, reassign View to ViewGroup
                View view = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) view.getParent();
                owner.removeView(view);
                TileView container = (TileView) v;

                mFinal = new double[]{event.getX(), event.getY()};
                double[] newPos = coordinatesAfterDragging(container.getScale(), (String) view.getTag());
                mTileView.addMarker(view, newPos[0], newPos[1]);
                view.setTag(newPos[0]+":"+newPos[1]);
                view.setVisibility(View.VISIBLE);

                mFinal=null;
                mStart=null;
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.i(TAG, "ACTION_DRAG_ENDED");
                v.setBackgroundResource(normalShape);
            default:
                break;
        }
        return true;
    }


    public double coordinateInBounds(double value, int max){
        if(value<0){
            return 0;
        }
        if(value>max){
            return max;
        }
        return value;
    }

    public double[] coordinatesAfterDragging(double scale, String coordinates){
        double dx = (mFinal[0]-mStart[0])/scale;
        double dy = (mFinal[1]-mStart[1]+mToolbarH)/scale;

        String[] split = coordinates.split(":");

        double x = Double.parseDouble(split[0])+dx;
        x = coordinateInBounds(x, mWidth);
        double y = Double.parseDouble(split[1])+dy;
        y = coordinateInBounds(y, mHeight);

        return new double[]{x, y};
    }
}
