package vite.mvp.base;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import vite.mvp.util.PageStateHelper;

/**
 * Created by trs on 16-10-18.
 */

public interface BaseView extends PageStateHelper.PageState {
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event);
}
