package cn.jianke.jkchat.data.shareperferences;

import android.content.Context;
import android.content.SharedPreferences;
import cn.jianke.jkchat.common.StringUtils;

/**
 * @className: JkChatSharePerferences
 * @classDescription: 健客聊天轻量数据储存
 * @author: leibing
 * @createTime: 2017/2/22
 */
public class JkChatSharePerferences {
    // 健客聊天表名
    private final static String JK_CHAT_PREF = "jk_chat_pref";
    // 以用户id命名的表名（用于关联不同账号）
    private final static String USER_ID_PREF = "user_id_pref";
    // 是否登录
    private final static String ISLOGIN = "isLogin";
    // 就诊人id
    private final static String PATIENT_ID = "patient_Id";
    // 就诊人名称
    private final static String PATIENT_NAME = "name";
    // 就诊人性别
    private final static String PATIENT_SEX = "sex";
    // 就诊人年龄
    private final static String PATIENT_AGE = "age";
    // 用户id
    private final static String USER_ID = "user_Id";
    // 会话状态
    private final static String CONVERSATION_STATUS = "conversation_Status";
    // 上下文引用
    private Context mContext;
    // 健客聊天SharedPreferences
    private SharedPreferences jkChatSp;
    // sington
    private static JkChatSharePerferences instance;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param context 上下文引用
     * @return
     */
    private JkChatSharePerferences(Context context){
        this.mContext = context;
        jkChatSp = context.getSharedPreferences(JK_CHAT_PREF, Context.MODE_PRIVATE);
    }

    /**
     * sington
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param context 上下文引用（此处传入application引用防止内存泄漏）
     * @return sington
     */
    public static JkChatSharePerferences getInstance(Context context){
        if (instance == null){
            synchronized (JkChatSharePerferences.class){
                if (instance == null)
                    instance = new JkChatSharePerferences(context);
            }
        }
        return instance;
    }

    /**
     * 获取用户登录状态
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public boolean getIsUserLogin(){
        synchronized (instance) {
            if (jkChatSp != null) {
                return jkChatSp.getBoolean(ISLOGIN, false);
            }
            return false;
        }
    }

    /**
     * 设置用户登录状态
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param isLogin 登录状态
     * @return
     */
    public void setIsUserLogin(boolean isLogin){
        synchronized (instance) {
            if (jkChatSp != null) {
                SharedPreferences.Editor editor = jkChatSp.edit();
                editor.putBoolean(ISLOGIN, isLogin);
                editor.commit();
            }
        }
    }

    /**
     * 获取就诊人id
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public String getPatientId(){
        synchronized (instance) {
            if (jkChatSp != null) {
                return jkChatSp.getString(PATIENT_ID, null);
            }
            return null;
        }
    }

    /**
     * 获取就诊人名称
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public String getPatientName(){
        synchronized (instance) {
            if (jkChatSp != null) {
                return jkChatSp.getString(PATIENT_NAME, null);
            }
            return null;
        }
    }

    /**
     * 获取就诊人性别
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public String getPatientSex(){
        synchronized (instance) {
            if (jkChatSp != null) {
                return jkChatSp.getString(PATIENT_SEX, null);
            }
            return null;
        }
    }

    /**
     * 获取就诊人年龄
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public String getPatientAge(){
        synchronized (instance) {
            if (jkChatSp != null) {
                return jkChatSp.getString(PATIENT_AGE, null);
            }
            return null;
        }
    }

    /**
     * 设置userId
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param userId 用户id
     * @return
     */
    public void setUserId(String userId){
        synchronized (instance) {
            if (jkChatSp != null) {
                SharedPreferences.Editor editor = jkChatSp.edit();
                editor.putString(USER_ID, userId);
                editor.commit();
            }
        }
    }

    /**
     * 获取userId
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param
     * @return
     */
    public String getUserId(){
        synchronized (instance) {
            if (jkChatSp != null) {
                return jkChatSp.getString(USER_ID, null);
            }
            return null;
        }
    }

    /**
     * 保存会话状态
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param status 会话状态 false为开始会话，true为结束会话
     * @return
     */
    public void saveConversationStatus(boolean status){
        synchronized (instance) {
            // 获取userId作为表名
            String tableName = getUserId();
            if (StringUtils.isNotEmpty(tableName)){
                SharedPreferences userIdSp = mContext.getSharedPreferences(tableName,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userIdSp.edit();
                editor.putBoolean(CONVERSATION_STATUS, false);
                // 提交当前数据
                editor.commit();
            }
        }
    }

    /**
     *
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param
     * @return
     */
    public boolean getConversationStatus(){
        synchronized (instance) {
            boolean status = false;
            // 获取userId作为表名
            String tableName = getUserId();
            if (StringUtils.isNotEmpty(tableName)){
                SharedPreferences userIdSp = mContext.getSharedPreferences(tableName,
                        Context.MODE_PRIVATE);
                status = userIdSp.getBoolean(CONVERSATION_STATUS, false);
            }
            return status;
        }
    }
}
