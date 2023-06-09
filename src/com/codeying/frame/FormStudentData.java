package com.codeying.frame;

import com.codeying.dao.*;
import com.codeying.entity.*;
import com.codeying.utils.*;
import com.codeying.utils.excel.ExcelUtil;
import com.mysql.cj.util.StringUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.io.*;

public class FormStudentData extends JInternalFrame {
    private Boolean editPermission = false;
    StudentDao dao = StudentDao.me();

    private List<Student> listData;//存储数据

    public FormStudentData() {
        initialize();//初始化界面
        initPrivilege();//设置权限
        initData();//初始化界面数据
        query(null);
        //查询数据
    }

    //初始化权限
    private void initPrivilege() {
        String role = Login.type;
        if (role == "admin") {
            jButtonDel.setVisible(true);
            jButtonEdit.setVisible(true);
            editPermission = true;
        }
        if (role == "teacher") {
            jButtonDel.setVisible(true);
            jButtonEdit.setVisible(true);
            editPermission = true;
        }
        if (role == "student") {
            tb_password.setEnabled(false);
        }
        //开启导出excel功能
        if (Config.export) {
            jButtonExport.setVisible(true);
        }
    }

    public void initData() {
        //设置数据头部
        List<String> l = new ArrayList<>();
        l.add("主键");
        l.add("用户名");
        l.add("密码");
        l.add("姓名");
        l.add("学号");
        l.add("性别");
        l.add("年龄");
        l.add("班级");
        String heads[] = l.toArray(new String[0]);
        model = new DefaultTableModel(null, heads);
        //加载下拉框数据
        con_gender.addItem("");
        tb_gender.addItem("");
        con_gender.addItem("男");
        tb_gender.addItem("男");
        con_gender.addItem("女");
        tb_gender.addItem("女");
    }

    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //点击查询按钮
            String s;
            if (e.getSource().equals(jButtonQuery)) {
                Map<String, String> paramMap = new HashMap<>();
                //条件 用户名
                s = con_username.getText().trim();//字符串
                if (!StringUtils.isNullOrEmpty(s)) paramMap.put("username", s);
                //条件 姓名
                s = con_name.getText().trim();//字符串
                if (!StringUtils.isNullOrEmpty(s)) paramMap.put("name", s);
                //条件 性别
                s = (String) con_gender.getSelectedItem();//下拉框
                if (!StringUtils.isNullOrEmpty(s)) paramMap.put("gender", s);
                query(paramMap);
            } else if (e.getSource().equals(jButtonDel)) {//点击删除按钮
                String id = tb_id.getText().trim(); //获取id
                if (StringUtils.isNullOrEmpty(id)) {
                    JOptionPane.showMessageDialog(null, "未选中任何记录！");
                    return;
                }
                //弹出确认框
                int opt = JOptionPane.showConfirmDialog(null, "确认删除?", "确认信息", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    //确认继续操作
                    int res = dao.deleteById(id);
                    if (res == 0) {
                        JOptionPane.showMessageDialog(null, "删除失败！");
                    } else {
                        JOptionPane.showMessageDialog(null, "删除成功！");
                        query(null);//删除完了重新查询。
                    }
                }
                //点击保存按钮
            } else if (e.getSource().equals(jButtonEdit)) {
                String id = tb_id.getText().trim();//获取id
                if (StringUtils.isNullOrEmpty(id)) {
                    JOptionPane.showMessageDialog(null, "未选中任何记录！");
                    return;
                }
                if (StringUtils.isNullOrEmpty(tb_name.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "姓名 不可为空");
                    return;
                }
                if (StringUtils.isNullOrEmpty(tb_numb.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "学号 不可为空");
                    return;
                }

                Student t = new Student();
                String templateStr;
                t.setId(id);
                //更新可更新字段  //非法判断
                t.setId(tb_id.getText().trim());//字符串
                templateStr = tb_username.getText().trim();
                if (FormStudentAdd.illegalAdd(id, "username", templateStr)) {
                    JOptionPane.showMessageDialog(null, "用户名 : 已存在！");
                    return;
                }
                t.setUsername(templateStr);
                t.setPassword(tb_password.getText().trim());//字符串
                t.setName(tb_name.getText().trim());//字符串
                templateStr = tb_numb.getText().trim();
                if (FormStudentAdd.illegalAdd(id, "numb", templateStr)) {
                    JOptionPane.showMessageDialog(null, "学号 : 已存在！");
                    return;
                }
                t.setNumb(templateStr);
                t.setGender((String) (tb_gender.getSelectedItem()));//下拉框
                String age = tb_age.getText().trim();//int
                if (!StringUtils.isNullOrEmpty(age) && Utils.strToInt(age) == null) {
                    JOptionPane.showMessageDialog(null, Utils.DIGIT_ERROR);
                    return;
                }
                t.setAge(Utils.strToInt(age));
                t.setClazz(tb_clazz.getText().trim());//字符串
                int res = dao.updateById(t);
                if (res == 0) {
                    JOptionPane.showMessageDialog(null, "更新失败！");
                } else {
                    JOptionPane.showMessageDialog(null, "更新成功！");
                    query(null);
                }
            }
            //导出数据到excel
            else if (e.getSource().equals(jButtonExport)) {
                List<Student> data = dao.list();
                //循环遍历list数据，统计、获取外键数据
                for (Student stAv : data) {
                }
                HashMap<String, String> headMap = new LinkedHashMap<>();
                headMap.put("username", "用户名");
                headMap.put("password", "密码");
                headMap.put("name", "姓名");
                headMap.put("numb", "学号");
                headMap.put("gender", "性别");
                headMap.put("age", "年龄");
                headMap.put("clazz", "班级");
                try {
                    File f = new File("学生数据.xls");
                    OutputStream out = new FileOutputStream(f);
                    ExcelUtil.exportExcel(headMap, data, out);
                    out.close();
                    JOptionPane.showMessageDialog(null, "导出成功：" + f.getAbsolutePath());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * 表格数据点击事件
     */
    public class TableListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (jTable.getSelectedRow() != -1) {
                Object o;
                int index = jTable.getSelectedRow();
                tb_id.setText(jTable.getValueAt(index, 0).toString());
                tb_username.setText(jTable.getValueAt(index, 1).toString());
                if (editPermission)
                    tb_password.setText(jTable.getValueAt(index, 2).toString());
                tb_name.setText(jTable.getValueAt(index, 3).toString());
                tb_numb.setText(jTable.getValueAt(index, 4).toString());
                o = jTable.getValueAt(index, 5);
                tb_gender.setSelectedItem(o == null ? "" : o.toString());
                tb_age.setText(jTable.getValueAt(index, 6).toString());
                tb_clazz.setText(jTable.getValueAt(index, 7).toString());
            }
        }
    }

