package vite.mvp.base.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Activity基类
 * Created by trs on 16-10-18.
 */
public class BaseActivity extends Activity {
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }
}
