package vite.mvp.base;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by trs on 16-10-18.
 */

public interface BaseView {
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event);
}
