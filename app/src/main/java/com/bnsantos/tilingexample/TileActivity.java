package com.bnsantos.tilingexample;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TileActivity extends ActionBarActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile);

        extractData(getIntent());
    }

    private void extractData(Intent intent){
        if(intent!=null){
            if(!intent.hasExtra(INTENT_EXTRA_PAGE)||!intent.hasExtra(INTENT_EXTRA_PDF)){
                finish();//TODO
            }
            mPage = intent.getIntExtra(INTENT_EXTRA_PAGE, 0);
            mPdf = intent.getStringExtra(INTENT_EXTRA_PDF);
        }
    }
}
