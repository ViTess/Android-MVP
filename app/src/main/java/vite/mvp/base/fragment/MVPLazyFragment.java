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

/**
 * 懒加载
 * Created by trs on 17-6-2.
 */
public abstract class MVPLazyFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment {

    private boolean isFragmentVisible;
    private boolean isFirstVisible;
    private View rootView;

    public T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetFlag();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //如果setUserVisibleHint()在rootView创建前调用时
        //那么就等到rootView创建完后才回调onVisible()
        //保证onVisible()的回调发生在rootView创建完成之后，以便支持ui操作

        if (rootView == null) {
            rootView = view;
            if (getUserVisibleHint()) {
                if (isFirstVisible) {
                    mPresenter = TUtil.getT(this, 0);
                    E model = TUtil.getT(this, 1);
                    if (this instanceof BaseView) {
                        mPresenter.setModelAndView(model, this);
                        mPresenter.subscribe();
                    }
                    init();

                    isFirstVisible = false;
                }
                onVisible();
                isFragmentVisible = true;
            }
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //setUserVisibleHint()有可能在fragment的生命周期外被调用
        if (rootView == null) {
            return;
        }
        if (isFirstVisible && isVisibleToUser) {
            init();
            isFirstVisible = false;
        }
        if (isVisibleToUser) {
            onVisible();
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onInvisible();
        }
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
        resetFlag();
    }

    private void resetFlag() {
        isFirstVisible = true;
        isFragmentVisible = false;
        rootView = null;
    }

    /**
     * 设置layout id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 代替onCreate，fragment可见时调用且只调用一次
     */
    public abstract void init();

    /**
     * 当前fragment处于可见状态
     */
    public abstract void onVisible();

    /**
     * 当前fragment处于不可见状态
     */
    public abstract void onInvisible();
}