package cn.jianke.jkchat.common;

/**
 * @className: RequestUrlUtils
 * @classDescription: 请求地址工具类
 * @author: leibing
 * @createTime: 2017/2/17
 */
public class RequestUrlUtils {
    // 是否是调式模式(内网)，开发阶段可以设置为true，上线了之后要设置为false，更新到外网
    public static boolean IS_DEUBG = false;
    // 是否是北京测试环境
    public final static boolean IS_BJ_DEBUG = false;
    // 内网：192.168.70.141，外网：askwx.sgz88.com 正式：askswt.sgz88.com 站点：ask
    // 192.168.70.102
    public static String WS_CONNECT_FORMAT = "";
    public static String PRAISE_URL = "";
    // 判断是否有未读消息
    public static String NO_READ_URL = "";
    // 判断医生在线数量
    public static String GET_DOCTOR_NUM_URL = "";
    // 判断当前用户是否在咨询中
    public static String CHECK_ASKING_NOW_URL = "";

    // 静态代码块，用来初始化URL
    static {
        // 对于DEBUG模式，使用测试线
        if (IS_DEUBG) {
            if (IS_BJ_DEBUG)
                WS_CONNECT_FORMAT = "ws://118.194.37.130:3001/user=%s&clientid=%s&tid=%s&fileid=%s&name=%s&sex=%s&age=%s&number=3";
            else
                WS_CONNECT_FORMAT = "ws://askwx.sgz88.com:3001/user=%s&clientid=%s&tid=%s&fileid=%s&name=%s&sex=%s&age=%s&number=3";

            PRAISE_URL = "http://askwx.sgz88.com/AppAppraise.aspx";
            NO_READ_URL = "http://askwx.sgz88.com/xyajax/InfoHander.ashx";
            GET_DOCTOR_NUM_URL = "http://askwx.sgz88.com/xyajax/InfoHander.ashx?type=chkDocOnLine&site=task";
            CHECK_ASKING_NOW_URL = "http://askwx.sgz88.com/xyajax/infohander.ashx?type=chkUserTalk&website=task&customid=%s&number=3";
        }
        // 正式线
        else {
            WS_CONNECT_FORMAT = "ws://askswt.sgz88.com:3003/user=%s&clientid=%s&tid=%s&fileid=%s&name=%s&sex=%s&age=%s&number=3";
            PRAISE_URL = "http://askswt.sgz88.com/AppAppraise.aspx";
            NO_READ_URL = "http://askswt.sgz88.com/xyajax/infohander.ashx";
            GET_DOCTOR_NUM_URL = "http://askswt.sgz88.com/xyajax/InfoHander.ashx?type=chkDocOnLine&site=ask";
            CHECK_ASKING_NOW_URL = "http://askswt.sgz88.com/xyajax/infohander.ashx?type=chkUserTalk&website=ask&customid=%s&number=3";
        }
    }
}
