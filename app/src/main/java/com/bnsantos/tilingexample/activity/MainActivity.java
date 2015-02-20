package com.bnsantos.tilingexample.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bnsantos.tilingexample.App;
import com.bnsantos.tilingexample.adapter.PDFInfoAdapter;
import com.bnsantos.tilingexample.fragment.PagePickerDialog;
import com.bnsantos.tilingexample.R;
import com.bnsantos.tilingexample.model.PdfInfo;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends ActionBarActivity implements PagePickerListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView mFiles;
    private PDFInfoAdapter mAdapter;

    private PdfInfo mInfo;
    private String mFileName;
    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initView();
        initAdapter();
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
    }

    private void initView(){
        mFiles = (ListView) findViewById(R.id.filesListView);
        mFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mInfo = mAdapter.getItem(position);
                mFileName = mInfo.getFilename().replace(".pdf", "");
                PagePickerDialog pagePickerDialog = PagePickerDialog.newInstance(mFileName, mInfo.getPages());
                pagePickerDialog.setListener(MainActivity.this);
                pagePickerDialog.show(getSupportFragmentManager(), "PAGE_PICKER");
            }
        });
    }

    private void initAdapter(){
        App.getService().retrieveFiles()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<PdfInfo>>() {
                    @Override
                    public void call(List<PdfInfo> files) {
                        mAdapter = new PDFInfoAdapter(MainActivity.this, R.layout.adapter_pdf_info, files);
                        mFiles.setAdapter(mAdapter);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "Error retrieving files", throwable);
                        Toast.makeText(MainActivity.this, R.string.error_files, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startTileActivity(){
        TileActivity.start(this, mInfo.getId(), mFileName, mPage);
    }

    @Override
    public void setPage(int page) {
        mPage = page;
        startTileActivity();
    }
}
