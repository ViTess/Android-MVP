package vite.mvp.ui.main;

import android.app.ProgressDialog;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import vite.data.entity.UserInfo;
import vite.mvp.R;
import vite.mvp.base.fragmentActivity.MVPFragmentActivity;
import vite.mvp.util.ToastUtil;

public class MainActivity extends MVPFragmentActivity<MainPresenter, MainModel> implements MainContract.View {

    @BindView(R.id.main_relative)
    RelativeLayout rl_main;
    @BindView(R.id.main_text)
    TextView tv_main;

    ProgressDialog pDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        tv_main.setText("Click screen to get user info!");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("loading...");
    }

    @Override
    public void showLoading() {
        pDialog.show();
    }

    @Override
    public void showLoadUserInfoSuccess(UserInfo info) {
        tv_main.setText(info.toString());
        pDialog.dismiss();
        rl_main.setEnabled(false);
        ToastUtil.showShort("fetch data success!");
    }

    @Override
    public void showLoadUserInfoFailure() {
        pDialog.dismiss();
        rl_main.setEnabled(true);
        tv_main.setText("Oh, something went wrong, please try again");
    }

    @OnClick(R.id.main_relative)
    public void clickScreen() {
        mPresenter.getUserInfo("JakeWharton");
    }
}
