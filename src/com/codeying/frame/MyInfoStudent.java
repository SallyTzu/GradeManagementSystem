package com.codeying.frame;

import com.codeying.dao.StudentDao;
import com.codeying.entity.Student;
import com.codeying.utils.Utils;
import com.mysql.cj.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class MyInfoStudent extends JInternalFrame {
    private JPanel jContentPane;
    private JLabel label_role = new JLabel("角色");
    private JTextField tb_role = new JTextField();
    private JLabel label_username = new JLabel("用户名");
    private JTextField tb_username= new JTextField();
    private JLabel label_name = new JLabel("姓名");
    private JTextField tb_name= new JTextField();
    private JLabel label_numb = new JLabel("学号");
    private JTextField tb_numb= new JTextField();
    private JLabel label_gender = new JLabel("性别");private JComboBox tb_gender = new JComboBox();
    private JLabel label_age = new JLabel("年龄");
    private JTextField tb_age= new JTextField();
    private JLabel label_clazz = new JLabel("班级");
    private JTextField tb_clazz= new JTextField();
    private JButton btn = new JButton("保存");
    private URL imgURL ;
    private BtnListener btnListener;
    public MyInfoStudent (){
        init();
    }
    /**
     * 监听类
     */
    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Student t = Login.student;
            if (StringUtils.isNullOrEmpty(tb_name .getText().trim())) {
                JOptionPane.showMessageDialog(null, "姓名 不可为空");
                return;
            }
            if (StringUtils.isNullOrEmpty(tb_numb .getText().trim())) {
                JOptionPane.showMessageDialog(null, "学号 不可为空");
                return;
            }
            if(FormStudentAdd .illegalAdd( Login.student .getId(),"username",
                    tb_username .getText().trim())){
                JOptionPane.showMessageDialog(null, "用户名 : 已存在！");
                return;
            }
            t.setUsername (tb_username .getText().trim());t.setName (tb_name .getText().trim());
            if(FormStudentAdd .illegalAdd( Login.student .getId(),"numb",tb_numb .getText().trim())){
                JOptionPane.showMessageDialog(null, "学号 : 已存在！");
                return;
            }
            t.setNumb (tb_numb .getText().trim());
            t.setGender ((String)(tb_gender .getSelectedItem()));
            String age = tb_age .getText().trim();
            if(!StringUtils.isNullOrEmpty(age) && Utils.strToInt(age)==null){
                JOptionPane.showMessageDialog(null, Utils.DIGIT_ERROR);
                return;
            }
            t.setAge (Utils.strToInt(age));
            t.setClazz (tb_clazz .getText().trim());
            StudentDao dao = StudentDao.me();
            int res = dao.updateById(t);
            if(res==1){
                Login.student = t;
                JOptionPane.showMessageDialog(null, "更新成功！");
            }else{
                JOptionPane.showMessageDialog(null, "更新失败");
            }
        }
    }

    /**
     * 初始化界面
     */
    public void init(){
        //设置下拉框\外键
        tb_gender .addItem("男");
        tb_gender .addItem("女");
                                                                                                                            //设置主窗体
        this.setSize(296, 350);
        this.setTitle("个人中心");//窗体名
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        int x = 29,y = 0;

        label_username.setBounds(new Rectangle(x, y, 71, 19));
        tb_username.setBounds(new Rectangle(x+90, y, 124, 23));
        tb_username.setText(Login.student .getUsername());
        tb_username.setEditable(false);
        y+=30;

        label_role.setBounds(new Rectangle(x, y, 71, 19));
        tb_role.setBounds(new Rectangle(x+90, y, 124, 23));
        tb_role.setText(Login.student .getRole());
        tb_role.setEditable(false);
        y+=30;

        label_name .setBounds(new Rectangle(x, y, 71, 19));
        tb_name .setBounds(new Rectangle(x+90, y, 124, 23));
        tb_name .setText(Login.student . getName ());
        y+=30;

        label_numb .setBounds(new Rectangle(x, y, 71, 19));
        tb_numb .setBounds(new Rectangle(x+90, y, 124, 23));
        tb_numb .setText(Login.student . getNumb ());
        y+=30;

        label_gender .setBounds(new Rectangle(x, y, 71, 19));
        tb_gender .setBounds(new Rectangle(x+90, y, 124, 23));
        tb_gender .setSelectedItem(Login.student .getGender ());
        y+=30;

        label_age .setBounds(new Rectangle(x, y, 71, 19));
        tb_age .setBounds(new Rectangle(x+90, y, 124, 23));
        Integer   age = Login.student .getAge ();
        tb_age .setText( age == null ? "" : age + "");
        y+=30;

        label_clazz .setBounds(new Rectangle(x, y, 71, 19));
        tb_clazz .setBounds(new Rectangle(x+90, y, 124, 23));
        tb_clazz .setText(Login.student . getClazz ());
        y+=30;

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
                                
}

