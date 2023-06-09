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

public class FormCourseData extends JInternalFrame {
    CourseDao dao = CourseDao.me();

    private List<Course> listData;//存储数据

    public FormCourseData() {
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
        }

        if (role == "teacher") {
            jButtonDel.setVisible(true);
            jButtonEdit.setVisible(true);
        }

        if (role == "student") {
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
        l.add("教师");
        l.add("名称");
        l.add("学分");
        l.add("学时");
        String heads[] = l.toArray(new String[0]);
        model = new DefaultTableModel(null, heads);

        //加载下拉框数据
        setTeacherid(con_teacherid);
        setTeacherid(tb_teacherid);
    }

    /**
     * 内部类监听器模块
     */
    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //点击查询按钮
            String s;
            if (e.getSource().equals(jButtonQuery)) {
                Map<String, String> paramMap = new HashMap<>();
                //条件 教师
                s = getByIndexTeacherid(con_teacherid.getSelectedIndex());
                //外键
                if (!StringUtils.isNullOrEmpty(s)) paramMap.put("teacherid", s);
                //条件 名称
                s = con_name.getText().trim();//字符串
                if (!StringUtils.isNullOrEmpty(s)) paramMap.put("name", s);
                query(paramMap);
            } else if (e.getSource().equals(jButtonDel)) {
                //点击删除按钮
                String id = tb_id.getText().trim();
                //获取id
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
                    JOptionPane.showMessageDialog(null, "名称 不可为空");
                    return;
                }

                Course t = new Course();
                String templateStr;
                t.setId(id);
                //更新可更新字段
                // 非法判断
                t.setId(tb_id.getText().trim());//字符串
                t.setTeacherid(getByIndexTeacherid(tb_teacherid.getSelectedIndex()));//外键
                t.setName(tb_name.getText().trim());//字符串
                String course = tb_course.getText().trim();//double

