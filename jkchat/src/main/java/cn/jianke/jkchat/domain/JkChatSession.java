package cn.jianke.jkchat.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @className: JkChatSession
 * @classDescription: 健客聊天会话消息体（服务器发送过来的会话信息）
 * @author: leibing
 * @createTime: 2017/2/17
 */
@Entity
public class JkChatSession {
    // 消息id
    private String tid;
    // 客服id
    private String staffSessionID;
    // 用户id
    private String customSessionID;
    // 客服名称
    private String staffName;

    @Generated(hash = 1424495042)
    public JkChatSession(String tid, String staffSessionID, String customSessionID, String staffName) {
        this.tid = tid;
        this.staffSessionID = staffSessionID;
        this.customSessionID = customSessionID;
        this.staffName = staffName;
    }

    @Generated(hash = 298244542)
    public JkChatSession() {
    }

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
