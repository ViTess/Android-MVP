package vite.mvp.base.activity;

import android.os.Bundle;
import android.view.View;


import butterknife.ButterKnife;
import vite.common.TUtil;
import vite.mvp.base.BasePresenter;

/**
 * Created by trs on 17-8-11.
 */

public abstract class MVPAppCompatActivity<P extends BasePresenter> extends BaseAppCompatActivity {
    public P mPresenter;

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
        mPresenter.setView(this);
        mPresenter.subscribe();
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