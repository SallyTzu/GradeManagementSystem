package com.codeying.dao;

import com.codeying.entity.*;
import com.codeying.utils.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.sql.SQLException;

import java.util.*;

public class CourseDao extends AbstractDao<Course>{

    private static CourseDao thisDao = new CourseDao("tb_course");

    public CourseDao(String tbName) {super(tbName);}

    public static CourseDao me(){return thisDao;}

    @Override
    public String toConditionSql(Map<String, String> paramMap){
        StringBuilder sqlBuilder=new StringBuilder("");//接下来拼接查询条件,比如 and xx=xx and XX < xx
        if(paramMap!=null){
                String id = paramMap.get("id");
            if(MyUtils.isNotEmpty(id)){
                        sqlBuilder.append(" and id like '%").append(id).append("%'");// 使用模糊查询
                    }
                    String teacherid = paramMap.get("teacherid");
            if(MyUtils.isNotEmpty(teacherid)){
                         sqlBuilder.append(" and teacherid = '").append(teacherid).append("'");
                    }
                    String name = paramMap.get("name");
            if(MyUtils.isNotEmpty(name)){
                        sqlBuilder.append(" and name like '%").append(name).append("%'");// 使用模糊查询
                    }
                    if(MyUtils.isNotEmpty(paramMap.get("courseL"))){
                sqlBuilder.append(" and course >= ").append(paramMap.get("courseL"));
            }
            if(MyUtils.isNotEmpty(paramMap.get("courseR"))){
                sqlBuilder.append(" and course <= ").append(paramMap.get("courseR"));
            }
                    if(MyUtils.isNotEmpty(paramMap.get("stutimeL"))){
                sqlBuilder.append(" and stutime >= ").append(paramMap.get("stutimeL"));
            }
            if(MyUtils.isNotEmpty(paramMap.get("stutimeR"))){
                sqlBuilder.append(" and stutime <= ").append(paramMap.get("stutimeR"));
            }
                //限制 教师 只能查询自己相关的信息
            String teacheridFrn=(String)paramMap.get("teacherid");//只能查看和自己相关的内容
            if(MyUtils.isNotEmpty(teacheridFrn)){
                sqlBuilder.append(" and teacherid = '").append(teacheridFrn).append("'");
            }
            //排序
            String orderby = paramMap.get("orderby");
            if(orderby!=null){
                sqlBuilder.append(" order by ");
                String[] orderbys = orderby.split(",");
                for (int i = 0 ; i < orderbys.length ; i++) {
                    String s = orderbys[i];
                    sqlBuilder.append(s);
                    if(i!=orderbys.length-1){
                        sqlBuilder.append(",");
                    }
                }
            }
        }
        return sqlBuilder.toString();
    }

    @Override
    public Course getBy(String paramName, String val) {
        Course t = getBy(paramName,val,Course.class);
        return t;
    }

    @Override
    public List<Course> list(Map<String,String> paramMap){
        try{
            QueryRunner qr = new QueryRunner();
            //将参数列表转换成查询条件
            String queryCondition = toConditionSql(paramMap);
            //获取分页信息
            Integer pageIndex = 1;
            Integer pageSize = null;
            if(paramMap!=null){
                try {
                    pageIndex = Integer.parseInt( paramMap.get("pageIndex"));
                    pageSize = Integer.parseInt(paramMap.get("pageSize"));
                }catch (Exception e){}
            }
            //接下来拼接查询条件，然后获取分页sql
            String sqlRes = JdbcUtils.getPageSql("SELECT * FROM tb_course where 1=1 " + queryCondition, pageIndex,pageSize);
            //打印sql语句和参数
            log(sqlRes);
            //执行查询
            List<Course> list = qr.query(JdbcUtils.getConnection(), sqlRes, new BeanListHandler<>(Course.class));
            return list;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("CourseDao -> list(...)报错了");
        }
        return new ArrayList();
    }

    @Override
    public int insert(Course entity){
        try{
            //构造sql语句
            List<Object> paramList = new ArrayList<>();
            StringBuilder reParamNames = new StringBuilder();
            StringBuilder reParams = new StringBuilder();
            paramList.add(entity.getId());
                if(entity.getTeacherid() != null){
                reParamNames.append(",teacherid");
                reParams.append(",?");
                paramList.add(entity.getTeacherid());
            }
                if(entity.getName() != null){
                reParamNames.append(",name");
                reParams.append(",?");
                paramList.add(entity.getName());
            }
                if(entity.getCourse() != null){
                reParamNames.append(",course");
                reParams.append(",?");
                paramList.add(entity.getCourse());
            }
                if(entity.getStutime() != null){
                reParamNames.append(",stutime");
                reParams.append(",?");
                paramList.add(entity.getStutime());
            }
                QueryRunner qr = new QueryRunner();  //创建QueryRunner类对象
            //不为空的插入
            String sqlBuilder = "INSERT INTO tb_course (id"+reParamNames.toString()+")VALUES(?"+reParams.toString()+")";
            log(sqlBuilder.toString());
            return qr.update(JdbcUtils.getConnection(), sqlBuilder, paramList.toArray());  // 执行SQL语句
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("CourseDao -> insert(-)报错了");
        }
        return 0;
    }

    @Override
    public int updateById(Course entity) {
        try{
            QueryRunner qr = new QueryRunner();  //创建QueryRunner类对象
            StringBuilder sqlBuilder = new StringBuilder("update tb_course set id = ? ");
            List<Object> paramList = new ArrayList<>();
            paramList.add(entity.getId());
                    if(entity.getTeacherid() != null){
                sqlBuilder.append(", teacherid = ? ");
                paramList.add(entity.getTeacherid());
            }
                    if(entity.getName() != null){
                sqlBuilder.append(", name = ? ");
                paramList.add(entity.getName());
            }
                    if(entity.getCourse() != null){
                sqlBuilder.append(", course = ? ");
                paramList.add(entity.getCourse());
            }
                    if(entity.getStutime() != null){
                sqlBuilder.append(", stutime = ? ");
                paramList.add(entity.getStutime());
            }
                //最后拼接id条件
            sqlBuilder.append("where id = ?");
            paramList.add(entity.getId());
            log(sqlBuilder.toString(),paramList);
            return qr.update(JdbcUtils.getConnection(), sqlBuilder.toString(), paramList.toArray());  // 执行SQL语句
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("CourseDao -> updateById(-)报错了");
        }
        return 0;
    }

}

