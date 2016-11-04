package vite.mvp.util;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by trs on 16-10-18.
 */

public class RxUtil {
    /**
     * subscribe在io线程，observe在主线程
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> io2main() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
