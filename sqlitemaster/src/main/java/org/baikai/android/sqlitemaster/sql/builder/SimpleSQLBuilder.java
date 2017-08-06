package org.baikai.android.sqlitemaster.sql.builder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.baikai.android.sqlitemaster.ConfigLoader;
import org.baikai.android.sqlitemaster.annotation.ColumnType;
import org.baikai.android.sqlitemaster.sql.ConditionBuilder;
import org.baikai.android.sqlitemaster.sql.JDBCType;
import org.baikai.android.sqlitemaster.sql.SQLBuilder;
import org.baikai.android.sqlitemaster.sql.SQLResolver;
import org.baikai.android.sqlitemaster.sql.Utils;


public class SimpleSQLBuilder implements SQLBuilder {

	@Override
	public SQLResolver buildInsertSQL(Object bean, boolean fill, boolean replace) {
		
		StringBuffer sql = new StringBuffer("INSERT");

		if(replace){
			sql.append(" OR REPLACE");
		}

		sql.append(" INTO ");

        Class<?> clazz = bean.getClass();

		String tableName = Utils.getTableName(clazz);
        sql.append(tableName);
        
        sql.append(" (");
        
        //
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> dataMap = null;
        String str = "";
        List<Object> values = null;
        
        if(! fill){
        	dataMap = new HashMap<>();
            values = new ArrayList<>();
        }
        
        
        for(Field field: fields){
        	
        	if(Modifier.isStatic(field.getModifiers())
        			|| Modifier.isFinal(field.getModifiers())){
        		continue;
        	}

			// 不是映射的字段
			if(field.getAnnotation(Transient.class) != null) {
				continue;
			}
        	
        	String name = field.getName();
        	
        	String methodName = null;
        	
        	if(field.getType() == boolean.class){
        		methodName = "is";
        	}else{
        		methodName = "get";
        	}
        	
        	methodName += String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
        	
        	Object value = null;
        	
        	try {
				Method method = clazz.getMethod(methodName, new Class<?>[]{});
				
				try {
					
					value = method.invoke(bean, new Object[0]);
					
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
        	
        	// 获取@Column 的值
        	Column column = field.getAnnotation(Column.class);
        	if(column != null && column.name().length() > 0){
        		name = column.name();
        	}
        	
        	Object result = getResult(value, field);
        	
        	if(!fill){
        		values.add(result);
        		dataMap.put(name, result);
        	}
        	
        	sql.append(name).append(", ");
        	
        	if(fill){
        		if(field.getType() == String.class || field.getType() == Date.class){
        			result = "'" + result + "'";
        		}
        		str += result + ", ";
        	}else{
        		str += "?, ";
        	}
        	
        }
        
        sql.delete(sql.length() - 2, sql.length());
        str = str.substring(0, str.length() - 2);
        
        sql.append(") VALUES (").append(str).append(")");
        
        SQLResolver resolver = new SQLResolver();
        resolver.setSqlText(sql.toString());
    	resolver.setColumnValue(dataMap);
    	resolver.setValues(values);
		resolver.setTableName(tableName);

		if(ConfigLoader.RUNNING_LEVEL.equals(ConfigLoader.RunningLevel.DEBUG)){
			System.out.println(resolver.getSqlText());

			Iterator<Map.Entry<String, Object>> iterator = dataMap.entrySet().iterator();

			for(; iterator.hasNext(); ){
				Map.Entry<String, Object> en = iterator.next();
				System.out.print(en.getKey() + "=" + en.getValue() + " ");
			}
			System.out.println();
		}
        
		return resolver;
	}

	private Object getResult(Object value, Field field) {

		if(value == null) return null;

		Object result = value;

		Class<?> type = field.getType();
		
		if(type == Date.class){
			ColumnType cType = field.getAnnotation(ColumnType.class);
			if(cType != null){
				if(cType.jdbcType() == JDBCType.DATE){
					result = DATE_FORMAT.format(value);
				}else{
					result = DATETIME_FORMAT.format(value);
				}
			}else{
				result = DATETIME_FORMAT.format(value);
			}
		}
		
		return result;
	}

	@Override
	public SQLResolver buildUpdateSQL(Object bean, ConditionBuilder condition, boolean fill, boolean ignoreNullProperty) {
		
		StringBuffer sql = new StringBuffer("UPDATE ");
		Class<?> clazz = bean.getClass();
		String tableName = Utils.getTableName(clazz);
		sql.append(tableName);
        sql.append(" SET ");
        
        //
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> dataMap = null;
        List<Object> values = null;
        
        if(! fill){
        	dataMap = new HashMap<>();
            values = new ArrayList<>();
        }
        
        
        for(Field field: fields){
        	
        	if(Modifier.isStatic(field.getModifiers())
        			|| Modifier.isFinal(field.getModifiers())){
        		continue;
        	}

			// 不是映射的字段
			if(field.getAnnotation(Transient.class) != null) {
				continue;
			}

        	String name = field.getName();
        	
        	String methodName = null;
        	
        	if(field.getType() == boolean.class){
        		methodName = "is";
        	}else{
        		methodName = "get";
        	}
        	
        	methodName += String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
        	
        	Object value = null;
        	
        	try {
				Method method = clazz.getMethod(methodName, new Class<?>[]{});
				
				try {
					
					value = method.invoke(bean, new Object[0]);
					
					if(ignoreNullProperty && value == null){
						continue;
					}
					
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
        	
        	// 获取@Column 的值
        	Column column = field.getAnnotation(Column.class);
        	if(column != null && column.name().length() > 0){
        		name = column.name();
        	}

			Object result = getResult(value, field);
        	
        	if(!fill){
        		values.add(result);
        		dataMap.put(name, result);
        	}
        	
        	sql.append(name).append(" = ");
        	
        	if(fill){
        		if(field.getType() == String.class || field.getType() == Date.class){
        			result = "'" + result + "'";
        		}
        		sql.append(result);
        	}else{
        		sql.append("?");
        	}
        	
        	sql.append(", ");
        	
        }
        
        sql.delete(sql.length() - 2, sql.length());
        
        if(condition != null){
        	if(condition.isWhere())
        		sql.append(" WHERE ");
        	sql.append(condition.getSQL());
        	
        	dataMap.putAll(condition.getPropertyValues());
        	values.addAll(condition.getValues());
        }
        
        SQLResolver resolver = new SQLResolver();
        resolver.setSqlText(sql.toString());
    	resolver.setColumnValue(dataMap);
    	resolver.setValues(values);
		resolver.setTableName(tableName);

		if(ConfigLoader.RUNNING_LEVEL.equals(ConfigLoader.RunningLevel.DEBUG)){
			System.out.println(resolver.getSqlText());
			Iterator<Map.Entry<String, Object>> iterator = dataMap.entrySet().iterator();

			for(; iterator.hasNext(); ){
				Map.Entry<String, Object> en = iterator.next();
				System.out.print(en.getKey() + "=" + en.getValue() + " ");
			}
			System.out.println();
		}
        
		return resolver;
	}


	@Override
	public SQLResolver buildSelectSQL(Class<?> clazz, ConditionBuilder condition, boolean fill, String... properties) {
		
		StringBuffer sql = new StringBuffer("SELECT ");

        ArrayList<String> columns = new ArrayList<>();
        
        if(properties.length == 0){
        	
        	Field[] fields = clazz.getDeclaredFields();
        	
        	for(Field field :fields){
        		if(Modifier.isStatic(field.getModifiers())
            			|| Modifier.isFinal(field.getModifiers())){
            		continue;
            	}
				// 不是映射的字段
				if(field.getAnnotation(Transient.class) != null) {
					continue;
				}
        		columns.add(getPropertyColumn(field));
        	}
        	
        }else{
        	for(String p: properties){
        		try {
        			Field field = clazz.getDeclaredField(p);
        			columns.add(getPropertyColumn(field));
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
        	}
        }

        
        for(String column: columns){
        	sql.append(column).append(", ");
        }
        
        sql.delete(sql.length() - 2, sql.length());
        sql.append(" FROM ");
		String tableName = Utils.getTableName(clazz);
		sql.append(tableName);
        
        
        SQLResolver resolver = new SQLResolver();
        if(condition != null){
        	if(condition.isWhere())
        		sql.append(" WHERE ");
        	sql.append(condition.getSQL());
        	
        	resolver.setColumnValue(condition.getPropertyValues());
        	resolver.setValues(condition.getValues());
        }
        resolver.setSqlText(sql.toString());
		resolver.setTableName(tableName);

		if(ConfigLoader.RUNNING_LEVEL.equals(ConfigLoader.RunningLevel.DEBUG)){
			System.out.println(resolver.getSqlText());

			if(condition != null) {
				Iterator<Map.Entry<String, Object>> iterator = condition.getPropertyValues().entrySet().iterator();

				for (; iterator.hasNext(); ) {
					Map.Entry<String, Object> en = iterator.next();
					System.out.print(en.getKey() + "=" + en.getValue() + " ");
				}
				System.out.println();
			}
		}
        
		return resolver;
	}
	
	
	private String getPropertyColumn(Field field){
		
		Column column = field.getAnnotation(Column.class);
		
		if(column != null){
			return column.name();
		}else{
			return field.getName();
		}
	}

	@Override
	public SQLResolver buildDeleteSQL(ConditionBuilder condition, boolean fill) {
		
		StringBuilder sql = new StringBuilder("DELETE FROM ");

		// condition
		SQLResolver resolver = new SQLResolver();
		if(condition != null){
			Class<?> clazz = condition.getHandleClass();
			String tableName = Utils.getTableName(clazz);
			resolver.setTableName(tableName);
			sql.append(tableName);
			if(condition.isWhere())
        		sql.append(" WHERE ");
        	sql.append(condition.getSQL());
        	
        	resolver.setColumnValue(condition.getPropertyValues());
        	resolver.setValues(condition.getValues());
		}
		resolver.setSqlText(sql.toString());
		if(ConfigLoader.RUNNING_LEVEL.equals(ConfigLoader.RunningLevel.DEBUG)){
			System.out.println(resolver.getSqlText());

			if(condition != null) {
				Iterator<Map.Entry<String, Object>> iterator = condition.getPropertyValues().entrySet().iterator();

				for (; iterator.hasNext(); ) {
					Map.Entry<String, Object> en = iterator.next();
					System.out.print(en.getKey() + "=" + en.getValue() + " ");
				}
				System.out.println();
			}
		}
		
		return resolver;
	}

	@Override
	public SQLResolver buildSelectCountSQL(ConditionBuilder condition){


		StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");

		// condition
		SQLResolver resolver = new SQLResolver();
		if(condition != null){
			Class<?> clazz = condition.getHandleClass();
			String tableName = Utils.getTableName(clazz);
			resolver.setTableName(tableName);
			sql.append(tableName);
			if(condition.isWhere())
				sql.append(" WHERE ");
			sql.append(condition.getSQL());

			resolver.setColumnValue(condition.getPropertyValues());
			resolver.setValues(condition.getValues());
		}

		resolver.setSqlText(sql.toString());

		if(ConfigLoader.RUNNING_LEVEL.equals(ConfigLoader.RunningLevel.DEBUG)){
			System.out.println(resolver.getSqlText());

			if(condition != null) {
				Iterator<Map.Entry<String, Object>> iterator = condition.getPropertyValues().entrySet().iterator();

				for (; iterator.hasNext(); ) {
					Map.Entry<String, Object> en = iterator.next();
					System.out.print(en.getKey() + "=" + en.getValue() + " ");
				}
				System.out.println();
			}
		}

		return resolver;
	}
}
