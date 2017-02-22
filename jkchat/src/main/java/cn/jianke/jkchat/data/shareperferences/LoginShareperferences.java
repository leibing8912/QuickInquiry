package cn.jianke.jkchat.data.shareperferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @className: LoginShareperferences
 * @classDescription: 登录轻量储存数据
 * @author: leibing
 * @createTime: 2017/2/22
 */
public class LoginShareperferences {
    // 登录Shareperferences文件名
    private final static String JK_LOGIN_PREF = "jk_login_pref";
    // 是否登录
    private final static String ISLOGIN = "isLogin";
    // 登录SharedPreferences
    private SharedPreferences mLoginSp;
    // sington
    private static LoginShareperferences instance;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param context 上下文引用
     * @return
     */
    private LoginShareperferences(Context context){
    }

    /**
     * sington
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param context 上下文引用（此处传入application引用防止内存泄漏）
     * @return sington
     */
    public static LoginShareperferences getInstance(Context context){
        if (instance == null){
            synchronized (LoginShareperferences.class){
                if (instance == null)
                    instance = new LoginShareperferences(context);
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
            if (mLoginSp != null) {
                return mLoginSp.getBoolean(ISLOGIN, false);
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
            if (mLoginSp != null) {
                SharedPreferences.Editor editor = mLoginSp.edit();
                editor.putBoolean(ISLOGIN, isLogin);
                editor.commit();
            }
        }
    }
}
