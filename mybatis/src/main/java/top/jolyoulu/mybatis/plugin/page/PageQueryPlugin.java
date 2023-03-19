package top.jolyoulu.mybatis.plugin.page;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import top.jolyoulu.mybatis.plugin.AbstractPlugin;
import top.jolyoulu.mybatis.utils.MybatisPluginUtil;
import top.jolyoulu.mybatis.utils.StatementHandlerMetaObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 15:01
 * @Description
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@Slf4j
public class PageQueryPlugin extends AbstractPlugin implements Interceptor {

    public PageQueryPlugin() {
        super();
    }

    public PageQueryPlugin(boolean debug) {
        super(debug);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        //statementHandler 转成 metaObject
        StatementHandlerMetaObject metaObject = MybatisPluginUtil.transition(statementHandler);
        //获取mapped的上下文
        MappedStatement mappedStatement = metaObject.getMappedStatement();
        //获取执行的sql的mapperId(这里可以根据apperId做一些限制，只对某些id进行分页查询)
        String mappedId = mappedStatement.getId();
        log("执行的mapperId ==> {}", mappedId);

        //获取sql的参数,获取查询里面的page参数
//        Map<String, Object> sqlParam = MybatisPluginUtil.getSqlParam(statementHandler);
//        JlPage jlPage = getJlPage(sqlParam);
        JlPage<?> jlPage = JlPageLocal.get();
        if (!Objects.isNull(jlPage)) { //找不到就直接过了
            if (jlPage.getOptimize()){
                optimizeCount(invocation,metaObject,jlPage);
            }else {
                notOptimizeCount(invocation,metaObject,jlPage);
            }
            //改造原始sql 添加limit
            String sql = MybatisPluginUtil.getRawSqlStr(statementHandler);
            log("原始sql ==> {}", sql);
            String limitSql = gentLimitSql(sql,jlPage);
            log("执行的分页sql ==> {}", limitSql);
            metaObject.setBoundSql(limitSql);
        }
        return invocation.proceed();
    }

    //利用EXPLAIN优化COUNT
    private void optimizeCount(Invocation invocation,StatementHandlerMetaObject metaObject,JlPage jlPage) throws SQLException {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        String sql = MybatisPluginUtil.getRawSqlStr(statementHandler);
        //拼接一条查询sql
        String countSql = "EXPLAIN " +gentCountSql(sql);
        log("执行的countSql ==> {}", countSql);
        MybatisPluginUtil.runQuerySql((Connection) invocation.getArgs()[0],
                metaObject.getParameterHandler(),
                countSql,
                rs -> {
                    try {
                        if (rs.next()) {
                            jlPage.setTotal(rs.getLong("rows"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    //非优化的COUNT，默认的分页合计
    private void notOptimizeCount(Invocation invocation,StatementHandlerMetaObject metaObject,JlPage jlPage) throws SQLException {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        String sql = MybatisPluginUtil.getRawSqlStr(statementHandler);
        //拼接一条查询sql
        String countSql = gentCountSql(sql);
        //log.info("执行的countSql ==> {}", countSql);
        MybatisPluginUtil.runQuerySql((Connection) invocation.getArgs()[0],
                metaObject.getParameterHandler(),
                countSql,
                rs -> {
                    try {
                        if (rs.next()) {
                            jlPage.setTotal(rs.getLong(1));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    //获取COUNT SQL
    private String gentCountSql(String sql) {
        return "SELECT COUNT(*) FROM (" + sql + ") a";
    }

    //拼接LIMIT SQL
    private String gentLimitSql(String sql, JlPage jlPage) {
        return sql + " limit " + jlPage.getLimitStart() + " , " + jlPage.getPageSize();
    }

    //从sqlParam中获取JlPage
    private JlPage getJlPage(Map<String, Object> sqlParam) {
        Object page = sqlParam.get("page");
        if (page instanceof JlPage) {
            return (JlPage) page;
        }
        return null;
    }
}
