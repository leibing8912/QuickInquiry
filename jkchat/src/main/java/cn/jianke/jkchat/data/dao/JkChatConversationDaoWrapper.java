package cn.jianke.jkchat.data.dao;

import android.content.Context;
import com.jk.chat.gen.JkChatConversationDao;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;
import cn.jianke.jkchat.domain.JkChatConversation;

/**
 * @className: JkChatConversationDaoWrapper
 * @classDescription: 健客聊天会话数据库包装（封装一些常用方法）
 * @author: leibing
 * @createTime: 2017/2/22
 */
public class JkChatConversationDaoWrapper {
    // sington
    private static JkChatConversationDaoWrapper instance;
    // 健客聊天会话Dao
    private JkChatConversationDao mJkChatConversationDao;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param context 引用
     * @return
     */
    private JkChatConversationDaoWrapper(Context context){
        // init jk chat conversation dao
        mJkChatConversationDao = JkChatDaoManager.getInstance(
                context.getApplicationContext()).getDaoSession().getJkChatConversationDao();
    }

    /**
     * sington
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param context 引用（此处传入application引用防止内存泄漏）
     * @return
     */
    public static JkChatConversationDaoWrapper getInstance(Context context){
        if (instance == null){
            synchronized(JkChatMessageDaoWrapper.class){
                if (instance == null){
                    instance = new JkChatConversationDaoWrapper(context);
                }
            }
        }
        return instance;
    }

    /**
     * 获取数据库中最新一条会话信息(按时间降序)
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param
     * @return 会话信息
     */
    public JkChatConversation findLastConversation(){
        JkChatConversation result = null;
        if (mJkChatConversationDao != null) {
            QueryBuilder jkCtQb = mJkChatConversationDao.queryBuilder();
            List<JkChatConversation> mJkChatConversationList =
                    // 按时间降序排序
                    jkCtQb.orderDesc(JkChatConversationDao
                            .Properties.ConversationCreateTime)
                            // 只查询一条数据
                            .limit(1)
                            // 返回查询结果
                            .list();
            if (mJkChatConversationList != null
                    && mJkChatConversationList.size() == 1) {
                // 获取数据库中最新聊天会话数据
                result = mJkChatConversationList.get(0);
            }
        }
        return result;
    }

    /**
     * 获取数据库中最新一条会话消息中的状态
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param
     * @return 会话状态
     */
    public int getLastConversationStatus() {
        JkChatConversation result = findLastConversation();
        if (result != null){
            return result.getStatus();
        }

        return JkChatConversation.STATUS_NULL;
    }
}
