package vite.mvp.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import vite.common.TUtil;
import vite.mvp.base.BaseModel;
import vite.mvp.base.BasePresenter;
import vite.mvp.base.BaseView;

/**
 * Created by trs on 16-11-4.
 */

public abstract class MVPFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment {
    public T mPresenter;

    private Unbinder mButterKnifeUnBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int layoutId = getLayoutId();

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
        E model = TUtil.getT(this, 1);
        if (this instanceof BaseView) {
            mPresenter.setModelAndView(model, this);
            mPresenter.subscribe();
        }
        init();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public abstract void init();
}
