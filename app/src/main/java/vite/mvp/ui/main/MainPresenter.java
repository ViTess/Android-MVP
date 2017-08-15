package vite.mvp.ui.main;

import android.util.Log;
import android.view.View;

import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.greendao.rx.RxDao;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import vite.api.NoNetworkException;
import vite.common.LogUtil;
import vite.data.DbManager;
import vite.data.entity.UserAccount;
import vite.data.entity.UserInfo;
import vite.mvp.base.BasePresenter;

/**
 * Created by trs on 16-10-18.
 */
public class MainPresenter extends BasePresenter<MainActivity> {

    private MainModel mModel = new MainModel();

    private Disposable mGetUserInfoDispose;

    @Override
    public void subscribe() {
        LogUtil.e("MainPresenter", "subscribe");
    }

    @Override
    public void unsubscribe() {
        LogUtil.e("MainPresenter", "unsubscribe");
    }

    public void getUserInfo(String userName) {
        if (mGetUserInfoDispose != null && !mGetUserInfoDispose.isDisposed())
            mGetUserInfoDispose.dispose();
        mGetUserInfoDispose = mModel.getUserInfo(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("MainPresenter", "Unsubscribing getUserInfo");
                    }
                })
                .compose(mView.<UserInfo>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo info) throws Exception {
                        mView.showContent();
                        mView.showUserInfo(info);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MainActivity", throwable.toString());
                        if (throwable instanceof NoNetworkException) {
                            mView.showNetError();
                        } else {
                            mView.showContent();
                            mView.showErrorMessage("Oh, something went wrong, please try again");
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("MainPresenter", "onComplete");
                        mView.showContent();
                    }
                });
    }
}
