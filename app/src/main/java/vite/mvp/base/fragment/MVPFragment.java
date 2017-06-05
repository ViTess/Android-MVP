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

/**
 * Created by trs on 16-11-4.
 */

public abstract class MVPFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment {
    public T mPresenter;

    protected final PageStateHelper[] mPageStateHelper = new PageStateHelper[]{PageStateHelper.NONE};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int layoutId = getLayoutId(mPageStateHelper);
        final PageStateHelper helper = mPageStateHelper[0];
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

        if (mPageStateHelper[0] != PageStateHelper.NONE)
            mPageStateHelper[0].clear();
    }

    /**
     * 设置layout id
     *
     * @param helper 传入数组方便在函数内修改引用参数，该数组只有一个值，平时用helper[0]表示使用
     * @return
     */
    public abstract int getLayoutId(PageStateHelper[] helper);

    /**
     * 代替onCreate
     */
    public abstract void init();
}
