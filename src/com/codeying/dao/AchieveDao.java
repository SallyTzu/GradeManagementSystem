package com.codeying.dao;

import com.codeying.entity.*;
import com.codeying.utils.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.*;
public class AchieveDao extends AbstractDao<Achieve>{
    private static AchieveDao thisDao = new AchieveDao("tb_achieve");
    public AchieveDao(String tbName) {
        super(tbName);
    }
    public static AchieveDao me(){
        return thisDao;
    }

    @Override
    public String toConditionSql(Map<String, String> paramMap){
        StringBuilder sqlBuilder=new StringBuilder("");//接下来拼接查询条件,比如 and xx=xx and XX < xx
        if(paramMap!=null){
            String id = paramMap.get("id");
            if(MyUtils.isNotEmpty(id)){
                sqlBuilder.append(" and id like '%").append(id).append("%'");// 使用模糊查询
            }

            String courseid = paramMap.get("courseid");
            if(MyUtils.isNotEmpty(courseid)){
                 sqlBuilder.append(" and courseid = '").append(courseid).append("'");
            }

            String studentid = paramMap.get("studentid");
            if(MyUtils.isNotEmpty(studentid)){
                 sqlBuilder.append(" and studentid = '").append(studentid).append("'");
            }

            if(MyUtils.isNotEmpty(paramMap.get("scoreL"))){
                sqlBuilder.append(" and score >= ").append(paramMap.get("scoreL"));
            }

            if(MyUtils.isNotEmpty(paramMap.get("scoreR"))){
                sqlBuilder.append(" and score <= ").append(paramMap.get("scoreR"));
            }

            if(MyUtils.isNotEmpty(paramMap.get("createtimeL"))){
                sqlBuilder.append(" and createtime >= '").append(paramMap.get("createtimeL").
                        replace("T"," ")).append("'");
            }

            if(MyUtils.isNotEmpty(paramMap.get("createtimeR"))){
                sqlBuilder.append(" and createtime <= '").append(paramMap.get("createtimeR").
                        replace("T"," ")).append("'");
            }

            //学生只能查询自己相关的信息
            String studentidFrn=(String)paramMap.get("studentid");
            if(MyUtils.isNotEmpty(studentidFrn)){
                sqlBuilder.append(" and studentid = '").append(studentidFrn).append("'");
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
    public Achieve getBy(String paramName, String val) {
        Achieve t = getBy(paramName,val,Achieve.class);
        return t;
    }

    /**
    * 根据条件查询数据
    */
    @Override
    public List<Achieve> list(Map<String,String> paramMap){
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
                }catch (Exception e){
                }
            }
            //接下来拼接查询条件，然后获取分页sql
            String sqlRes = JdbcUtils.getPageSql("SELECT * FROM tb_achieve where 1=1 " +
                    queryCondition, pageIndex,pageSize);
            //打印sql语句和参数
            log(sqlRes);
            //执行查询
            List<Achieve> list = qr.query(JdbcUtils.getConnection(), sqlRes, new BeanListHandler<>(Achieve.class));
            return list;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("AchieveDao -> list(...)报错了");
        }
        return new ArrayList();
    }

    /**
    * 插入
    */
    @Override
    public int insert(Achieve entity){
        try{
            //构造sql语句
            List<Object> paramList = new ArrayList<>();
            StringBuilder reParamNames = new StringBuilder();
            StringBuilder reParams = new StringBuilder();
            paramList.add(entity.getId());
                if(entity.getCourseid() != null){
                reParamNames.append(",courseid");
                reParams.append(",?");
                paramList.add(entity.getCourseid());
            }
                if(entity.getStudentid() != null){
                reParamNames.append(",studentid");
                reParams.append(",?");
                paramList.add(entity.getStudentid());
            }
                if(entity.getScore() != null){
                reParamNames.append(",score");
                reParams.append(",?");
                paramList.add(entity.getScore());
            }
                if(entity.getCreatetime() != null){
                reParamNames.append(",createtime");
                reParams.append(",?");
                paramList.add(entity.getCreatetime());
            }
                QueryRunner qr = new QueryRunner();  //创建QueryRunner类对象
            //不为空的插入
            String sqlBuilder = "INSERT INTO tb_achieve (id"+reParamNames.toString()+")VALUES(?"+reParams.toString()+")";
            log(sqlBuilder.toString());
            return qr.update(JdbcUtils.getConnection(), sqlBuilder, paramList.toArray());  // 执行SQL语句
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("AchieveDao -> insert(-)报错了");
        }
        return 0;
    }

    /**
    * 根据Id更新
    */
    @Override
    public int updateById(Achieve entity) {
        try{
            QueryRunner qr = new QueryRunner();  //创建QueryRunner类对象
            StringBuilder sqlBuilder = new StringBuilder("update tb_achieve set id = ? ");
            List<Object> paramList = new ArrayList<>();
            paramList.add(entity.getId());
                    if(entity.getCourseid() != null){
                sqlBuilder.append(", courseid = ? ");
                paramList.add(entity.getCourseid());
            }
                    if(entity.getStudentid() != null){
                sqlBuilder.append(", studentid = ? ");
                paramList.add(entity.getStudentid());
            }
                    if(entity.getScore() != null){
                sqlBuilder.append(", score = ? ");
                paramList.add(entity.getScore());
            }
                    if(entity.getCreatetime() != null){
                sqlBuilder.append(", createtime = ? ");
                paramList.add(entity.getCreatetime());
            }
                //最后拼接id条件
            sqlBuilder.append("where id = ?");
            paramList.add(entity.getId());
            log(sqlBuilder.toString(),paramList);
            return qr.update(JdbcUtils.getConnection(), sqlBuilder.toString(), paramList.toArray());  // 执行SQL语句
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("AchieveDao -> updateById(-)报错了");
        }
        return 0;
    }

}

