package com.bnsantos.tilingexample.service;

import com.bnsantos.tilingexample.model.FilePageCount;
import com.bnsantos.tilingexample.model.PictureInfo;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by bruno on 26/01/15.
 */
public interface TileService {
    @GET("/files")
    public Observable<List<String>> retrieveFiles();

    @GET("/files/{id}/{page}")
    public Response retrieveTile(@Path("id") String id,
                               @Path("page") int page,
                               @Query("zoom") float zoom,
                               @Query("row") int row,
                               @Query("col") int col);

    @GET("/files/{id}/{page}/100/info")
    public Observable<PictureInfo> retrievePictureInfo(@Path("id") String id,
                                                       @Path("page") int page);

    @GET("/files/{id}/pages")
    public Observable<FilePageCount> retrievePdfPages(@Path("id") String id);
}
