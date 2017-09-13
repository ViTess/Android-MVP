package vite.common;

/**
 * Created by trs on 17-8-28.
 */

public abstract class Singleton<T, V> {
    private volatile T mInstance;

    protected abstract T newInstance(V... args);

    public final T getInstance(V... args) {
        if (mInstance == null) {
            synchronized (this) {
                if (mInstance == null)
                    mInstance = newInstance(args);
            }
        }
        return mInstance;
    }
}
