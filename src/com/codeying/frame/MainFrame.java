package com.codeying.frame;

import com.codeying.utils.component.StyleConfig;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
 
/**
 * 登陆后展示的主窗体
 */
public class MainFrame extends JFrame {
    public MainFrame() {
        super();
        initialize();
        // 设置访问权限
        if("admin".equals(Login.type)){
            tmsiTeacherAdd. setEnabled(true);
            tmsiTeacherData .setEnabled(true);
            tmsiTeacher .setVisible(true);

            tmsiStudentAdd. setEnabled(true);
            tmsiStudentData .setEnabled(true);
            tmsiStudent .setVisible(true);

            tmsiCourseAdd. setEnabled(true);tmsiCourseData .setEnabled(true);
            tmsiCourse .setVisible(true);

            tmsiAchieveAdd. setEnabled(true);
            tmsiAchieveData .setEnabled(true);
            tmsiAchieve .setVisible(true);
        }

        if("teacher".equals(Login.type)){
            tmsiStudentAdd. setEnabled(true);
            tmsiStudentData .setEnabled(true);
            tmsiStudent .setVisible(true);

            tmsiCourseAdd. setEnabled(true);
            tmsiCourseData .setEnabled(true);
            tmsiCourse .setVisible(true);

            tmsiAchieveAdd. setEnabled(true);
            tmsiAchieveData .setEnabled(true);
            tmsiAchieve .setVisible(true);
        }

        if("student".equals(Login.type)){
            tmsiCourseData .setEnabled(true);//设置课程对象的启用状态
            tmsiCourse .setVisible(true);
            tmsiAchieveData .setEnabled(true);
            tmsiAchieve .setVisible(true);
        }
    }



    /**
     * 初始界面
     */
    private void initialize() {
        //初始化窗体
        this.setSize(1250, 750);// 主界面大小
        this.setTitle("学生成绩管理系统");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        BtnListener btn = new BtnListener();//设置监听类

        setContentPane(jPanel);//设置内容：面板
        jPanel.setLayout(null);
        jPanel.add(jLabel, null);
        //设置背景
        jLabel.setBounds(new Rectangle(1, -2, 1250, 750));
        if(StyleConfig.mainBgImg!=null){
            imgURL = this.getClass().getResource(StyleConfig.mainBgImg);
            jLabel.setIcon(new ImageIcon(imgURL));
        }

        //设置菜单栏
        setJMenuBar(jJMenuBar);
        jJMenuBar.setPreferredSize(new Dimension(10, 30));
        //添加子项
        jJMenuBar.add(jMenuMyInfo);
        jMenuMyInfo.add(jMenuItemMyInfo);
        jMenuMyInfo.add(jMenuItemResetPwd);
        jMenuMyInfo.add(jMenuItemLogout);
        jMenuMyInfo.addSeparator();// 分割线
        jMenuMyInfo.add(jMenuItemExit);
        jMenuItemMyInfo.addActionListener(btn);//添加监听事件
        jMenuItemResetPwd.addActionListener(btn);
        jMenuItemLogout.addActionListener(btn);
        jMenuItemExit.addActionListener(btn);

        jJMenuBar.add(tmsiAdmin);
        tmsiAdmin .setVisible(false);
        tmsiAdmin .add(tmsiAdminAdd);
        tmsiAdmin .add(tmsiAdminData);
        tmsiAdminAdd .addActionListener(btn);
        tmsiAdminAdd .setEnabled(false);
        tmsiAdminData .addActionListener(btn);
        tmsiAdminData .setEnabled(false);

        jJMenuBar.add(tmsiTeacher);
        tmsiTeacher .setVisible(false);
        tmsiTeacher .add(tmsiTeacherAdd);
        tmsiTeacher .add(tmsiTeacherData);
        tmsiTeacherAdd .addActionListener(btn);
        tmsiTeacherAdd .setEnabled(false);
        tmsiTeacherData .addActionListener(btn);
        tmsiTeacherData .setEnabled(false);

        jJMenuBar.add(tmsiStudent);
        tmsiStudent .setVisible(false);
        tmsiStudent .add(tmsiStudentAdd);
        tmsiStudent .add(tmsiStudentData);
        tmsiStudentAdd .addActionListener(btn);
        tmsiStudentAdd .setEnabled(false);
        tmsiStudentData .addActionListener(btn);
        tmsiStudentData .setEnabled(false);

        jJMenuBar.add(tmsiCourse);
        tmsiCourse .setVisible(false);
        tmsiCourse .add(tmsiCourseAdd);
        tmsiCourse .add(tmsiCourseData);
        tmsiCourseAdd .addActionListener(btn);
        tmsiCourseAdd .setEnabled(false);
        tmsiCourseData .addActionListener(btn);
        tmsiCourseData .setEnabled(false);

        jJMenuBar.add(tmsiAchieve);
        tmsiAchieve .setVisible(false);
        tmsiAchieve .add(tmsiAchieveAdd);
        tmsiAchieve .add(tmsiAchieveData);
        tmsiAchieveAdd .addActionListener(btn);
        tmsiAchieveAdd .setEnabled(false);
        tmsiAchieveData .addActionListener(btn);
        tmsiAchieveData .setEnabled(false);
    }

    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //登出
            if (e.getSource() == jMenuItemLogout) {
                dispose();
                Login login = new Login();
                login.setVisible(true);
            }
            //我的信息
            else if (e.getSource() == jMenuItemMyInfo) {
                if(Login.type.equals("admin")){
                    MyInfoAdmin t = new MyInfoAdmin ();
                    showSonFrame(t);
            }
                if(Login.type.equals("teacher")){
                    MyInfoTeacher t = new MyInfoTeacher ();
                    showSonFrame(t);
            }
                if(Login.type.equals("student")){
                    MyInfoStudent t = new MyInfoStudent ();
                    showSonFrame(t);
                }
            }
            //重置密码
            else if (e.getSource() == jMenuItemResetPwd) {
                MyPassword t = new MyPassword();
                showSonFrame(t);
            }
             //添加管理员
            else if (e.getSource() == tmsiAdminAdd ) {
                FormAdminAdd t = new  FormAdminAdd ();
                showSonFrame(t);
            }
            //管理管理员
            else if (e.getSource() == tmsiAdminData) {
                FormAdminData t = new FormAdminData ();
                showSonFrame(t);
            }
             //添加教师
            else if (e.getSource() == tmsiTeacherAdd ) {
                FormTeacherAdd t = new  FormTeacherAdd ();
                showSonFrame(t);
            }
            //管理教师
            else if (e.getSource() == tmsiTeacherData) {
                FormTeacherData t = new FormTeacherData ();
                showSonFrame(t);
            }
             //添加学生
            else if (e.getSource() == tmsiStudentAdd ) {
                FormStudentAdd t = new  FormStudentAdd ();
                showSonFrame(t);
            }
            //管理学生
            else if (e.getSource() == tmsiStudentData) {
                FormStudentData t = new FormStudentData ();
                showSonFrame(t);
            }
             //添加课程
            else if (e.getSource() == tmsiCourseAdd ) {
                FormCourseAdd t = new  FormCourseAdd ();
                showSonFrame(t);
            }
            //管理课程
            else if (e.getSource() == tmsiCourseData) {
                FormCourseData t = new FormCourseData ();
                showSonFrame(t);
            }
             //添加成绩
            else if (e.getSource() == tmsiAchieveAdd ) {
                FormAchieveAdd t = new  FormAchieveAdd ();
                showSonFrame(t);
            }
            //管理成绩
            else if (e.getSource() == tmsiAchieveData) {
                FormAchieveData t = new FormAchieveData ();
                showSonFrame(t);
            }

