package org.baikai.android.sqlitemaster.sql.builder;

import org.baikai.android.sqlitemaster.ConfigLoader;
import org.baikai.android.sqlitemaster.annotation.ColumnType;
import org.baikai.android.sqlitemaster.sql.DDLSQLBuilder;
import org.baikai.android.sqlitemaster.sql.JDBCType;
import org.baikai.android.sqlitemaster.sql.SQLResolver;
import org.baikai.android.sqlitemaster.sql.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


public class SimpleDDLSQLBuilder implements DDLSQLBuilder {

	@Override
	public SQLResolver buildCreateTableSQL(Class<?> clazz) {
		
		String tableName = Utils.getTableName(clazz);
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName);
		sql.append("(");
		
		Field[] fields = clazz.getDeclaredFields();
		
		for(Field field: fields){
			
			if(Modifier.isStatic(field.getModifiers())
        			|| Modifier.isFinal(field.getModifiers())){
        		continue;
        	}
			
			StringBuilder columnStr = new StringBuilder();
			
			// 不是映射的字段
			if(field.getAnnotation(Transient.class) != null) {
				continue;
			}
			
			Column column = field.getAnnotation(Column.class);
			if(column != null){
				columnStr.append(column.name());
			}else{
				columnStr.append(field.getName());
			}
			// name varchar(32) primary key not null auto_increment
			
			ColumnType columnType = field.getAnnotation(ColumnType.class);
			
			if(columnType != null){
				columnStr.append(" ").append(columnType.jdbcType().name());
			}else{
				columnStr.append(" ").append(JDBCType.VARCHAR);
			}
			
			if (field.getType() == String.class) {
				columnStr.append("(").append(column.length() == 0 ? 64 : column.length()).append(")");
			}
			
			// id
			if(field.getAnnotation(Id.class) != null){
				columnStr.append(" NOT NULL PRIMARY KEY ");
			}
			
			if(column != null && !column.nullable()){
				columnStr.append(" NOT NULL ");
			}
			
			if(column != null && column.unique()){
				columnStr.append(" UNIQUE ");
			}
			
			// GeneratedValue
			GeneratedValue gv = field.getAnnotation(GeneratedValue.class);
			if(gv != null){
				if(gv.strategy().equals(GenerationType.IDENTITY)){
					columnStr.append(" AUTOINCREMENT");
				}
			}
			
			
			sql.append(columnStr).append(", ");
			
		}
		
		sql.delete(sql.length() - 2, sql.length());
		sql.append(")");
		
		SQLResolver resolver = new SQLResolver();
		resolver.setSqlText(sql.toString());

		if(ConfigLoader.RUNNING_LEVEL.equals(ConfigLoader.RunningLevel.DEBUG)) {
			System.out.println(sql.toString());
		}

		return resolver;
	}

	@Override
	public SQLResolver buildDropTableSQL(Class<?> clazz) {
		
		String tableName = Utils.getTableName(clazz);
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("DROP TABLE IF EXISTS ").append(tableName);
		
		SQLResolver resolver = new SQLResolver();
		resolver.setSqlText(sql.toString());

		if(ConfigLoader.RUNNING_LEVEL.equals(ConfigLoader.RunningLevel.DEBUG)) {
			System.out.println(sql.toString());
		}

		return resolver;
	}
	


}
