package ru.bda.itunessong.view;

import ru.bda.itunessong.model.data.SongsData;

/**
 * Created by User on 05.04.2017.
 */

public interface View {

    void showData(SongsData songsData);

    void showError(String error);

    void showEmptyList();

    String getSongName();
}
