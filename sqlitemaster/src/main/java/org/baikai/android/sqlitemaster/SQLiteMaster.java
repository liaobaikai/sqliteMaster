package org.baikai.android.sqlitemaster;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.baikai.android.sqlitemaster.sql.ConditionBuilder;
import org.baikai.android.sqlitemaster.sql.SQLResolver;
import org.baikai.android.sqlitemaster.sql.Utils;
import org.baikai.android.sqlitemaster.sql.builder.SimpleDDLSQLBuilder;
import org.baikai.android.sqlitemaster.sql.builder.SimpleSQLBuilder;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by baikai on 2017/8/4.
 */

public class SQLiteMaster {

    private Context mContext;

    private int databaseVersion;
    private String databaseName;
    private SimpleSQLBuilder mSimpleSQLBuilder = new SimpleSQLBuilder();

    private SQLiteMaster(Context context){
        this(context, ConfigLoader.DB_VERSION, ConfigLoader.DB_NAME);
    }

    private SQLiteMaster(Context context, int databaseVersion, String databaseName){
        this.mContext = context;
        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion;
    }

    public void setDatabaseVersion(int databaseVersion){
        this.databaseVersion = databaseVersion;
    }

    public void setDatabaseName(String databaseName){
        this.databaseName = databaseName;
    }


    private static SQLiteMaster mSQLiteMaster;
    public static SQLiteMaster getInstance() {
        return mSQLiteMaster;
    }


    public static SQLiteMaster init(Context context, int databaseVersion, String databaseName) {
        if(mSQLiteMaster == null){
            mSQLiteMaster = new SQLiteMaster(context, databaseVersion, databaseName);
        }

        return mSQLiteMaster;
    }

    public interface OnDatabaseCreateListener{
        void onCreate(SQLiteDatabase sqLiteDatabase);
    }

    public interface OnDatabaseUpgradeListener{
        void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion);
    }

    public MasterHelper getMasterHelper(){
        return MasterHelper.getInstance(mContext, databaseName, null, databaseVersion, new DatabaseErrorHandler() {
            @Override
            public void onCorruption(SQLiteDatabase sqLiteDatabase) {
                Toast.makeText(mContext, "数据库创建失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void insert(Object bean){
        insert(bean, false);
    }

    private void insert(Object bean, boolean replace){
        try {
            SQLResolver resolver = mSimpleSQLBuilder.buildInsertSQL(bean, false, replace);

            getMasterHelper().getWritableDatabase().execSQL(resolver.getSqlText(),
                    resolver.getValues().toArray(new Object[resolver.getValues().size()]));
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void insertOrReplace(Object bean){
        insert(bean, true);
    }

    public <T> List<T> selectList(Class<?> clazz, ConditionBuilder conditionBuilder){

        try {

            SQLResolver resolver = mSimpleSQLBuilder.buildSelectSQL(clazz, conditionBuilder, false);

            Cursor cursor = getMasterHelper().getReadableDatabase().rawQuery(resolver.getSqlText(),
                    resolver.getValues().toArray(new String[resolver.getValues().size()]));


            if(cursor != null && cursor.getCount() > 0){

                // 数据装载器
                return new DataLoader(cursor, clazz).getAll();

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public <T> T select(Class<?> clazz, ConditionBuilder conditionBuilder){

        try {

            SQLResolver resolver = mSimpleSQLBuilder.buildSelectSQL(clazz, conditionBuilder, false);

            Cursor cursor = getMasterHelper().getReadableDatabase().rawQuery(resolver.getSqlText(),
                    resolver.getValues().toArray(new String[resolver.getValues().size()]));


            if(cursor != null && cursor.getCount() > 0){

                // 数据装载器
                return (T) new DataLoader(cursor, clazz).get();

            }

        }catch (Exception e){
            e.printStackTrace();
        }



        return null;
    }


    public void delete(ConditionBuilder conditionBuilder){

        try {

            SQLResolver resolver = mSimpleSQLBuilder.buildDeleteSQL(conditionBuilder, false);

            getMasterHelper().getWritableDatabase().execSQL(resolver.getSqlText(),
                    resolver.getValues().toArray(new Object[resolver.getValues().size()]));

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static class MasterHelper extends SQLiteOpenHelper {

        static MasterHelper mMasterHelper;
        OnDatabaseUpgradeListener mOnDatabaseUpgradeListener;
        OnDatabaseCreateListener mOnDatabaseCreateListener;

        public static MasterHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler){
            if(mMasterHelper == null){
                mMasterHelper = new MasterHelper(context, name, factory, version, errorHandler);
            }
            return mMasterHelper;
        }

        private MasterHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        private MasterHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            if(mOnDatabaseCreateListener != null){
                mOnDatabaseCreateListener.onCreate(sqLiteDatabase);
            }
        }

        public void setOnDatabaseCreateListener(OnDatabaseCreateListener listener){
            this.mOnDatabaseCreateListener = listener;
        }

        public void setOnDatabaseUpgradeListener(OnDatabaseUpgradeListener listener){
            this.mOnDatabaseUpgradeListener = listener;
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            if(mOnDatabaseUpgradeListener != null){
                mOnDatabaseUpgradeListener.onUpgrade(sqLiteDatabase, i, i1);
            }
        }
    }

}