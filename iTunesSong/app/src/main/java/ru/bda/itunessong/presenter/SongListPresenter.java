package ru.bda.itunessong.presenter;

import ru.bda.itunessong.model.Model;
import ru.bda.itunessong.model.ModelImpl;
import ru.bda.itunessong.model.data.SongsData;
import ru.bda.itunessong.view.View;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;


public class SongListPresenter implements Presenter {

    private Model model = new ModelImpl();

    private View view;
    private Subscription subscription = Subscriptions.empty();

    public SongListPresenter(View view) {
        this.view = view;
    }

    @Override
    public void onSearchButtonClick() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        if (view.getSongName().length() >= 5) {
            subscription = model.getSongList(view.getSongName())
                    .subscribe(new Observer<SongsData>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(e.getMessage());
                        }

                        @Override
                        public void onNext(SongsData data) {
                            if (data != null && !data.getResults().isEmpty()) {
                                view.showData(data);
                            } else {
                                view.showEmptyList();
                            }
                        }
                    });
        } else {
            view.showError("Длина строки поиска должна быть не менее 5 символов");
        }
    }

    @Override
    public void onStop() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
