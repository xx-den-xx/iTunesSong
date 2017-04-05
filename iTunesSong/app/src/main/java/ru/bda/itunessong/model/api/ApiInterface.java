package ru.bda.itunessong.model.api;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.bda.itunessong.model.data.SongsData;
import rx.Observable;

public interface ApiInterface {

    @GET("search?")
    Observable<SongsData> getSearchSongs(@Query("term") String term);
}
