package ru.bda.itunessong.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.bda.itunessong.R;
import ru.bda.itunessong.model.data.Result;
import ru.bda.itunessong.model.data.SongsData;
import ru.bda.itunessong.presenter.Presenter;
import ru.bda.itunessong.presenter.SongListPresenter;
import ru.bda.itunessong.view.adapters.OnElementClickListener;
import ru.bda.itunessong.view.adapters.RecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements View{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.search_text)
    EditText searchText;

    @Bind(R.id.search_button)
    ImageButton searchButton;

    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private int typeView;
    private int orientation;

    private Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.inflateMenu(R.menu.menu_activity_main);
        adapter = new RecyclerViewAdapter(this);
        orientation = isLandscapeMode(this) ? RecyclerViewAdapter.HORIZONTAL_ORIENTATION
                : RecyclerViewAdapter.VERTICAL_ORIENTATION;
        typeView = RecyclerViewAdapter.TABLE_TYPE;
        adapter.setLayoutType(orientation, typeView);
        adapter.setOnElementClickListener(new OnElementClickListener() {
            @Override
            public void onElementClick(Result song, ImageView view) {
                startActivity(song, view);
            }
        });
        layoutManager = new StaggeredGridLayoutManager(isLandscapeMode(MainActivity.this) ? 3 : 2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        presenter = new SongListPresenter(this);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_change_list) {
                    orientation = isLandscapeMode(MainActivity.this) ? RecyclerViewAdapter.HORIZONTAL_ORIENTATION
                            : RecyclerViewAdapter.VERTICAL_ORIENTATION;
                    if (typeView == RecyclerViewAdapter.TABLE_TYPE) {
                        typeView = RecyclerViewAdapter.LIST_TYPE;
                        layoutManager = new LinearLayoutManager(MainActivity.this);
                    } else if (typeView == RecyclerViewAdapter.LIST_TYPE){
                        typeView = RecyclerViewAdapter.TABLE_TYPE;
                        layoutManager = new StaggeredGridLayoutManager(isLandscapeMode(MainActivity.this) ? 3 : 2, StaggeredGridLayoutManager.VERTICAL);
                    }
                    recyclerView.setLayoutManager(layoutManager);
                    adapter.setLayoutType(orientation, typeView);
                }
                return false;
            }
        });

        searchButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                progressBar.setVisibility(android.view.View.VISIBLE);
                presenter.onSearchButtonClick();
                InputMethodManager inputManager =
                        (InputMethodManager) MainActivity.this.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        MainActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (typeView == RecyclerViewAdapter.TABLE_TYPE) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                orientation = RecyclerViewAdapter.HORIZONTAL_ORIENTATION;
                layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                orientation = RecyclerViewAdapter.VERTICAL_ORIENTATION;
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            }
            recyclerView.setLayoutManager(layoutManager);
            adapter.setLayoutType(orientation, typeView);
        }
    }


    private void startActivity(Result song, ImageView sharedView) {
        Intent intent = new Intent(MainActivity.this, SongActivity.class);
        intent.putExtra("song", song);
        intent.putExtra("trans_name", ViewCompat.getTransitionName(sharedView));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MainActivity.this,
                sharedView,
                ViewCompat.getTransitionName(sharedView));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private boolean isLandscapeMode(Activity activity)
    {
        int width =
                activity.getWindowManager().getDefaultDisplay().getWidth();
        int height =
                activity.getWindowManager().getDefaultDisplay().getHeight();

        boolean isLandscape = width > height;
        return isLandscape;
    }

    @Override
    public void showData(SongsData songsData) {
        stopProgressBar();
        adapter.setSongsData(songsData);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    private void makeToast(String text) {
        Snackbar.make(toolbar, text, Snackbar.LENGTH_LONG).show();
    }

    private void stopProgressBar() {
        progressBar.setVisibility(android.view.View.GONE);
    }

    @Override
    public void showError(String error) {
        stopProgressBar();
        makeToast(error);
    }

    @Override
    public void showEmptyList() {
        stopProgressBar();
        makeToast(getString(R.string.empty_songsdata));
    }

    @Override
    public String getSongName() {
        return searchText.getText().toString();
    }
}
