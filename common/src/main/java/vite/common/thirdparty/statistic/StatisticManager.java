package vite.common.thirdparty.statistic;

import android.content.Context;

import vite.common.AppUtil;
import vite.common.thirdparty.ThirdPartyConstants;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/**
 * 第三方统计
 * <p>
 * Created by trs on 17-9-11.
 */

public final class StatisticManager {

    /**
     * 初始化(在Application)
     *
     * @param context
     */
    public static void init(Context context) {
        initUmeng(context.getApplicationContext());
    }

    public static void onResume(Context context) {
        onResumeUmeng(context);
    }

    public static void onPause(Context context) {
        onPauseUmeng(context);
    }

    /**
     * @param context
     * @param event see {@link vite.common.thirdparty.ThirdPartyConstants.StatisticEvent}
     * @param params
     */
    public static void onEvent(Context context, String event, Map<String, String> params) {
        onEventUmeng(context, event, params);
    }

    private static void initUmeng(Context context) {
//        MobclickAgent.setDebugMode(true);
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(context, ThirdPartyConstants.UMENG_APPKEY,
                AppUtil.getUmengChannel(context), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.startWithConfigure(config);
        MobclickAgent.setSessionContinueMillis(30000);//定义进入后台后再再返回的间隔，ms
        MobclickAgent.openActivityDurationTrack(false);//禁止默认的页面统计方式
    }

    private static void onResumeUmeng(Context context) {
        MobclickAgent.onPageStart(context.getClass().getName());
        MobclickAgent.onResume(context);
    }

    private static void onPauseUmeng(Context context) {
        MobclickAgent.onPageEnd(context.getClass().getName());
        MobclickAgent.onPause(context);
    }

    private static void onEventUmeng(Context context, String event, Map<String, String> params) {
        MobclickAgent.onEvent(context, event, params);
    }

    /* if need other statistic sdk , just add onResumeXXX、onPauseXXX、onEventXXX */
}
