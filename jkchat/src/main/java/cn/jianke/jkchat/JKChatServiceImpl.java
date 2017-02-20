package cn.jianke.jkchat;

import android.content.Context;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import cn.jianke.jkchat.domain.JkChatMessage;
import cn.jianke.jkchat.domain.JkChatSession;

/**
 * @className: JKChatServiceImpl
 * @classDescription: 健客聊天服务实现
 * @author: leibing
 * @createTime: 2017/2/20
 */
public class JKChatServiceImpl implements JkChatService{
    // Context weakRef
    private WeakReference<Context> mContextWeakRef;
    // 健客聊天服务实现单例
    private static JKChatServiceImpl instance;
    // 健客聊天api
    private JkChatApi mJkChatApi;
    // 健客聊天连接
    private JkChatConnection mJkChatConnection;
    // 健客聊天api监听
    private JkChatApiListener mJkChatApiListener = new JkChatApiListener() {
        @Override
        public void onReponseGetDoctorNum(int doctorNum) {

        }

        @Override
        public void onGetDoctorNumError(Exception e) {

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

    @Override
    public void start() {

    }

    @Override
    public void stop() {

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
}
