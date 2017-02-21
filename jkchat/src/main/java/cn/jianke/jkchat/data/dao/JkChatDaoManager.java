package cn.jianke.jkchat.data.dao;

import android.content.Context;
import com.jk.chat.gen.DaoMaster;
import com.jk.chat.gen.DaoSession;
/**
 * @className: JkChatDaoManager
 * @classDescription: 健客聊天数据库管理
 * @author: leibing
 * @createTime: 2017/2/21
 */
public class JkChatDaoManager {
    // jk chat dao name
    public final static String JK_CHAT_DAO_DATABASE_NAME = "jk_chat_dao_db";
    // sington
    private static JkChatDaoManager instance;
    // DaoSession
    private DaoSession mDaoSession;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param context 引用
     * @return
     */
    private JkChatDaoManager(Context context){
        JkChatSQLiteOpenHelper devOpenHelper = new JkChatSQLiteOpenHelper(context,
                JK_CHAT_DAO_DATABASE_NAME, null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * sington
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param context 引用（建议此处传入application引用防止内存泄漏）
     * @return
     */
    public static JkChatDaoManager getInstance(Context context){
        if (instance == null){
            synchronized (JkChatDaoManager.class){
                if (instance == null){
                    instance = new JkChatDaoManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * get DaoSession
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
