package vite.mvp.ui.main;

import rx.Observable;
import vite.mvp.base.BaseModel;
import vite.mvp.base.BasePresenter;
import vite.mvp.base.BaseView;
import vite.mvp.bean.UserInfo;

/**
 * Created by trs on 16-10-18.
 */

public interface MainContract {
    interface Model extends BaseModel {
        Observable<UserInfo> getUserInfo(String userName);
    }

    interface View extends BaseView {
        void showUserInfo(UserInfo info);

        void showLoading(boolean isShow);

        void showResultState(boolean isSuccessful);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getUserInfo(String userName);
    }
}
