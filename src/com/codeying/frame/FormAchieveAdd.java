package com.codeying.frame;

import com.codeying.dao.*;
import com.codeying.entity.*;
import com.codeying.utils.JdbcUtils;
import com.codeying.utils.Utils;
import com.mysql.cj.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Date;

public class FormAchieveAdd extends JInternalFrame {

    private JPanel jContentPane;
    private JTextField tb_id = new JTextField();
    private JLabel label_courseid = new JLabel("课程");
    private JComboBox tb_courseid = new JComboBox();
    private JLabel label_studentid = new JLabel("学生");
    private JComboBox tb_studentid = new JComboBox();
    private JLabel label_score = new JLabel("成绩");
    private JTextField tb_score = new JTextField();
    private JButton btn = new JButton("添加");
    private URL imgURL;
    private BtnListener btnListener;

    public FormAchieveAdd() {
        init();
        if (Login.type.equals("student")) {
            tb_studentid.setEnabled(false);//相关用户只能选择自己
            tb_studentid.setSelectedItem(Login.student.getName());
        }
    }

    //按钮点击事件
    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Achieve entityValue = new Achieve();
            entityValue.setId(Utils.newId());
            String id;//主键
            String courseid;//课程
            String studentid;//学生
            Double score;//成绩
            Date createtime;//评定时间
            if (tb_courseid.getSelectedIndex() == 0)//检查外键字段是否为空
            {
                JOptionPane.showMessageDialog(null, "课程 不可为空");
                return;
            }
            if (tb_studentid.getSelectedIndex() == 0)//检查外键字段是否为空
            {
                JOptionPane.showMessageDialog(null, "学生 不可为空");
                return;
            }
            //校验输入合法,获取输入值
            id = Utils.newId(); //生成一个新的唯一标识符，并将其设置为实体对象的id属性。
            entityValue.setId(id);
            //从两个下拉框中选择获取课程id和学生id，设置为实体对象
            courseid = getByIndexCourseid(tb_courseid.getSelectedIndex());
            entityValue.setCourseid(courseid);
            studentid = getByIndexStudentid(tb_studentid.getSelectedIndex());
            entityValue.setStudentid(studentid);
            //将文本框中的分数值转换为double类型，并将其设置为实体对象的score属性。
            score = Utils.strToDouble(tb_score.getText().trim());
            // 如果分数值无法转换为double类型，则会弹出一个对话框，提示用户输入的值无效。
            if (!StringUtils.isNullOrEmpty(tb_score.getText().trim()) && score == null) {
                JOptionPane.showMessageDialog(null, Utils.DIGIT_ERROR);
                return;
            }
            entityValue.setScore(score);
            createtime = new Date(); //设置默认值
            entityValue.setCreatetime(createtime);
            //提交
            int res = AchieveDao.me().insert(entityValue);
            if (res == 1) {
                JOptionPane.showMessageDialog(null, "添加成功！");
            } else {
                JOptionPane.showMessageDialog(null, "添加失败");
            }
        }
    }

    //初始化界面
    public void init() {
        //设置下拉框
        setCourseid(tb_courseid);
        setStudentid(tb_studentid);
        //设置主窗体
        this.setSize(296, 450);
        this.setTitle("添加成绩");//窗体名
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        int x = 29, y = 1;
        label_courseid.setBounds(new Rectangle(x, y, 71, 19));
        tb_courseid.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_studentid.setBounds(new Rectangle(x, y, 71, 19));
        tb_studentid.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_score.setBounds(new Rectangle(x, y, 71, 19));
        tb_score.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        btn.setBounds(new Rectangle(130, y, 100, 26));
        getRootPane().setDefaultButton(btn);// 设置回车键

        jContentPane = new JPanel();// 新建jPanel面板
        jContentPane.setLayout(null);
        jContentPane.setBackground(new Color(255, 255, 255));
        setContentPane(jContentPane);
        jContentPane.add(label_courseid);
        jContentPane.add(tb_courseid);
        jContentPane.add(label_studentid);
        jContentPane.add(tb_studentid);
        jContentPane.add(label_score);
        jContentPane.add(tb_score);
        jContentPane.add(btn);
        btnListener = new BtnListener();
        btn.addActionListener(btnListener);
    }

    //存储外键数据 tb_courseid
    private java.util.List<Course> listCourseid;

    private void setCourseid(JComboBox comboBox) {
        listCourseid = CourseDao.me().list();
        comboBox.addItem("");
        for (Course i : listCourseid) {
            comboBox.addItem(i.getName());//对应外键字段
        }
    }

    //获取id
    private String getByIndexCourseid(int index) {
        index = index - 1;
        if (index < 0 || index >= listCourseid.size()) {
            return "";
        }
        return listCourseid.get(index).getId();
    }

    //存储外键数据 tb_studentid
    private java.util.List<Student> listStudentid;

    private void setStudentid(JComboBox comboBox) {
        listStudentid = StudentDao.me().list();
        comboBox.addItem("");
        for (Student i : listStudentid) {
            comboBox.addItem(i.getName());//对应外键字段
        }
    }

    //获取id
    private String getByIndexStudentid(int index) {
        index = index - 1;
        if (index < 0 || index >= listStudentid.size()) {
            return "";
        }
        return listStudentid.get(index).getId();
    }

    //唯一判断
    public static boolean illegalAdd(String id, String key, String val) {
        Achieve entityValue = AchieveDao.me().getBy(key, val);
        if (entityValue == null) {
            return false;//不重复
        } else {
            if (id == null) {
                return true;//非法
            } else {
                if (id.equals(entityValue.getId())) {
                    return false;
                }
                return true;//合法
            }
        }
    }
}

