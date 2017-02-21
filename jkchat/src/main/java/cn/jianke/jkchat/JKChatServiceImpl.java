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
import cn.jianke.jkchat.domain.JkChatConversation;
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
    // 常见对医生说谢谢的话
    private final static String[] thanks = {"谢谢医生", "谢了",
            "谢谢您", "非常感谢", "下次还找你"};
    // 连接状态
    private String connectStatus = INIT;
    // Context weak Ref
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
    // 会话是否结束
    private boolean isConversationFinished = false;
    // 医生客户端关闭监听
    private OnClientClosedListener mOnClientClosedListener;
    // 健客聊天消息列表
    public static List<JkChatMessage> tmpMessages = new ArrayList<JkChatMessage>();
    // 用户id
    private String customSessionID = "Unknow_CustomSessionID";
    // 客服id
    private String staffSessionID = "Unknow_StaffSesionID";
    // 健客聊天会话
    private JkChatConversation jkCurrentConversation;
    // 是否完成首次打开fragment时，对服务器的请求 保证请求完服务器信息才允许发送信息
    // 因为考虑到网速慢，导致发送消息的时候，第一次请求还没响应，破坏了期望的连接状态
    private boolean isFinishedFirstRequest = false;
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
            // 设置标识用户会话状态
            setAskingFlag(askingFlag);
        }

        @Override
        public void onCheckUserAskingNowError(Exception e) {
            // 网络失败回调（网络连接失败）
            if (mJkChatServiceListener != null)
                mJkChatServiceListener.onNetworkUnavaiable(NETWORK_CONNECTION_FAILED);
        }
    };

    // 健客聊天连接监听
    private JkChatConnectionListener mJkChatConnectionListener = new JkChatConnectionListener() {
        @Override
        public void onOpen() {
            // 设置会话未结束
            isConversationFinished = false;
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

            try {
                // 设置连接状态为断开
                connectStatus = DISCONECTED;
                // 设置会话已结束
                isConversationFinished = true;
                // 设置已完成第一次请求
                isFinishedFirstRequest = true;
            } catch (Exception e) {
            } finally {
                if (code != WebSocket.ConnectionHandler.CLOSE_NORMAL
                        && code != WebSocket.ConnectionHandler.CLOSE_CONNECTION_LOST) {
                    // 若连接状态非连接中或已连上则回调服务器连接失败
                    if (connectStatus != CONNECTING
                            || connectStatus != CONNECTED) {
                        if (mJkChatServiceListener != null)
                            mJkChatServiceListener.onNetworkUnavaiable(SERVER_CONNECTION_FAILED);
                    }
                }
            }
        }

        @Override
        public void onStuffNumChanged(int number) {
            // 再次判断医生在线数目
            // 设置医生在线数目
            setDoctorNum(number);
            // 设置聊天连接状态为正在连接中
            connectStatus = CONNECTING;
            // 若页面实例引用为空则返回
            if (mContextWeakRef == null || mContextWeakRef.get() == null)
                return;
            if (number < 1){
                // 发送系统消息
                sendSystemText(mContextWeakRef.get().getResources().getString(
                        R.string.chat_few_keyword_doctor_offline_tips));
            }
        }

        @Override
        public void onSessionChanged(JkChatSession session) {
            // 设置聊天连接状态为已经连接上
            connectStatus = CONNECTED;
            // 若session为空则返回
            if (session == null)
                return;
            // 若页面实例引用为空则返回
            if (mContextWeakRef == null || mContextWeakRef.get() == null)
                return;
            // 有医生接受通话时发送的系统消息
            sendSystemText(mContextWeakRef.get().getResources().getString(
                    R.string.chat_doctor_answering_pre)
                    + session.getStaffName()
                    + mContextWeakRef.get().getResources().getString(
                    R.string.chat_doctor_answering));
            // 客服名称、接收消息回调
            if (mJkChatServiceListener != null){
                // 客服名称回调
                mJkChatServiceListener.onStuffChanged(session.getStaffName());
                // 组建健客聊天消息
                JkChatMessage chatMessage = new JkChatMessage();
                chatMessage.setMsg("");
                chatMessage.setTid(session.getTid());
                chatMessage.setMsgType("");
                // 接收消息回调
                mJkChatServiceListener.onReceivedMessage(chatMessage);
            }
        }

        @Override
        public void onReceiveMessage(JkChatMessage message) {
            // 回调医生发来的消息
            if (mOnClientClosedListener != null){
                mOnClientClosedListener.onReceiveMessage(message);
            }
            if (mJkChatServiceListener != null)
                mJkChatServiceListener.onReceivedMessage(message);
        }

        @Override
        public void onClientClosed() {
            // 若页面实例引用为空则返回
            if (mContextWeakRef == null || mContextWeakRef.get() == null)
                return;
            // 发送系统消息
            sendSystemText(mContextWeakRef.get().getResources().getString(
                    R.string.chat_ending));
            // 医生客户端关闭时回调或是客户主动关闭在线咨询时回调
            if (mOnClientClosedListener != null)
                mOnClientClosedListener.OnClientClosed();
        }

        @Override
        public void onStuffOffline() {
            // 若页面实例引用为空则返回
            if (mContextWeakRef == null || mContextWeakRef.get() == null)
                return;
            // 发送系统消息
            sendSystemText(mContextWeakRef.get().getResources().getString(
                    R.string.chat_doctor_lost_conn));
        }

        @Override
        public void onChangetoWait() {
            // 医生把提问者放到等待应答列表，则上一次的tid作废，
            // 到了再次有医生应答时，即回调onSessionChanged时，会将之前tid为空的消息记录当作是本次会话的记录
            // 转换多个医生也是如此
            // 若页面实例引用为空则返回
            if (mContextWeakRef == null || mContextWeakRef.get() == null)
                return;
            // 发送系统消息
            sendSystemText(mContextWeakRef.get().getResources().getString(
                    R.string.chat_doctor_change_Wait));
        }

        @Override
        public void onChatClosed() {
            // 设置连接状态为正在连接中
            connectStatus = CONNECTING;
        }

        @Override
        public void onChatStillAlive() {
            // 设置连接状态为已经连接上
            connectStatus = CONNECTED;
        }

        @Override
        public void onGetServerMessageDone() {
            // 设置已完成第一次请求
            isFinishedFirstRequest = true;
        }

        @Override
        public void onFilterAskQuestion() {
            // 若页面实例引用为空则返回
            if (mContextWeakRef == null || mContextWeakRef.get() == null)
                return;
            // 发送系统消息
            sendSystemText(mContextWeakRef.get().getResources().getString(
                    R.string.chat_filter_word_tips));
        }

        @Override
        public void onForbidAskQuestion() {
            // 设置禁止聊天
            isForbidAsk = true;
            // 若页面实例引用为空则返回
            if (mContextWeakRef == null || mContextWeakRef.get() == null)
                return;
            // 发送系统消息
            sendSystemText(mContextWeakRef.get().getResources().getString(
                    R.string.chat_limit_ip));
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
        // 图片消息不用过滤，直接发送
        if (!msg.getMsgType().equalsIgnoreCase(JkChatMessage.TYPE_IMG)) {
            int result = filterMessage(msg);
            // 若不可发送文字则返回
            if (result != SENDSTATUS_CAN_SEND) {
                return result;
            }
        }
        // 根据连接状态处理消息发送
        switch (connectStatus){
            case INIT:
            case DISCONECTED:
                // 连接并发送消息
                connectAndSendMessage(msg);
                return SENDSTATUS_FILTERED;
            case CONNECTING:
                // 医生未应答时，消息加到消息队列，
                // 当应答后回调了onOpen时，将遍历消息队列，发送给医生
                addMsgAndFilterRepeatMsg(msg);
//                sendAndSaveMsgByLoop();
                return SENDSTATUS_FILTERED;
            case CONNECTED:
                sendAndSaveMessage(msg);
                break;
            default:
                break;
        }

        return SENDSTATUS_CAN_SEND;
    }

    @Override
    public void setListener(JkChatServiceListener listener) {
        // 设置健客聊天服务监听
        this.mJkChatServiceListener = listener;
    }

    @Override
    public void setConfig(JkChatConfig config) {
        // 设置健客聊天配置
        this.mJkChatConfig = config;
    }

    @Override
    public boolean isFinishedFirstRequest() {
        return isFinishedFirstRequest;
    }

    @Override
    public boolean isConversationFinished() {
        return isConversationFinished;
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
        if (connectStatus == CONNECTED)
            return true;
        else
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

    /**
     * 过滤消息
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param  message 健客聊天消息
     * @return
     */
    private int filterMessage(JkChatMessage message) {
        // 默认可以发送文字
        int result = SENDSTATUS_CAN_SEND;
        // 对话结束后，再次发送消息，先执行connectStatus = ConnectStatus.INIT;
        if (isConversationFinished && (connectStatus != INIT)) {
            connectStatus = INIT;
            // 医生结束对话后,原则上是不会进入这里来的
            // 用户发送语句中包含“谢”字，且字数在10个字以内。常见语句有“谢谢医生”、“谢了”、“谢谢您”、“非常感谢，下次还找你”等等。
            String msg = message.getMsg();
            if (msg.length() < 10) {
                for (int i = 0; i < thanks.length; i++) {
                    if (msg.contains(thanks[i])) {
                        if (mJkChatServiceListener != null) {
                            mJkChatServiceListener.onReceivedMessage(message);
                            // 若页面实例引用为空则返回
                            if (mContextWeakRef != null && mContextWeakRef.get() != null) {
                                sendSystemText(mContextWeakRef.get().getResources().getString(
                                        R.string.chat_thx2doctor));
                            }
                            // 相当于主动结束对话
                            if (mJkChatConnection != null)
                                mJkChatConnection.clientPositionClose(message.getTid());
                            // 清除EditText文字
                            result = SENDSTATUS_CLEAN_ET;
                            return result;
                        }
                    }
                }
            }
        }
        // 没有建立对话前，判断字数过滤关键词之类的
        if (connectStatus == INIT) {
            String msg = message.getMsg();
            // 没有建立对话前，判断字数过滤关键词之类的
            if ((msg.contains("在吗") || msg.contains("有人吗")
                    || msg.contains("你好") || msg.contains("您好")
                    || msg.contains("hi") || msg.contains("hello"))
                    && msg.length() < 10) {
                // 若禁止聊天则返回禁止提问
                if (isForbidAsk) {
                    return SENDSTATUS_FORBID;
                }
                // 提醒医生是否在线提示（系统提示）
                if (mJkChatApi != null)
                    mJkChatApi.requestGetDoctorNum();
                // 设置被过滤状态
                result = SENDSTATUS_FILTERED;
            } else if (msg.length() < 10) {
                // 若禁止聊天则返回禁止提问
                if (isForbidAsk) {
                    return SENDSTATUS_FORBID;
                }
                // 设置被过滤状态
                result = SENDSTATUS_FILTERED;
            }
        }
        // 若禁止聊天则返回禁止提问
        if (isForbidAsk) {
            result = SENDSTATUS_FORBID;
        }
        return result;
    }

    /**
     * 过滤相同消息
     * 防止在等待服务器响应时，多次点击发送，造成多条相同的消息
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param message 健客聊天消息
     * @return
     */
    private void addMsgAndFilterRepeatMsg(JkChatMessage message) {
        // 图片消息不是通过点击发送按钮发送的，所以不用过滤
        if (message.getMsgType().equalsIgnoreCase(JkChatMessage.TYPE_IMG)) {
            tmpMessages.add(message);
            return;
        }
        if (tmpMessages.size() != 0) {
            for (int i = 0; i < tmpMessages.size(); i++) {
                if (tmpMessages.get(i).getMsg().equals(message.getMsg())) {
                    break;
                }
                if (i == tmpMessages.size() - 1) {
                    tmpMessages.add(message);
                }
            }
        } else {
            tmpMessages.add(message);
        }
    }

    /**
     * 连接并发送消息
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param message 健客聊天消息
     * @return
     */
    private void connectAndSendMessage(JkChatMessage message) {
        // 过滤相同消息
        addMsgAndFilterRepeatMsg(message);
        // 若连接状态为非正在连接中或已经连接上则需连接健客聊天服务器
        if (connectStatus != CONNECTING
                || connectStatus != CONNECTED) {
            jkConnection();
        }
    }

    /**
     * 发送并保存消息
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param message 健客聊天消息
     * @return
     */
    private boolean sendAndSaveMessage(JkChatMessage message) {
        boolean sendMsgSucessed = localSendMessage(message);
        if (sendMsgSucessed) {
//            saveMessage(message);
        }
        return sendMsgSucessed;
    }

    /**
     * 发送聊天信息
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param message 健客聊天信息
     * @return
     */
    private boolean localSendMessage(JkChatMessage message) {
        if (mJkChatConnection == null)
            return false;
        // 给消息设置用户id
        message.setCustomSessionID(customSessionID);
        // 给消息设置客服id
        message.setStaffSessionID(staffSessionID);
        // 给消息设置消息id
        if (jkCurrentConversation != null)
            message.setTid(jkCurrentConversation.getTid());
        return mJkChatConnection.sendMessage(message);
    }

    /**
     * 设置医生客户端关闭监听
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public void setOnClientClosedListener(OnClientClosedListener mOnClientClosedListener) {
        this.mOnClientClosedListener = mOnClientClosedListener;
    }
    
    /**
     * @interfaceName: OnClientClosedListener
     * @interfaceDescription: 医生客户端关闭的时候调用
     * @author: leibing
     * @createTime: 2017/2/21
     */
    public interface OnClientClosedListener {
        /**
         * 医生客户端关闭的时候回调或是客户主动关闭在线咨询的时候用
         * @param
         */
        void OnClientClosed();

        /**
         * 医生发送消息过来的时候回调
         * @param message 医生消息
         */
        void onReceiveMessage(JkChatMessage message);
    }
}
