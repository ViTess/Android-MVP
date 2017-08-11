package vite.mvp.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import vite.common.LogUtil;
import vite.data.entity.UserInfo;
import vite.mvp.R;
import vite.mvp.base.fragmentActivity.MVPFragmentActivity;
import vite.mvp.util.PageStateHelper;
import vite.mvp.util.ToastUtil;

public class MainActivity extends MVPFragmentActivity<MainPresenter, MainModel> implements MainContract.View {

    @BindView(R.id.main_linear)
    LinearLayout rl_main;
    @BindView(R.id.main_text)
    TextView tv_main;
    @BindView(R.id.main_image)
    ImageView iv_main;

    private Observer<UserInfo> mShowUserInfo = new Observer<UserInfo>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            showLoading();
        }

        @Override
        public void onNext(@NonNull UserInfo userInfo) {
            showContent();
            tv_main.setText(userInfo.toString());
            rl_main.setEnabled(false);

            Glide.with(context)
                    .load(userInfo.getAvatarUrl())
                    .into(iv_main);

            ToastUtil.showShort("fetch data success!");
        }

        @Override
        public void onError(@NonNull Throwable e) {
            showContent();
            rl_main.setEnabled(true);
            tv_main.setText("Oh, something went wrong, please try again");
        }

        @Override
        public void onComplete() {
            LogUtil.v("MainActivity", "retry");
        }
    };

    @Override
    public int getLayoutId(PageStateHelper.PageStateHolder holder) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        };

        holder.helper = new PageStateHelper.Builder(this)
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
    public Observer<UserInfo> showUserInfo() {
        return mShowUserInfo;
    }

    @OnClick(R.id.main_linear)
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
