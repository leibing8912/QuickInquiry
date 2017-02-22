package cn.jianke.jkchat.data.dao;

import android.content.Context;
import com.jk.chat.gen.JkChatMessageDao;

/**
 * @className: JkChatMessageDaoWrapper
 * @classDescription: 健客聊天信息数据库包装（封装一些常用方法）
 * @author: leibing
 * @createTime: 2017/2/22
 */
public class JkChatMessageDaoWrapper {
    // sington
    private static JkChatMessageDaoWrapper instance;
    // 健客聊天消息Dao
    private JkChatMessageDao mJkChatMessageDao;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param context 引用
     * @return
     */
    private JkChatMessageDaoWrapper(Context context){
        // init jk chat message dao
        mJkChatMessageDao = JkChatDaoManager.getInstance(
                context.getApplicationContext()).getDaoSession().getJkChatMessageDao();
    }

    /**
     * sington
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param context 引用（此处传入application引用防止内存泄漏）
     * @return
     */
    public static JkChatMessageDaoWrapper getInstance(Context context){
        if (instance == null){
            synchronized(JkChatMessageDaoWrapper.class){
                if (instance == null){
                    instance = new JkChatMessageDaoWrapper(context);
                }
            }
        }
        return instance;
    }
}
