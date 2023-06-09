package com.codeying.dao;

import com.codeying.utils.JdbcUtils;
import com.codeying.utils.component.JdbcEntity;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;
import java.util.Map;


public abstract class AbstractDao<T> {
    protected String tbName;
    protected String clazzName = this.getClass().getName();

    public AbstractDao(String tbName) {
        this.tbName = tbName;
    }

    public abstract String toConditionSql(Map<String, String> paramMap);

    public abstract List<T> list(Map<String, String> paramMap);

    public abstract T getBy(String paramName, String val);

    public abstract int insert(T t);

    public abstract int updateById(T t);

    public T getBy(String paramName, String val, Class<T> clazz) {
        try {
            QueryRunner qr = new QueryRunner();
            String sqlBuilder = "SELECT * FROM " + tbName + " where " + paramName + " = ?";
            log(sqlBuilder + " 。 " + val);
            List<T> list = qr.query(JdbcUtils.getConnection(), sqlBuilder, new BeanListHandler<>(clazz), val);
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(clazzName + " -> getBy" + paramName + ": val:" + val + "报错了");
        }
        return null;
    }

    public List<T> list() {
        return list(null);
    }


    public Integer count(String lastSql) {
        try {
            QueryRunner qr = new QueryRunner();
            String sqlBuilder = "select count(1) as intP from " + tbName + " " + lastSql;
            log(sqlBuilder);
            List<JdbcEntity> list = qr.query(JdbcUtils.getConnection(),
                    sqlBuilder, null, new BeanListHandler<>(JdbcEntity.class));
            if (list.size() > 0) {
                return list.get(0).getIntP();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(clazzName + " -> count()  " + "报错了");
        }
        return null;
    }

    public double avg(Map<String, String> paramMap, String column) {
        String conditionSql = toConditionSql(paramMap);
        try {
            QueryRunner qr = new QueryRunner();
            String sqlBuilder = "select avg(" + column + ") as doubleP from " + tbName + " where 1=1 " + conditionSql;
            log(sqlBuilder);
            List<JdbcEntity> list = qr.query(JdbcUtils.getConnection(), sqlBuilder,
                    null, new BeanListHandler<>(JdbcEntity.class));
            if (list.size() > 0 && list.get(0).getDoubleP() != null) {
                return list.get(0).getDoubleP();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(clazzName + " -> avg()  " + "报错了");
        }
        return 0;
    }

    public double sum(Map<String, String> paramMap, String column) {
        String conditionSql = toConditionSql(paramMap);
        try {
            QueryRunner qr = new QueryRunner();
            String sqlBuilder = "select sum(" + column + ") as doubleP from " + tbName + "  where 1=1 " + conditionSql;
            log(sqlBuilder);
            List<JdbcEntity> list = qr.query(JdbcUtils.getConnection(), sqlBuilder, null, new BeanListHandler<>(JdbcEntity.class));
            if (list.size() > 0 && list.get(0).getDoubleP() != null) {
                return list.get(0).getDoubleP();
            }
        } catch (Exception e) {
            System.out.println(clazzName + " -> sum()  " + "报错了");
        }
        return 0;
    }

    public int deleteById(String id) {
        try {
            QueryRunner qr = new QueryRunner();  //创建QueryRunner类对象
            String sqlBuilder = "delete from " + tbName + " where id = ?";
            //输出sql语句
            log(sqlBuilder + " 。 " + id);
            return qr.update(JdbcUtils.getConnection(), sqlBuilder, id);  // 执行SQL语句
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(clazzName + " -> deleteById(id)报错了");
        }
        return 0;
    }

    protected void log(String sql) {
        log(sql, null);
    }

    protected void log(String sql, List<Object> params) {
        System.out.println("-------------------------------");
        System.out.println(">>>执行sql语句：" + sql);
        if (params != null) {
            System.out.print(">>>参数：");
            for (Object o : params) {
                System.out.print("[" + o + "] ");
            }
        }
        System.out.println("\n-------------------------------");
    }
}
