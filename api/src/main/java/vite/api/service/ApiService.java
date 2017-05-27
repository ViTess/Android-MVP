package vite.api.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vite.data.entity.UserInfo;

/**
 * Created by trs on 16-10-18.
 */

public interface ApiService{
    String BASE_URL = "https://api.github.com/";

    @GET("/users/{username}")
    Observable<UserInfo> getUserInfo(@Path("username") String userName);
}
