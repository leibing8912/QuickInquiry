package cn.jianke.jkchat;

import android.text.TextUtils;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import cn.jianke.jkchat.common.RequestUrlUtils;
import cn.jianke.jkchat.common.StringUtils;
import cn.jianke.jkchat.domain.JkChatMessage;
import cn.jianke.jkchat.domain.JkChatSession;
import cn.jianke.jkchat.response.JkReceiveMsgResponse;
import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;

/**
 * @className: JkChatConnectionImpl
 * @classDescription: 健客聊天连接实现
 * @author: leibing
 * @createTime: 2017/2/20
 */
public class JkChatConnectionImpl implements JkChatConnection, WebSocket.ConnectionHandler {
    // 接收消息时判断是否为图片消息的标识
    public final static String MSG_TYPE_IMG_IDENTIFY = "<img src=";
    // 消息接收确认时消息内容
    public final static String MSG_ACK_MSG = "Msg";
    // 消息接收确认时消息id
    public final static String MSG_ACK_TID = "TID";
    // 消息接收确认时类型
    public final static String MSG_ACK_TYPE = "Type";
    // 消息接收确认时类型内容
    public final static String MSG_ACK_TYPE_CONTENT = "消息接收确认";
    // 消息类型----客服状态
    public final static String MSG_TYPE_SERVICE_STATUS = "客服状态";
    // 消息类型----会话ID
    public final static String MSG_TYPE_CONVERSATION_ID = "会话ID";
    // 消息类型----正常消息
    public final static String MSG_TYPE_NORMAL_MSG = "正常消息";
    // 消息类型----转到等待应答
    public final static String MSG_TYPE_CHANGETOWAIT = "转到等待应答";
    // 消息类型----新客户消息
    public final static String MSG_TYPE_NEW_CUSTOMER_MSG = "新客户消息";
    // 消息类型----关闭客户连接
    public final static String MSG_TYPE_CLOSE_CLIENT_CONNECTION = "关闭客户连接";
    // 消息类型----客服下线
    public final static String MSG_TYPE_SERVICE_OFFLINE = "客服下线";
    // 消息类型----用户提问过滤
    public final static String MSG_TYPE_USER_QUESTIONS_FILTER = "用户提问过滤";
    // 消息类型----禁止用户提问
    public final static String MSG_TYPE_BAN_USERS_ASK_QUESTIONS = "禁止用户提问";
    // 消息类型----连接处理完毕
    public final static String MSG_TYPE_CONNECT_THE_PROCESSED = "连接处理完毕";
    // 对话已结束
    public final static String CONVERSATION_IS_OVER = "0";
    // 对话未结束
    public final static String CONVERSATION_IS_NOT_OVER = "1";
    // 连接websokcet异常详细信息
    public final static String CONNECT_WEBSOCKET_CHATEXCEPTION_MSG
            = "jk chat websocket connect fail!";
    // 用户主动结束对话
    public final static String USER_INITIATES_THE_CONVERSATION = "用户主动结束对话";
    // websocket实例
    private WebSocket mWebSocket;
    // 健客聊天连接监听
    private JkChatConnectionListener mListener;
    // Gson实例，用于解析gson数据到响应对象
    private Gson mGson;
    // 是否关闭咨询对话
    private boolean isClosedSucess = false;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/20
     * @lastModify 2017/2/20
     * @param
     * @return
     */
    public JkChatConnectionImpl(){
        // init websocket
        mWebSocket = new WebSocketConnection();
        // init Gson
        mGson = new Gson();
    }

    /**
     * 消息确认
     * @author leibing
     * @createTime 2017/2/20
     * @lastModify 2017/2/20
     * @param msgId 消息id
     * @param type 消息类型
     * @param msg 消息内容
     * @return
     */
    private String ackMsg(String msgId, String type, String msg){
        Map ackMap = new HashMap();
        ackMap.put(MSG_ACK_TID, msgId);
        ackMap.put(MSG_ACK_TYPE, type);
        ackMap.put(MSG_ACK_MSG, msg);
        return StringUtils.mapToJson(ackMap);
    }

    @Override
    public void onOpen() {
        // 连接健客聊天websocket
        if (mListener != null){
            mListener.onOpen();
        }
    }

    @Override
    public void onClose(int code, String reason) {
        // 关闭健客聊天websocket
        if (mListener != null){
            mListener.onClose(code, reason);
        }
    }

