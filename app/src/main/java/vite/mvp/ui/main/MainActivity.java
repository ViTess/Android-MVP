package vite.mvp.ui.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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
    public int getLayoutId(PageStateHelper[] helper) {
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

        helper[0] = new PageStateHelper.Builder(this)
                .setFragmentManager(getSupportFragmentManager())
                .setContent(R.layout.activity_main)
                .setLoading(new ProgressDialogFragment())
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
        mPageStateHelper[0].showContent();
    }

    @Override
    public void showError() {
        mPageStateHelper[0].showError();
    }

    @Override
    public void showNetError() {
        mPageStateHelper[0].showNetError();
    }

    @Override
    public void showEmpty() {
        mPageStateHelper[0].showEmpty();
    }

    @Override
    public void showLoading() {
        mPageStateHelper[0].showLoading();
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

    public static class ProgressDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity()).setTitle("Title").setMessage("loading")
                    .create();
        }
    }
}
