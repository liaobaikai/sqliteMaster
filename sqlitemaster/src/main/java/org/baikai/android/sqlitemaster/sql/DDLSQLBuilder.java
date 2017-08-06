package org.baikai.android.sqlitemaster.sql;

public interface DDLSQLBuilder {
	
	SQLResolver buildCreateTableSQL(Class<?> clazz);
	
	SQLResolver buildDropTableSQL(Class<?> clazz);
	

}
