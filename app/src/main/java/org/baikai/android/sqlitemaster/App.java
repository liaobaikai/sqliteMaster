package org.baikai.android.sqlitemaster;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import org.baikai.android.sqlitemaster.beans.User;
import org.baikai.android.sqlitemaster.beans.UserInfo;
import org.baikai.android.sqlitemaster.sql.SQLResolver;
import org.baikai.android.sqlitemaster.sql.builder.SimpleDDLSQLBuilder;

/**
 * Created by baikai on 2017/8/6.
 */

public class App extends Application {

    final Class<?>[] classes = {User.class, UserInfo.class};
    public static final int dbVersion = 5;
    public static final String dbName = "db-master";

    @Override
    public void onCreate() {
        super.onCreate();

        SQLiteMaster master = SQLiteMaster.init(getApplicationContext(), dbVersion, dbName);
        SQLiteMaster.MasterHelper helper = master.getMasterHelper();

        helper.setOnDatabaseCreateListener(new SQLiteMaster.OnDatabaseCreateListener() {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                SimpleDDLSQLBuilder builder = new SimpleDDLSQLBuilder();

                for(Class<?> clazz: classes){

                    SQLResolver resolver = builder.buildCreateTableSQL(clazz);

                    sqLiteDatabase.execSQL(resolver.getSqlText());

                }

            }
        });

        helper.setOnDatabaseUpgradeListener(new SQLiteMaster.OnDatabaseUpgradeListener() {
            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

                SimpleDDLSQLBuilder builder = new SimpleDDLSQLBuilder();

                for(Class<?> clazz: classes){

                    SQLResolver resolver = builder.buildDropTableSQL(clazz);
                    sqLiteDatabase.execSQL(resolver.getSqlText());

                    resolver = builder.buildCreateTableSQL(clazz);
                    sqLiteDatabase.execSQL(resolver.getSqlText());

                }
            }
        });
    }
}
