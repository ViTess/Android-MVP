package vite.mvp.base.activity;

import android.content.Context;
import android.os.Bundle;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

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
}
