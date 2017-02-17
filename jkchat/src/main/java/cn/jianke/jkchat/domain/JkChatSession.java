package cn.jianke.jkchat.domain;

/**
 * @className: JkChatSession
 * @classDescription: 健客聊天会话消息体（服务器发送过来的会话信息）
 * @author: leibing
 * @createTime: 2017/2/17
 */
public class JkChatSession {
    // 消息id
    private String tid;
    // 客服id
    private String staffSessionID;
    // 用户id
    private String customSessionID;
    // 客服名称
    private String staffName;

    /**-----------------------------------------get and set---------------------------------------
     --------------------------------------------------------------------------------------------*/
    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getStaffSessionID() {
        return staffSessionID;
    }

    public void setStaffSessionID(String staffSessionID) {
        this.staffSessionID = staffSessionID;
    }

    public String getCustomSessionID() {
        return customSessionID;
    }

    public void setCustomSessionID(String customSessionID) {
        this.customSessionID = customSessionID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
