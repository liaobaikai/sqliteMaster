package org.baikai.android.sqlitemaster.sql;

import java.util.List;
import java.util.Map;

public class SQLResolver {

    private String tableName;

    private String sqlText;

    private List<Object> values;

    private Map<String, Object> columnValue;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public Map<String, Object> getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(Map<String, Object> columnValue) {
        this.columnValue = columnValue;
    }

    @Override
    public String toString() {
        return "SQLResolver [sqlText=" + sqlText + ", values=" + values + ", columnValue=" + columnValue + "]";
    }


}