    @Override
    public void onTextMessage(String payload) {
        try {
            // 若健客聊天监听为空则返回
            if (mListener == null)
                return;
            // 若健客聊天接收数据为空则返回
            if (StringUtils.isEmpty(payload))
                return;
            // 解析gson数据
            if (mGson == null)
                mGson = new Gson();
            JkReceiveMsgResponse msgResponse = mGson.fromJson(payload, JkReceiveMsgResponse.class);
            // 若解析后对象数据或数据中类型为空则返回
            if (msgResponse == null || StringUtils.isEmpty(msgResponse.Type))
                return;
            switch (msgResponse.Type){
                case MSG_TYPE_SERVICE_STATUS:
                    // 客服状态
                    // 当响应数据中消息内容不为空并且为数字类型才处理
                    if (StringUtils.isNotEmpty(msgResponse.Msg)
                            && StringUtils.strIsNum(msgResponse.Msg)){
                        int number = Integer.valueOf(msgResponse.Msg);
                        mListener.onStuffNumChanged(number);
                    }
                    break;
                case MSG_TYPE_CONVERSATION_ID:
                    // 会话ID
                    JkChatSession mJkChatSession = new JkChatSession();
                    mJkChatSession.setTid(msgResponse.TID);
                    mJkChatSession.setStaffName(msgResponse.StaffName);
                    mJkChatSession.setCustomSessionID(msgResponse.CustomSessionID);
                    mJkChatSession.setStaffSessionID(msgResponse.StaffSessionID);
                    mListener.onSessionChanged(mJkChatSession);
                    break;
                case MSG_TYPE_NORMAL_MSG:
                    // 正常消息
                    JkChatMessage mJkChatMessage = new JkChatMessage();
                    mJkChatMessage.setCustomSessionID(msgResponse.CustomSessionID);
                    mJkChatMessage.setDirect(JkChatMessage.DIRECT_RECEIVE);
                    mJkChatMessage.setMsg(msgResponse.Msg);
                    mJkChatMessage.setStaffName(msgResponse.StaffName);
                    mJkChatMessage.setStaffSessionID(msgResponse.StaffSessionID);
                    mJkChatMessage.setTid(msgResponse.TID);
                    mJkChatMessage.setTime(System.currentTimeMillis());
                    mJkChatMessage.setType(msgResponse.Type);
                    if (payload.contains(MSG_TYPE_IMG_IDENTIFY)){
                        // 处理图片消息
                        mJkChatMessage.setMsgType(JkChatMessage.TYPE_IMG);
                    }else {
                        // 处理文字消息
                        mJkChatMessage.setMsgType(JkChatMessage.TYPE_TXT);
                    }
                    mListener.onReceiveMessage(mJkChatMessage);
                    // 消息确认处理
                    String ackStr = ackMsg(msgResponse.MsgId, MSG_ACK_TYPE_CONTENT, "");
                    if (mWebSocket != null){
                        // 发送消息确认
                        mWebSocket.sendTextMessage(ackStr);
                    }
                    break;
                case MSG_TYPE_CHANGETOWAIT:
                    // 转到等待应答
                    mListener.onChangetoWait();
                    break;
                case MSG_TYPE_NEW_CUSTOMER_MSG:
                    // 新客户消息
                    mListener.onChangetoWait();
                    break;
                case MSG_TYPE_CLOSE_CLIENT_CONNECTION:
                    // 关闭客户连接
                    isClosedSucess = true;
                    mListener.onClientClosed();
                    break;
                case MSG_TYPE_SERVICE_OFFLINE:
                    // 客服下线
                    mListener.onStuffOffline();
                    break;
                case MSG_TYPE_USER_QUESTIONS_FILTER:
                    // 用户提问过滤
                    mListener.onFilterAskQuestion();
                    break;
                case MSG_TYPE_BAN_USERS_ASK_QUESTIONS:
                    // 禁止用户提问
                    mListener.onForbidAskQuestion();
                    break;
                case MSG_TYPE_CONNECT_THE_PROCESSED:
                    // 连接处理完毕
                    if (StringUtils.isNotEmpty(msgResponse.TID)) {
                        switch (msgResponse.TID) {
                            case CONVERSATION_IS_OVER:
                                // 对话已结束
                                // 无用户记录，即表示当前用户与服务器的对话已经结束了
                                mListener.onChatClosed();
                                break;
                            case CONVERSATION_IS_NOT_OVER:
                                // 对话未结束
                                // 存在用户记录，即表示当前用户与服务器的对话还没有结束
                                mListener.onChatStillAlive();
                                break;
                            default:
                                break;
                        }
                    }
                    // 表示服务器发完离线消息 + 无用户记录（如果没有会话）
                    mListener.onGetServerMessageDone();
                    break;
                default:
                    break;
            }
        }catch (Exception ex){

        }
    }

    @Override
    public void onRawTextMessage(byte[] payload) {

    }

    @Override
    public void onBinaryMessage(byte[] payload) {

    }

    @Override
    public void setListener(JkChatConnectionListener listener) {
        // 设置健客聊天连接监听
        this.mListener = listener;
    }

    @Override
    public void connect(String username, String clientId,
                        String fileId, String name,
                        String sex, String age) throws JkChatException {
        connect(username, clientId, null,fileId,name,sex,age);
    }

    @Override
    public void connect(String username, String clientId,
                        String tid, String fileId, String name,
                        String sex, String age) throws JkChatException {
        // 组建健客websocket链接地址
        String wsUri = String.format(Locale.getDefault(), RequestUrlUtils.WS_CONNECT_FORMAT,
                username, clientId, tid == null ? "" : tid, fileId, name, sex, age);
        // 开始连接健客websocket服务器
        try {
            mWebSocket.connect(wsUri, this);
        } catch (WebSocketException e) {
            throw new JkChatException(CONNECT_WEBSOCKET_CHATEXCEPTION_MSG, e);
        }
    }

    @Override
    public void disconnect() {
        // 若websocket实例不为空并且已经连接上了健客websocket服务器则断开连接
        if (isConnected() && mWebSocket != null){
            mWebSocket.disconnect();
        }
    }

    @Override
    public boolean isConnected() {
        // 若websocket实例不为空并且已经连接上了健客websocket服务器则返回已经连接上状态
        if (mWebSocket != null && mWebSocket.isConnected())
            return true;
        return false;
    }

    @Override
    public boolean sendMessage(JkChatMessage message) {
        if (message == null || mGson == null)
            return false;
        // 将数据转gson格式
        String payload = mGson.toJson(message);
        // websocket实例不为空并且发送内容不为空则发送数据
        if (mWebSocket != null && !TextUtils.isEmpty(payload)) {
            mWebSocket.sendTextMessage(payload);
            return true;
        }
        return false;
    }

    @Override
    public boolean clientPositionClose(String tid) {
        String ackStr = ackMsg(tid, USER_INITIATES_THE_CONVERSATION, "");
        // 发送消息主动关闭在线咨询对话
        if (mWebSocket != null)
            mWebSocket.sendTextMessage(ackStr);
        return isClosedSucess;
    }
}
