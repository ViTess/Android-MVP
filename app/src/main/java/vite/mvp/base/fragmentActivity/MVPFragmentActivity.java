package vite.mvp.base.fragmentActivity;

import android.os.Bundle;

import butterknife.ButterKnife;
import vite.mvp.base.BaseModel;
import vite.mvp.base.BasePresenter;
import vite.mvp.base.BaseView;
import vite.mvp.util.TUtil;

/**
 * Created by trs on 16-11-4.
 */
public abstract class MVPFragmentActivity<T extends BasePresenter, E extends BaseModel> extends BaseFragmentActivity {
    public T mPresenter;
    public E mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        ButterKnife.bind(this);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof BaseView) {
            mPresenter.setModelAndView(mModel, this);
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
     * 代替onCreate
     */
    public abstract void init();
}