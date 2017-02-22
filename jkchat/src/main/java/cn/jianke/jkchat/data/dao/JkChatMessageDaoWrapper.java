package cn.jianke.jkchat.data.dao;

import android.content.Context;
import android.util.Log;

import com.jk.chat.gen.JkChatMessageDao;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.ArrayList;
import java.util.List;

import cn.jianke.jkchat.common.JsonUtils;
import cn.jianke.jkchat.common.StringUtils;
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

    /**
     * 根据会话id移除消息id
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param cid 会话id
     * @return
     */
    public void removeTidByCid(String cid){
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
                    mJkChatMessage.setTid("");
                    mJkChatMessageDao.update(mJkChatMessage);
                }
            }
        }
    }

    /**
     * 部分数据查询
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param tid 消息id
     * @param limit 最大值
     * @param offset 偏移
     * @return
     */
    public List<JkChatMessage> findByPart(String tid, int limit, int offset){
        List<JkChatMessage> result = new ArrayList<JkChatMessage>();
        if (mJkChatMessageDao != null){
            QueryBuilder jkMsgQb = mJkChatMessageDao.queryBuilder();
            List<JkChatMessage> mJkChatMessageList  =
                    // 查询条件为tip
                    jkMsgQb.where(JkChatMessageDao.Properties.Tid
                            .eq(tid))
                            // 查询数据最大值
                            .limit(limit)
                            // 偏移
                            .offset(offset)
                            // 按时间降序排序
                            .orderDesc(JkChatMessageDao.Properties.Time)
                            // 返回查询结果
                            .list();
            if (mJkChatMessageList != null
                    && mJkChatMessageList.size() != 0) {
                // 遍历数据并更新数据
                for (JkChatMessage mJkChatMessage: mJkChatMessageList){
                    result.add(0, mJkChatMessage);
                }
            }
        }
        return result;
    }

    /**
     * 获取上一个消息id
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param
     * @return
     */
    public String getLastTid(){
        String tid = null;
        List<JkChatMessage> result = new ArrayList<JkChatMessage>();
        if (mJkChatMessageDao != null){
            QueryBuilder jkMsgQb = mJkChatMessageDao.queryBuilder();
            List<JkChatMessage> mJkChatMessageList  =
                    // 按时间降序排序
                    jkMsgQb.orderDesc(JkChatMessageDao.Properties.Time)
                            // 返回查询结果
                            .list();
            if (mJkChatMessageList != null
                    && mJkChatMessageList.size() != 0) {
                // 遍历数据并更新数据
                for (JkChatMessage mJkChatMessage: mJkChatMessageList){
                    String tmpTid = mJkChatMessage.getTid();
                    if (StringUtils.isNotEmpty(tmpTid)){
                        tid = tmpTid;
                        break;
                    }
                }
            }
        }
        return tid;
    }

    /**
     * 保存消息
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param msg 健客聊天消息
     * @return
     */
    public void saveMsg(JkChatMessage msg){
        if (mJkChatMessageDao != null){
            mJkChatMessageDao.insert(msg);
        }
    }

    /**
     * 更新消息
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param msg 健客聊天消息
     * @return
     */
    public void updateMsg(JkChatMessage msg){
        if (mJkChatMessageDao != null){
            mJkChatMessageDao.update(msg);
        }
    }

    /**
     * 添加网络图片地址
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param isSuccess 是否发送成功
     * @param id 自增id
     * @param remoteUrl 网络图片地址
     * @return
     */
    public void addRemoteImgUrl(boolean isSuccess, int id, String remoteUrl) {
        if (mJkChatMessageDao != null){
            QueryBuilder jkMsgQb = mJkChatMessageDao.queryBuilder();
            List<JkChatMessage> mJkChatMessageList  =
                    // 查询条件自增id
                    jkMsgQb.where(JkChatMessageDao.Properties.Id.eq(id))
                            // 按时间降序排序
                            .orderDesc(JkChatMessageDao.Properties.Time)
                            // 返回查询结果
                            .list();
            if (mJkChatMessageList != null
                    && mJkChatMessageList.size() == 1) {
                JkChatMessage newJkChatMessage = mJkChatMessageList.get(0);
                newJkChatMessage.setRemoteUrl(remoteUrl);
                newJkChatMessage.setMsg(remoteUrl);
                if (isSuccess)
                    newJkChatMessage.setStatus(JkChatMessage.STATUS_SUCCESS);
                else
                    newJkChatMessage.setStatus(JkChatMessage.STATUS_FAIL);
                // 更新数据
                mJkChatMessageDao.update(newJkChatMessage);
            }
        }
    }

    /**
     * 添加多张网络图片集合
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param isSuccess 是否发送成功
     * @param id 自增id
     * @param remoteUrls 网络图片地址集合
     * @return
     */
    public void addMoreImgRemoteUrl(boolean isSuccess, int id, ArrayList remoteUrls) {
        if (mJkChatMessageDao != null){
            QueryBuilder jkMsgQb = mJkChatMessageDao.queryBuilder();
            List<JkChatMessage> mJkChatMessageList  =
                    // 查询条件自增id
                    jkMsgQb.where(JkChatMessageDao.Properties.Id.eq(id))
                            // 按时间降序排序
                            .orderDesc(JkChatMessageDao.Properties.Time)
                            // 返回查询结果
                            .list();
            if (mJkChatMessageList != null
                    && mJkChatMessageList.size() == 1) {
                JkChatMessage newJkChatMessage = mJkChatMessageList.get(0);
                // 将ArrayList转Json字符串
                String remoteUrlsJson = JsonUtils.list2Json(remoteUrls);
                newJkChatMessage.setRemoteUrlsJson(remoteUrlsJson);
                newJkChatMessage.setMsg(remoteUrlsJson);
                if (isSuccess)
                    newJkChatMessage.setStatus(JkChatMessage.STATUS_SUCCESS);
                else
                    newJkChatMessage.setStatus(JkChatMessage.STATUS_FAIL);
                // 更新数据
                mJkChatMessageDao.update(newJkChatMessage);
            }
        }
    }

    /**
     * 根据自增id获取图片消息
     * @author leibing
     * @createTime 2017/2/22
     * @lastModify 2017/2/22
     * @param id 自增id
     * @return
     */
    public JkChatMessage getImgMsg(int id) {
        JkChatMessage result = null;
        if (mJkChatMessageDao != null){
            QueryBuilder jkMsgQb = mJkChatMessageDao.queryBuilder();
            List<JkChatMessage> mJkChatMessageList  =
                    // 查询条件自增id
                    jkMsgQb.where(JkChatMessageDao.Properties.Id.eq(id))
                            // 按时间降序排序
                            .orderDesc(JkChatMessageDao.Properties.Time)
                            // 返回查询结果
                            .list();
            if (mJkChatMessageList != null
                    && mJkChatMessageList.size() == 1) {
                result =mJkChatMessageList.get(0);
            }
        }
        return result;
    }
}
