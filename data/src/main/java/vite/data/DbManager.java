package vite.data;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import vite.data.dao.DaoMaster;
import vite.data.dao.DaoSession;

/**
 * Created by trs on 17-5-27.
 */

public final class DbManager {
    private static final String sDBName = "test";

    private volatile static DaoSession sDaoSession;

    /**
     * 在Application里初始化
     *
     * @param application
     */
    public static void init(Application application) {
        if (sDaoSession == null)
            sDaoSession = new DaoMaster(new DefaultOpenHelper(application, sDBName).getWritableDb()).newSession();
    }

    public static DaoSession getDaoSession() {
        if (sDaoSession == null)
            throw new NullPointerException("Isn't call init() in Application?");
        return sDaoSession;
    }

    private static final class DefaultOpenHelper extends DaoMaster.OpenHelper {

        public DefaultOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DefaultOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            super.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
