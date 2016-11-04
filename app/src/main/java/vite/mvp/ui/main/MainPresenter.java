package vite.mvp.ui.main;

import android.util.Log;

import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import vite.mvp.bean.UserInfo;

/**
 * Created by trs on 16-10-18.
 */
public class MainPresenter extends MainContract.Presenter {

    @Override
    public void subscribe() {
        Log.e("MainPresenter", "subscribe");
    }

    @Override
    public void unsubscribe() {
        Log.e("MainPresenter", "unsubscribe");
    }

    @Override
    void getUserInfo(String userName) {
        mModel.getUserInfo(userName)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading(true);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading(false);
                        mView.showResultState(true);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showLoading(false);
                        mView.showResultState(false);
                    }
                })
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo info) {
                        mView.showUserInfo(info);
                    }
                });
    }
}
