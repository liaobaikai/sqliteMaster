package org.baikai.android.sqlitemaster.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;


public class ConditionBuilder {
	
	private Class<?> clazz;
	private boolean isDesc;
	private boolean isAsc;
	private final StringBuilder condtion;
	private final List<Object> values;
	private final Map<String, Object> propertyValue;
	private boolean isWhere;
	
	public ConditionBuilder(Class<?> clazz){
		this.clazz = clazz;
		condtion = new StringBuilder();
		values = new ArrayList<>();
		propertyValue = new HashMap<>();
	}
	
	public Class<?> getHandleClass(){
		return clazz;
	}
	
	public ConditionBuilder eq(String properties, Object value){
		
		String columnName = getPropertyColumn(properties);
		
		if(condtion.length() > 0){
			condtion.append(" AND ");
		}
		
		condtion.append(columnName).append(" = ").append("?");
		
		String result = null;
		
		if(value instanceof Date){
			result = SQLBuilder.DATETIME_FORMAT.format(value);
		}else{
			result = String.valueOf(value);
		}
		
		values.add(result);
		propertyValue.put(properties, result);
		
		isWhere = true;
		
		return this;
	}
	
	public ConditionBuilder in(String properties, Object...values){
		isWhere = true;
		return this;
	}
	
	public ConditionBuilder notIn(String properties, Object...values){
		isWhere = true;
		return this;
	}
	
	public ConditionBuilder like(String properties, String value){
		
		String columnName = getPropertyColumn(properties);
		
		if(condtion.length() > 0){
			condtion.append(" AND ");
		}
		condtion.append(columnName).append(" = ").append("?");
		
		value = "%" + value + "%";
		values.add(value);
		propertyValue.put(properties, value);
		
		isWhere = true;
		
		return this;
	}
	
	private String getPropertyColumn(String properties){
		
		Field field = null;
		
		try {
			field = clazz.getDeclaredField(properties);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("No such field " + clazz.getName() + "." + properties);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		Column column = field.getAnnotation(Column.class);
		
		if(column != null){
			return column.name();
		}else{
			return field.getName();
		}
	}
	
	public ConditionBuilder desc(String... properties){
		isDesc = true;
		
		if(!isAsc){
			condtion.append(" ORDER BY ");
		}else{
			condtion.append(", ");
		}
		
		for(String p: properties){
			condtion.append(getPropertyColumn(p)).append(", ");
		}
		
		condtion.delete(condtion.length() - 2, condtion.length());
		condtion.append(" DESC");
		
		return this;
	}
	
	public ConditionBuilder asc(String... properties){
		isAsc = true;
		if(!isDesc){
			condtion.append(" ORDER BY ");
		}else{
			condtion.append(", ");
		}
		
		for(String p: properties){
			condtion.append(getPropertyColumn(p)).append(", ");
		}
		
		condtion.delete(condtion.length() - 2, condtion.length());
		
		condtion.append(" ASC");
		
		return this;
	}
	
	public String getSQL(){
		return condtion.toString();
	}
	
	public List<Object> getValues(){
		return values;
	}
	
	public Map<String, Object> getPropertyValues(){
		return propertyValue;
	}

	public boolean isWhere() {
		return isWhere;
	}

	
}
