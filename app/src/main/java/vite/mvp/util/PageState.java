package vite.mvp.util;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import vite.mvp.R;

/**
 * Created by trs on 17-6-2.
 */

public final class PageState implements PageStateIface {

    private final PageStateIface mPSImpl;

    private PageState(PageStateIface impl) {
        mPSImpl = impl;
    }

    @Override
    public View getView() {
        return mPSImpl.getView();
    }

    @Override
    public void showContent() {
        mPSImpl.showContent();
    }

    @Override
    public void showError() {
        mPSImpl.showError();
    }

    @Override
    public void showNetError() {
        mPSImpl.showNetError();
    }

    @Override
    public void showEmpty() {
        mPSImpl.showEmpty();
    }

    @Override
    public void showLoading() {
        mPSImpl.showLoading();
    }

    public static class DefaultBuilder {
        private final WeakReference<Context> mContextRef;

        @LayoutRes
        private int mContentId;
        private ViewStub vs_error, vs_netError, vs_empty, vs_loading;

        @IdRes
        private int mErrorRetryId, mNetErrorRetryId, mEmptyRetryId;
        private View.OnClickListener mErrorRetryListener, mNetErrorRetryListener, mEmptyRetryListener;

        public DefaultBuilder(Context context) {
            mContextRef = new WeakReference<Context>(context);
        }

        public DefaultBuilder setContent(@LayoutRes int id) {
            mContentId = id;
            return this;
        }

        public DefaultBuilder setError(@LayoutRes int id) {
            if (mContextRef.get() == null)
                return this;

            vs_error = new ViewStub(mContextRef.get());
            vs_error.setLayoutResource(id);
            return this;
        }

        public DefaultBuilder setNetError(@LayoutRes int id) {
            if (mContextRef.get() == null)
                return this;

            vs_netError = new ViewStub(mContextRef.get());
            vs_netError.setLayoutResource(id);
            return this;
        }

        public DefaultBuilder setEmpty(@LayoutRes int id) {
            if (mContextRef.get() == null)
                return this;

            vs_empty = new ViewStub(mContextRef.get());
            vs_empty.setLayoutResource(id);
            return this;
        }

        public DefaultBuilder setLoading(@LayoutRes int id) {
            if (mContextRef.get() == null)
                return this;

            vs_loading = new ViewStub(mContextRef.get());
            vs_loading.setLayoutResource(id);
            return this;
        }

        public DefaultBuilder setErrorRetry(@IdRes int id, View.OnClickListener listener) {
            mErrorRetryId = id;
            mEmptyRetryListener = listener;
            return this;
        }

        public DefaultBuilder setNetErrorRetry(@IdRes int id, View.OnClickListener listener) {
            mNetErrorRetryId = id;
            mNetErrorRetryListener = listener;
            return this;
        }

        public DefaultBuilder setEmptyRetry(@IdRes int id, View.OnClickListener listener) {
            mEmptyRetryId = id;
            mEmptyRetryListener = listener;
            return this;
        }

        public PageStateIface create() {
            if (mContextRef.get() == null)
                return null;

            final Context context = mContextRef.get();

            final PageStateIface impl = new PageStateIface() {

                FrameLayout fl_root = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.layout_basestatu, null);

                ViewStub vs_error = DefaultBuilder.this.vs_error,
                        vs_netError = DefaultBuilder.this.vs_netError,
                        vs_empty = DefaultBuilder.this.vs_empty,
                        vs_loading = DefaultBuilder.this.vs_loading;

                View v_content = LayoutInflater.from(context).inflate(DefaultBuilder.this.mContentId, null),
                        v_error, v_netError, v_empty, v_loading;

                @IdRes
                private int mErrorRetryId = DefaultBuilder.this.mErrorRetryId,
                        mNetErrorRetryId = DefaultBuilder.this.mNetErrorRetryId,
                        mEmptyRetryId = DefaultBuilder.this.mEmptyRetryId;

                private View.OnClickListener mErrorRetryListener = DefaultBuilder.this.mErrorRetryListener,
                        mNetErrorRetryListener = DefaultBuilder.this.mNetErrorRetryListener,
                        mEmptyRetryListener = DefaultBuilder.this.mEmptyRetryListener;

                private void setInvisible(View view) {
                    if (view != null)
                        view.setVisibility(View.INVISIBLE);
                }

                @Override
                public View getView() {
                    return fl_root;
                }

                @Override
                public void showContent() {
                    v_content.setVisibility(View.VISIBLE);
                    setInvisible(v_error);
                    setInvisible(v_netError);
                    setInvisible(v_empty);
                    setInvisible(v_loading);
                }

                @Override
                public void showError() {
                    if (v_error == null) {
                        v_error = vs_error.inflate();
                        v_error.findViewById(mErrorRetryId).setOnClickListener(mErrorRetryListener);
                    } else
                        v_error.setVisibility(View.VISIBLE);
                    setInvisible(v_content);
                    setInvisible(v_netError);
                    setInvisible(v_empty);
                    setInvisible(v_loading);
                }

                @Override
                public void showNetError() {
                    if (v_netError == null) {
                        v_netError = vs_netError.inflate();
                        v_netError.findViewById(mNetErrorRetryId).setOnClickListener(mNetErrorRetryListener);
                    } else
                        v_netError.setVisibility(View.VISIBLE);
                    setInvisible(v_content);
                    setInvisible(v_error);
                    setInvisible(v_empty);
                    setInvisible(v_loading);
                }

                @Override
                public void showEmpty() {
                    if (v_empty == null) {
                        v_empty = vs_empty.inflate();
                        v_empty.findViewById(mEmptyRetryId).setOnClickListener(mEmptyRetryListener);
                    } else
                        v_empty.setVisibility(View.VISIBLE);
                    setInvisible(v_content);
                    setInvisible(v_error);
                    setInvisible(v_netError);
                    setInvisible(v_loading);
                }

                @Override
                public void showLoading() {
                    if (v_loading == null) {
                        v_loading = vs_loading.inflate();
                    } else
                        v_loading.setVisibility(View.VISIBLE);
                    setInvisible(v_content);
                    setInvisible(v_error);
                    setInvisible(v_netError);
                    setInvisible(v_empty);
                }
            };

            return new PageState(impl);
        }
    }

}

interface PageStateIface {
    View getView();

    void showContent();

    void showError();

    void showNetError();

    void showEmpty();

    void showLoading();
}