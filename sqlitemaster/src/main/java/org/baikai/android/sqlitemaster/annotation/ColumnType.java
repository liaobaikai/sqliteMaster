package org.baikai.android.sqlitemaster.annotation;

import org.baikai.android.sqlitemaster.sql.JDBCType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnType {
	
	JDBCType jdbcType() default JDBCType.VARCHAR;

}
