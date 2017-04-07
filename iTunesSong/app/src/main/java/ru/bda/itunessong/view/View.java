package ru.bda.itunessong.view;

import ru.bda.itunessong.model.data.SongsData;

public interface View {

    void showData(SongsData songsData);

    void showError(String error);

    void showEmptyList();

    String getSongName();
}
