package vite.mvp.base.fragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

/**
 * Created by trs on 16-11-4.
 */

public class BaseFragmentActivity extends RxFragmentActivity {
    protected Context context;
    protected FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mFragmentManager = getSupportFragmentManager();
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
