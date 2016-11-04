package vite.mvp.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by trs on 16-10-18.
 */
public abstract class BasePresenter<M, T> {
    public M mModel;
    public T mView;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public void setModelAndView(M model, T view) {
        mModel = model;
        mView = view;
    }

    public void onDestory() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed())
            mCompositeSubscription.unsubscribe();
    }

    /**
     * 将Subscription加入记录，最后退出时会自动清除Subscription
     *
     * @param s
     */
    public void addSubscription(Subscription s) {
        if (mCompositeSubscription != null)
            mCompositeSubscription.add(s);
    }

    /**
     * 将Subscription从记录中去除，这样退出activity时不会自动unsubscribe
     *
     * @param s
     */
    public void removeSubscription(Subscription s) {
        if (mCompositeSubscription != null)
            mCompositeSubscription.remove(s);
    }

    public abstract void subscribe();

    public abstract void unsubscribe();
}
