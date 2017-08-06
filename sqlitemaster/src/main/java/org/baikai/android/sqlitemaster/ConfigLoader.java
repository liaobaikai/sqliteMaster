package org.baikai.android.sqlitemaster;

/**
 * Created by baikai on 2017/8/4.
 */

public class ConfigLoader {

    public enum RunningLevel{
        INFO, DEBUG
    }

    public static final RunningLevel RUNNING_LEVEL = RunningLevel.DEBUG;

    public static final int DB_VERSION = 10;
    public static final String DB_NAME = "sqlite-mater";

}
