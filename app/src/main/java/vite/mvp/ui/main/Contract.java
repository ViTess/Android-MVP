package vite.mvp.ui.main;

import vite.data.entity.UserInfo;
import vite.mvp.base.BasePresenter;
import vite.mvp.base.BaseView;
import vite.mvp.util.PageStateHelper;

/**
 * Created by trs on 17-9-15.
 */

interface Contract {
    interface View extends BaseView, PageStateHelper.PageState {
        void showUserInfo(UserInfo userInfo);

        void showErrorMessage(String message);

        void retry();
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getUserInfo(String userName);
    }
}
