package ru.bda.itunessong.view.adapters;

import android.widget.ImageView;

import ru.bda.itunessong.model.data.Result;

/**
 * Created by User on 06.04.2017.
 */

public interface OnElementClickListener {
    void onElementClick(Result song, ImageView view);
}