                if (!StringUtils.isNullOrEmpty(course) && Utils.strToDouble(course) == null) {
                    JOptionPane.showMessageDialog(null, Utils.DIGIT_ERROR);
                    return;
                }
                t.setCourse(Utils.strToDouble(course));
                String stutime = tb_stutime.getText().trim();//int
                if (!StringUtils.isNullOrEmpty(stutime) && Utils.strToInt(stutime) == null) {
                    JOptionPane.showMessageDialog(null, Utils.DIGIT_ERROR);
                    return;
                }
                t.setStutime(Utils.strToInt(stutime));
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
                List<Course> data = dao.list();
                //循环遍历list数据，统计、获取外键数据
                for (Course stAv : data) {
                    //获取外键数据
                    stAv.setTeacheridFrn(TeacherDao.me().getBy("id", stAv.getTeacherid()));
                    stAv.setTeacherid(stAv.getTeacheridFrn().getName());

                }
                HashMap<String, String> headMap = new LinkedHashMap<>();
                headMap.put("name", "名称");
                headMap.put("course", "学分");
                headMap.put("stutime", "学时");
                try {
                    File f = new File("课程数据.xls");
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
                o = jTable.getValueAt(index, 1);
                tb_teacherid.setSelectedItem(o == null ? "" : o.toString());
                tb_name.setText(jTable.getValueAt(index, 2).toString());
                tb_course.setText(jTable.getValueAt(index, 3).toString());
                tb_stutime.setText(jTable.getValueAt(index, 4).toString());
            }
        }
    }

    //查询数据到界面
    private void query(Map<String, String> wrapper) {
        if (wrapper == null) {
            wrapper = new HashMap<>();
        }

        if (Login.type.equals("teacher")) {
            wrapper.put("teacherid", Login.teacher.getId());//只能查看和自己相关的内容
            con_teacherid.setEnabled(false);
            tb_teacherid.setEnabled(false);
        }

        listData = dao.list(wrapper);
        //设置外键、统计、平均
        for (Course t : listData) {
            //获取外键数据
            // t .setTeacheridFrn( TeacherDao.me().getBy ("id", t .getTeacherid () ) );
        }

        //统计和平均：
        int count = listData.size();
        jLabelCounts.setText("记录数:" + count + "");
        String labelStaAvg = "";
        jLabelTips.setText(labelStaAvg);
        model.setRowCount(count);// 设置行数
        String d;
        for (int i = 0; i < count; i++) {
            int j = 0;
            model.setValueAt(listData.get(i).getId(), i, j);
            j++;
            if (listData.get(i).getTeacheridFrn() != null)
                model.setValueAt(listData.get(i).getTeacheridFrn().getName(), i, j);
            else model.setValueAt("", i, j);
            j++;
            model.setValueAt(listData.get(i).getName(), i, j);
            j++;
            model.setValueAt(listData.get(i).getCourse(), i, j);
            j++;
            model.setValueAt(listData.get(i).getStutime(), i, j);
            j++;
        }

        jTable.setModel(model);
        jTable.setAutoCreateRowSorter(true);//为JTable设置排序器
        hideColumn(jTable, 0);//隐藏id列
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

    //存储外键数据 tb_teacherid
    private List<Teacher> listTeacherid;

    private void setTeacherid(JComboBox comboBox) {
        TeacherDao dao = TeacherDao.me();
        listTeacherid = dao.list(null);
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


    private void initialize() {
        int x, y;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setSize(1200, 650);
        this.setTitle("课程");

        //检索区域
        jPanel.setLayout(null);
        jPanel.setBounds(new Rectangle(1, 1, 1150, 80));
        jPanel.setBorder(BorderFactory.createTitledBorder("查找数据"));
        jPanel.add(jButtonQuery, null);//查询按钮
        jButtonQuery.setBounds(new Rectangle(1000, 50, 100, 24));
        jPanel.add(labelC_teacherid, null);
        labelC_teacherid.setBounds(new Rectangle(10, 20, 60, 24));
        jPanel.add(con_teacherid, null);
        con_teacherid.setBounds(new Rectangle(80, 20, 100, 24));
        jPanel.add(labelC_name, null);
        labelC_name.setBounds(new Rectangle(185, 20, 60, 24));
        jPanel.add(con_name, null);
        con_name.setBounds(new Rectangle(255, 20, 100, 24));

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
        jContentPane.add(label_teacherid, null);
        label_teacherid.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_teacherid, null);
        tb_teacherid.setBounds(new Rectangle(x + 65, y, 130, 24));
        y += 35;
        //添加右边输入框和标签用来更新
        jContentPane.add(label_name, null);
        label_name.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_name, null);
        tb_name.setBounds(new Rectangle(x + 65, y, 130, 24));
        y += 35;
        //添加右边输入框和标签用来更新
        jContentPane.add(label_course, null);
        label_course.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_course, null);
        tb_course.setBounds(new Rectangle(x + 65, y, 130, 24));
        y += 35;
        //添加右边输入框和标签用来更新
        jContentPane.add(label_stutime, null);
        label_stutime.setBounds(new Rectangle(x, y, 65, 24));
        jContentPane.add(tb_stutime, null);
        tb_stutime.setBounds(new Rectangle(x + 65, y, 130, 24));
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

    private JLabel label_teacherid = new JLabel("教师");
    private JComboBox tb_teacherid = new JComboBox();
    private JLabel labelC_teacherid = new JLabel("教师");
    private JComboBox con_teacherid = new JComboBox();
    private JLabel label_name = new JLabel("名称");
    private JTextField tb_name = new JTextField();
    private JLabel labelC_name = new JLabel("名称");
    private JTextField con_name = new JTextField();
    private JLabel label_course = new JLabel("学分");
    private JTextField tb_course = new JTextField();
    private JLabel label_stutime = new JLabel("学时");
    private JTextField tb_stutime = new JTextField();

}

