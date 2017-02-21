package cn.jianke.jkchat;

import cn.jianke.jkchat.domain.JkChatMessage;

/**
 * @interfaceName: JkChatServiceListener
 * @interfaceDescription:健客聊天服务监听接口
 * @author: leibing
 * @createTime: 2017/2/21
 */

public interface JkChatServiceListener {

    /**
     * 接收消息
     * @param msg 健客聊天消息数据
     */
    void onReceivedMessage(JkChatMessage msg);

    /**
     * 当客服发生改变
     * @param staffName 客服名称
     */
    void onStuffChanged(String staffName);

    /**
     * 网络失败回调
     * @param type 网络失败参数（1：网络连接失败；2：服务器连接失败）
     */
    void onNetworkUnavaiable(int type);

    /**
     * 刷新数据
     * @param msg 健客聊天消息数据
     */
    void onRefreshData(JkChatMessage msg);

    /**
     * 根据type执行不同的命令
     * @param type 命令参数
     */
    void onCommand(int type);

    /**
     * 获取数据集的size
     * @param
     */
    int getDataSetSize();

    /**
     * 压缩图片保存到本地和磁盘缓存
     * @param originalPath 图片还没压缩的原来的地址
     * @param targetWidth 图片压缩后的宽度
     * @param targetHeight 图片压缩后的高度
     */
    void compressImgSaveLocalAndDisCache(String originalPath, int targetWidth, int targetHeight);
}
