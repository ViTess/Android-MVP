package vite.mvp.ui.main;

import io.reactivex.Observable;
import vite.api.API;
import vite.api.service.ApiService;
import vite.common.RxUtil;
import vite.data.entity.UserInfo;

/**
 * Created by trs on 16-10-18.
 */

public class MainModel implements MainContract.Model {

    @Override
    public Observable<UserInfo> getUserInfo(String userName) {
        return API.getService(ApiService.class)
                .getUserInfo(userName)
                .compose(RxUtil.<UserInfo>observable_io2main());
    }
}
