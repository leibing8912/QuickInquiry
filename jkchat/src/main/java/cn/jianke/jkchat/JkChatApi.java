package cn.jianke.jkchat;

/**
 * @interfaceName: JkChatApi
 * @interfaceDescription: 健客聊天api
 * @author: leibing
 * @createTime: 2017/2/17
 */
public interface JkChatApi {
    /**
     * 设置健客聊天api监听
     * @param mJkChatApiListener 健客聊天api监听
     */
    void setJkChatApiListener(JkChatApiListener mJkChatApiListener);

    /**
     * 请求获取医生数量
     * @param
     */
    void requestGetDoctorNum();

    /**
     * 判断用户是否正在咨询中
     * @param
     */
    public void checkUserAskingNow();

    /**
     * 取消
     * @param
     */
    public void cancel();
}
