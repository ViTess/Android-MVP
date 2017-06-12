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
import vite.mvp.util.PageStateHelper;

import static vite.mvp.util.PageStateHelper.NONE;
import static vite.mvp.util.PageStateHelper.PageStateHolder;

/**
 * Created by trs on 16-11-4.
 */

public abstract class MVPFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment {
    public T mPresenter;

    protected final PageStateHolder mPageStateHolder = new PageStateHolder();
    private Unbinder mButterKnifeUnBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int layoutId = getLayoutId(mPageStateHolder);
        final PageStateHelper helper = mPageStateHolder.helper;
        View view;
        if (helper != PageStateHelper.NONE && helper.getView() != null)
            view = helper.getView();
        else
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
