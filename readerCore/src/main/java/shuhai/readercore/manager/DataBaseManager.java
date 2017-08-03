package shuhai.readercore.manager;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.Query;
import shuhai.readercore.bean.BookInfoEntity;
import shuhai.readercore.dao.Bkbaseinfo;
import shuhai.readercore.dao.BkbaseinfoDao;
import shuhai.readercore.dao.BkchpinfoDao;
import shuhai.readercore.dao.BkmarkDao;
import shuhai.readercore.dao.DaoMaster;
import shuhai.readercore.dao.DaoSession;
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

    private BkbaseinfoDao bkbaseinfoDao;
    private BkchpinfoDao bkchpinfoDao;
    private BkmarkDao bkmarkDao;


    private DataBaseManager(){
        devOpenHelper = new DaoMaster.DevOpenHelper(AppUtils.getAppContext(), "shuhaios.db", null);
        db = devOpenHelper.getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();

        bkbaseinfoDao = session.getBkbaseinfoDao();
        bkchpinfoDao = session.getBkchpinfoDao();
        bkmarkDao = session.getBkmarkDao();

    }

    private static class DataManagerHolder{
        private static final DataBaseManager INSTANCE = new DataBaseManager();
    }

    public static DataBaseManager getInstance(){
        return DataManagerHolder.INSTANCE;
    }


    /**
     * 书籍基本信息插入
     * @param message
     * @return
     */
    public long insertBookInfo(BookInfoEntity.MessageBean message){
        Bkbaseinfo bkbaseinfo = new Bkbaseinfo();
        bkbaseinfo.setArticleid(Integer.valueOf(message.getArticleid()));
        bkbaseinfo.setArticlename(message.getArticlename());
        bkbaseinfo.setAuthor(message.getAuthor());
        bkbaseinfo.setBkbmurl(message.getCover());
        bkbaseinfo.setEndtype(1);
        bkbaseinfo.setNewchpname(1);
        bkbaseinfo.setOwner(message.getAuthor());
        bkbaseinfo.setReadtime(new Date());
        bkbaseinfo.setLastreadchporder(new Date());
        bkbaseinfo.setBktype(2);
        bkbaseinfo.setNewchporder(Integer.valueOf(message.getLastchapterid()));
        return bkbaseinfoDao.insertOrReplace(bkbaseinfo);
    }


    /**
     * 查询书籍信息
     * @return
     */
    public List<Bkbaseinfo> queryBookInfoList(){
        Query<Bkbaseinfo> queryBuilder = bkbaseinfoDao.queryBuilder().build();
        return queryBuilder.list();
    }



}
