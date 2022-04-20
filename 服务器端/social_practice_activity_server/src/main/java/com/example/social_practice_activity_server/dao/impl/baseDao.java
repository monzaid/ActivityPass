package com.example.social_practice_activity_server.dao.impl;

import com.example.social_practice_activity_server.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class baseDao {
    private QueryRunner queryRunner = new QueryRunner();

    /**
     * 数据库添加，更新，删除操作
     * @param sql sql语句
     * @param args 参数
     * @return 返回 -1 说明操作失败， 否则返回数据库对应的操作行数
     */
    public int update(String sql, Object ... args){
        Connection connection = JdbcUtils.getConnection();
        try {
            return queryRunner.update(connection, sql, args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
        return -1;
    }

    /**
     * 数据库查询操作（返回一个对象）
     * @param type 返回对象的类型
     * @param sql sql语句
     * @param args 参数
     * @param <T> 返回类型的泛型
     * @return 返回 null 说明操作失败， 否则返回数据库对应的查询结果对象
     */
    public <T> T queryForOne(Class<T> type, String sql, Object ... args){
        Connection connection = JdbcUtils.getConnection();
        try {
            return queryRunner.query(connection, sql, new BeanHandler<>(type), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
        return null;
    }

    /**
     * 数据库查询操作（返回多个对象）
     * @param type 返回对象的类型
     * @param sql sql语句
     * @param args 参数
     * @param <T> 返回类型的泛型
     * @return 返回 null / [] 说明操作失败， 否则返回数据库对应的查询结果对象列表
     */
    public <T> List<T> queryForAll(Class<T> type, String sql, Object ... args){
        Connection connection = JdbcUtils.getConnection();
        try {
            return queryRunner.query(connection, sql, new BeanListHandler<>(type), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
        return null;
    }

    /**
     * 数据库查询操作（返回一个属性和值）
     * @param sql sql语句
     * @param args 参数
     * @return 返回 null 说明操作失败， 否则返回数据库对应的查询结果对象
     */
    public Object queryForSingleValue(String sql, Object ... args){
        Connection connection = JdbcUtils.getConnection();
        try {
            return queryRunner.query(connection, sql, new ScalarHandler(), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
        return null;
    }
}
