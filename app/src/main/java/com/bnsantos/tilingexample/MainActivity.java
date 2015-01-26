package com.bnsantos.tilingexample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {
    private EditText mPdfId;
    private EditText mPage;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListeners();
    }

    private void initView(){
        mPdfId = (EditText) findViewById(R.id.pdfId);
        mPage = (EditText) findViewById(R.id.page);
        mButton = (Button) findViewById(R.id.startBtn);
    }

    private void initListeners(){
        mPage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validate();
            }
        });
        mPdfId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validate();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });
    }

    private void validate(){
        mButton.setEnabled(mPage.getText().length()>0&&mPdfId.getText().length()>0);
    }

    private void connect(){
        TileActivity.start(this, mPdfId.getText().toString(), Integer.parseInt(mPage.getText().toString()));
    }
}
