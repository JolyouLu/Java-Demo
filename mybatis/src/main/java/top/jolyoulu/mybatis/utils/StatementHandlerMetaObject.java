package top.jolyoulu.mybatis.utils;

import lombok.Data;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

@Data
public class StatementHandlerMetaObject {

    private final MetaObject metaObject;

    public StatementHandlerMetaObject(StatementHandler statementHandler) {
        this.metaObject = SystemMetaObject.forObject(statementHandler);
    }

    public MappedStatement getMappedStatement() {
        if (isProxy()){
            return (MappedStatement) metaObject.getValue("h.target.delegate.mappedStatement");
        }else {
            return (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        }
    }

    public ParameterHandler getParameterHandler() {
        if (isProxy()){
            return (ParameterHandler) metaObject.getValue("h.target.delegate.parameterHandler");
        }else {
            return (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
        }
    }

    public void setBoundSql(String sql) {
        if (isProxy()){
            metaObject.setValue("h.target.delegate.boundSql.sql",sql);
        }else {
            metaObject.setValue("delegate.boundSql.sql",sql);
        }
    }

    /**
     * 是否是代理对象
     */
    public boolean isProxy() {
        return metaObject.hasGetter("h.target");
    }
}