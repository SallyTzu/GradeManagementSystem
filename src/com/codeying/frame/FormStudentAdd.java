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

public class FormStudentAdd extends JInternalFrame {

    private JPanel jContentPane;
    private JTextField tb_id = new JTextField();
    private JLabel label_username = new JLabel("用户名");
    private JTextField tb_username = new JTextField();
    private JLabel label_password = new JLabel("密码");
    private JTextField tb_password = new JTextField();
    private JLabel label_name = new JLabel("姓名");
    private JTextField tb_name = new JTextField();
    private JLabel label_numb = new JLabel("学号");
    private JTextField tb_numb = new JTextField();
    private JLabel label_gender = new JLabel("性别");
    private JComboBox tb_gender = new JComboBox();
    private JLabel label_age = new JLabel("年龄");
    private JTextField tb_age = new JTextField();
    private JLabel label_clazz = new JLabel("班级");
    private JTextField tb_clazz = new JTextField();
    private JButton btn = new JButton("添加");
    private URL imgURL;
    private BtnListener btnListener;

    public FormStudentAdd() {
        init();
    }

    //按钮点击事件
    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Student entityValue = new Student();
            entityValue.setId(Utils.newId());
            String id;//主键
            String username;//用户名
            String password;//密码
            String name;//姓名
            String numb;//学号
            String gender;//性别
            Integer age;//年龄
            String clazz;//班级
            //不可为空字段
            if (StringUtils.isNullOrEmpty(tb_username.getText().trim())) {
                JOptionPane.showMessageDialog(null, "用户名 不可为空");
                return;
            }
            //不可为空字段
            if (StringUtils.isNullOrEmpty(tb_name.getText().trim())) {
                JOptionPane.showMessageDialog(null, "姓名 不可为空");
                return;
            }
            //不可为空字段
            if (StringUtils.isNullOrEmpty(tb_numb.getText().trim())) {
                JOptionPane.showMessageDialog(null, "学号 不可为空");
                return;
            }
            //校验输入合法,获取输入值
            id = Utils.newId(); //设置默认值
            entityValue.setId(id);
            username = tb_username.getText().trim();//字符串
            entityValue.setUsername(username);
            password = tb_password.getText().trim();//字符串
            entityValue.setPassword(password);
            name = tb_name.getText().trim();//字符串
            entityValue.setName(name);
            numb = tb_numb.getText().trim();//字符串
            entityValue.setNumb(numb);
            gender = (String) (tb_gender.getSelectedItem());//下拉框
            entityValue.setGender(gender);
            age = Utils.strToInt(tb_age.getText().trim());//int
            if (!StringUtils.isNullOrEmpty(tb_age.getText().trim()) && age == null) {
                JOptionPane.showMessageDialog(null, Utils.DIGIT_ERROR);
                return;
            }
            entityValue.setAge(age);
            clazz = tb_clazz.getText().trim();//字符串
            entityValue.setClazz(clazz);
            if (FormStudentAdd.illegalAdd(null, "username", username)) {
                JOptionPane.showMessageDialog(null, "用户名 : 已存在！");
                return;
            }
            if (FormStudentAdd.illegalAdd(null, "numb", numb)) {
                JOptionPane.showMessageDialog(null, "学号 : 已存在！");
                return;
            }
            //提交
            int res = StudentDao.me().insert(entityValue);
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
        tb_gender.addItem("");
        tb_gender.addItem("男");
        tb_gender.addItem("女");
        //设置主窗体
        this.setSize(296, 450);
        this.setTitle("添加学生");//窗体名
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        int x = 29, y = 1;
        label_username.setBounds(new Rectangle(x, y, 71, 19));
        tb_username.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_password.setBounds(new Rectangle(x, y, 71, 19));
        tb_password.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_name.setBounds(new Rectangle(x, y, 71, 19));
        tb_name.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_numb.setBounds(new Rectangle(x, y, 71, 19));
        tb_numb.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_gender.setBounds(new Rectangle(x, y, 71, 19));
        tb_gender.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_age.setBounds(new Rectangle(x, y, 71, 19));
        tb_age.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        label_clazz.setBounds(new Rectangle(x, y, 71, 19));
        tb_clazz.setBounds(new Rectangle(x + 90, y, 124, 23));
        y += 30;
        btn.setBounds(new Rectangle(130, y, 100, 26));
        getRootPane().setDefaultButton(btn);// 设置回车键

        jContentPane = new JPanel();// 新建jPanel面板
        jContentPane.setLayout(null);
        jContentPane.setBackground(new Color(255, 255, 255));
        setContentPane(jContentPane);
        jContentPane.add(label_username);
        jContentPane.add(tb_username);
        jContentPane.add(label_password);
        jContentPane.add(tb_password);
        jContentPane.add(label_name);
        jContentPane.add(tb_name);
        jContentPane.add(label_numb);
        jContentPane.add(tb_numb);
        jContentPane.add(label_gender);
        jContentPane.add(tb_gender);
        jContentPane.add(label_age);
        jContentPane.add(tb_age);
        jContentPane.add(label_clazz);
        jContentPane.add(tb_clazz);
        jContentPane.add(btn);
        btnListener = new BtnListener();
        btn.addActionListener(btnListener);
    }

    //唯一判断
    public static boolean illegalAdd(String id, String key, String val) {
        Student entityValue = StudentDao.me().getBy(key, val);
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

