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

    @Override
    public void subscribe() {
        LogUtil.e("MainPresenter", "subscribe");
        mView.clickScreen()
                .flatMap(new Function<String, Observable<UserInfo>>() {
                    @Override
                    public Observable<UserInfo> apply(@NonNull String s) throws Exception {
                        return mModel.getUserInfo(s)
                                .doOnDispose(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        LogUtil.e("MainPresenter", "Unsubscribing getUserInfo");
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .compose(mView.<UserInfo>bindUntilEvent(ActivityEvent.PAUSE));
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("MainPresenter", "Unsubscribing clickScreen from DESTORY");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.<UserInfo>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(mView.showUserInfo());
    }

    @Override
    public void unsubscribe() {
        LogUtil.e("MainPresenter", "unsubscribe");
    }
}
