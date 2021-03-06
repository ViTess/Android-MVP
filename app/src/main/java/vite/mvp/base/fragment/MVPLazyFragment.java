package vite.mvp.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import vite.common.TUtil;
import vite.mvp.base.BasePresenter;

/**
 * 懒加载
 * Created by trs on 17-6-2.
 */
public abstract class MVPLazyFragment<P extends BasePresenter> extends BaseFragment {

    private boolean isViewFirstCreate;
    private boolean isFragmentVisible;
    private boolean isFirstVisible;
    private View rootView;
    private Unbinder mButterKnifeUnBinder;

    public P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetFlag();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            final int layoutId = getLayoutId();
            final View view = getLayoutView();
            if (view != null)
                rootView = view;
            else
                rootView = inflater.inflate(layoutId, container, false);
            mButterKnifeUnBinder = ButterKnife.bind(this, rootView);

            mPresenter = TUtil.getT(this, 0);
            mPresenter.setView(this);

            init(savedInstanceState);
            mPresenter.subscribe();
            isViewFirstCreate = true;
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //如果setUserVisibleHint()在rootView创建前调用时
        //那么就等到rootView创建完后才回调onVisible()
        //保证onVisible()的回调发生在rootView创建完成之后，以便支持ui操作

//        if (rootView == null) {
//            rootView = view;
        if (isViewFirstCreate && getUserVisibleHint()) {
            if (isFirstVisible) {
                isFirstVisible = false;

                //call lazyLoad
                lazyLoad();
            }
            onVisible();
            isFragmentVisible = true;
            isViewFirstCreate = false;
        }
//        }
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
//            init();
            lazyLoad();
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
//        if (mPresenter != null) {
//            mPresenter.unsubscribe();
//            mPresenter.onDestory();
//        }
//        mButterKnifeUnBinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetFlag();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
            mPresenter.onDestory();
        }
        if (mButterKnifeUnBinder != null) {
            mButterKnifeUnBinder.unbind();
        }
    }

    private void resetFlag() {
        isViewFirstCreate = false;
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
     * 设置layout view
     *
     * @return
     */
    protected View getLayoutView() {
        return null;
    }

    /**
     * 代替onCreate，fragment创建时即调用，可在此初始化一些参数、界面等
     */
    public abstract void init(@Nullable Bundle savedInstanceState);

    /**
     * 懒加载，仅调用一次，在fragment可见时才执行,可在此调用加载数据等操作，保证仅调用一次
     * Presenter的subscribe在lazyload之前执行
     */
    public abstract void lazyLoad();

    /**
     * 当前fragment处于可见状态
     */
    public abstract void onVisible();

    /**
     * 当前fragment处于不可见状态
     */
    public abstract void onInvisible();
}
