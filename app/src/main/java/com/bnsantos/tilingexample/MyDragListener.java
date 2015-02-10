package com.bnsantos.tilingexample;

import android.graphics.Point;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qozix.tileview.TileView;

/**
 * Created by bruno on 10/02/15.
 */
public class MyDragListener implements View.OnDragListener {
    private final String TAG = MyDragListener.class.getSimpleName();

    private final TileView mTileView;
    private int enterShape = R.drawable.shape_droptarget;
    private int normalShape = R.drawable.shape;

    private Point mStart;
    private Point mFinal;

    public MyDragListener(TileView mTileView) {
        this.mTileView = mTileView;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                mStart = new Point((int)event.getX(), (int)event.getY());
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
                //TODO count size of borders
                Log.i(TAG, "ACTION_DROP");
                // Dropped, reassign View to ViewGroup
                View view = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) view.getParent();
                owner.removeView(view);
                TileView container = (TileView) v;
                mFinal = new Point((int)event.getX(), (int)event.getY());
                double scale = container.getScale();

                double dx = (mFinal.x-mStart.x)/scale;
                double dy = (mFinal.y-mStart.y)/scale;

                String coordinates = (String) view.getTag();
                String[] split = coordinates.split(":");

                double x = Double.parseDouble(split[0])+dx;
                if(x<0){
                    x=0;
                }
                if(x>5184){//TODO width of picture
                    x=5184;
                }
                double y = Double.parseDouble(split[1])+dy;
                if(y<0){
                    y=0;
                }
                if(y>3456){//TODO height of picture
                    y=3456;
                }

                mTileView.addMarker(view, x, y);
                view.setTag(x+":"+y);

                //container.addView(view);
                //Add pin to tileView
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


}
