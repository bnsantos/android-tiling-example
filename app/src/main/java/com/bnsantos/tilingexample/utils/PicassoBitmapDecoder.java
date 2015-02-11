package com.bnsantos.tilingexample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bnsantos.tilingexample.App;
import com.bnsantos.tilingexample.R;
import com.qozix.tileview.graphics.BitmapDecoder;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by bruno on 29/01/15.
 */
public class PicassoBitmapDecoder implements BitmapDecoder {
    private static final String TAG = PicassoBitmapDecoder.class.getSimpleName();
    private final String mPdf;
    private final int mPage;

    public PicassoBitmapDecoder(String pdf, int page) {
        this.mPdf = pdf;
        this.mPage = page;
    }

    private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();
    static {
        OPTIONS.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    @Override
    public Bitmap decode(String s, Context context) {
        String[] split = s.split(":");
        try {
            return Picasso
                    .with(context)
                    .load(url(split[0], split[2], split[1]))
                    .error(R.drawable.error)
                    .get();
        } catch (IOException e) {
            Log.e(TAG, "Error loading image["+url(split[0], split[2], split[1])+"]", e);
            return null;
        }
    }

    private String url(String zoom, String row, String col){
        return App.END_POINT + "/files/" + mPdf + "/" + mPage + "?zoom="+zoom+"&col="+col+"&row="+row;
    }
}
