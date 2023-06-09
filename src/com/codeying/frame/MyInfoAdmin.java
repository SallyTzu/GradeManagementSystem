package com.codeying.frame;

import com.codeying.dao.AdminDao;
import com.codeying.entity.Admin;
import com.mysql.cj.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class MyInfoAdmin extends JInternalFrame {
    private JPanel jContentPane;
    private JLabel label_role = new JLabel("角色");
    private JTextField tb_role = new JTextField();
    private JLabel label_username = new JLabel("用户名");
    private JTextField tb_username = new JTextField();
    private JLabel label_name = new JLabel("姓名");
    private JTextField tb_name = new JTextField();
    private JLabel label_tele = new JLabel("电话");
    private JTextField tb_tele = new JTextField();
    private JLabel label_gender = new JLabel("性别");
    private JComboBox tb_gender = new JComboBox();
    private JButton btn = new JButton("保存");
    private URL imgURL;
    private BtnListener btnListener;

    public MyInfoAdmin() {
        init();
    }

    //我的信息
    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Admin t = Login.admin;
            //不可为空字段
            if (StringUtils.isNullOrEmpty(tb_name.getText().trim())) {
                JOptionPane.showMessageDialog(null, "姓名 不可为空");
                return;
            }
            if (FormAdminAdd.illegalAdd(Login.admin.getId(), "username", tb_username.getText().trim())) {
                JOptionPane.showMessageDialog(null, "用户名 : 已存在！");
                return;
            }
            t.setUsername(tb_username.getText().trim());
            t.setName(tb_name.getText().trim());
            t.setTele(tb_tele.getText().trim());
            t.setGender((String) (tb_gender.getSelectedItem()));//下拉框
            //提交
            AdminDao dao = AdminDao.me();
            int res = dao.updateById(t);
            if (res == 1) {
                Login.admin = t;
                JOptionPane.showMessageDialog(null, "更新成功！");
            } else {
                JOptionPane.showMessageDialog(null, "更新失败");
            }
        }
    }

    public void init() {
        tb_gender.addItem("");
        tb_gender.addItem("男");
        tb_gender.addItem("女");
        //设置主窗体
        this.setSize(296, 350);
        this.setTitle("个人中心");//窗体名
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        int x = 29, y = 0;

        label_username.setBounds(new Rectangle(x, y, 71, 19));
        tb_username.setBounds(new Rectangle(x + 90, y, 124, 23));
        tb_username.setText(Login.admin.getUsername());
        tb_username.setEditable(false);
        y += 30;

        label_role.setBounds(new Rectangle(x, y, 71, 19));
        tb_role.setBounds(new Rectangle(x + 90, y, 124, 23));
        tb_role.setText(Login.admin.getRole());
        tb_role.setEditable(false);
        y += 30;

        label_name.setBounds(new Rectangle(x, y, 71, 19));
        tb_name.setBounds(new Rectangle(x + 90, y, 124, 23));
        tb_name.setText(Login.admin.getName());
        y += 30;

        label_tele.setBounds(new Rectangle(x, y, 71, 19));
        tb_tele.setBounds(new Rectangle(x + 90, y, 124, 23));
        tb_tele.setText(Login.admin.getTele());
        y += 30;

        label_gender.setBounds(new Rectangle(x, y, 71, 19));
        tb_gender.setBounds(new Rectangle(x + 90, y, 124, 23));
        tb_gender.setSelectedItem(Login.admin.getGender());
        y += 30;

        btn.setBounds(new Rectangle(130, y, 100, 26));
        getRootPane().setDefaultButton(btn);// 设置回车键

        jContentPane = new JPanel();// 新建jPanel面板
        jContentPane.setLayout(null);
        jContentPane.setBackground(new Color(255, 255, 255));
        setContentPane(jContentPane);
        jContentPane.add(label_username);
        jContentPane.add(tb_username);
        jContentPane.add(label_role);
        jContentPane.add(tb_role);

        jContentPane.add(label_name);
        jContentPane.add(tb_name);
        jContentPane.add(label_tele);
        jContentPane.add(tb_tele);
        jContentPane.add(label_gender);
        jContentPane.add(tb_gender);
        jContentPane.add(btn);
        btnListener = new BtnListener();
        btn.addActionListener(btnListener);
    }

}

