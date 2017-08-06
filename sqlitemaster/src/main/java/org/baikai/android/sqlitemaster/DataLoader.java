package org.baikai.android.sqlitemaster;

import android.database.Cursor;

import org.baikai.android.sqlitemaster.annotation.ColumnType;
import org.baikai.android.sqlitemaster.sql.JDBCType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

/**
 * Created by baikai on 2017/8/5.
 */

public final class DataLoader <T> {

    private Cursor mCursor;
    private Class<T> mClass;

    public DataLoader(Cursor cursor, Class<T> clazz) {
        this.mCursor = cursor;
        this.mClass = clazz;
    }

    public List<T> getAll(){

        List<T> lists = new ArrayList<>();

        while(mCursor.moveToNext()){

            T t = bindValue(mCursor);

            lists.add(t);
        }

        return lists;
    }

    public T get(){

        if(mCursor.moveToFirst()){

            T t = bindValue(mCursor);

            return t;
        }

        return null;

    }

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public T bindValue(Cursor cursor){

        T t = null;
        try {
            t = mClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Class<?> clazz = t.getClass();

        Field[] fields = clazz.getDeclaredFields();

        for(Field field: fields){

            if(Modifier.isStatic(field.getModifiers())
                    || Modifier.isFinal(field.getModifiers())){
                continue;
            }

            // 不是映射的字段
            if(field.getAnnotation(Transient.class) != null) {
                continue;
            }

            Column column = field.getAnnotation(Column.class);
            ColumnType columnType = field.getAnnotation(ColumnType.class);
            String columnName = column != null ? column.name() : field.getName();

            Object value = null;
            Class<?> type = field.getType();
            int index = cursor.getColumnIndex(columnName);

            if(type == String.class){
                value = cursor.getString(index);
            }else if(type == Integer.class || type == int.class){
                value = cursor.getInt(index);
            }else if (type == Double.class || type == double.class){
                value = cursor.getDouble(index);
            }else if(type == Float.class || type == float.class){
                value = cursor.getFloat(index);
            }else if(type == Character.class || type == char.class){

            }else if(type == Long.class || type == long.class){
                value = cursor.getLong(index);
            }else if(type == Date.class){
                try {
                    String str = cursor.getString(index);
                    if(str == null) continue;

                    value = SIMPLE_DATE_FORMAT.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String methodName = "set" + String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);

            Method method = null;
            try {
                method = clazz.getMethod(methodName, type);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                continue;
            }

            if(value == null) continue;

            //
            try {
                method.invoke(t, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        return t;
    }
}
