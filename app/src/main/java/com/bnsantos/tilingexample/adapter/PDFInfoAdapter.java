package com.bnsantos.tilingexample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bnsantos.tilingexample.R;
import com.bnsantos.tilingexample.model.PdfInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by bruno on 20/02/15.
 */
public class PDFInfoAdapter extends ArrayAdapter<PdfInfo> {
    public PDFInfoAdapter(Context context, int resource, List<PdfInfo> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_pdf_info, parent, false);

            convertView.setTag(new ViewHolder((TextView)convertView.findViewById(R.id.filename), (TextView)convertView.findViewById(R.id.filePages), (TextView) convertView.findViewById(R.id.timeRender)));
        }
        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        final PdfInfo info = getItem(position);

        viewHolder.fileName.setText(info.getFilename());
        viewHolder.pages.setText(Integer.toString(info.getPages()));
        if(info.getCompletedAt()!=null){
            viewHolder.timeRender.setText(Long.toString(TimeUnit.MILLISECONDS.toSeconds(info.getCompletedAt().getTime()-info.getCreatedAt().getTime())));
        }else{
            viewHolder.timeRender.setText(R.string.still_processing);
        }
        return convertView;
    }

    private class ViewHolder {
        public final TextView fileName;
        public final TextView pages;
        public final TextView timeRender;

        private ViewHolder(TextView fileName, TextView pages, TextView timeRender) {
            this.fileName = fileName;
            this.pages = pages;
            this.timeRender = timeRender;
        }
    }
}
