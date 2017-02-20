package cn.jianke.jkchat.response;

/**
 * @className: JkReceiveMsgResponse
 * @classDescription: 健客聊天接收消息数据
 * @author: leibing
 * @createTime: 2017/2/20
 */
public class JkReceiveMsgResponse {
    // 消息类型（如：客服状态、会话ID、正常消息、转到等待应答......）
    public String Type = "";
    // 消息id
    public String TID = "";
    // 客服名称
    public String StaffName = "";
    // 用户id
    public String CustomSessionID = "";
    // 客服id
    public String StaffSessionID = "";
    // 消息内容
    public String Msg = "";
    // 消息id
    public String MsgId = "";
}