    //查询数据到界面
    private void query(Map<String, String> wrapper) {
        if (wrapper == null) {
            wrapper = new HashMap<>();
        }
        listData = dao.list(wrapper);
        //设置外键、统计、平均
        //定义需要统计的字段
        int sumage = 0;
        for (Student t : listData) {
            if (t.getAge() != null) sumage += t.getAge();
        }
        //统计和平均：
        int count = listData.size();
        jLabelCounts.setText("记录数:" + count + "");
        String labelStaAvg = "";
        if (count > 0) {
            labelStaAvg += "年龄" + "平均：" + sumage / count + "; ";
        }
        jLabelTips.setText(labelStaAvg);
        model.setRowCount(count);// 设置行数
        String d;
        for (int i = 0; i < count; i++) {
            int j = 0;
            model.setValueAt(listData.get(i).getId(), i, j);
            j++;
            model.setValueAt(listData.get(i).getUsername(), i, j);
            j++;
            model.setValueAt(listData.get(i).getPassword(), i, j);
            j++;
            model.setValueAt(listData.get(i).getName(), i, j);
            j++;
            model.setValueAt(listData.get(i).getNumb(), i, j);
            j++;
            model.setValueAt(listData.get(i).getGender(), i, j);
            j++;
            model.setValueAt(listData.get(i).getAge(), i, j);
            j++;
            model.setValueAt(listData.get(i).getClazz(), i, j);
            j++;
        }

        jTable.setModel(model);
        jTable.setAutoCreateRowSorter(true);//为JTable设置排序器
        hideColumn(jTable, 0);//隐藏id列
        if (!editPermission) hideColumn(jTable, 2);//没有编辑权限，则隐藏密码列
    }

