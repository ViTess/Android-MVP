package vite.mvp.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import vite.api.NoNetworkException;
import vite.common.LogUtil;
import vite.data.entity.UserInfo;
import vite.mvp.R;
import vite.mvp.base.fragmentActivity.MVPFragmentActivity;
import vite.mvp.util.PageStateHelper;
import vite.mvp.util.ToastUtil;

public class MainActivity extends MVPFragmentActivity<MainPresenter> implements PageStateHelper.PageState {

    @BindView(R.id.main_linear)
    LinearLayout rl_main;
    @BindView(R.id.main_text)
    TextView tv_main;
    @BindView(R.id.main_image)
    ImageView iv_main;

    private PageStateHelper mPageStateHelper;

    private Observer<UserInfo> mShowUserInfoObserver;
    private Observable<String> mClickScreenObservable;

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
    protected void onDestroy() {
        super.onDestroy();
        if (mPageStateHelper != null)
            mPageStateHelper.clear();
    }

    public Observer<UserInfo> showUserInfo() {
        if (mShowUserInfoObserver == null) {
            mShowUserInfoObserver = new Observer<UserInfo>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    Log.i("MainActivity", "showUserInfo onSubscribe");
                }

                @Override
                public void onNext(@NonNull UserInfo userInfo) {
                    showContent();
                    tv_main.setText(userInfo.toString());
//                    rl_main.setEnabled(false);

                    Glide.with(context)
                            .load(userInfo.getAvatarUrl())
                            .into(iv_main);

                    ToastUtil.showShort("fetch data success!");
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Log.i("MainActivity", e.toString());
                    if (e instanceof NoNetworkException) {
                        showNetError();
                    } else {
                        showContent();
                        rl_main.setEnabled(true);
                        tv_main.setText("Oh, something went wrong, please try again");
                    }
                }

                @Override
                public void onComplete() {
                    LogUtil.v("MainActivity", "retry");
                }
            };
        }
        return mShowUserInfoObserver;
    }

    public Observable<String> clickScreen() {
        //注意使用RxBinding要手动dispose，否则会对view持有强引用
        if (mClickScreenObservable == null) {
            mClickScreenObservable = RxView.clicks(rl_main)
                    .map(new Function<Object, String>() {
                        @Override
                        public String apply(@NonNull Object o) throws Exception {
                            return "JakeWharton";
                        }
                    })
                    .doOnNext(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            showLoading();
                        }
                    });
        }
        return mClickScreenObservable;
    }

    public void retry() {
        LogUtil.i("MainActivity", "retry");
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
