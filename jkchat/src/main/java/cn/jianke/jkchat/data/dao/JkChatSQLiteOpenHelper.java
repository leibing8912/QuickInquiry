package cn.jianke.jkchat.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.jk.chat.gen.DaoMaster;
import com.jk.chat.gen.JkChatConversationDao;
import com.jk.chat.gen.JkChatMessageDao;
import com.jk.chat.gen.JkChatSessionDao;

/**
 * @className: JkChatSQLiteOpenHelper
 * @classDescription: 健客聊天SQLiteOpenHelper（用于升级）
 * @author: leibing
 * @createTime: 2017/2/21
 */
public class JkChatSQLiteOpenHelper extends DaoMaster.OpenHelper{

    public JkChatSQLiteOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级处理
        MigrationHelper.migrate(db,JkChatConversationDao.class,
                JkChatMessageDao.class, JkChatSessionDao.class);
    }
}
