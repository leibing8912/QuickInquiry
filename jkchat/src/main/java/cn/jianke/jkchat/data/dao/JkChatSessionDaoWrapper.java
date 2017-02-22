package cn.jianke.jkchat.data.dao;

import android.content.Context;
import com.jk.chat.gen.JkChatSessionDao;

/**
 * @className: JkChatSessionDaoWrapper
 * @classDescription: 健客聊天会话消息包装（封装一些常用方法）
 * @author: leibing
 * @createTime: 2017/2/22
 */
public class JkChatSessionDaoWrapper {
    // sington
    private static JkChatSessionDaoWrapper instance;
    // 健客聊天会话消息Dao
    private JkChatSessionDao mJkChatSessionDao;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param context 引用
     * @return
     */
    private JkChatSessionDaoWrapper(Context context){
        // init jk chat session dao
        mJkChatSessionDao = JkChatDaoManager.getInstance(
                context.getApplicationContext()).getDaoSession().getJkChatSessionDao();
    }

    /**
     * sington
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param context 引用（此处传入application引用防止内存泄漏）
     * @return
     */
    public static JkChatSessionDaoWrapper getInstance(Context context){
        if (instance == null){
            synchronized(JkChatMessageDaoWrapper.class){
                if (instance == null){
                    instance = new JkChatSessionDaoWrapper(context);
                }
            }
        }
        return instance;
    }


}
