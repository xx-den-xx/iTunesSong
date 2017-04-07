package ru.bda.itunessong.model;

import ru.bda.itunessong.model.api.ApiInterface;
import ru.bda.itunessong.model.api.ApiModule;
import ru.bda.itunessong.model.data.SongsData;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModelImpl implements Model {

    ApiInterface apiInterface = ApiModule.getApiInterface();

    @Override
    public Observable<SongsData> getSongList(String name) {
        return apiInterface.getSearchSongs(name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
