package vite.common.thirdparty.upgrade;

import android.app.Activity;
import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * Created by trs on 17-9-12.
 */

public final class UpgradeManager {

    /**
     * 目前仅使用Bugly
     *
     * @param context
     */
    public static void init(Context context) {
        initBugly(context);
    }

    /**
     * 设置可显示更新弹窗的Activity
     *
     * @param activitys
     */
    public static void setShowUpgradeActivity(Class<Activity>... activitys) {
        setShowUpGradeActsBugly(activitys);
    }

    /**
     * 自动检查是否有更新（将自动检查一次）
     */
    public static void autoCheckUpgrade() {
        checkUpgradeBugly(false, false);
    }

    /**
     * 手动检查是否有更新（需要点击的地方调用）
     */
    public static void manualCheckUpgrade() {
        checkUpgradeBugly(true, false);
    }

    /**
     * 注意Beta.autoInit在{@link vite.common.thirdparty.crash.CrashManager}中设置了false，因此此处手动初始化
     *
     * @param context
     */
    private static void initBugly(Context context) {
//        Beta.autoInit = false;//自动初始化开关，默认true
        Beta.autoCheckUpgrade = true;//自动检查更新开关
        Beta.upgradeCheckPeriod = 60 * 1000;//升级检查周期设置，单位ms，默认0
        Beta.initDelay = 5 * 1000;//延迟初始化，默认3s
//        Beta.smallIconId = R.drawable.ic_launcher;//设置状态栏小图标
//        Beta.defaultBannerId = R.drawable.ic_launcher;//设置更新弹窗默认展示的banner
//        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);//设置更新资源存放目录
//        Beta.showInterruptedStrategy = true;//开启显示打断策略
//        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;//设置自定义升级对话框ui
//        Beta.tipsDialogLayoutId = R.layout.tips_dialog;//设置自定义弹窗ui
//        Beta.enableNotification = true;//设置是否显示消息通知，默认true
//        Beta.autoDownloadOnWifi = false;//设置wifi自动下载，默认false
//        Beta.canShowApkInfo = true;//设置是否显示弹窗中的apk信息
//        Beta.enableHotfix = true;//热更新能力，默认开启(true)
        Beta.init(context, false);//手动初始化
    }

    private static void setShowUpGradeActsBugly(Class<? extends Activity>... activitys) {
        if (activitys != null && activitys.length > 0) {
            Beta.canShowUpgradeActs.clear();
            for (Class<? extends Activity> c : activitys) {
                Beta.canShowUpgradeActs.add(c);
            }
        }
    }

    /**
     * @param isManual  true - 手动点击 , false - 代码调用
     * @param isSilence true - 无交互 , false - 有交互
     */
    private static void checkUpgradeBugly(boolean isManual, boolean isSilence) {
        Beta.checkUpgrade(isManual, isSilence);
    }
}
