package vite.mvp.ui.main;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import vite.common.LogUtil;
import vite.data.entity.UserInfo;
import vite.mvp.R;
import vite.mvp.base.fragmentActivity.MVPFragmentActivity;
import vite.mvp.util.PageStateHelper;
import vite.mvp.util.ToastUtil;

public class MainActivity extends MVPFragmentActivity<MainPresenter, MainModel> implements MainContract.View {

    @BindView(R.id.main_relative)
    RelativeLayout rl_main;
    @BindView(R.id.main_text)
    TextView tv_main;

    @Override
    public int getLayoutId() {
//        ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("loading...");
//
//        mPageStateHelper = new PageStateHelper.Builder(this)
//                .setContent(R.layout.activity_main)
//                .setLoading(pDialog)
//                .create();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        };

        mPageStateHelper = new PageStateHelper.Builder(this)
                .setContent(R.layout.activity_main)
                .setLoading(R.layout.layout_loading)
                .setError(R.layout.layout_error)
                .setNetError(R.layout.layout_neterror)
                .setEmpty(R.layout.layout_empty)
                .setErrorRetry(R.id.layout_error_retry_btn, listener)
                .setNetErrorRetry(R.id.layout_net_error_retry_btn, listener)
                .setEmptyRetry(R.id.layout_empty_retry_btn, listener)
                .create();

        return R.layout.activity_main;
    }

    @Override
    public void init() {
        tv_main.setText("Click screen to get user info!");
    }

    @Override
    public void showContent() {
        mPageStateHelper.showContent();
    }

    @Override
    public void showError() {
        mPageStateHelper.showError();
    }

    @Override
    public void showNetError() {
        mPageStateHelper.showNetError();
    }

    @Override
    public void showEmpty() {
        mPageStateHelper.showEmpty();
    }

    @Override
    public void showLoading() {
        mPageStateHelper.showLoading();
    }

    @Override
    public void showLoadUserInfoSuccess(UserInfo info) {
        showContent();
        tv_main.setText(info.toString());
        rl_main.setEnabled(false);
        ToastUtil.showShort("fetch data success!");
    }

    @Override
    public void showLoadUserInfoFailure() {
        showContent();
        rl_main.setEnabled(true);
        tv_main.setText("Oh, something went wrong, please try again");
    }

    @OnClick(R.id.main_relative)
    public void clickScreen() {
        mPresenter.getUserInfo("JakeWharton");
    }

    public void retry() {
        LogUtil.v("MainActivity", "retry");
    }
}
