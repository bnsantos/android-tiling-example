package com.bnsantos.tilingexample;

import android.content.ClipData;
import android.view.View;

/**
 * Created by bruno on 10/02/15.
 */
public class MyOnLongClickListener implements View.OnLongClickListener {
    @Override
    public boolean onLongClick(View view) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, view, 0);
        view.setVisibility(View.INVISIBLE);
        return true;
    }
}
