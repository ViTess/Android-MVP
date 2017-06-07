package vite.mvp.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import vite.common.TUtil;
import vite.mvp.base.BaseModel;
import vite.mvp.base.BasePresenter;
import vite.mvp.base.BaseView;
import vite.mvp.util.PageStateHelper;

import static vite.mvp.util.PageStateHelper.NONE;
import static vite.mvp.util.PageStateHelper.PageStateHolder;

/**
 * Created by trs on 16-11-4.
 */

public abstract class MVPFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment {
    public T mPresenter;

    protected final PageStateHolder mPageStateHolder = new PageStateHolder();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int layoutId = getLayoutId(mPageStateHolder);
        final PageStateHelper helper = mPageStateHolder.helper;
        if (helper != PageStateHelper.NONE && helper.getView() != null)
            return helper.getView();
        else
            return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPageStateHolder.helper != null && mPageStateHolder.helper != NONE)
            mPageStateHolder.helper.clear();
    }

    /**
     * 设置layout id
     *
     * @param holder
     * @return
     */
    public abstract int getLayoutId(PageStateHolder holder);

    /**
     * 代替onCreate
     */
    public abstract void init();
}
