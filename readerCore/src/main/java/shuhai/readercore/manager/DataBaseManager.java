package shuhai.readercore.manager;

import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import shuhai.readercore.bean.BookEntity;
import shuhai.readercore.bean.ChapterEntity;
import shuhai.readercore.greendao.gen.BookEntityDao;
import shuhai.readercore.greendao.gen.BookMarkEntityDao;
import shuhai.readercore.greendao.gen.ChapterEntityDao;
import shuhai.readercore.greendao.gen.DaoMaster;
import shuhai.readercore.greendao.gen.DaoSession;
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

    private BookEntityDao bookEntityDao;
    private ChapterEntityDao chapterEntityDao;
    private BookMarkEntityDao markEntityDao;

    private static final String DB_NAME = "shuhaios.db";


    private DataBaseManager() {
        devOpenHelper = new DaoMaster.DevOpenHelper(Utils.getAppContext(), DB_NAME, null);
        db = devOpenHelper.getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();

        bookEntityDao = session.getBookEntityDao();
        chapterEntityDao = session.getChapterEntityDao();
        markEntityDao = session.getBookMarkEntityDao();

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
     * @param bookEntity
     * @return
     */
    public long insertBookInfo(BookEntity bookEntity) {
        return bookEntityDao.insertOrReplace(bookEntity);
    }


    /**
     * 查询书籍信息
     * @return
     */
    public List<BookEntity> queryBookInfoList(){
        Query<BookEntity> queryBuilder = bookEntityDao.queryBuilder().build();
        return queryBuilder.list();
    }


    /**
     * 章节信息存库
     *
     * @param chapterEntity
     * @return
     */
    public void insertChapterInfo(ChapterEntity chapterEntity) {
        ChapterEntity entity = queryChapterInfo(chapterEntity.getArticleId(),chapterEntity.getChapterId());
        if(null == entity){
            chapterEntityDao.save(chapterEntity);
        }else{
            chapterEntity.setId(entity.getId());
            chapterEntityDao.update(chapterEntity);
        }
    }


    /**
     * 查询章节信息
     *
     * @return
     */
    public ChapterEntity queryChapterInfo(int order) {
        ChapterEntity queryBuilder;
        try {
            queryBuilder = chapterEntityDao.queryBuilder().where(ChapterEntityDao.Properties.ChapterOrder.eq(order)).build().uniqueOrThrow();
        } catch (Exception e) {
            queryBuilder = null;
        }
        return queryBuilder;
    }
    public ChapterEntity queryChapterInfo(int articleId,int chapterId){

        ChapterEntity queryBuilder;
        try{
            queryBuilder = chapterEntityDao.queryBuilder().where(ChapterEntityDao.Properties.ArticleId.eq(articleId),ChapterEntityDao.Properties.ChapterId.eq(chapterId)).unique();
        }catch (Exception e){
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
            queryBuilder = chapterEntityDao.queryBuilder().where(ChapterEntityDao.Properties.ChapterId.eq(chapterId)).build().uniqueOrThrow();
        } catch (Exception e) {
            queryBuilder = null;
        }

        if(null == queryBuilder){
            return "";
        }
        return queryBuilder.getChapterName();
    }

    /**
     * 查询下一章是否有章节信息
     * @param chapterType
     * @param articleId
     * @param chapterOrder
     * @param status
     * @return
     */
    public ChapterEntity queryNextChapterInfo(int chapterType, int articleId, int chapterOrder, FlipStatus status) {

        String where;
        switch (status) {
            case ON_FLIP_PRE:
                where = " where CHAPTER_TYPE = ? and ARTICLE_ID = ? and CHAPTER_ORDER < ? order by CHAPTER_ORDER desc limit 1 ";
                break;
            case ON_FLIP_CUR:
                where = " where CHAPTER_TYPE = ? and  ARTICLE_ID = ? and CHAPTER_ORDER = ? ";
                break;

            case ON_FLIP_NEXT:
                where = " where CHAPTER_TYPE = ? and ARTICLE_ID = ? and CHAPTER_ORDER > ? order by CHAPTER_ORDER asc limit 1 ";
                break;

            default:
                where = "where CHAPTER_TYPE = ? and ARTICLE_ID = ? and CHAPTER_ORDER < ? order by CHAPTER_ORDER desc limit 1 ";
                break;
        }
        List<ChapterEntity> list = DataBaseManager.getInstance().getDaoSession().queryRaw(ChapterEntity.class, where, new String[]{
                String.valueOf(chapterType), String.valueOf(articleId), String.valueOf(chapterOrder)});
        if (null == list || list.size() == 0) {
            return null;
        }

        return list.get(0);
    }
}
