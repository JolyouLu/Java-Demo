package top.jolyoulu.mybatis.utils;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

public class StatementHandlerMetaObject {

    private final MetaObject metaObject;

    public StatementHandlerMetaObject(StatementHandler statementHandler) {
        this.metaObject = SystemMetaObject.forObject(statementHandler);
    }

    public MappedStatement getMappedStatement() {
        return (MappedStatement) metaObject.getValue("delegate.mappedStatement");
    }

    public ParameterHandler getParameterHandler() {
        return (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
    }

    public void setBoundSql(String sql) {
        metaObject.setValue("delegate.boundSql.sql",sql);
    }
}