package vite.mvp.base.activity;

import android.content.Context;
import android.os.Bundle;

import com.trello.rxlifecycle2.components.RxActivity;

/**
 * Activity基类
 * Created by trs on 16-10-18.
 */
public class BaseActivity extends RxActivity {
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
