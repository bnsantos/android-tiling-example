package com.bnsantos.tilingexample;

import android.content.Context;
import android.view.DragEvent;

import com.qozix.tileview.TileView;

/**
 * Created by bruno on 10/02/15.
 */
public class MyTileView extends TileView {

    public MyTileView(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchDragEvent(DragEvent ev){
        boolean r = super.dispatchDragEvent(ev);
        if (r && (ev.getAction() == DragEvent.ACTION_DRAG_STARTED
                || ev.getAction() == DragEvent.ACTION_DRAG_ENDED)){
            // If we got a start or end and the return value is true, our
            // onDragEvent wasn't called by ViewGroup.dispatchDragEvent
            // So we do it here.
            onDragEvent(ev);
        }
        return r;
    }
}
