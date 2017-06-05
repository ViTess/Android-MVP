package vite.mvp.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import vite.mvp.R;

/**
 * 管理页面状态，辅助显示如网络异常、加载中等页面
 * 如果MVP的getLayoutId中对其初始化即可调用
 * <p>
 * Created by trs on 17-6-2.
 */

public final class PageStateHelper {

    public static final PageStateHelper NONE = new PageStateHelper();

    @LayoutRes
    private static final int sBaseLayoutId = R.layout.layout_basestatu;

    private FragmentManager mFragmentManager;
    private String mTag_df_error = "pagestatehelper_error",
            mTag_df_netError = "pagestatehelper_neterror",
            mTag_df_empty = "pagestatehelper_empty",
            mTag_df_loading = "pagestatehelper_loading";

    private FrameLayout fl_root;
    private View v_content, v_error, v_netError, v_empty, v_loading;
    private ViewStub vs_error, vs_netError, vs_empty, vs_loading;

    private Dialog dl_error, dl_netError, dl_empty, dl_loading;
    private DialogFragment df_error, df_netError, df_empty, df_loading;

    @IdRes
    private int mErrorRetryId, mNetErrorRetryId, mEmptyRetryId;
    private View.OnClickListener mErrorRetryListener, mNetErrorRetryListener, mEmptyRetryListener;
    private boolean isSetErrorClick, isSetNetErrorClick, isSetEmptyClick;

    private PageStateHelper() {
    }

    private PageStateHelper(Context context) {
        fl_root = (FrameLayout) LayoutInflater.from(context).inflate(sBaseLayoutId, null);
    }

    private void create() {
        if (v_content != null) {
            fl_root.addView(v_content, 0);

            addView(v_error, vs_error);
            addView(v_netError, vs_netError);
            addView(v_empty, vs_empty);
            addView(v_loading, vs_loading);
        }
    }