    //隐藏表格列，设置不可见。
    void hideColumn(JTable table, int column) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setMaxWidth(0);
        tc.setPreferredWidth(0);
        tc.setWidth(0);
        tc.setMinWidth(0);
        table.getTableHeader().getColumnModel().getColumn(column).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(column).setMinWidth(0);
    }

    private void initialize() {
        int x, y;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setSize(1200, 650);
        this.setTitle("学生");
        //检索区域
        jPanel.setLayout(null);
        jPanel.setBounds(new Rectangle(1, 1, 1150, 80));
        jPanel.setBorder(BorderFactory.createTitledBorder("查找数据"));
        jPanel.add(jButtonQuery, null);//查询按钮
        jButtonQuery.setBounds(new Rectangle(1000, 50, 100, 24));
        jPanel.add(labelC_username, null);
        labelC_username.setBounds(new Rectangle(10, 20, 60, 24));
        jPanel.add(con_username, null);
        con_username.setBounds(new Rectangle(80, 20, 100, 24));
        jPanel.add(labelC_name, null);
        labelC_name.setBounds(new Rectangle(185, 20, 60, 24));
        jPanel.add(con_name, null);
        con_name.setBounds(new Rectangle(255, 20, 100, 24));
        jPanel.add(labelC_gender, null);
        labelC_gender.setBounds(new Rectangle(360, 20, 60, 24));
        jPanel.add(con_gender, null);
        con_gender.setBounds(new Rectangle(430, 20, 100, 24));
        //父面板
        this.setContentPane(jContentPane);
        jContentPane.setLayout(null);
        jContentPane.setBorder(BorderFactory.createTitledBorder(""));
        jContentPane.add(jPanel, null);
        jContentPane.add(jScrollPane, null);
        jScrollPane.setBounds(new Rectangle(10, 110, 950, 450));
        jScrollPane.setViewportView(jTable);
        jContentPane.add(jLabelCounts, null);
        jLabelCounts.setBounds(new Rectangle(10, 85, 200, 24));
        jContentPane.add(jLabelTips, null);
        jLabelTips.setBounds(new Rectangle(10, 560, 650, 24));
        jContentPane.add(tb_id);
        tb_id.setBounds(new Rectangle(600, 85, 100, 24));
        tb_id.setVisible(false);//存id
        x = 970;
        y = 110;
        //添加右边输入框和标签用来更新
        //添加右边输入框和标签用来更新
        jContentPane.add(label_username, null);
        label_username.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_username, null);
        tb_username.setBounds(new Rectangle(x + 65, y, 130, 24));
        y += 35;
        tb_username.setEnabled(false);
        //添加右边输入框和标签用来更新
        jContentPane.add(label_password, null);
        label_password.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_password, null);
        tb_password.setBounds(new Rectangle(x + 65, y, 130, 24));
        y += 35;
        //添加右边输入框和标签用来更新
        jContentPane.add(label_name, null);
        label_name.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_name, null);
        tb_name.setBounds(new Rectangle(x + 65, y, 130, 24));
        y += 35;
        //添加右边输入框和标签用来更新
        jContentPane.add(label_numb, null);
        label_numb.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_numb, null);
        tb_numb.setBounds(new Rectangle(x + 65, y, 130, 24));
        y += 35;
        //添加右边输入框和标签用来更新
        jContentPane.add(label_gender, null);
        label_gender.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_gender, null);
        tb_gender.setBounds(new Rectangle(x + 65, y, 130, 24));
        y += 35;
        //添加右边输入框和标签用来更新
        jContentPane.add(label_age, null);
        label_age.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_age, null);
        tb_age.setBounds(new Rectangle(x + 65, y, 130, 24));
        y += 35;
        //添加右边输入框和标签用来更新
        jContentPane.add(label_clazz, null);
        label_clazz.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_clazz, null);
        tb_clazz.setBounds(new Rectangle(x + 65, y, 130, 24));
        y += 35;
        //按钮
        jContentPane.add(jButtonDel, null);
        jButtonDel.setVisible(false);
        jButtonDel.setBounds(new Rectangle(x, y, 80, 24));
        jContentPane.add(jButtonEdit, null);
        jButtonEdit.setVisible(false);
        jButtonEdit.setBounds(new Rectangle(x + 90, y, 80, 24));
        jContentPane.add(jButtonExport, null);
        jButtonExport.setVisible(false);
        jButtonExport.setBounds(new Rectangle(800, 85, 150, 24));
        //添加监听
        jButtonQuery.addActionListener(listener);
        jButtonEdit.addActionListener(listener);
        jButtonExport.addActionListener(listener);
        jButtonDel.addActionListener(listener);
        jTable.getSelectionModel().addListSelectionListener(new TableListener());
    }

    BtnListener listener = new BtnListener();
    private JPanel jContentPane = new JPanel();//主Panel
    private JPanel jPanel = new JPanel();
    private JLabel jLabelCounts = new JLabel("记录数量：xx条");//计数用
    private JLabel jLabelTips = new JLabel("统计信息...");//平均，总计信息
    private JScrollPane jScrollPane = new JScrollPane();
    private JTable jTable = new JTable();
    DefaultTableModel model = new DefaultTableModel();
    private JButton jButtonEdit = new JButton("保存");
    private JButton jButtonExport = new JButton("导出到excel");
    private JButton jButtonDel = new JButton("删除");
    private JButton jButtonQuery = new JButton("查询");
    private JTextField tb_id = new JTextField();

    private JLabel label_username = new JLabel("用户名");
    private JTextField tb_username = new JTextField();
    private JLabel labelC_username = new JLabel("用户名");
    private JTextField con_username = new JTextField();
    private JLabel label_password = new JLabel("密码");
    private JTextField tb_password = new JTextField();
    private JLabel label_name = new JLabel("姓名");
    private JTextField tb_name = new JTextField();
    private JLabel labelC_name = new JLabel("姓名");
    private JTextField con_name = new JTextField();
    private JLabel label_numb = new JLabel("学号");
    private JTextField tb_numb = new JTextField();
    private JLabel label_gender = new JLabel("性别");
    private JComboBox tb_gender = new JComboBox();
    private JLabel labelC_gender = new JLabel("性别");
    private JComboBox con_gender = new JComboBox();
    private JLabel label_age = new JLabel("年龄");
    private JTextField tb_age = new JTextField();
    private JLabel label_clazz = new JLabel("班级");
    private JTextField tb_clazz = new JTextField();

}

