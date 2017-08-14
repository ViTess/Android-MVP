package vite.mvp.ui.main;

import io.reactivex.Observable;
import io.reactivex.Observer;
import vite.data.entity.UserInfo;
import vite.mvp.base.BaseModel;
import vite.mvp.base.BasePresenter;
import vite.mvp.base.BaseView;
import vite.mvp.util.PageStateHelper;

/**
 * Created by trs on 16-10-18.
 */

public interface MainContract {
    interface Model extends BaseModel {
        Observable<UserInfo> getUserInfo(String userName);
    }

    interface View extends BaseView, PageStateHelper.PageState {
        Observer<UserInfo> showUserInfo();
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getUserInfo(String userName);
    }
}
