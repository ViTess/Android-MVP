package vite.api;

import android.content.Context;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import vite.common.LogUtil;
import vite.common.NetworkUtil;

/**
 * 提供各种拦截器
 * Created by trs on 16-9-20.
 */
final class Interceptors {

    /**
     * 打印用
     */
    public static final class LoggerInterceptor implements Interceptor {
        private String tag;

        public LoggerInterceptor(String tag) {
            this.tag = tag;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            LogUtil.i(tag, String.format("Sending request %s on %s%n%s\n%s",
                    request.url(), chain.connection(), request.headers(), request.body()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            LogUtil.i(tag, String.format("Received response for %s in %.1fms%n%s\n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers(), request.body()));
            LogUtil.i(tag, "currentThread:" + Thread.currentThread());
            return response;
        }
    }

    /**
     * 缓存拦截器，自动修改请求头和响应头<br>
     * 需要多做处理，部分接口不需要缓存
     */
    public static final class CacheInterceptor implements Interceptor {
        private static final int MAX_AGE = 60;//一分钟
        private static final int MAX_SRALE = 31536000;//一年

        private Context context;

        public CacheInterceptor(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isNetworkConnecting(context)) {
                //没有网络时强制从缓存里读取数据
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            if (NetworkUtil.isNetworkConnecting(context)) {
                //max-age单位为s
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + MAX_AGE)
                        .build();
            } else {
                //max-stale单位为s
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + MAX_SRALE)
                        .build();
            }
            return response;
        }
    }

    /**
     * 加密拦截器，有的api可能需要在头部加上部分加密
     */
    public static final class EncryptInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            return null;
        }
    }

    /**
     * 存储Cookie
     */
    public static final class SaveCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();
                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }
                //TODO:将cookie持久化保存
            }
            return originalResponse;
        }
    }

    /**
     * 读取cookie
     */
    public static final class ReadCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences =/* TODO:从保存的cookie中获取 */ null;
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                LogUtil.v("OkHttp", "Adding Header: " + cookie);
            }
            return chain.proceed(builder.build());
        }
    }

    /**
     * 检查网络，无网络抛出
     */
    public static final class NetworkCheckInterceptor implements Interceptor {

        private Context context;

        public NetworkCheckInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            if (!NetworkUtil.isNetworkConnecting(context)) {
                throw new NoNetworkException();
            }
            return chain.proceed(chain.request());
        }
    }
}
