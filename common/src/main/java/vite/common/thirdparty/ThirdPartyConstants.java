package vite.common.thirdparty;

/**
 * Created by trs on 17-9-11.
 */

public final class ThirdPartyConstants {

    //微信
    public static final String WX_APPID = "";
    public static final String WX_SCOPE = "snsapi_userinfo";
    public static final String WX_STATE = "none";

    //QQ
    public static final String QQ_APPID = "";//注意要在manifest中设置
    public static final String QQ_SCOPE = "get_simple_userinfo";

    //微博
    public static final String SINA_APPKEY = "";
    public static final String SINA_SCOPE = "follow_app_official_microblog";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    //友盟
    public static final String UMENG_APPKEY = "";//注意要在manifest中设置

    //Bugly
    public static final String BUGLY_APPID = "";

    /**
     * 统计事件，用于StatisticManager
     */
    public static class StatisticEvent {
        public static final String SE_CLICK_XXX = "xxx";
    }
}
