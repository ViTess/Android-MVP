package vite.mvp.base.activity;

import android.os.Bundle;

import butterknife.ButterKnife;
import vite.common.TUtil;
import vite.mvp.base.BaseModel;
import vite.mvp.base.BasePresenter;
import vite.mvp.base.BaseView;
import vite.mvp.util.PageStateHelper;

import static vite.mvp.util.PageStateHelper.NONE;
import static vite.mvp.util.PageStateHelper.PageStateHolder;

/**
 * 使用mvp模式的activity基类
 * Created by trs on 16-10-18.
 */
public abstract class MVPActivity<T extends BasePresenter, E extends BaseModel> extends BaseActivity {
    public T mPresenter;

    protected final PageStateHolder mPageStateHolder = new PageStateHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int layoutId = getLayoutId(mPageStateHolder);
        final PageStateHelper helper = mPageStateHolder.helper;
        if (helper != PageStateHelper.NONE && helper.getView() != null)
            setContentView(helper.getView());
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
