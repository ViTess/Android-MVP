package vite.mvp.ui.main;

import android.util.Log;

import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.greendao.rx.RxDao;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
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
    }

    @Override
    public void unsubscribe() {
        LogUtil.e("MainPresenter", "unsubscribe");
    }

    void getUserInfo(String userName) {
        mModel.getUserInfo(userName)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i("MainPresenter", "Unsubscribing subscription from onCreate()");
                    }
                })
                .compose(mView.<UserInfo>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(mView.showUserInfo());
    }
}
