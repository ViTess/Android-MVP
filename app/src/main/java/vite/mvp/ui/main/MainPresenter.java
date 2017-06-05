package vite.mvp.ui.main;

import org.greenrobot.greendao.rx.RxDao;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import vite.common.LogUtil;
import vite.data.DbManager;
import vite.data.entity.UserAccount;
import vite.data.entity.UserInfo;

/**
 * Created by trs on 16-10-18.
 */
public class MainPresenter extends MainContract.Presenter {

    @Override
    public void subscribe() {
        LogUtil.e("MainPresenter", "subscribe");
    }

    @Override
    public void unsubscribe() {
        LogUtil.e("MainPresenter", "unsubscribe");
    }

    @Override
    void getUserInfo(String userName) {
        mModel.getUserInfo(userName)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        mView.showLoading();
                    }
                })
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(@NonNull UserInfo userInfo) throws Exception {
                        //onNext
                        mView.showLoadUserInfoSuccess(userInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        //onError
                        mView.showLoadUserInfoFailure();
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                    }
                });
    }
}
