package cn.jianke.jkchat;

import android.text.TextUtils;
import java.util.Locale;
import cn.jianke.jkchat.common.RequestUrlUtils;
import cn.jianke.jkchat.common.StringUtils;
import cn.jianke.jkchat.httprequest.JkChatRequest;
import okhttp3.Response;

/**
 * @className: JkChatApiImpl
 * @classDescription: 健客聊天api实现
 * @author: leibing
 * @createTime: 2017/2/17
 */
public class JkChatApiImpl implements JkChatApi{
    // 用户id
    private String userId;
    // 健客聊天api监听
    private JkChatApiListener mJkChatApiListener;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param userId 用户id
     * @return
     */
    public JkChatApiImpl(String userId){
        this.userId = userId;
    }

    @Override
    public void setJkChatApiListener(JkChatApiListener mJkChatApiListener) {
        this.mJkChatApiListener = mJkChatApiListener;
    }

    @Override
    public void requestGetDoctorNum() {
        // 向服务器获取医生在线数量
        JkChatRequest.getInstance().getAsynHttp(RequestUrlUtils.GET_DOCTOR_NUM_URL,
                new JkChatRequest.ResponseCallBack() {
            @Override
            public void onSuccess(Response response) {
                if (response != null && response.body() != null){
                    String content = response.body().toString();
                    // 判断字符串是否为空
                    if (!TextUtils.isEmpty(content)){
                        // 判断字符串是否数字
                        if (StringUtils.strIsNum(content)){
                            // 字符串转整型
                            int doctorNum = Integer.parseInt(content);
                            if (mJkChatApiListener != null){
                                // 回调医生数量
                                mJkChatApiListener.onReponseGetDoctorNum(doctorNum);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (mJkChatApiListener != null){
                    // 回调获取医生数量异常信息
                    mJkChatApiListener.onGetDoctorNumError(e);
                }
            }
        });
    }

    @Override
    public void checkUserAskingNow() {
        // 拼接判断当前用户是否在咨询中请求url地址
        String checkAskingNowUrl = String.format(Locale.getDefault(),
                RequestUrlUtils.CHECK_ASKING_NOW_URL, userId);
        // 向服务器请求数据判断当前用户是否在咨询中
        JkChatRequest.getInstance().getAsynHttp(checkAskingNowUrl,
                new JkChatRequest.ResponseCallBack() {
            @Override
            public void onSuccess(Response response) {
                if (response != null && response.body() != null){
                    String content = response.body().toString();
                    // 判断字符串是否为空
                    if (!TextUtils.isEmpty(content)){
                        // 判断字符串是否数字
                        if (StringUtils.strIsNum(content)){
                            // 字符串转整型
                            int askingFlag = Integer.parseInt(content);
                            if (mJkChatApiListener != null){
                                // 回调咨询标识
                                mJkChatApiListener.onCheckUserAskingNow(askingFlag);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (mJkChatApiListener != null){
                    // 回调判断当前用户是否在咨询中异常信息
                    mJkChatApiListener.onGetDoctorNumError(e);
                }
            }
        });
    }

    @Override
    public void cancel() {
        // 取消所有请求
        JkChatRequest.getInstance().cancelAllRequest();
    }
}
