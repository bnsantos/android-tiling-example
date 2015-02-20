package com.bnsantos.tilingexample.model;

import java.util.Date;

/**
 * Created by bruno on 20/02/15.
 */
public class PdfInfo {
    private String _id;
    private String filename;
    private Date createdAt;
    private Date completedAt;
    private int pages;
    private double[] zoom;

    public String getId() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public double[] getZoom() {
        return zoom;
    }

    public void setZoom(double[] zoom) {
        this.zoom = zoom;
    }
}
