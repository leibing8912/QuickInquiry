package cn.jianke.jkchat.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @className: JkChatMessage
 * @classDescription: 健客聊天消息实体
 * @author: leibing
 * @createTime: 2017/2/17
 */
@Entity
public class JkChatMessage {
    // 消息类型----正常消息
    public final static String TYPE_MSG_NORMAL = "正常消息";
    // 消息类型----提示消息
    public final static String TYPE_MSG_TIP = "提示消息";
    // 消息内容类型----文字
    public final static String TYPE_TXT = "TYPE_TXT";
    // 消息内容类型----处方签
    public final static String TYPE_PRESCRIPTION = "TYPE_PRESCRIPTION";
    // 消息内容类型----图片
    public final static String TYPE_IMG = "TYPE_IMG";
    // 消息内容类型----多张图片图片
    public final static String TYPE_IMG_MORE = "TYPE_IMG_MORE";
    // 消息内容类型----系统消息
    public final static String TYPE_SYSTEM_NEWS = "TYPE_SYSTEM_NEWS";
    // 消息方向----发送
    public final static String DIRECT_SEND = "DIRECT_SEND";
    // 消息方向----接收
    public final static String DIRECT_RECEIVE = "DIRECT_RECEIVE";
    // 消息方向----系统消息无方向
    public final static String DIRECT_NONE = "DIRECT_NONE";
    // 发送成功
    public final static String STATUS_SUCCESS = "STATUS_SUCCESS";
    // 发送失败
    public final static String STATUS_FAIL = "STATUS_FAIL";
    // 发送中
    public final static String STATUS_INPROGRESS = "STATUS_INPROGRESS";
    // 数据库自增id
    @Id(autoincrement = true)
    private long id;
    // 消息id
    private String msgId;
    // 会话id
    private String cid;
    // 消息内容类型
    private String msgType;
    // 消息发送方向
    private String direct;
    // 消息记录时间（时间戳，用于一周后数删除）
    private long time;
    // 消息接收时间
    private String receiveDate;
    // 状态
    private String status;
    // 本地图片地址
    private String localUrl;
    // 本地多张图片数组Json数据
    private String localUrlsJson;
    // 网络图片地址
    private String remoteUrl;
    // 多张图片网络地址
    private String remoteUrlsJson;
    // 消息id
    private String tid;
    // 消息类型 "type":"正常消息" "type":"关闭客户连接"
    private String type;
    // 消息内容
    private String msg;
    // 客服id
    private String staffSessionID;
    // 用户id
    private String customSessionID;
    // 客服名称
    private String staffName;
    // 是否对医生评价标识
    private String isHasAppraised;
    @Generated(hash = 88097976)
    public JkChatMessage(long id, String msgId, String cid, String msgType, String direct, long time,
            String receiveDate, String status, String localUrl, String localUrlsJson, String remoteUrl,
            String remoteUrlsJson, String tid, String type, String msg, String staffSessionID,
            String customSessionID, String staffName, String isHasAppraised) {
        this.id = id;
        this.msgId = msgId;
        this.cid = cid;
        this.msgType = msgType;
        this.direct = direct;
        this.time = time;
        this.receiveDate = receiveDate;
        this.status = status;
        this.localUrl = localUrl;
        this.localUrlsJson = localUrlsJson;
        this.remoteUrl = remoteUrl;
        this.remoteUrlsJson = remoteUrlsJson;
        this.tid = tid;
        this.type = type;
        this.msg = msg;
        this.staffSessionID = staffSessionID;
        this.customSessionID = customSessionID;
        this.staffName = staffName;
        this.isHasAppraised = isHasAppraised;
    }

    @Generated(hash = 1975531948)
    public JkChatMessage() {
    }
    /**-----------------------------------------获取不同消息---------------------------------------
     --------------------------------------------------------------------------------------------*/
    /**
     * 处方签消息
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param content 处方签内容
     * @return
     */
    public static JkChatMessage newPrescriptionMessage(String content){
        JkChatMessage result = new JkChatMessage();
        result.setDirect(JkChatMessage.DIRECT_NONE);
        result.setMsg(content);
        result.setType(JkChatMessage.TYPE_MSG_NORMAL);
        result.setMsgType(JkChatMessage.TYPE_PRESCRIPTION);
        result.setTime(System.currentTimeMillis());
        return result;
    }

