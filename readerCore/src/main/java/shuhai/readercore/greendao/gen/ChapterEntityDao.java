package shuhai.readercore.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import shuhai.readercore.bean.ChapterEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CHAPTER_ENTITY".
*/
public class ChapterEntityDao extends AbstractDao<ChapterEntity, Long> {

    public static final String TABLENAME = "CHAPTER_ENTITY";

    /**
     * Properties of entity ChapterEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ArticleId = new Property(1, int.class, "articleId", false, "ARTICLE_ID");
        public final static Property ChapterId = new Property(2, int.class, "chapterId", false, "CHAPTER_ID");
        public final static Property ChapterOrder = new Property(3, int.class, "chapterOrder", false, "CHAPTER_ORDER");
        public final static Property ChapterName = new Property(4, String.class, "chapterName", false, "CHAPTER_NAME");
        public final static Property ChapterType = new Property(5, int.class, "chapterType", false, "CHAPTER_TYPE");
    }


    public ChapterEntityDao(DaoConfig config) {
        super(config);
    }
    
    public ChapterEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHAPTER_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ARTICLE_ID\" INTEGER NOT NULL ," + // 1: articleId
                "\"CHAPTER_ID\" INTEGER NOT NULL ," + // 2: chapterId
                "\"CHAPTER_ORDER\" INTEGER NOT NULL ," + // 3: chapterOrder
                "\"CHAPTER_NAME\" TEXT," + // 4: chapterName
                "\"CHAPTER_TYPE\" INTEGER NOT NULL );"); // 5: chapterType
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHAPTER_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ChapterEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getArticleId());
        stmt.bindLong(3, entity.getChapterId());
        stmt.bindLong(4, entity.getChapterOrder());
 
        String chapterName = entity.getChapterName();
        if (chapterName != null) {
            stmt.bindString(5, chapterName);
        }
        stmt.bindLong(6, entity.getChapterType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ChapterEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getArticleId());
        stmt.bindLong(3, entity.getChapterId());
        stmt.bindLong(4, entity.getChapterOrder());
 
        String chapterName = entity.getChapterName();
        if (chapterName != null) {
            stmt.bindString(5, chapterName);
        }
        stmt.bindLong(6, entity.getChapterType());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ChapterEntity readEntity(Cursor cursor, int offset) {
        ChapterEntity entity = new ChapterEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // articleId
            cursor.getInt(offset + 2), // chapterId
            cursor.getInt(offset + 3), // chapterOrder
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // chapterName
            cursor.getInt(offset + 5) // chapterType
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ChapterEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setArticleId(cursor.getInt(offset + 1));
        entity.setChapterId(cursor.getInt(offset + 2));
        entity.setChapterOrder(cursor.getInt(offset + 3));
        entity.setChapterName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setChapterType(cursor.getInt(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ChapterEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ChapterEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ChapterEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
