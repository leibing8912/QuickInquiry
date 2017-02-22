package cn.jianke.jkchat;

import cn.jianke.jkchat.domain.JkChatMessage;
import cn.jianke.jkchat.domain.JkChatSession;

/**
 * @interfaceName: JkChatConnectionListener
 * @interfaceDescription: 健客聊天连接监听接口
 * @author: leibing
 * @createTime: 2017/2/17
 */
public interface JkChatConnectionListener {

    /**
     * invoke when web socket is open
     * 当websocket打开的时候调用
     * @param
     */
    void onOpen();

    /**
     * invoke when web socket is closed
     * 当websocket关闭的时候调用
     * @param code 关闭码
     * @param reason 原因
     */
    void onClose(int code, String reason);

    /**
     * invoke when total number of available customer service people changed
     * 当可用的客户服务人员的总数改变时调用
     * @param number 可用客户服务人员数目
     */
    void onStuffNumChanged(int number);

    /**
     * invoke when received a tid
     * 当接收一个tid时调用
     * @param session 健客聊天会话消息
     */
    void onSessionChanged(JkChatSession session);

    /**
     * invoke when receive a normal message
     * 接收一条正常消息时调用
     * @param message 健客聊天消息实体
     */
    void onReceiveMessage(JkChatMessage message);

    /**
     * invoke when the client is closed
     * 当客户端关闭时调用
     * @param
     */
    void onClientClosed();

    /**
     * invoke when customer service people offline
     * 当客服人员离线时调用
     * @param
     */
    void onStuffOffline();

    /**
     * invoke when customer service people put you into waiting list
     * 当客服人员把你放在等候名单上时调用
     * @param
     */
    void onChangetoWait();

    /**
     * invoke when get offline message if the chat is end
     * 如果聊天结束得到离线消息
     * @param
     */
    void onChatClosed();

    /**
     * invoke when is told chat still alive on server
     * 当被告诉聊天在服务端依然活着时被调用
     * @param
     */
    void onChatStillAlive();

    /**
     * invoke when get server message done.
     * socket连接后，服务器处理完会回调此方法
     * @param
     */
    void onGetServerMessageDone();

    /**
     * 用户发送包含敏感词，则回调此方法
     * @param
     */
    void onFilterAskQuestion();

    /**
     * 禁止用户提问，则回调此方法
     * @param
     */
    void onForbidAskQuestion();

    /**
     * 等待提示
     * @param message 健客聊天消息实体
     */
    void onWaitForAnswer(JkChatMessage message);
}
