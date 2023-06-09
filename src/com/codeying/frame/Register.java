package com.codeying.frame;
 
import com.codeying.dao.*;
import com.codeying.entity.*;
import com.codeying.utils.Utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Date;

public class Register  extends JFrame {


    public Register(){
        initialize();
    }
    private void initialize() {
        //设置主窗体
        this.setResizable(false);
        this.setSize(296, 300);
        this.setTitle("注册");//窗体名
        this.setLocationRelativeTo(null);//此窗口将置于屏幕的中央。
        //设置标题
        titleLabel = new JLabel("注册新用户",JLabel.CENTER);
        titleLabel.setBounds(new Rectangle(0, 0, 291, 50));
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));

        //用户名密码标签和输入框
        jLabel_userName = new JLabel("用 户 名：");
        jLabel_userName.setBounds(new Rectangle(29, 60, 71, 19));
        jLabel_password = new JLabel("密    码：");
        jLabel_password.setBounds(new Rectangle(29, 95, 71, 19));
        jLabel_password2 = new JLabel("确认密码：");
        jLabel_password2.setBounds(new Rectangle(29, 130, 71, 19));
        jUserNameField = new JTextField(20);
        jUserNameField.setBounds(new Rectangle(120, 60, 124, 23));
        jPasswordField = new JPasswordField();
        jPasswordField.setBounds(new Rectangle(120, 95, 124, 23));
        jPasswordField2 = new JPasswordField();
        jPasswordField2.setBounds(new Rectangle(120, 130, 124, 23));
        //登陆选择
        jLabel_privilege = new JLabel("注册类型：");
        jLabel_privilege.setBounds(new Rectangle(28, 165, 71, 19));
        jComboBox = new JComboBox();
        jComboBox.setBounds(new Rectangle(120, 165, 123, 23));
             jComboBox.addItem("管理员");
                 jComboBox.addItem("教师");
                 jComboBox.addItem("学生");
              
        //返回登陆注册按钮
        loginButton = new JButton("去登录");
        loginButton.setBounds(new Rectangle(40, 210, 78, 26));

        registerBtn = new JButton("注册");
        registerBtn.setBounds(new Rectangle(160, 210, 78, 26));
        getRootPane().setDefaultButton(registerBtn);// 设置回车键

        jContentPane = new JPanel();// 新建jPanel面板
        jContentPane.setLayout(null);
        jContentPane.setBackground(new Color(255, 255, 255));
        jContentPane.add(jLabel_userName, null);
        jContentPane.add(jLabel_password, null);
        jContentPane.add(jLabel_password2, null);
        jContentPane.add(loginButton, null);
        jContentPane.add(registerBtn, null);
        jContentPane.add(jUserNameField, null);
        jContentPane.add(jPasswordField, null);
        jContentPane.add(jPasswordField2, null);
        jContentPane.add(titleLabel, null);

        jContentPane.add(jComboBox, null);
        jContentPane.add(jLabel_privilege, null);
        setContentPane(jContentPane);

        btnListener = new BtnListener();
        loginButton.addActionListener(btnListener);
        registerBtn.addActionListener(btnListener);

    }
    /**
     * @监听类
     * @author Administrator
     */
    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try{
                // 点击注册
                if (e.getSource() == registerBtn) {
                    //获取用户名密码
                    String id = Utils.newId();
                    String username = jUserNameField.getText().trim();
                    String password =new String(jPasswordField.getPassword()).trim();//char to String
                    String password2 =new String(jPasswordField2.getPassword()).trim();//char to String
                    //判断
                    if("".equals(username)){
                        JOptionPane.showMessageDialog(null, "用户名不能为空");return;
                    }
                    if("".equals(password) || "".equals(password2)){
                        JOptionPane.showMessageDialog(null, "密码不能为空");return;
                    }
                    if(!password.equals(password2)){
                        JOptionPane.showMessageDialog(null, "密码输入不一致！");return;
                    }
                    //获取用户类型
                    int userTypeIndex=jComboBox.getSelectedIndex();
                                        //0 : 管理员
                    if(userTypeIndex==0){
                        AdminDao dao = AdminDao.me();
                        Admin user = dao.getBy("username",username);
                        if(user!=null) //用户已存在
                        {
                            JOptionPane.showMessageDialog(null, "用户名已存在！", "注册失败",JOptionPane.WARNING_MESSAGE);
                            return;
                        }else {
                            //插入用户
                            Admin t = new Admin ();//插入新用户
                                 String name = "";                                              String tele = "";                                              String gender = "";                                                        t.setId(id);
                            t.setUsername(username);
                            t.setPassword(password);
                            dao.insert(t);
                            JOptionPane.showMessageDialog(null, "注册成功！请登陆。", "注册成功",JOptionPane.INFORMATION_MESSAGE);
                            goLogin();
                            return;
                        }
                    }
                    //1 : 教师
                    if(userTypeIndex==1){
                        TeacherDao dao = TeacherDao.me();
                        Teacher user = dao.getBy("username",username);
                        if(user!=null) //用户已存在
                        {
                            JOptionPane.showMessageDialog(null, "用户名已存在！", "注册失败",JOptionPane.WARNING_MESSAGE);
                            return;
                        }else {
                            //插入用户
                            Teacher t = new Teacher ();//插入新用户
                                 String name = "";                                              String gender = "";                                              String numb = "";                                              Integer age = 0;                                                        t.setId(id);
                            t.setUsername(username);
                            t.setPassword(password);
                            dao.insert(t);
                            JOptionPane.showMessageDialog(null, "注册成功！请登陆。", "注册成功",JOptionPane.INFORMATION_MESSAGE);
                            goLogin();
                            return;
                        }
                    }
                    //2 : 学生
                    if(userTypeIndex==2){
                        StudentDao dao = StudentDao.me();
                        Student user = dao.getBy("username",username);
                        if(user!=null) //用户已存在
                        {
                            JOptionPane.showMessageDialog(null, "用户名已存在！", "注册失败",JOptionPane.WARNING_MESSAGE);
                            return;
                        }else {
                            //插入用户
                            Student t = new Student ();//插入新用户
                                 String name = "";                                              String numb = "";                                              String gender = "";                                              Integer age = 0;                                              String clazz = "";                                                        t.setId(id);
                            t.setUsername(username);
                            t.setPassword(password);
                            dao.insert(t);
                            JOptionPane.showMessageDialog(null, "注册成功！请登陆。", "注册成功",JOptionPane.INFORMATION_MESSAGE);
                            goLogin();
                            return;
                        }
                    }
                }
                else if (e.getSource() == loginButton) {//返回登陆
                    dispose();
                    Login login = new Login();
                    login.setVisible(true);
                }
            }catch (Exception e1){
                JOptionPane.showMessageDialog(null, "数据库连接失败，请检查JdbcUtils.java中的数据库配置", "数据库连接失败", JOptionPane.WARNING_MESSAGE);
                e1.printStackTrace();
            }
        }
    }

    private void goLogin(){
        dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    private JPanel jContentPane = null;
    private JButton loginButton = null;
    private JButton registerBtn = null;
    private JTextField jUserNameField = null;
    private  JPasswordField jPasswordField = null;
    private  JPasswordField jPasswordField2 = null;
    private JLabel titleLabel = null;

    private JLabel jLabel_userName = null;
    private JLabel jLabel_password = null;
    private JLabel jLabel_password2 = null;
    private JLabel jLabel_privilege = null;
    private URL imgURL = null;

    private BtnListener btnListener = null;
    private JComboBox jComboBox = null;


}

