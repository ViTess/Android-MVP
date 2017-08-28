package vite.common;

/**
 * Created by trs on 17-8-28.
 */

public abstract class Singleton<T> {
    private volatile T mInstance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (mInstance == null) {
            synchronized (this) {
                if (mInstance == null)
                    mInstance = newInstance();
            }
        }
        return mInstance;
    }
}
