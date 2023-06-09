package com.codeying.dao;

import com.codeying.entity.*;
import com.codeying.utils.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.sql.SQLException;

import java.util.*;

public class StudentDao extends AbstractDao<Student>{

    private static StudentDao thisDao = new StudentDao("tb_student");

    public StudentDao(String tbName) {super(tbName);}

    public static StudentDao me(){return thisDao;}

    public Student login(String username,String password){
        QueryRunner qr = new QueryRunner();
        String sqlBuilder = "SELECT * FROM tb_student where username = ? and password = ?";
        List<Student> list = null;
        try {
            log(sqlBuilder+" 。 "+username+" , "+password);
            list = qr.query(JdbcUtils.getConnection(), sqlBuilder,
                    new BeanListHandler<>(Student.class),username,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //获取查询出的第一个用户
        if(list.size()>0){
            return list.get(0);
        }
        //若没查到，则返回空
        return null;
    }

    @Override
    public String toConditionSql(Map<String, String> paramMap){
        StringBuilder sqlBuilder=new StringBuilder("");
        if(paramMap!=null){
                String id = paramMap.get("id");
            if(MyUtils.isNotEmpty(id)){
                        sqlBuilder.append(" and id like '%").append(id).append("%'");
                    }
                    String username = paramMap.get("username");
            if(MyUtils.isNotEmpty(username)){
                        sqlBuilder.append(" and username like '%").append(username).append("%'");
                    }
                    String password = paramMap.get("password");
            if(MyUtils.isNotEmpty(password)){
                        sqlBuilder.append(" and password like '%").append(password).append("%'");
                    }
                    String name = paramMap.get("name");
            if(MyUtils.isNotEmpty(name)){
                        sqlBuilder.append(" and name like '%").append(name).append("%'");
                    }
                    String numb = paramMap.get("numb");
            if(MyUtils.isNotEmpty(numb)){
                        sqlBuilder.append(" and numb like '%").append(numb).append("%'");
                    }
                    String gender = paramMap.get("gender");
            if(MyUtils.isNotEmpty(gender)){
                         sqlBuilder.append(" and gender = '").append(gender).append("'");
                    }
                    if(MyUtils.isNotEmpty(paramMap.get("ageL"))){
                sqlBuilder.append(" and age >= ").append(paramMap.get("ageL"));
            }
            if(MyUtils.isNotEmpty(paramMap.get("ageR"))){
                sqlBuilder.append(" and age <= ").append(paramMap.get("ageR"));
            }
                    String clazz = paramMap.get("clazz");
            if(MyUtils.isNotEmpty(clazz)){
                        sqlBuilder.append(" and clazz like '%").append(clazz).append("%'");
                    }

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
    public Student getBy(String paramName, String val) {
        Student t = getBy(paramName,val,Student.class);
        return t;
    }

    @Override
    public List<Student> list(Map<String,String> paramMap){
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
            String sqlRes = JdbcUtils.getPageSql("SELECT * FROM tb_student where 1=1 " + queryCondition, pageIndex,pageSize);
            //打印sql语句和参数
            log(sqlRes);
            //执行查询
            List<Student> list = qr.query(JdbcUtils.getConnection(), sqlRes, new BeanListHandler<>(Student.class));
            return list;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("StudentDao -> list(...)报错了");
        }
        return new ArrayList();
    }

    /**
    * 插入
    */
    @Override
    public int insert(Student entity){
        try{
            //构造sql语句
            List<Object> paramList = new ArrayList<>();
            StringBuilder reParamNames = new StringBuilder();
            StringBuilder reParams = new StringBuilder();
            paramList.add(entity.getId());
                if(entity.getUsername() != null){
                reParamNames.append(",username");
                reParams.append(",?");
                paramList.add(entity.getUsername());
            }
                if(entity.getPassword() != null){
                reParamNames.append(",password");
                reParams.append(",?");
                paramList.add(entity.getPassword());
            }
                if(entity.getName() != null){
                reParamNames.append(",name");
                reParams.append(",?");
                paramList.add(entity.getName());
            }
                if(entity.getNumb() != null){
                reParamNames.append(",numb");
                reParams.append(",?");
                paramList.add(entity.getNumb());
            }
                if(entity.getGender() != null){
                reParamNames.append(",gender");
                reParams.append(",?");
                paramList.add(entity.getGender());
            }
                if(entity.getAge() != null){
                reParamNames.append(",age");
                reParams.append(",?");
                paramList.add(entity.getAge());
            }
                if(entity.getClazz() != null){
                reParamNames.append(",clazz");
                reParams.append(",?");
                paramList.add(entity.getClazz());
            }
                QueryRunner qr = new QueryRunner();
            //不为空的插入
            String sqlBuilder = "INSERT INTO tb_student (id"+reParamNames.toString()+")VALUES(?"+reParams.toString()+")";
            log(sqlBuilder.toString());
            return qr.update(JdbcUtils.getConnection(), sqlBuilder, paramList.toArray());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("StudentDao -> insert(-)报错了");
        }
        return 0;
    }

    @Override
    public int updateById(Student entity) {
        try{
            QueryRunner qr = new QueryRunner();
            StringBuilder sqlBuilder = new StringBuilder("update tb_student set id = ? ");
            List<Object> paramList = new ArrayList<>();
            paramList.add(entity.getId());
                    if(entity.getUsername() != null){
                sqlBuilder.append(", username = ? ");
                paramList.add(entity.getUsername());
            }
                    if(entity.getPassword() != null){
                sqlBuilder.append(", password = ? ");
                paramList.add(entity.getPassword());
            }
                    if(entity.getName() != null){
                sqlBuilder.append(", name = ? ");
                paramList.add(entity.getName());
            }
                    if(entity.getNumb() != null){
                sqlBuilder.append(", numb = ? ");
                paramList.add(entity.getNumb());
            }
                    if(entity.getGender() != null){
                sqlBuilder.append(", gender = ? ");
                paramList.add(entity.getGender());
            }
                    if(entity.getAge() != null){
                sqlBuilder.append(", age = ? ");
                paramList.add(entity.getAge());
            }
                    if(entity.getClazz() != null){
                sqlBuilder.append(", clazz = ? ");
                paramList.add(entity.getClazz());
            }
                //最后拼接id条件
            sqlBuilder.append("where id = ?");
            paramList.add(entity.getId());
            log(sqlBuilder.toString(),paramList);
            return qr.update(JdbcUtils.getConnection(), sqlBuilder.toString(), paramList.toArray());  // 执行SQL语句
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("StudentDao -> updateById(-)报错了");
        }
        return 0;
    }

}

