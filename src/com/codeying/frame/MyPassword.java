package com.codeying.frame;
         import com.codeying.dao.AdminDao;
            import com.codeying.dao.TeacherDao;
            import com.codeying.dao.StudentDao;
            
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * 修改密码窗体
 */
public class MyPassword extends JInternalFrame {

    private JPanel jContentPane;
    private JButton btn;
    private  JPasswordField jPasswordField;//旧密码
    private  JPasswordField jPasswordField2;//新密码
    private  JPasswordField jPasswordField3;//确认密码
    private JLabel jLabel_password ;
    private JLabel jLabel_password2 ;
    private JLabel jLabel_password3 ;
    private JLabel titleLabel ;
    private URL imgURL ;
    private BtnListener btnListener;

    public MyPassword(){
        //设置主窗体
        this.setResizable(true);
        this.setSize(296, 300);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setTitle("重置密码");//窗体名
        //设置标题
        titleLabel = new JLabel("重置密码",JLabel.CENTER);
        titleLabel.setBounds(new Rectangle(0, 0, 291, 50));
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));

        //用户名密码标签和输入框
        jLabel_password = new JLabel("旧 密 码：");
        jLabel_password.setBounds(new Rectangle(29, 60, 71, 19));
        jLabel_password2 = new JLabel("新 密 码：");
        jLabel_password2.setBounds(new Rectangle(29, 95, 71, 19));
        jLabel_password3 = new JLabel("确认密码：");
        jLabel_password3.setBounds(new Rectangle(29, 130, 71, 19));

        jPasswordField = new JPasswordField();
        jPasswordField.setBounds(new Rectangle(120, 60, 124, 23));
        jPasswordField2 = new JPasswordField();
        jPasswordField2.setBounds(new Rectangle(120, 95, 124, 23));
        jPasswordField3 = new JPasswordField();
        jPasswordField3.setBounds(new Rectangle(120, 130, 124, 23));

        btn = new JButton("提交修改");
        btn.setBounds(new Rectangle(130, 165, 100, 26));
        getRootPane().setDefaultButton(btn);// 设置回车键

        jContentPane = new JPanel();// 新建jPanel面板
        jContentPane.setLayout(null);
        jContentPane.setBackground(new Color(255, 255, 255));
        setContentPane(jContentPane);
        jContentPane.add(jPasswordField);
        jContentPane.add(jPasswordField2);
        jContentPane.add(jPasswordField3);
        jContentPane.add(btn);
        jContentPane.add(jLabel_password);
        jContentPane.add(jLabel_password2);
        jContentPane.add(jLabel_password3);
        jContentPane.add(titleLabel);

        btnListener = new BtnListener();
        btn.addActionListener(btnListener);
    }

    /**
     * 监听类
     */
    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //检查输入
            String password =new String(jPasswordField.getPassword()).trim();//char to String
            String password2 =new String(jPasswordField2.getPassword()).trim();//char to String
            String password3 =new String(jPasswordField3.getPassword()).trim();//char to String
            String oldPwd = "";
                  if(Login.type=="admin"){
                oldPwd = Login. admin .getPassword();
            }
                      if(Login.type=="teacher"){
                oldPwd = Login. teacher .getPassword();
            }
                      if(Login.type=="student"){
                oldPwd = Login. student .getPassword();
            }
                            if("".equals(password) || !oldPwd.equals(password)){
                JOptionPane.showMessageDialog(null, "旧密码错误！");return;
            }
            if("".equals(password2) || "".equals(password3)){
                JOptionPane.showMessageDialog(null, "密码不能为空");return;
            }
            if(!password3.equals(password2)){
                JOptionPane.showMessageDialog(null, "密码输入不一致！");return;
            }
            //重置密码
              if(Login.type=="admin"){
                AdminDao dao = AdminDao.me();
                Login.admin .setPassword(password2);
                dao.updateById(Login.admin);
            }
              if(Login.type=="teacher"){
                TeacherDao dao = TeacherDao.me();
                Login.teacher .setPassword(password2);
                dao.updateById(Login.teacher);
            }
              if(Login.type=="student"){
                StudentDao dao = StudentDao.me();
                Login.student .setPassword(password2);
                dao.updateById(Login.student);
            }
                JOptionPane.showMessageDialog(null, "修改成功！");
        }
    }
}

