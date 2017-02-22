package cn.jianke.jkchat.data.dao;

import android.content.Context;
import com.jk.chat.gen.JkChatConversationDao;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;
import cn.jianke.jkchat.domain.JkChatConversation;
import cn.jianke.jkchat.domain.JkChatMessage;

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
     * 获取数据库最新一条会话消息中的状态
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

    /**
     * 设置数据库最新一条会话消息中的状态
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param status 会话状态
     * @return
     */
    public void setLastConversationStatus(int status) {
        try {
            JkChatConversation mJkChatConversation = findLastConversation();
            if (mJkChatConversation != null){
                mJkChatConversation.setStatus(status);
                if (mJkChatConversationDao != null){
                    mJkChatConversationDao.update(mJkChatConversation);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 获取数据库最新一条会话消息中的是否登录标识
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param
     * @return
     */
    public String getLastConversationIsLogin() {
        String isLogin = null;
        try {
            JkChatConversation mJkChatConversation = findLastConversation();
            if (mJkChatConversation != null){
                isLogin = mJkChatConversation.getIsLogin();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return isLogin;
    }

    /**
     * 在数据库中根据cid更新tid，如果没有该记录时则创建
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param cid 会话id
     * @param tid 消息id
     * @param status 状态
     * @param createdTime 会话创建时间
     * @return
     */
    public void updataTidByCid(String cid, String tid, int status, long createdTime) {
        if (mJkChatConversationDao != null){
            QueryBuilder jkCtQb = mJkChatConversationDao.queryBuilder();
            List<JkChatConversation> mJkChatConversationList =
                    // 条件查询cid
                    jkCtQb.where(JkChatConversationDao.Properties.Cid.eq(cid))
                            // 返回查询结果
                            .list();
            if (mJkChatConversationList != null
                    && mJkChatConversationList.size() != 0) {
                // 更新会话
                for (JkChatConversation mJkChatConversation : mJkChatConversationList){
                    mJkChatConversation.setTid(tid);
                    mJkChatConversation.setStatus(status);
                    mJkChatConversation.setConversationCreateTime(createdTime);
                    // 更新数据
                    mJkChatConversationDao.update(mJkChatConversation);
                }
            }else {
                // 创建会话
                JkChatConversation newJkChatConversation = new JkChatConversation();
                newJkChatConversation.setCid(cid);
                newJkChatConversation.setTid(tid);
                newJkChatConversation.setStatus(status);
                newJkChatConversation.setConversationCreateTime(createdTime);
                // 插入数据
                mJkChatConversationDao.insert(newJkChatConversation);
            }
        }
    }

    /**
     * 保存会话消息
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param mJkChatConversation 健客聊天会话信息
     * @return
     */
    public void saveConversation(JkChatConversation mJkChatConversation){
        if (mJkChatConversationDao != null){
            mJkChatConversationDao.insert(mJkChatConversation);
        }
    }

    /**
     * 更新会话消息
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param mJkChatConversation 健客聊天会话信息
     * @return
     */
    public void updateConversation(JkChatConversation mJkChatConversation){
        if (mJkChatConversationDao != null){
            mJkChatConversationDao.update(mJkChatConversation);
        }
    }
}
