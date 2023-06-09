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

public class FormCourseAdd extends JInternalFrame {

    private JPanel jContentPane;
    private JTextField tb_id = new JTextField();
    private JLabel label_teacherid = new JLabel("教师");
    private JComboBox tb_teacherid = new JComboBox();
    private JLabel label_name = new JLabel("名称");
    private JTextField tb_name = new JTextField();
    private JLabel label_course = new JLabel("学分");
    private JTextField tb_course = new JTextField();
    private JLabel label_stutime = new JLabel("学时");
    private JTextField tb_stutime = new JTextField();
    private JButton btn = new JButton("添加");
    private URL imgURL;
    private BtnListener btnListener;

    public FormCourseAdd() {
        init();
        if (Login.type.equals("teacher")) {
            tb_teacherid.setEnabled(false);//相关用户只能选择自己
            tb_teacherid.setSelectedItem(Login.teacher.getName());
        }
    }

    //按钮点击事件
    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Course entityValue = new Course();
            entityValue.setId(Utils.newId());
            String id;//主键
            String teacherid;//教师
            String name;//名称
            Double course;//学分
            Integer stutime;//学时
            //不可为空字段
            if (StringUtils.isNullOrEmpty(tb_name.getText().trim())) {
                JOptionPane.showMessageDialog(null, "名称 不可为空");
                return;
            }
            //校验输入合法,获取输入值
            id = Utils.newId(); //设置默认值
            entityValue.setId(id);
            teacherid = getByIndexTeacherid(tb_teacherid.getSelectedIndex());//外键
            entityValue.setTeacherid(teacherid);
            name = tb_name.getText().trim();//字符串
            entityValue.setName(name);
            course = Utils.strToDouble(tb_course.getText().trim());//double
            if (!StringUtils.isNullOrEmpty(tb_course.getText().trim()) && course == null) {
                JOptionPane.showMessageDialog(null, Utils.DIGIT_ERROR);
                return;
            }
            entityValue.setCourse(course);
            stutime = Utils.strToInt(tb_stutime.getText().trim());//int
            if (!StringUtils.isNullOrEmpty(tb_stutime.getText().trim()) && stutime == null) {
                JOptionPane.showMessageDialog(null, Utils.DIGIT_ERROR);
                return;
            }
            entityValue.setStutime(stutime);
            //提交
            int res = CourseDao.me().insert(entityValue);
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
        setTeacherid(tb_teacherid);
        //设置主窗体
        this.setSize(296, 450);
        this.setTitle("添加课程");//窗体名
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        int x = 29, y = 1;
        label_teacherid.setBounds(new Rectangle(x, y, 71, 19));
        tb_teacherid.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_name.setBounds(new Rectangle(x, y, 71, 19));
        tb_name.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_course.setBounds(new Rectangle(x, y, 71, 19));
        tb_course.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_stutime.setBounds(new Rectangle(x, y, 71, 19));
        tb_stutime.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        btn.setBounds(new Rectangle(130, y, 100, 26));
        getRootPane().setDefaultButton(btn);// 设置回车键

        jContentPane = new JPanel();// 新建jPanel面板
        jContentPane.setLayout(null);
        jContentPane.setBackground(new Color(255, 255, 255));
        setContentPane(jContentPane);
        jContentPane.add(label_teacherid);
        jContentPane.add(tb_teacherid);
        jContentPane.add(label_name);
        jContentPane.add(tb_name);
        jContentPane.add(label_course);
        jContentPane.add(tb_course);
        jContentPane.add(label_stutime);
        jContentPane.add(tb_stutime);
        jContentPane.add(btn);
        btnListener = new BtnListener();
        btn.addActionListener(btnListener);
    }

    //存储外键数据 tb_teacherid
    private java.util.List<Teacher> listTeacherid;

    private void setTeacherid(JComboBox comboBox) {
        listTeacherid = TeacherDao.me().list();
        comboBox.addItem("");
        for (Teacher i : listTeacherid) {
            comboBox.addItem(i.getName());//对应外键字段
        }
    }

    //获取id
    private String getByIndexTeacherid(int index) {
        index = index - 1;
        if (index < 0 || index >= listTeacherid.size()) {
            return "";
        }
        return listTeacherid.get(index).getId();
    }

    //唯一判断
    public static boolean illegalAdd(String id, String key, String val) {
        Course entityValue = CourseDao.me().getBy(key, val);
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

