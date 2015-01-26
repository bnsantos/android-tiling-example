package com.bnsantos.tilingexample;

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

    @GET("/files/{id}")
    public Observable<Response> retrieveFile(@Path("id") String id);

    @GET("/files/{id}/{page}")
    public Observable<Response> retrieveTile(@Path("id") String id,
                                             @Path("page") String page,
                                             @Query("zoom") int zoom,
                                             @Query("row") int row,
                                             @Query("col") int col);

    @GET("/files/{id}/{page}/info")
    public Observable<PictureInfo> retrievePictureInfo(@Path("id") String id,
                                                       @Path("page") String page);
}