    private void addView(View view, ViewStub viewStub) {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
            fl_root.addView(view);
        } else if (viewStub != null)
            fl_root.addView(viewStub);
    }

    private void visible(View view) {
        if (view != null && view.getVisibility() != View.VISIBLE)
            view.setVisibility(View.VISIBLE);
    }

    private void invisible(View... views) {
        for (View view : views) {
            if (view != null)
                view.setVisibility(View.INVISIBLE);
        }
    }

    private void showDialog(Dialog dialog) {
        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }

    private void showDialogFragment(DialogFragment dialogFragment, String tag) {
        if (mFragmentManager != null) {
            mFragmentManager.executePendingTransactions();
            if (dialogFragment != null &&
                    !dialogFragment.isAdded() && !dialogFragment.isVisible() && !dialogFragment.isRemoving())
                dialogFragment.show(mFragmentManager, tag);
        }
    }

    private void dismiss(Dialog... dialogs) {
        for (Dialog d : dialogs) {
            if (d != null && d.isShowing())
                d.dismiss();
        }
    }

    private void dismiss(DialogFragment... dialogFragments) {
        for (DialogFragment df : dialogFragments) {
            if (df != null && df.isAdded() &&
                    df.getDialog() != null && df.getDialog().isShowing())
                df.dismissAllowingStateLoss();
        }
    }

    public View getView() {
        return fl_root;
    }

    public void showContent() {
        visible(v_content);
        invisible(v_error, v_netError, v_empty, v_loading);
        dismiss(dl_error, dl_netError, dl_empty, dl_loading);
        dismiss(df_error, df_netError, df_empty, df_loading);
    }

    public void showError() {
        if (v_error == null && vs_error != null)
            v_error = vs_error.inflate();
        else
            visible(v_error);

        if (v_error != null) {
            invisible(v_content, v_netError, v_empty, v_loading);
            if (!isSetErrorClick && mErrorRetryId != 0 && mErrorRetryListener != null) {
                v_error.findViewById(mErrorRetryId).setOnClickListener(mErrorRetryListener);
                isSetErrorClick = true;
            }
        } else
            invisible(v_netError, v_empty, v_loading);

        dismiss(dl_netError, dl_empty, dl_loading);
        dismiss(df_netError, df_empty, df_loading);
        showDialog(dl_error);
        showDialogFragment(df_error, mTag_df_error);
    }

    public void showNetError() {
        if (v_netError == null && vs_netError != null)
            v_netError = vs_netError.inflate();
        else
            visible(v_netError);

        if (v_netError != null) {
            invisible(v_content, v_error, v_empty, v_loading);
            if (!isSetNetErrorClick && mNetErrorRetryId != 0 && mNetErrorRetryListener != null) {
                v_netError.findViewById(mNetErrorRetryId).setOnClickListener(mNetErrorRetryListener);
                isSetNetErrorClick = true;
            }
        } else
            invisible(v_error, v_empty, v_loading);

        invisible(v_error, v_empty, v_loading);
        dismiss(dl_error, dl_empty, dl_loading);
        dismiss(df_error, df_empty, df_loading);
        showDialog(dl_netError);
        showDialogFragment(df_netError, mTag_df_netError);
    }

    public void showEmpty() {
        if (v_empty == null && vs_empty != null)
            v_empty = vs_empty.inflate();
        else
            visible(v_empty);

        if (v_empty != null) {
            invisible(v_content, v_error, v_netError, v_loading);
            if (!isSetEmptyClick && mEmptyRetryId != 0 && mEmptyRetryListener != null) {
                v_empty.findViewById(mEmptyRetryId).setOnClickListener(mEmptyRetryListener);
                isSetEmptyClick = true;
            }
        } else
            invisible(v_error, v_netError, v_loading);

        dismiss(dl_error, dl_netError, dl_loading);
        dismiss(df_error, df_netError, df_loading);
        showDialog(dl_empty);
        showDialogFragment(df_empty, mTag_df_empty);
    }

    public void showLoading() {
        if (v_loading == null && vs_loading != null)
            v_loading = vs_loading.inflate();
        else
            visible(v_loading);

        if (v_loading != null)
            invisible(v_content, v_error, v_netError, v_empty);
        else
            invisible(v_error, v_netError, v_empty);

        dismiss(dl_error, dl_netError, dl_empty);
        dismiss(df_error, df_netError, df_empty);
        showDialog(dl_loading);
        showDialogFragment(df_loading, mTag_df_loading);
    }

    public void clear() {
        dismiss(dl_error, dl_netError, dl_empty, dl_loading);
        dismiss(df_error, df_netError, df_empty, df_loading);

        mFragmentManager = null;
        mTag_df_error = null;
        mTag_df_netError = null;
        mTag_df_empty = null;
        mTag_df_loading = null;

        fl_root = null;
        v_content = null;
        v_error = null;
        v_netError = null;
        v_empty = null;
        v_loading = null;
        vs_error = null;
        vs_netError = null;
        vs_empty = null;
        vs_loading = null;

        dl_error = null;
        dl_netError = null;
        dl_empty = null;
        dl_loading = null;
        df_error = null;
        df_netError = null;
        df_empty = null;
        df_loading = null;
    }

    public static class Builder {
        private final WeakReference<Context> mContextRef;
        private FragmentManager mFragmentManager;

        private View v_content, v_error, v_netError, v_empty, v_loading;
        private ViewStub vs_error, vs_netError, vs_empty, vs_loading;

        private Dialog dl_error, dl_netError, dl_empty, dl_loading;
        private DialogFragment df_error, df_netError, df_empty, df_loading;

        @IdRes
        private int mErrorRetryId, mNetErrorRetryId, mEmptyRetryId;
        private View.OnClickListener mErrorRetryListener, mNetErrorRetryListener, mEmptyRetryListener;

        public Builder(Context context) {
            mContextRef = new WeakReference<Context>(context);
        }

        public Builder setFragmentManager(FragmentManager fragmentManager) {
            mFragmentManager = fragmentManager;
            return this;
        }

        public Builder setContent(@LayoutRes int id) {
            v_content = LayoutInflater.from(mContextRef.get()).inflate(id, null);
            return this;
        }

        public Builder setContent(View view) {
            v_content = view;
            return this;
        }

        public Builder setError(@LayoutRes int id) {
            if (mContextRef.get() == null)
                return this;

            vs_error = new ViewStub(mContextRef.get());
            vs_error.setLayoutResource(id);
            return this;
        }

        public Builder setError(View view) {
            v_error = view;
            return this;
        }

        public Builder setError(Dialog dialog) {
            dl_error = dialog;
            return this;
        }

        public Builder setError(DialogFragment dialogFragment) {
            df_error = dialogFragment;
            return this;
        }

        public Builder setNetError(@LayoutRes int id) {
            if (mContextRef.get() == null)
                return this;

            vs_netError = new ViewStub(mContextRef.get());
            vs_netError.setLayoutResource(id);
            return this;
        }

        public Builder setNetError(View view) {
            v_netError = view;
            return this;
        }

        public Builder setNetError(Dialog dialog) {
            dl_netError = dialog;
            return this;
        }

        public Builder setNetError(DialogFragment dialogFragment) {
            df_netError = dialogFragment;
            return this;
        }

        public Builder setEmpty(@LayoutRes int id) {
            if (mContextRef.get() == null)
                return this;

            vs_empty = new ViewStub(mContextRef.get());
            vs_empty.setLayoutResource(id);
            return this;
        }

        public Builder setEmpty(View view) {
            v_empty = view;
            return this;
        }

        public Builder setEmpty(Dialog dialog) {
            dl_empty = dialog;
            return this;
        }

        public Builder setEmpty(DialogFragment dialogFragment) {
            df_empty = dialogFragment;
            return this;
        }

        public Builder setLoading(@LayoutRes int id) {
            if (mContextRef.get() == null)
                return this;

            vs_loading = new ViewStub(mContextRef.get());
            vs_loading.setLayoutResource(id);
            return this;
        }

        public Builder setLoading(View view) {
            v_loading = view;
            return this;
        }

        public Builder setLoading(Dialog dialog) {
            dl_loading = dialog;
            return this;
        }

        public Builder setLoading(DialogFragment dialogFragment) {
            df_loading = dialogFragment;
            return this;
        }

        /**
         * 仅支持在{@link Builder#setError}时设置为view或layout id
         *
         * @param id
         * @param listener
         * @return
         */
        public Builder setErrorRetry(@IdRes int id, View.OnClickListener listener) {
            mErrorRetryId = id;
            mErrorRetryListener = listener;
            return this;
        }

        /**
         * 仅支持在{@link Builder#setNetError}时设置为view或layout id
         *
         * @param id
         * @param listener
         * @return
         */
        public Builder setNetErrorRetry(@IdRes int id, View.OnClickListener listener) {
            mNetErrorRetryId = id;
            mNetErrorRetryListener = listener;
            return this;
        }

        /**
         * 仅支持在{@link Builder#setEmpty}时设置为view或layout id
         *
         * @param id
         * @param listener
         * @return
         */
        public Builder setEmptyRetry(@IdRes int id, View.OnClickListener listener) {
            mEmptyRetryId = id;
            mEmptyRetryListener = listener;
            return this;
        }

        public PageStateHelper create() {
            if (mContextRef.get() == null)
                return null;

            final Context context = mContextRef.get();

            final PageStateHelper helper = new PageStateHelper(context);
            helper.mFragmentManager = this.mFragmentManager;
            helper.v_content = this.v_content;
            helper.v_error = this.v_error;
            helper.v_netError = this.v_netError;
            helper.v_empty = this.v_empty;
            helper.v_loading = this.v_loading;

            helper.vs_error = this.vs_error;
            helper.vs_netError = this.vs_netError;
            helper.vs_empty = this.vs_empty;
            helper.vs_loading = this.vs_loading;

            helper.dl_error = this.dl_error;
            helper.dl_netError = this.dl_netError;
            helper.dl_empty = this.dl_empty;
            helper.dl_loading = this.dl_loading;

            helper.df_error = this.df_error;
            helper.df_netError = this.df_netError;
            helper.df_empty = this.df_empty;
            helper.df_loading = this.df_loading;

            helper.mErrorRetryId = this.mErrorRetryId;
            helper.mNetErrorRetryId = this.mNetErrorRetryId;
            helper.mEmptyRetryId = this.mEmptyRetryId;
            helper.mErrorRetryListener = this.mErrorRetryListener;
            helper.mNetErrorRetryListener = this.mNetErrorRetryListener;
            helper.mEmptyRetryListener = this.mEmptyRetryListener;

            helper.create();
            return helper;
        }
    }

    public interface PageState {
        void showContent();

        void showError();

        void showNetError();

        void showEmpty();

        void showLoading();
    }
}
