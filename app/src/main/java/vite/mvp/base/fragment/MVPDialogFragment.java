package vite.mvp.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import vite.common.TUtil;
import vite.mvp.base.BasePresenter;

/**
 * Created by trs on 17-5-27.
 */

public abstract class MVPDialogFragment<P extends BasePresenter> extends BaseDialogFragment {
    public P mPresenter;

    private Unbinder mButterKnifeUnBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final int layoutId = getLayoutId();

        Log.i("MVPDialogFragment", "onCreateView");

        View view = getLayoutView();
        if (view == null)
            view = inflater.inflate(layoutId, container, false);
        mButterKnifeUnBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = TUtil.getT(this, 0);
        mPresenter.setView(this);

        init(savedInstanceState);
        mPresenter.subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
            mPresenter.onDestory();
        }
        mButterKnifeUnBinder.unbind();
    }

    /**
     * 设置layout id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 设置layout view
     *
     * @return
     */
    protected View getLayoutView() {
        return null;
    }

    /**
     * 代替onCreate
     */
    public abstract void init(@Nullable Bundle savedInstanceState);
}
