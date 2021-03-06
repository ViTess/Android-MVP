package vite.mvp.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import vite.common.LogUtil;
import vite.data.entity.UserInfo;
import vite.mvp.R;
import vite.mvp.base.fragmentActivity.MVPFragmentActivity;
import vite.mvp.util.PageStateHelper;
import vite.mvp.util.ToastUtil;

public class MainActivity extends MVPFragmentActivity<MainPresenter> implements Contract.View {

    @BindView(R.id.main_linear)
    LinearLayout rl_main;
    @BindView(R.id.main_text)
    TextView tv_main;
    @BindView(R.id.main_image)
    ImageView iv_main;

    private PageStateHelper mPageStateHelper;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected View getLayoutView() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        };

        mPageStateHelper = new PageStateHelper.Builder(this)
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
        return mPageStateHelper.getView();
    }

    @Override
    public void init() {
        tv_main.setText("Click screen to get user info!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(context).resumeRequests();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(context).pauseRequests();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPageStateHelper != null)
            mPageStateHelper.clear();
    }

    @Override
    public void showUserInfo(UserInfo userInfo) {
        tv_main.setText(userInfo.toString());
//                    rl_main.setEnabled(false);

        Glide.with(context)
                .load(userInfo.getAvatarUrl())
                .into(iv_main);

        ToastUtil.showShort("fetch data success!");
    }

    @Override
    public void showErrorMessage(String message) {
        rl_main.setEnabled(true);
        tv_main.setText(message);
    }

    @OnClick(R.id.main_linear)
    public void clickScreen() {
        mPresenter.getUserInfo("JakeWharton");
//        mPresenter.getTest();
    }

    @Override
    public void retry() {
        LogUtil.i("MainActivity", "retry");
        mPresenter.getUserInfo("JakeWharton");
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

    public static class ProgressDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity()).setTitle("Title").setMessage("loading")
                    .create();
        }
    }
}