            // 退出系统
            else {
                System.exit(0);
            }
        }

    }
    private void showSonFrame(JInternalFrame t){
        t.setVisible(true);
        jPanel.add(t);
        try {
            t.setSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JDesktopPane jPanel = new JDesktopPane();
    private JMenuBar jJMenuBar = new JMenuBar();
    private JMenu jMenuMyInfo = new JMenu("我的");
    private JMenuItem jMenuItemMyInfo =  new JMenuItem("我的信息");
    private JMenuItem jMenuItemResetPwd = new JMenuItem("重置密码");
    private JMenuItem jMenuItemLogout = new JMenuItem("登出");
    private JMenuItem jMenuItemExit = new JMenuItem("退出");

    private JMenu tmsiAdmin  = new JMenu("管理员");
    private JMenuItem tmsiAdminAdd = new JMenuItem("添加管理员");
    private JMenuItem tmsiAdminData = new JMenuItem("查看管理员");

    private JMenu tmsiTeacher  = new JMenu("教师");
    private JMenuItem tmsiTeacherAdd = new JMenuItem("添加教师");
    private JMenuItem tmsiTeacherData = new JMenuItem("查看教师");

    private JMenu tmsiStudent  = new JMenu("学生");
    private JMenuItem tmsiStudentAdd = new JMenuItem("添加学生");
    private JMenuItem tmsiStudentData = new JMenuItem("查看学生");

    private JMenu tmsiCourse  = new JMenu("课程");
    private JMenuItem tmsiCourseAdd = new JMenuItem("添加课程");
    private JMenuItem tmsiCourseData = new JMenuItem("查看课程");

    private JMenu tmsiAchieve  = new JMenu("成绩");
    private JMenuItem tmsiAchieveAdd = new JMenuItem("添加成绩");
    private JMenuItem tmsiAchieveData = new JMenuItem("查看成绩");

    //背景图片
    private JLabel jLabel = new JLabel();
    private URL imgURL = null;

}

