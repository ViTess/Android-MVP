package vite.mvp.base.activity;

import android.content.Context;
import android.os.Bundle;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import vite.common.thirdparty.statistic.StatisticManager;

/**
 * Created by trs on 17-8-11.
 */

public class BaseAppCompatActivity extends RxAppCompatActivity {
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticManager.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatisticManager.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
