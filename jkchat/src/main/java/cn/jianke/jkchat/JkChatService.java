package cn.jianke.jkchat;

import java.util.ArrayList;
import java.util.List;
import cn.jianke.jkchat.domain.JkChatMessage;

/**
 * @interfaceName: JkChatService
 * @interfaceDescription: 健客聊天服务接口
 * @author: leibing
 * @createTime: 2017/2/21
 */

public interface JkChatService {

    /**
     * 聊天服务启动
     * @param
     */
    void start();

    /**
     * 聊天服务停止
     * @param
     */
    void stop();

    /**
     * 从上一个会话id读取消息
     * @param limit
     * @param offset
     * @return List<JkChatMessage>
     */
    List<JkChatMessage> readMesssages(int limit, int offset);

    /**
     * 根据消息id读取消息
     * @param tid
     * @param limit
     * @param offset
     * @return
     */
    List<JkChatMessage> readMesssages(String tid, int limit, int offset);

    /**
     * 获取上一个消息id
     * @param
     */
    String getLastTid();

    /**
     * 发送消息
     * @param msg 健客聊天消息数据
     */
    int sendMessage(JkChatMessage msg) ;

    /**
     * 设置健客聊天服务监听
     * @param listener 健客聊天服务监听
     */
    void setListener(JkChatServiceListener listener);

    /**
     * 设置健客聊天配置
     * @param config 健客聊天配置
     */
    void setConfig(JkChatConfig config);

    /**
     * 服务器是否响应完毕
     * @param
     * @return true表示服务器响应完毕，否者反之
     */
    boolean isFinishedFirstRequest();

    /**
     * 判断一次会话是否结束
     * @param
     * @return true表示一次会话结束，否者反之
     */
    boolean isConversationFinished();

    /**
     * 保存数据到数据库
     * @param msg 健客聊天消息数据
     */
    void saveMessage2Db(JkChatMessage msg);

    /**
     * 添加单张图片网络地址
     * @param isSuccess 是否发送成功
     * @param id 图片id
     * @param remoteUrl 网络图片地址
     */
    void addImgRemoteUrl(boolean isSuccess, int id, String remoteUrl);

    /**
     * 添加多张图片网络地址
     * @param isSuccess 是否发送成功
     * @param id 图片id
     * @param remoteUrls 网络图片地址集合
     */
    void addMoreImgRemoteUrl(boolean isSuccess, int id, ArrayList remoteUrls);

    /**
     * 根据图片id获取图片聊天消息数据
     * @param id 图片id
     */
    JkChatMessage getImgMsg(int id);

    /**
     * 获取连接状态
     * @param
     * @return 应答后返回true；否则false
     */
    boolean getConnectStatus();
}
