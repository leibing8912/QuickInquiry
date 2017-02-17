package cn.jianke.jkchat;

/**
 * @interfaceName: JkChatApiListener
 * @interfaceDescription: 健客聊天api监听接口
 * @author: leibing
 * @createTime: 2017/2/17
 */
public interface JkChatApiListener {
    /**
     * 获取医生数量回调
     * @param doctorNum 医生数量
     */
    void onReponseGetDoctorNum(int doctorNum);
    /**
     *  获取医生数量错误回调
     * @param e 异常信息
     */
    void onGetDoctorNumError(Exception e);
    /**
     * 判断当前用户是否在咨询中
     * @param askingFlag 0表示没有通话，2表示通话中，1表示之前遗留的问题，重复提问一次
     */
    void onCheckUserAskingNow(int askingFlag);

    /**
     * 判断当前用户是否在咨询中异常信息回调
     * @param e 异常信息
     */
    void onCheckUserAskingNowError(Exception e);
}
