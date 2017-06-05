package vite.mvp.base.fragmentActivity;

import android.os.Bundle;

import butterknife.ButterKnife;
import vite.common.TUtil;
import vite.mvp.base.BaseModel;
import vite.mvp.base.BasePresenter;
import vite.mvp.base.BaseView;
import vite.mvp.util.PageStateHelper;

/**
 * Created by trs on 16-11-4.
 */
public abstract class MVPFragmentActivity<T extends BasePresenter, E extends BaseModel> extends BaseFragmentActivity {
    public T mPresenter;

    protected PageStateHelper mPageStateHelper = PageStateHelper.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int layoutId = getLayoutId();
        if (mPageStateHelper != null && mPageStateHelper.getView() != null)
            setContentView(mPageStateHelper.getView());
        else
            setContentView(layoutId);

        ButterKnife.bind(this);
        mPresenter = TUtil.getT(this, 0);
        E model = TUtil.getT(this, 1);
        if (this instanceof BaseView) {
            mPresenter.setModelAndView(model, this);
            mPresenter.subscribe();
        }
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
            mPresenter.onDestory();
        }

        if (mPageStateHelper != null)
            mPageStateHelper.clear();
    }

    /**
     * 设置layout id
     * 如要使用PageStateHelper请在getLayoutId里初始化
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 代替onCreate
     */
    public abstract void init();
}