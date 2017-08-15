package vite.mvp.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by trs on 16-10-18.
 */
public abstract class BasePresenter<V> {
    public V mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public void setView(V view) {
        mView = view;
    }

    public void onDestory() {
        removeAllDispose();
        mView = null;
    }

    /**
     * Disposable，最后退出时会自动清除Disposable
     *
     * @param d
     */
    public void addDispose(Disposable d) {
        if (mCompositeDisposable != null)
            mCompositeDisposable.add(d);
    }

    /**
     * 将Disposable从记录中去除，这样退出activity时不会自动dispose
     *
     * @param d
     */
    public void removeDispose(Disposable d) {
        if (mCompositeDisposable != null)
            mCompositeDisposable.remove(d);
    }

    public void removeAllDispose() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed())
            mCompositeDisposable.dispose();
    }

    public abstract void subscribe();

    public abstract void unsubscribe();
}
