package com.bnsantos.tilingexample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.bnsantos.tilingexample.App;
import com.bnsantos.tilingexample.model.FilePageCount;
import com.bnsantos.tilingexample.R;
import com.bnsantos.tilingexample.activity.PagePickerListener;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by bruno on 26/01/15.
 */
public class PagePickerDialog extends DialogFragment {
    private static final String TAG = PagePickerDialog.class.getName();
    private String mFileId;
    private int mPages;
    private View mView;
    private NumberPicker mPicker;
    private LinearLayout mContent;
    private LinearLayout mLoading;
    private PagePickerListener mListener;

    public static PagePickerDialog newInstance(String fileId, int pages) {
        PagePickerDialog dialogFragment = new PagePickerDialog();
        dialogFragment.mFileId = fileId;
        dialogFragment.mPages = pages;
        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialog_picker_page, container, false);
        getDialog().setTitle(R.string.choose_page);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPicker = (NumberPicker) mView.findViewById(R.id.numberPicker);
        mPicker.setMinValue(1);
        mContent = (LinearLayout) mView.findViewById(R.id.contentLayout);
        mLoading = (LinearLayout) mView.findViewById(R.id.layoutProgress);

        mView.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageSelected();
            }
        });

        mView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mPicker.setMaxValue(mPages);
        mLoading.setVisibility(View.GONE);
        mContent.setVisibility(View.VISIBLE);
    }

    private void pageSelected(){
        if(mListener!=null){
            mListener.setPage(mPicker.getValue());
        }
        dismiss();
    }

    public void setListener(PagePickerListener listener){
        mListener = listener;
    }
}
