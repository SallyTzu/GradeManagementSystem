package com.codeying.utils;

import com.codeying.entity.Admin;
import com.codeying.dao.AdminDao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class JdbcUtils {
    private static final String driverClass="com.mysql.cj.jdbc.Driver";
    private static final String url="jdbc:mysql://localhost:3306/app_score_judge?useSSL=false&serverTimezone=GMT%2B8";
    private static final String username="root";//自行修改用户名和密码
    private static final String password="12345";

    public static void main(String[] args) {
        AdminDao dao = AdminDao . me();
        List<Admin> list = dao.list();
        System.out.println("数据库连接成功！");
        System.out.println("获取到 " + list.size() + "条 管理员的数据");
    }

    private static Connection con ;
    private static long timeNow;
    static{  // 静态代码块,只会被执行一次
        try{
            Class.forName(driverClass);//加载数据库连接的驱动
            con = DriverManager.getConnection(url, username, password);//创建连接
            timeNow = new Date().getTime();
        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("数据库连接失败");//连接失败则抛错
        }
    }

    public static Connection getConnection(){
        //连接关闭了则重新连接
        try {
            if(new Date().getTime() - 180000 > timeNow){ //3分钟重新或者一次数据库连接
                timeNow = new Date().getTime();
                con.close();
                con = DriverManager.getConnection(url, username, password);//创建连接
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return con;
    }

    public static String getPageSql(String sql,Integer pageIndex,Integer pageSize){
        //这个情况不分页
        if(pageIndex == null || pageIndex < 1 || pageSize == null){
            return sql;
        }
        pageIndex = pageIndex - 1;
        StringBuilder res = new StringBuilder(sql);
        if(!JdbcUtils.driverClass.contains("sqlserver")){//my sqlite
            res.append(" limit ").append(pageIndex * pageSize).append(",").append(pageSize);
        }else {//sql
            if(!sql.contains("order by") ){
                res.append(" order by id ");
            }
            res.append(" offset ").append(pageIndex * pageSize).append(" rows fetch next ").append(pageSize).append(" rows only");
        }
        return res.toString();
    }

}

