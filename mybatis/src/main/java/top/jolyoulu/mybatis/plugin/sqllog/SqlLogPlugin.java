package top.jolyoulu.mybatis.plugin.sqllog;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 1:07
 * @Description
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Slf4j
public class SqlLogPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取 MappedStatement(mapper映射)
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        //获取 Parameter(参数映射)
        Object parameter = invocation.getArgs()[1];
        //获取 BoundSql(里面封装了sql与参数)
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        //获取 Configuration(配置)
        Configuration configuration = mappedStatement.getConfiguration();

        long start = System.currentTimeMillis();
        Object res = null;
        try {
            res = invocation.proceed();
        }finally {
            long useTime = System.currentTimeMillis() - start;
            String sql = getSql(configuration, boundSql);
            log.info("SQL语句 ==> {}",sql);
            log.info("SQL耗时 ==> {} ms",useTime);
        }
        return res;
    }

    private String getSql(Configuration configuration,BoundSql boundSql){
        String sql = boundSql.getSql();
        if (StringUtils.isBlank(sql)){
            return "";
        }
        sql = beautifySql(sql);
        //获取入参对象
        Object parameterObject = boundSql.getParameterObject();
        //获取入参的占位符(xml中的#{})
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        if (!parameterMappings.isEmpty() && Objects.nonNull(parameterObject)){
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterMappings.getClass())){
                sql = replaceSql(sql,parameterObject);
            }else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)){
                        Object value = metaObject.getValue(propertyName);
                        sql = replaceSql(sql,value);
                    }else if (boundSql.hasAdditionalParameter(propertyName)){
                        Object value = boundSql.getAdditionalParameter(propertyName);
                        sql = replaceSql(sql,value);
                    }
                }
            }
        }
        return sql;
    }

    //格式化sql中的？参数
    private String replaceSql(String sql, Object parameterObject) {
        String res;
        if (parameterObject instanceof String){
            res = "'" + parameterObject + "'";
        }else if (parameterObject instanceof Date){
            res = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parameterObject) + "'";
        }else {
            res = parameterObject.toString();
        }
        return sql.replaceFirst("\\?",res);
    }

    //美化sql
    private String beautifySql(String sql) {
        return sql.replaceAll("[\\s\n]+"," ");
    }
}
