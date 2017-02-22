package cn.jianke.jkchat.data.dao;

import android.content.Context;
import com.jk.chat.gen.JkChatMessageDao;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;
import cn.jianke.jkchat.domain.JkChatMessage;

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

    /**
     * 获取数据库中最新一条发送方向的聊天消息(按时间降序)
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param
     * @return 会话信息
     */
    public JkChatMessage findLastMessage(){
        JkChatMessage result = null;
        if (mJkChatMessageDao != null) {
            QueryBuilder jkMsgQb = mJkChatMessageDao.queryBuilder();
            List<JkChatMessage> mJkChatMessageList  =
                    // 查询条件为消息方向----发送
                    jkMsgQb.where(JkChatMessageDao.Properties.Direct
                            .eq(JkChatMessage.DIRECT_SEND))
                            // 按时间降序排序
                            .orderDesc(JkChatMessageDao.Properties.Time)
                            // 只查询一条数据
                            .limit(1)
                            // 返回查询结果
                            .list();
            if (mJkChatMessageList != null
                    && mJkChatMessageList.size() == 1) {
                // 获取数据库中最新聊天会话数据
                result = mJkChatMessageList.get(0);
            }
        }
        return result;
    }

    /**
     * 根据会话id更改消息id
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param cid 会话id
     * @param tid 消息id
     * @return
     */
    public void modifyTidByCid(String cid, String tid){
        if (mJkChatMessageDao != null){
            QueryBuilder jkMsgQb = mJkChatMessageDao.queryBuilder();
            List<JkChatMessage> mJkChatMessageList  =
                    // 查询条件为cid
                    jkMsgQb.where(JkChatMessageDao.Properties.Cid
                            .eq(cid))
                            // 按时间降序排序
                            .orderDesc(JkChatMessageDao.Properties.Time)
                            // 返回查询结果
                            .list();
            if (mJkChatMessageList != null
                    && mJkChatMessageList.size() != 0) {
                // 遍历数据并更新数据
                for (JkChatMessage mJkChatMessage: mJkChatMessageList){
                    mJkChatMessage.setTid(tid);
                    mJkChatMessageDao.update(mJkChatMessage);
                }
            }
        }
    }
}
