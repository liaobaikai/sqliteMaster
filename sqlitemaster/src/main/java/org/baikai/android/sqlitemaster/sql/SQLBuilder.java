package org.baikai.android.sqlitemaster.sql;

import java.text.SimpleDateFormat;

public interface SQLBuilder {
	
	SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	SQLResolver buildDeleteSQL(ConditionBuilder condition, boolean fill);
	
	SQLResolver buildInsertSQL(Object bean, boolean fill, boolean replace);
	
	SQLResolver buildUpdateSQL(Object bean, ConditionBuilder condition, boolean fill, boolean ignoreNullProperty);
	
	SQLResolver buildSelectSQL(Class<?> clazz, ConditionBuilder condition, boolean fill, String... properties);

	SQLResolver buildSelectCountSQL(ConditionBuilder condition);

}
