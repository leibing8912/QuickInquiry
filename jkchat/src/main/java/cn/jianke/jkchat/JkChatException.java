package cn.jianke.jkchat;

/**
 * @className: JkChatException
 * @classDescription: 健客聊天异常处理
 * @author: leibing
 * @createTime: 2017/2/20
 */
public class JkChatException extends Exception {

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/20
     * @lastModify 2017/2/20
     * @param detailMessage 详细信息
     * @param throwable 异常
     * @return
     */
    public JkChatException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/20
     * @lastModify 2017/2/20
     * @param detailMessage 详细信息
     * @param
     * @return
     */
    public JkChatException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/20
     * @lastModify 2017/2/20
     * @param throwable 异常
     * @return
     */
    public JkChatException(Throwable throwable) {
        super(throwable);
    }
}