    /**
     * 系统消息
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param content 系统消息内容
     * @return
     */
    public static JkChatMessage newSystemMessage(String content){
        JkChatMessage result = new JkChatMessage();
        result.setDirect(JkChatMessage.DIRECT_NONE);
        result.setMsg(content);
        result.setType(JkChatMessage.TYPE_MSG_TIP);
        result.setMsgType(JkChatMessage.TYPE_SYSTEM_NEWS);
        result.setTime(System.currentTimeMillis());
        return result;
    }

    /**
     * 文本消息
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param content 文本消息内容
     * @return
     */
    public static JkChatMessage newTextMessage(String content){
        JkChatMessage result = new JkChatMessage();
        result.setDirect(JkChatMessage.DIRECT_SEND);
        result.setMsg(content);
        result.setMsgType(JkChatMessage.TYPE_TXT);
        result.setTime(System.currentTimeMillis());
        result.setType(JkChatMessage.TYPE_MSG_NORMAL);
        result.setStatus(JkChatMessage.STATUS_SUCCESS);
        result.setMsgId(UUID.randomUUID().toString());
        return result;
    }

    /**
     * 单张图片消息
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param imgUrl 图片地址
     * @return
     */
    public static JkChatMessage newImageMessage(String imgUrl){
        JkChatMessage result = new JkChatMessage();
        result.setDirect(JkChatMessage.DIRECT_SEND);
        result.setMsg("");
        result.setMsgType(JkChatMessage.TYPE_IMG);
        result.setTime(System.currentTimeMillis());
        result.setType(JkChatMessage.TYPE_MSG_NORMAL);
        result.setStatus(JkChatMessage.STATUS_INPROGRESS);
        result.setMsgId(UUID.randomUUID().toString());
        if (imgUrl.contains("http") || imgUrl.contains("https")){
            // 网络图片地址
            result.setRemoteUrl(imgUrl);
        }else {
            // 本地图片地址
            result.setLocalUrl(imgUrl);
        }
        return result;
    }

    /**
     * 多张图片
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param imgUrlsJson 图片url列表Json
     * @param isLocal 是否本地图片列表
     * @return
     */
    public static JkChatMessage newImageMessage(String imgUrlsJson, boolean isLocal){
        JkChatMessage result = new JkChatMessage();
        result.setDirect(JkChatMessage.DIRECT_SEND);
        result.setMsg("");
        result.setMsgType(JkChatMessage.TYPE_IMG_MORE);
        result.setTime(System.currentTimeMillis());
        result.setType(JkChatMessage.TYPE_MSG_NORMAL);
        result.setStatus(JkChatMessage.STATUS_INPROGRESS);
        result.setMsgId(UUID.randomUUID().toString());
        if (isLocal)
            result.setLocalUrlsJson(imgUrlsJson);
        else
            result.setRemoteUrlsJson(imgUrlsJson);
        return result;
    }
    
    /**-----------------------------------------get and set---------------------------------------
     --------------------------------------------------------------------------------------------*/
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getLocalUrlsJson() {
        return localUrlsJson;
    }

    public void setLocalUrlsJson(String localUrlsJson) {
        this.localUrlsJson = localUrlsJson;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getRemoteUrlsJson() {
        return remoteUrlsJson;
    }

    public void setRemoteUrlsJson(String remoteUrlsJson) {
        this.remoteUrlsJson = remoteUrlsJson;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStaffSessionID() {
        return staffSessionID;
    }

    public void setStaffSessionID(String staffSessionID) {
        this.staffSessionID = staffSessionID;
    }

    public String getCustomSessionID() {
        return customSessionID;
    }

    public void setCustomSessionID(String customSessionID) {
        this.customSessionID = customSessionID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getIsHasAppraised() {
        return isHasAppraised;
    }

    public void setIsHasAppraised(String isHasAppraised) {
        this.isHasAppraised = isHasAppraised;
    }

    public void setId(long id) {
        this.id = id;
    }
}
