package com.codeying.dao;

import com.codeying.entity.*;
import com.codeying.utils.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.sql.SQLException;

import java.util.*;
/**
 *  教师Dao，和数据库互动。
 */
public class TeacherDao extends AbstractDao<Teacher>{

    private static TeacherDao thisDao = new TeacherDao("tb_teacher");

    public TeacherDao(String tbName) {super(tbName);}

    public static TeacherDao me(){return thisDao;}

    /**
    *  登录
    */
    public Teacher login(String username,String password){
        QueryRunner qr = new QueryRunner();
        String sqlBuilder = "SELECT * FROM tb_teacher where username = ? and password = ?";
        List<Teacher> list = null;
        try {
            log(sqlBuilder+" 。 "+username+" , "+password);
            list = qr.query(JdbcUtils.getConnection(), sqlBuilder, new BeanListHandler<>(Teacher.class),username,password);
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
    /**
     * 将map参数转换成sql的where条件语句。
     */
    @Override
    public String toConditionSql(Map<String, String> paramMap){
        StringBuilder sqlBuilder=new StringBuilder("");//接下来拼接查询条件,比如 and xx=xx and XX < xx
        if(paramMap!=null){
                String id = paramMap.get("id");
            if(MyUtils.isNotEmpty(id)){
                        sqlBuilder.append(" and id like '%").append(id).append("%'");// 使用模糊查询
                    }
                    String username = paramMap.get("username");
            if(MyUtils.isNotEmpty(username)){
                        sqlBuilder.append(" and username like '%").append(username).append("%'");// 使用模糊查询
                    }
                    String password = paramMap.get("password");
            if(MyUtils.isNotEmpty(password)){
                        sqlBuilder.append(" and password like '%").append(password).append("%'");// 使用模糊查询
                    }
                    String name = paramMap.get("name");
            if(MyUtils.isNotEmpty(name)){
                        sqlBuilder.append(" and name like '%").append(name).append("%'");// 使用模糊查询
                    }
                    String gender = paramMap.get("gender");
            if(MyUtils.isNotEmpty(gender)){
                         sqlBuilder.append(" and gender = '").append(gender).append("'");
                    }
                    String numb = paramMap.get("numb");
            if(MyUtils.isNotEmpty(numb)){
                        sqlBuilder.append(" and numb like '%").append(numb).append("%'");// 使用模糊查询
                    }
                    if(MyUtils.isNotEmpty(paramMap.get("ageL"))){
                sqlBuilder.append(" and age >= ").append(paramMap.get("ageL"));
            }
            if(MyUtils.isNotEmpty(paramMap.get("ageR"))){
                sqlBuilder.append(" and age <= ").append(paramMap.get("ageR"));
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

    /**
     * 根据..条件获取一条数据
     */
    @Override
    public Teacher getBy(String paramName, String val) {
        Teacher t = getBy(paramName,val,Teacher.class);
        return t;
    }

    /**
    * 根据条件查询数据
    */
    @Override
    public List<Teacher> list(Map<String,String> paramMap){
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
            String sqlRes = JdbcUtils.getPageSql("SELECT * FROM tb_teacher where 1=1 " + queryCondition, pageIndex,pageSize);
            //打印sql语句和参数
            log(sqlRes);
            //执行查询
            List<Teacher> list = qr.query(JdbcUtils.getConnection(), sqlRes, new BeanListHandler<>(Teacher.class));
            return list;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("TeacherDao -> list(...)报错了");
        }
        return new ArrayList();
    }

    /**
    * 插入
    */
    @Override
    public int insert(Teacher entity){
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
                if(entity.getGender() != null){
                reParamNames.append(",gender");
                reParams.append(",?");
                paramList.add(entity.getGender());
            }
                if(entity.getNumb() != null){
                reParamNames.append(",numb");
                reParams.append(",?");
                paramList.add(entity.getNumb());
            }
                if(entity.getAge() != null){
                reParamNames.append(",age");
                reParams.append(",?");
                paramList.add(entity.getAge());
            }
                QueryRunner qr = new QueryRunner();  //创建QueryRunner类对象
            //不为空的插入
            String sqlBuilder = "INSERT INTO tb_teacher (id"+reParamNames.toString()+")VALUES(?"+reParams.toString()+")";
            log(sqlBuilder.toString());
            return qr.update(JdbcUtils.getConnection(), sqlBuilder, paramList.toArray());  // 执行SQL语句
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("TeacherDao -> insert(-)报错了");
        }
        return 0;
    }

    /**
    * 根据Id更新
    */
    @Override
    public int updateById(Teacher entity) {
        try{
            QueryRunner qr = new QueryRunner();  //创建QueryRunner类对象
            StringBuilder sqlBuilder = new StringBuilder("update tb_teacher set id = ? ");
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
                    if(entity.getGender() != null){
                sqlBuilder.append(", gender = ? ");
                paramList.add(entity.getGender());
            }
                    if(entity.getNumb() != null){
                sqlBuilder.append(", numb = ? ");
                paramList.add(entity.getNumb());
            }
                    if(entity.getAge() != null){
                sqlBuilder.append(", age = ? ");
                paramList.add(entity.getAge());
            }
                //最后拼接id条件
            sqlBuilder.append("where id = ?");
            paramList.add(entity.getId());
            log(sqlBuilder.toString(),paramList);
            return qr.update(JdbcUtils.getConnection(), sqlBuilder.toString(), paramList.toArray());  // 执行SQL语句
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("TeacherDao -> updateById(-)报错了");
        }
        return 0;
    }

}

