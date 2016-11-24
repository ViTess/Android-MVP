package vite.mvp.ui.main;

import rx.Observable;
import vite.mvp.api.Api;
import vite.mvp.api.service.ApiService;
import vite.mvp.bean.UserInfo;
import vite.mvp.util.RxUtil;

/**
 * Created by trs on 16-10-18.
 */

public class MainModel implements MainContract.Model {

    @Override
    public Observable<UserInfo> getUserInfo(String userName) {
        return Api.getService(ApiService.class)
                .getUserInfo(userName)
                .compose(RxUtil.<UserInfo>io2main());
    }
}
