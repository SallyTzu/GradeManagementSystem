package com.codeying.utils.component;

import com.codeying.frame.Login;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;


public class LoginFrame extends JFrame {
    protected String sysName;//系统名
    protected ArrayList<String> loginUsers = new ArrayList<>();//登录用户类型
    protected JPanel jContentPane = null;
    protected JButton loginButton = null;
    protected JButton registerBtn = null;
    protected JTextField jUserNameField = null;
    protected JPasswordField jPasswordField = null;
    protected JLabel titleLabel = null;
    protected JLabel jLabel_User = null;
    protected JLabel jLabel_userName = null;
    protected JLabel jLabel_password = null;
    protected JLabel jLabel_privilege = null;
    protected URL imgURL = null;

    protected Login.BtnListener btnListener = null;
    protected JComboBox jComboBox = null;

    public LoginFrame(){
    }

    //初始化窗体
    protected void initialize() {
        //设置主窗体
        int x = 296,y=StyleConfig.loginFrameHeight;
        this.setResizable(false);
        this.setSize(x, y);
        this.setTitle("欢迎登陆");//窗体名
        this.setLocationRelativeTo(null);//此窗口将置于屏幕的中央。

        titleLabel = new JLabel(sysName, JLabel.CENTER);
        titleLabel.setBounds(new Rectangle(0, 0, 291, 50));
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        //登陆面板
        jLabel_User = new JLabel();
        if(StyleConfig.loginBgStyle == 1){
            jLabel_User.setBounds(new Rectangle(10, 55, 275, 140));
            imgURL = this.getClass().getResource("/com/codeying/images/user.gif");
            jLabel_User.setIcon(new ImageIcon(imgURL));
        }
        if(StyleConfig.loginBgStyle == 2){
            jLabel_User.setBounds(new Rectangle(0, 0, x, y));
            imgURL = this.getClass().getResource("/com/codeying/images/main.jpg");
            jLabel_User.setIcon(new ImageIcon(imgURL));
        }
        if(StyleConfig.loginBgStyle == 3){
            jLabel_User.setBounds(new Rectangle(0, 0, x, y));
            imgURL = this.getClass().getResource("/com/codeying/images/smile.jpg");
            jLabel_User.setIcon(new ImageIcon(imgURL));
        }
        //用户名密码标签和输入框
        jLabel_userName = new JLabel("用户名：");
        jLabel_userName.setBounds(new Rectangle(29, 89, 71, 19));
        jLabel_password = new JLabel("密  码：");
        jLabel_password.setBounds(new Rectangle(29, 118, 71, 19));
        jUserNameField = new JTextField(20);
        jUserNameField.setBounds(new Rectangle(120, 89, 124, 23));
        jPasswordField = new JPasswordField();
        jPasswordField.setBounds(new Rectangle(120, 118, 124, 23));
        //登陆选择
        jLabel_privilege = new JLabel("登陆类型：");
        jLabel_privilege.setBounds(new Rectangle(28, 150, 71, 19));
        jComboBox = new JComboBox();
        jComboBox.setBounds(new Rectangle(120, 150, 123, 23));
        for (String loginUser : loginUsers) {
            jComboBox.addItem(loginUser);
        }
        //登陆注册按钮
        loginButton = new JButton("登录");
        loginButton.setBounds(new Rectangle(160, 210, 78, 26));
        getRootPane().setDefaultButton(loginButton);// 设置回车键登录

        registerBtn = new JButton("注册");
        registerBtn.setBounds(new Rectangle(40, 210, 78, 26));

        jContentPane = new JPanel();// 新建jPanel面板
        jContentPane.setLayout(null);
        jContentPane.setBackground(new Color(255, 255, 255));
        jContentPane.add(jLabel_userName, null);
        jContentPane.add(jLabel_password, null);
        jContentPane.add(loginButton, null);
        jContentPane.add(registerBtn, null);
        jContentPane.add(jUserNameField, null);
        jContentPane.add(jPasswordField, null);
        jContentPane.add(titleLabel, null);
        jContentPane.add(jComboBox, null);
        jContentPane.add(jLabel_privilege, null);
        jContentPane.add(jLabel_User, null);
        setContentPane(jContentPane);

    }
}
