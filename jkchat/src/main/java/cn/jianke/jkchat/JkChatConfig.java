package cn.jianke.jkchat;

/**
 * @className: JkChatConfig
 * @classDescription: 健客聊天配置类
 * @author: leibing
 * @createTime: 2017/2/17
 */
public class JkChatConfig {
    // 用户名
    private String username;
    // 客户id
    private String clientId;
    // 当前消息id
    private String tid;

    /**-----------------------------------------get and set---------------------------------------
     --------------------------------------------------------------------------------------------*/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
