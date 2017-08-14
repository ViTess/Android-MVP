package vite.mvp.base;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * Created by trs on 17-8-14.
 */

public interface BaseFragmentView {
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event);
}
