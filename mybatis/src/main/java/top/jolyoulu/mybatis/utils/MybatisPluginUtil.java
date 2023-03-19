package top.jolyoulu.mybatis.utils;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;

import java.sql.*;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 15:02
 * @Description
 */
public class MybatisPluginUtil{

    /**
     * 获取StatementHandler的Sql字符串
     * @param statementHandler
     * @return
     */
    public static String getRawSqlStr(StatementHandler statementHandler){
        BoundSql boundSql = statementHandler.getBoundSql();
        return boundSql.getSql();
    }

    /**
     * 获取StatementHandler的Sql参数
     * @param statementHandler
     * @return
     */
    public static Map<String,Object> getSqlParam(StatementHandler statementHandler){
        BoundSql boundSql = statementHandler.getBoundSql();
        return (Map<String, Object>) boundSql.getParameterObject();
    }

    /**
     * 执行一条sql
     * @param conn 拦截器
     * @param parameterHandler sql中的参数
     * @param sql sql语句
     * @param resulHandle 结果处理者
     * @throws SQLException
     */
    public static void runQuerySql(Connection conn, ParameterHandler parameterHandler,String sql, Consumer<ResultSet> resulHandle) throws SQLException {
        PreparedStatement countStatement = conn.prepareStatement(sql);
        parameterHandler.setParameters(countStatement);
        ResultSet rs = countStatement.executeQuery();
        resulHandle.accept(rs);
        rs.close();
        countStatement.close();
    }

    /**
     * statementHandler 转成 metaObject
     */
    public static StatementHandlerMetaObject transition(StatementHandler statementHandler){
        return new StatementHandlerMetaObject(statementHandler);
    }

    /**
     * 打印ResultSet中的内容
     * 打印后将无法获取到内容
     */
    public static void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i < columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }
        System.out.println();
        while (resultSet.next()){
            for (int i = 1; i < columnCount; i++) {
                System.out.print(resultSet.getString(i)+"\t");
            }
            System.out.println();
        }
    }


}
