package vite.mvp.api;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by trs on 16-10-18.
 */

public class Api {
    private static final long CAHCE_SIZE = 100 * 1024 * 1024;//100m缓存大小

    private static Api instance;
    private static Context sContext;

    public static Api getInstance() {
        if (instance == null) {
            synchronized (Api.class) {
                if (instance == null) {
                    Api temp = new Api();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    public static void setApplicationContext(Context context) {
        sContext = context.getApplicationContext();
    }

    private Retrofit mRetrofit;
    private Cache mCache;
    private OkHttpClient mOkHttpClient;
    private ApiService mApiService;

    private Api() {
        initClient();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_API_URL)
                .client(mOkHttpClient)
//                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    private void initClient() {
        mCache = new Cache(sContext.getCacheDir(), CAHCE_SIZE);
        mOkHttpClient = new OkHttpClient.Builder()
                .cache(mCache)
                .addInterceptor(new Interceptors.LoggerInterceptor("App"))//应用拦截
                .addInterceptor(new Interceptors.CacheInterceptor(sContext))
                .addNetworkInterceptor(new Interceptors.LoggerInterceptor("Network"))//网络拦截
                .addNetworkInterceptor(new Interceptors.CacheInterceptor(sContext))
                //.authenticator() //验证拦截器，验证失败时可以使用
                //.certificatePinner() //添加证书用的
                //.connectionPool() //根据需求自定义一个连接池
                //.connectionSpecs() //安全配置
                .connectTimeout(10, TimeUnit.SECONDS)//超时时间设置，默认是10秒
                //.dispatcher() //Dispatcher的作用为维护请求的状态，并维护一个线程池，用于执行请求
                //.followRedirects() //是否自动重定向，默认自动
                .readTimeout(10, TimeUnit.SECONDS)//读取时间超时设置，默认10秒
                //.retryOnConnectionFailure()//出现错误重新链接，默认为true
                .writeTimeout(10, TimeUnit.SECONDS)//写入时间超时设置，默认10秒
                .build();
    }

    public void clearCache() {
        if (mCache != null)
            try {
                mCache.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public ApiService getService() {
        return mApiService;
    }

    public <T> T create(Class<T> t) {
        return mRetrofit.create(t);
    }

    /**
     * retrofit 2.0下返回string会异常，需要加入该string转换，一般用作测试，正常开发应返回gson的entity
     */
    public static final class StringConverterFactory extends Converter.Factory {
        public static StringConverterFactory create() {
            return new StringConverterFactory();
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return new ConfigurationServiceConverter();
        }

        final class ConfigurationServiceConverter implements Converter<ResponseBody, String> {

            @Override
            public String convert(ResponseBody value) throws IOException {
                BufferedReader r = new BufferedReader(new InputStreamReader(value.byteStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                return total.toString();
            }
        }
    }
}
