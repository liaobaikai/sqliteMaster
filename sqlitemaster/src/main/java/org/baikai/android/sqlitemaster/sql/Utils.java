package org.baikai.android.sqlitemaster.sql;

import android.content.ContentValues;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

/**
 * Created by baikai on 2017/8/4.
 */

public class Utils {

    public static String getTableName(Class<?> clazz) {
        if(clazz == null){
            throw new NullPointerException("clazz is null");
        }

        Table table = clazz.getAnnotation(Table.class);

        String tableName = "";

        if(table != null){
            if(table.schema().length() > 0){
                tableName = table.schema() + ".";
            }

            if(table.name().length() > 0)
                tableName += table.name();

        }else{
            tableName += clazz.getSimpleName();
        }

        return tableName;

    }

    public static ContentValues convertMap(Map<String, Object> values){

        ContentValues cv = new ContentValues();

        Iterator<Map.Entry<String, Object>> iterator = values.entrySet().iterator();
        for(;iterator.hasNext();){
            Map.Entry<String, Object> en = iterator.next();
            cv.put(en.getKey(), String.valueOf(en.getValue()));
        }

        return cv;
    }

}
