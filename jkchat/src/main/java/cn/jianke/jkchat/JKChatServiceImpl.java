package cn.jianke.jkchat;

import android.content.Context;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import cn.jianke.jkchat.common.RequestUrlUtils;
import cn.jianke.jkchat.common.StringUtils;
import cn.jianke.jkchat.data.Shareperferences.PatientShareperferences;
import cn.jianke.jkchat.domain.JkChatMessage;
import cn.jianke.jkchat.domain.JkChatSession;
import cn.jianke.jkchat.httprequest.JkChatRequest;
import de.tavendo.autobahn.WebSocket;
import okhttp3.Response;

/**
 * @className: JKChatServiceImpl
 * @classDescription: 健客聊天服务实现
 * @author: leibing
 * @createTime: 2017/2/20
 */
public class JKChatServiceImpl implements JkChatService{
    // 被过滤了
    public final static int SENDSTATUS_FILTERED = 0;
    // 清除EditText的文字
    public final static int SENDSTATUS_CLEAN_ET = 1;
    // 可以发送文字
    public final static int SENDSTATUS_CAN_SEND = 2;
    // 禁止提问
    public final static int SENDSTATUS_FORBID = 3;
    // 网络失败回调----网络连接失败
    public final static int NETWORK_CONNECTION_FAILED = 1;
    // 网络失败回调----服务器连接失败
    public final static int SERVER_CONNECTION_FAILED = 2;
    // 连接状态----初始化
    private final static String INIT = "init";
    // 连接状态----正在连接中
    private final static String CONNECTING = "connecting";
    // 连接状态----已经连接上
    private final static String CONNECTED = "connected";
    // 连接状态----断开连接
    private final static String DISCONECTED = "disconected";
    // 连接状态
    private String connectStatus = INIT;
    // Context weakRef
    private WeakReference<Context> mContextWeakRef;
    // 健客聊天服务实现单例
    private static JKChatServiceImpl instance;
    // 健客聊天api
    private JkChatApi mJkChatApi;
    // 健客聊天连接
    private JkChatConnection mJkChatConnection;
    // 健客聊天配置
    private JkChatConfig mJkChatConfig;
    // 健客聊天服务监听
    private JkChatServiceListener mJkChatServiceListener;
    // 是否正在聊天中
    private boolean isChatRunning = false;
    // 在线医生数目
    private int doctorNum = 0;
    // 是否禁止聊天
    private boolean isForbidAsk = false;
    // 标识用户会话状态
    private int askingFlag;
    // 健客聊天api监听
    private JkChatApiListener mJkChatApiListener = new JkChatApiListener() {
        @Override
        public void onReponseGetDoctorNum(int doctorNum) {
            // 设置在线医生数目
            setDoctorNum(doctorNum);
            // 1、医生在线时给予系统提示： 您好，当前医生在线，请详细描述您的症状,不少于10个字。
            // 2、没有医生在线时给予系统提示：对不起，当前没有医生在线，请见谅！为方便您与医生及时沟通，请尽量在8:30am——21:00pm之间咨询！
            // 若页面实例引用为空则返回
            if (mContextWeakRef == null || mContextWeakRef.get() == null)
                return;
            // 发送系统消息
            if (doctorNum > 0){
                // 有医生在线
                sendSystemText(mContextWeakRef.get().getResources().getString(
                        R.string.chat_few_keyword_doctor_online_tips));
            }else {
                // 没有医生在线
                sendSystemText(mContextWeakRef.get().getResources().getString(
                        R.string.chat_few_keyword_doctor_offline_tips));
            }
        }

        @Override
        public void onGetDoctorNumError(Exception e) {
            // 网络失败回调（网络连接失败）
            if (mJkChatServiceListener != null)
                mJkChatServiceListener.onNetworkUnavaiable(NETWORK_CONNECTION_FAILED);
        }

        @Override
        public void onCheckUserAskingNow(int askingFlag) {

        }

        @Override
        public void onCheckUserAskingNowError(Exception e) {

        }
    };
    // 健客聊天连接监听
    private JkChatConnectionListener mJkChatConnectionListener = new JkChatConnectionListener() {
        @Override
        public void onOpen() {

        }

        @Override
        public void onClose(int code, String reason) {
            if (code == WebSocket.ConnectionHandler.CLOSE_CONNECTION_LOST
                    && mJkChatConfig != null){
                // 组建判断当前用户是否在咨询中请求url地址
                String checkAskingNowUrl  = String.format(Locale.getDefault(),
                        RequestUrlUtils.CHECK_ASKING_NOW_URL,
                        mJkChatConfig.getUsername());
                // 请求数据
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
                                    int askFlag = Integer.parseInt(content);
                                    if (askFlag != 0){
                                        jkConnection();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        }

        @Override
        public void onStuffNumChanged(int number) {

        }

        @Override
        public void onSessionChanged(JkChatSession session) {

        }

        @Override
        public void onReceiveMessage(JkChatMessage message) {

        }

        @Override
        public void onClientClosed() {

        }

        @Override
        public void onStuffOffline() {

        }

        @Override
        public void onChangetoWait() {

        }

        @Override
        public void onChatClosed() {

        }

        @Override
        public void onChatStillAlive() {

        }

        @Override
        public void onGetServerMessageDone() {

        }

        @Override
        public void onFilterAskQuestion() {

        }

        @Override
        public void onForbidAskQuestion() {

        }
    };

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/20
     * @lastModify 2017/2/20
     * @param context 引用
     * @param userId 用户id
     * @return
     */
    public JKChatServiceImpl(Context context, String userId){
        // init context weak ref
        mContextWeakRef = new WeakReference<Context>(context);
        // init jk chat api
        mJkChatApi = new JkChatApiImpl(userId);
        // set jk chat api listener
        mJkChatApi.setJkChatApiListener(mJkChatApiListener);
        // init jk chat connect
        mJkChatConnection = new JkChatConnectionImpl();
        // set jk chat connection listener
        mJkChatConnection.setListener(mJkChatConnectionListener);
    }

    /**
     * sington
     * @author leibing
     * @createTime 2017/2/20
     * @lastModify 2017/2/20
     * @param context 引用
     * @param userId 用户id
     * @return
     */
    public static JKChatServiceImpl getInstance(Context context, String userId){
        if (instance == null){
            synchronized (JKChatServiceImpl.class){
                if (instance == null){
                    instance = new JKChatServiceImpl(context, userId);
                }
            }
        }
        return instance;
    }

    /**
     * 连接健客聊天服务器
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    private void jkConnection(){
        if (mJkChatConnection != null
                && mContextWeakRef != null
                && mJkChatConfig != null
                && !mJkChatConnection.isConnected()){
            String fileId = PatientShareperferences.getInstance(
                    mContextWeakRef.get()).getPatientId();
            String name = PatientShareperferences.getInstance(
                    mContextWeakRef.get()).getPatientName();
            String sex = PatientShareperferences.getInstance(
                    mContextWeakRef.get()).getPatientSex();
            String age = PatientShareperferences.getInstance(
                    mContextWeakRef.get()).getPatientAge();
            // 若聊天连接参数有一个为空则返回
            if (StringUtils.isEmpty(fileId)
                    || StringUtils.isEmpty(name)
                    || StringUtils.isEmpty(sex)
                    || StringUtils.isEmpty(age)
                    || StringUtils.isEmpty(mJkChatConfig.getUsername())
                    || StringUtils.isEmpty(mJkChatConfig.getClientId()))
                return;
            try {
                mJkChatConnection.connect(mJkChatConfig.getUsername(),
                        mJkChatConfig.getClientId(),fileId,name,sex,age);
                // 更改连接状态为正在连接中
                connectStatus = CONNECTING;
            } catch (JkChatException e) {
                // 更改连接状态为断开连接
                connectStatus = DISCONECTED;
                // 网络失败回调（服务器连接失败）
                if (mJkChatServiceListener != null)
                    mJkChatServiceListener.onNetworkUnavaiable(SERVER_CONNECTION_FAILED);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        if (!isChatRunning){
            isChatRunning = true;
            // 连接健客聊天服务器
            jkConnection();
        }
    }

    @Override
    public void stop() {
        if (isChatRunning){
            isChatRunning = false;
            // 取消所有请求
            if (mJkChatApi != null)
                mJkChatApi.cancel();
            // 断开聊天连接
            if (mJkChatConnection != null
                    && mJkChatConnection.isConnected())
                mJkChatConnection.disconnect();
        }
    }

    @Override
    public List<JkChatMessage> readMesssages(int limit, int offset) {
        return null;
    }

    @Override
    public List<JkChatMessage> readMesssages(String cid, int limit, int offset) {
        return null;
    }

    @Override
    public String getLastTid() {
        return null;
    }

    @Override
    public int sendMessage(JkChatMessage msg) {
        return 0;
    }

    @Override
    public void setListener(JkChatServiceListener listener) {

    }

    @Override
    public void setConfig(JkChatConfig config) {
        // 设置健客聊天配置
        this.mJkChatConfig = config;
    }

    @Override
    public boolean isFinishedFirstRequest() {
        return false;
    }

    @Override
    public boolean isConversationFinished() {
        return false;
    }

    @Override
    public void saveMessage2Db(JkChatMessage msg) {

    }

    @Override
    public void addImgRemoteUrl(boolean finish, int id, String remoteUrl) {

    }

    @Override
    public void addMoreImgRemoteUrl(boolean finish, int id, ArrayList remoteUrls) {

    }

    @Override
    public JkChatMessage getImgMsg(int id) {
        return null;
    }

    @Override
    public boolean getConnectStatus() {
        return false;
    }

    /**
     * 设置在线医生数目
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public void setDoctorNum(int doctorNum) {
        this.doctorNum = doctorNum;
    }

    /**
     * 设置标识用户会话状态
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param askingFlag 用户会话状态标识
     * @return
     */
    public void setAskingFlag(int askingFlag) {
        this.askingFlag = askingFlag;
    }

    /**
     * 发送系统消息
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param content 消息内容
     * @return
     */
    public void sendSystemText(String content) {
        // 若页面实例引用为空则返回
        if (mContextWeakRef == null || mContextWeakRef.get() == null)
            return;
        if (!isForbidAsk
                && StringUtils.isNotEmpty(content)
                && !mContextWeakRef.get().getResources().getString(R.string.chat_limit_ip)
                .equals(content)) {
                // 未被禁止且不是“对不起，你已被限制提问”的时候才发送系统消息
                // 组建系统聊天消息
                JkChatMessage msg = JkChatMessage.newSystemMessage(content);
                // 回调接收消息
                if (mJkChatServiceListener != null) {
                    mJkChatServiceListener.onReceivedMessage(msg);
                }
        }else {
            // 组建系统聊天消息
            JkChatMessage msg = JkChatMessage.newSystemMessage(mContextWeakRef.get()
                    .getResources().getString(R.string.chat_limit_ip));
            // 回调接收消息
            if (mJkChatServiceListener != null)
                mJkChatServiceListener.onReceivedMessage(msg);
        }
    }
}
