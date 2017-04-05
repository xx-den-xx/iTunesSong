package ru.bda.itunessong.model;

import ru.bda.itunessong.model.data.SongsData;
import rx.Observable;

public interface Model {
    Observable<SongsData> getSongList(String name);
}
