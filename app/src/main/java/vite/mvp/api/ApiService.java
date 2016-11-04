package vite.mvp.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import vite.mvp.bean.UserInfo;

/**
 * Created by trs on 16-10-18.
 */

public interface ApiService {
    String BASE_API_URL = "https://api.github.com/";

    @GET("/users/{username}")
    Observable<UserInfo> getUserInfo(@Path("username") String userName);
}
