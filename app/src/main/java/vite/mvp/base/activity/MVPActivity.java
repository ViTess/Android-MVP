package vite.mvp.base.activity;

import android.os.Bundle;

import butterknife.ButterKnife;
import vite.common.TUtil;
import vite.mvp.base.BaseModel;
import vite.mvp.base.BasePresenter;
import vite.mvp.base.BaseView;
import vite.mvp.util.PageStateHelper;

/**
 * 使用mvp模式的activity基类
 * Created by trs on 16-10-18.
 */
public abstract class MVPActivity<T extends BasePresenter, E extends BaseModel> extends BaseActivity {
    public T mPresenter;

    private PageStateHelper mPageStateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int layoutId = getLayoutId(mPageStateHelper);
        if (mPageStateHelper != null)
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
    }

    /**
     * 设置layout id
     *
     * @return
     */
    public abstract int getLayoutId(PageStateHelper pageStateHelper);

    /**
     * 代替onCreate
     */
    public abstract void init();
}
