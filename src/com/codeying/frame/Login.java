package com.codeying.frame;

import com.codeying.dao.*;
import com.codeying.entity.*;
import com.codeying.utils.component.LoginFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends LoginFrame {
    //全局变量，表示登陆用户的类型
    public static String type;
    public static Admin admin;//表示已经登录的管理员
    public static Teacher teacher;//表示已经登录的教师
    public static Student student;//表示已经登录的学生

    public Login() {
        sysName = "学生成绩管理系统";
        loginUsers.add("管理员");
        loginUsers.add("教师");
        loginUsers.add("学生");
        initialize();
        btnListener = new BtnListener();
        loginButton.addActionListener(btnListener);
        registerBtn.addActionListener(btnListener);
    }


    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == loginButton) {
                    //获取用户名密码
                    String username = jUserNameField.getText().trim();
                    String password = new String(jPasswordField.getPassword()).trim();//char to String
                    //判断不为空
                    if ("".equals(username)) {
                        JOptionPane.showMessageDialog(null, "用户名不能为空");
                        return;
                    }
                    if ("".equals(password)) {
                        JOptionPane.showMessageDialog(null, "密码不能为空");
                        return;
                    }
                    //获取用户登陆类型
                    int userTypeIndex = jComboBox.getSelectedIndex();
                    //0 : 管理员
                    if (userTypeIndex == 0) {
                        AdminDao dao = AdminDao.me();
                        Admin user = dao.getBy("username", username);
                        if (user != null) {
                            if (!password.equals(user.getPassword())) {
                                JOptionPane.showMessageDialog(null, "密码错误！",
                                        "登陆失败", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            dispose();//登陆成功 释放资源
                            Login.admin = user;
                            Login.type = "admin";
                            MainFrame mf = new MainFrame();
                            mf.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "用户名不存在！",
                                    "登陆失败", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                    //1 : 教师
                    if (userTypeIndex == 1) {
                        TeacherDao dao = TeacherDao.me();
                        Teacher user = dao.getBy("username", username);
                        if (user != null) {
                            if (!password.equals(user.getPassword())) {
                                JOptionPane.showMessageDialog(null, "密码错误！",
                                        "登陆失败", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            dispose();//登陆成功 释放资源
                            Login.teacher = user;
                            Login.type = "teacher";
                            MainFrame mf = new MainFrame();
                            mf.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "用户名不存在！",
                                    "登陆失败", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }

                    if (userTypeIndex == 2) {
                        StudentDao dao = StudentDao.me();
                        Student user = dao.getBy("username", username);
                        if (user != null) {
                            if (!password.equals(user.getPassword())) {
                                JOptionPane.showMessageDialog(null, "密码错误！",
                                        "登陆失败", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            dispose();//登陆成功 释放资源
                            Login.student = user;
                            Login.type = "student";
                            MainFrame mf = new MainFrame();
                            mf.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "用户名不存在！",
                                    "登陆失败", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                } else if (e.getSource() == registerBtn) {
                    //注册
                    dispose();
                    Register register = new Register();
                    register.setVisible(true);
                }
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "数据库连接失败，请检查" +
                        "JdbcUtils.java中的数据库配置", "数据库连接失败", JOptionPane.WARNING_MESSAGE);
                e1.printStackTrace();
            }
        }
    }

}

