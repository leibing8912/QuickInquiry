package cn.jianke.jkchat.domain;

import java.util.UUID;

/**
 * @className: JkChatConversation
 * @classDescription: 健客聊天会话
 * @author: leibing
 * @createTime: 2017/2/17
 */
public class JkChatConversation {
    // 状态--初始化
    public final static int STATUS_INIT = 0x0;
    // 状态--等待中
    public final static int STATUS_WAITING = 0x1;
    // 状态--聊天中
    public final static int STATUS_CHATING = 0x2;
    // 状态--完成
    public final static int STATUS_FINISHED = 0x3;
    // 状态--空
    public final static int STATUS_NULL = 0x4;
    // 数据库自增id
    private int id;
    // 会话id
    private String cid;
    // 消息id
    private String tid;
    // 状态
    private int status;
    // 会话创建时间
    private long conversationCreateTime;
    // accessToken
    private String accesstoken;
    // 是否登录
    private String isLogin;

    /**-----------------------------------------获取会话-----------------------------------------
     --------------------------------------------------------------------------------------------*/
    /**
     * 获取会话（初始化状态）
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param
     * @return
     */
    public static JkChatConversation newConversation() {
        JkChatConversation conversation = new JkChatConversation();
        conversation.setStatus(STATUS_INIT);
        conversation.setConversationCreateTime(System.currentTimeMillis());
        conversation.setCid(UUID.randomUUID().toString());
        return conversation;
    }

    /**-----------------------------------------get and set---------------------------------------
     --------------------------------------------------------------------------------------------*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getConversationCreateTime() {
        return conversationCreateTime;
    }

    public void setConversationCreateTime(long conversationCreateTime) {
        this.conversationCreateTime = conversationCreateTime;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }
}
