package vite.mvp.ui.main;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vite.mvp.R;
import vite.mvp.base.fragmentActivity.MVPFragmentActivity;
import vite.mvp.bean.UserInfo;

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
    public void showUserInfo(UserInfo info) {
        tv_main.setText(info.toString());
    }

    @Override
    public void showLoading(boolean isShow) {
        if (isShow)
            pDialog.show();
        else
            pDialog.dismiss();
    }

    @Override
    public void showResultState(boolean isSuccessful) {
        if (isSuccessful) {
            rl_main.setEnabled(false);
            Toast.makeText(context, "fetch data success!", Toast.LENGTH_SHORT).show();
        } else {
            rl_main.setEnabled(true);
            tv_main.setText("Oh, something went wrong, please try again");
        }
    }

    @OnClick(R.id.main_relative)
    public void clickScreen() {
        mPresenter.getUserInfo("JakeWharton");
    }
}
