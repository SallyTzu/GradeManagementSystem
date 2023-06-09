package com.codeying.dao;

import com.codeying.entity.*;
import com.codeying.utils.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.sql.SQLException;

import java.util.*;
/**
 *  管理员Dao，和数据库互动。
 */
public class AdminDao extends AbstractDao<Admin>{

    private static AdminDao thisDao = new AdminDao("tb_admin");

    public AdminDao(String tbName) {super(tbName);}

    public static AdminDao me(){return thisDao;}

    public Admin login(String username,String password){
        QueryRunner qr = new QueryRunner();
        String sqlBuilder = "SELECT * FROM tb_admin where username = ? and password = ?";
        List<Admin> list = null;
        try {
            log(sqlBuilder+" 。 "+username+" , "+password);
            list = qr.query(JdbcUtils.getConnection(), sqlBuilder, new BeanListHandler<>(Admin.class),username,password);
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
                    String tele = paramMap.get("tele");
            if(MyUtils.isNotEmpty(tele)){
                        sqlBuilder.append(" and tele like '%").append(tele).append("%'");// 使用模糊查询
                    }
                    String gender = paramMap.get("gender");
            if(MyUtils.isNotEmpty(gender)){
                         sqlBuilder.append(" and gender = '").append(gender).append("'");
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
    public Admin getBy(String paramName, String val) {
        Admin t = getBy(paramName,val,Admin.class);
        return t;
    }

    @Override
    public List<Admin> list(Map<String,String> paramMap){
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
            String sqlRes = JdbcUtils.getPageSql("SELECT * FROM tb_admin where 1=1 " + queryCondition, pageIndex,pageSize);
            //打印sql语句和参数
            log(sqlRes);
            //执行查询
            List<Admin> list = qr.query(JdbcUtils.getConnection(), sqlRes, new BeanListHandler<>(Admin.class));
            return list;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("AdminDao -> list(...)报错了");
        }
        return new ArrayList();
    }

    @Override
    public int insert(Admin entity){
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
                if(entity.getTele() != null){
                reParamNames.append(",tele");
                reParams.append(",?");
                paramList.add(entity.getTele());
            }
                if(entity.getGender() != null){
                reParamNames.append(",gender");
                reParams.append(",?");
                paramList.add(entity.getGender());
            }
                QueryRunner qr = new QueryRunner();  //创建QueryRunner类对象
            //不为空的插入
            String sqlBuilder = "INSERT INTO tb_admin (id"+reParamNames.toString()+")VALUES(?"+reParams.toString()+")";
            log(sqlBuilder.toString());
            return qr.update(JdbcUtils.getConnection(), sqlBuilder, paramList.toArray());  // 执行SQL语句
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("AdminDao -> insert(-)报错了");
        }
        return 0;
    }

    @Override
    public int updateById(Admin entity) {
        try{
            QueryRunner qr = new QueryRunner();  //创建QueryRunner类对象
            StringBuilder sqlBuilder = new StringBuilder("update tb_admin set id = ? ");
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
                    if(entity.getTele() != null){
                sqlBuilder.append(", tele = ? ");
                paramList.add(entity.getTele());
            }
                    if(entity.getGender() != null){
                sqlBuilder.append(", gender = ? ");
                paramList.add(entity.getGender());
            }
                //最后拼接id条件
            sqlBuilder.append("where id = ?");
            paramList.add(entity.getId());
            log(sqlBuilder.toString(),paramList);
            return qr.update(JdbcUtils.getConnection(), sqlBuilder.toString(), paramList.toArray());  // 执行SQL语句
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("AdminDao -> updateById(-)报错了");
        }
        return 0;
    }

}

