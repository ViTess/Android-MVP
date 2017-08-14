package vite.mvp.base.fragmentActivity;

import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int layoutId = getLayoutId();
        final View view = getLayoutView();
        if (view != null)
            setContentView(view);
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