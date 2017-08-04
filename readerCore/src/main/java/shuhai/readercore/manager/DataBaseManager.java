package shuhai.readercore.manager;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import de.greenrobot.dao.query.Query;
import shuhai.readercore.dao.BookInfoEntity;
import shuhai.readercore.dao.BookInfoEntityDao;
import shuhai.readercore.dao.ChapterEntityDao;
import shuhai.readercore.dao.DaoMaster;
import shuhai.readercore.dao.DaoSession;
import shuhai.readercore.dao.MarkEntityDao;
import shuhai.readercore.utils.AppUtils;

/**
 * @author 55345364
 * @date 2017/8/3.
 */

public class DataBaseManager {

    private DaoMaster master;
    private DaoSession session;
    private DaoMaster.DevOpenHelper devOpenHelper;
    private SQLiteDatabase db;

    private BookInfoEntityDao bookInfoEntityDao;
    private ChapterEntityDao chapterEntityDao;
    private MarkEntityDao markEntityDao;


    private DataBaseManager(){
        devOpenHelper = new DaoMaster.DevOpenHelper(AppUtils.getAppContext(), "shuhaios.db", null);
        db = devOpenHelper.getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();

        bookInfoEntityDao = session.getBookInfoEntityDao();
        chapterEntityDao = session.getChapterEntityDao();
        markEntityDao = session.getMarkEntityDao();

    }

    private static class DataManagerHolder{
        private static final DataBaseManager INSTANCE = new DataBaseManager();
    }

    public static DataBaseManager getInstance(){
        return DataManagerHolder.INSTANCE;
    }


    /**
     * 书籍基本信息插入
     * @param bookInfoEntity
     * @return
     */
    public long insertBookInfo(BookInfoEntity bookInfoEntity){
        return bookInfoEntityDao.insertOrReplace(bookInfoEntity);
    }


    /**
     * 查询书籍信息
     * @return
     */
    public List<BookInfoEntity> queryBookInfoList(){
        Query<BookInfoEntity> queryBuilder = bookInfoEntityDao.queryBuilder().build();
        return queryBuilder.list();
    }



}
