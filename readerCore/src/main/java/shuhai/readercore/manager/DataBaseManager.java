package shuhai.readercore.manager;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import de.greenrobot.dao.query.Query;
import shuhai.readercore.dao.BookInfoEntity;
import shuhai.readercore.dao.BookInfoEntityDao;
import shuhai.readercore.dao.ChapterEntity;
import shuhai.readercore.dao.ChapterEntityDao;
import shuhai.readercore.dao.DaoMaster;
import shuhai.readercore.dao.DaoSession;
import shuhai.readercore.dao.MarkEntityDao;
import shuhai.readercore.utils.AppUtils;
import shuhai.readercore.utils.Utils;
import shuhai.readercore.view.readview.status.FlipStatus;

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

    private static final String DB_NAME = "shuhaios.db";


    private DataBaseManager() {
        devOpenHelper = new DaoMaster.DevOpenHelper(Utils.getAppContext(), DB_NAME, null);
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
     * 完成对数据库的操作，只是个接口
     *
     * @return
     */
    public DaoSession getDaoSession() {
        return session;
    }


    /**
     * 判断是否存在数据库，如果没有则创建
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        return master;
    }
    /**
     * 书籍基本信息插入
     *
     * @param bookInfoEntity
     * @return
     */
    public long insertBookInfo(BookInfoEntity bookInfoEntity) {
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


    /**
     * 章节信息存库
     *
     * @param chapterEntity
     * @return
     */
    public long insertChapterInfo(ChapterEntity chapterEntity) {
        return chapterEntityDao.insertOrReplace(chapterEntity);
    }


    /**
     * 查询章节信息
     *
     * @return
     */
    public ChapterEntity queryChapterInfo(int order) {
        ChapterEntity queryBuilder;
        try {
            queryBuilder = chapterEntityDao.queryBuilder().where(ChapterEntityDao.Properties.Chiporder.eq(order)).build().uniqueOrThrow();
        } catch (Exception e) {
            queryBuilder = null;
        }
        return queryBuilder;
    }


    /**
     * 获取章节名字
     * @param chapterId
     * @return
     */
    public String queryChapterName(int chapterId){

        ChapterEntity queryBuilder;
        try {
            queryBuilder = chapterEntityDao.queryBuilder().where(ChapterEntityDao.Properties.Chpid.eq(chapterId)).build().uniqueOrThrow();
        } catch (Exception e) {
            queryBuilder = null;
        }

        if(null == queryBuilder){
            return "";
        }
        return queryBuilder.getChpnamme();
    }

    /**
     * 查询下一章是否有章节信息
     * @param chapterType
     * @param articleid
     * @param chapterOrder
     * @param status
     * @return
     */
    public ChapterEntity queryNextChapterInfo(int chapterType, int articleid, int chapterOrder, FlipStatus status) {

        String where;
        switch (status) {
            case ON_FLIP_PRE:
                where = " where CHPTYPE = ? and ARTICLEID = ? and CHIPORDER < ? order by CHIPORDER desc limit 1 ";
                break;
            case ON_FLIP_CUR:
                where = " where CHPTYPE = ? and  ARTICLEID = ? and CHIPORDER = ? ";
                break;

            case ON_FLIP_NEXT:
                where = " where CHPTYPE = ? and ARTICLEID = ? and CHIPORDER > ? order by CHIPORDER asc limit 1 ";
                break;

            default:
                where = "where CHPTYPE = ? and ARTICLEID = ? and CHIPORDER < ? order by CHIPORDER desc limit 1 ";
                break;
        }
        List<ChapterEntity> list = DataBaseManager.getInstance().getDaoSession().queryRaw(ChapterEntity.class, where, new String[]{
                String.valueOf(chapterType), String.valueOf(articleid), String.valueOf(chapterOrder)});
        if (null == list || list.size() == 0) {
            return null;
        }

        return list.get(0);
    }
}
