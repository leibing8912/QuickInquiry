package cn.jianke.jkchat;

import cn.jianke.jkchat.domain.JkChatMessage;

/**
 * @className: JkChatConnection
 * @classDescription: 健客聊天连接接口
 * @author: leibing
 * @createTime: 2017/2/20
 */
public interface JkChatConnection {

    /**
     * set the chat listener
     * 设置聊天监听
     * @param listener 聊天监听
     */
    void setListener(JkChatConnectionListener listener);

    /**
     * connect to the chat websocket
     * 连接聊天websocket
     * @param username 用户名
     * @param clientId 客户id
     * @param fileId 文件id
     * @param name 名称
     * @param sex 性别
     * @param age 年龄
     */
    void connect(String username, String clientId,
                 String fileId,String name,
                 String sex,String age) throws JkChatException;

    /**
     * connect / reconnect to the chat websocket
     * 连接或重新连接聊天websocket
     * @param username 用户名
     * @param clientId 客户id
     * @param tid 消息id
     * @param fileId 文件id
     * @param name 名称
     * @param sex 性别
     * @param age 年龄
     */
    void connect(String username, String clientId,
                 String tid,String fileId,
                 String name,String sex,String age) throws JkChatException;

    /**
     * disconnect from chat websocket
     * 断开聊天websocket连接
     * @param
     */
    void disconnect();

    /**
     *  get the websocket connected status
     *  获取websocket连接状态（判断是否连接）
     * @param
     */
    boolean isConnected();

    /**
     * send a message to chat websocket
     * 往聊天websocket发送一条信息
     * @param message 健客聊天消息
     */
    boolean sendMessage(JkChatMessage message);

    /**
     * 客户主动关闭在线咨询对话
     * @param tid  消息id
     * @return true：关闭成功 ，false：关闭失败
     */
    boolean clientPositionClose(String tid);
}
