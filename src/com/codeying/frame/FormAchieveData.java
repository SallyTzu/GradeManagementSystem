package com.codeying.frame;
import com.codeying.dao.*;
import com.codeying.entity.*;
import com.codeying.utils.*;
import com.codeying.utils.excel.ExcelUtil;
import com.mysql.cj.util.StringUtils;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.io.*;

public class FormAchieveData extends JInternalFrame {
    AchieveDao dao = AchieveDao.me();

    private List<Achieve> listData;//存储数据

    public FormAchieveData () {
        initialize();//初始化界面
        initPrivilege();//设置权限
        initData();//初始化界面数据
        query(null);
        //查询数据
    }
    //初始化权限
    private void initPrivilege(){
        String role = Login.type;
        if (role == "admin")
        {
            jButtonDel.setVisible(true);
            jButtonEdit.setVisible(true);
            }
        if (role == "teacher")
        {
            jButtonDel.setVisible(true);
            jButtonEdit.setVisible(true);
            }
        if (role == "student")
        {
        }
        //开启导出excel功能
        if(Config.export){
            jButtonExport.setVisible(true);
        }
    }

    public void initData() {
        //设置数据头部
        List<String> l = new ArrayList<>();
                        l.add("主键");
                        l.add("课程");
                        l.add("学生");
                        l.add("成绩");
                        l.add("评定时间");
                String heads[] = l.toArray(new String[0]);
        model = new DefaultTableModel(null, heads);
        //加载下拉框数据
                                                                            setCourseid ( con_courseid);                    setCourseid ( tb_courseid);
                                                            setStudentid ( con_studentid);                    setStudentid ( tb_studentid);
                                                                                }

    /**
     * 内部类监听器模块
     */
    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //点击查询按钮
            String s;
            if(e.getSource().equals(jButtonQuery)){
                Map<String,String> paramMap = new HashMap<>();
                                                                                                                                        //条件 课程
                                                    s = getByIndexCourseid (con_courseid .getSelectedIndex());//外键
                            if(!StringUtils.isNullOrEmpty(s))paramMap.put("courseid",s);
                                                                                                                            //条件 学生
                                                    s = getByIndexStudentid (con_studentid .getSelectedIndex());//外键
                            if(!StringUtils.isNullOrEmpty(s))paramMap.put("studentid",s);
                                                                                                                            //条件 成绩
                                                     s= con_scoreL .getText().trim();//数字
                            if(Utils.strToDouble(s)!=null)paramMap.put("scoreL",s);
                            s= con_scoreR .getText().trim();
                            if(Utils.strToDouble(s)!=null)paramMap.put("scoreR",s);
                                                                                                                                    query(paramMap);
            }else if(e.getSource().equals(jButtonDel)){//点击删除按钮
                String id = tb_id.getText().trim(); //获取id
                if(StringUtils.isNullOrEmpty(id)){
                    JOptionPane.showMessageDialog(null, "未选中任何记录！");return;
                }
                //弹出确认框
                int opt = JOptionPane.showConfirmDialog(null, "确认删除?", "确认信息", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    //确认继续操作
                    int res = dao.deleteById(id);
                    if(res == 0){
                        JOptionPane.showMessageDialog(null, "删除失败！");
                    }else {
                        JOptionPane.showMessageDialog(null, "删除成功！");
                        query(null);//删除完了重新查询。
                    }
                }
            //点击保存按钮
            }else if(e.getSource().equals(jButtonEdit)){
                String id = tb_id.getText().trim();//获取id
                if(StringUtils.isNullOrEmpty(id)){
                    JOptionPane.showMessageDialog(null, "未选中任何记录！");return;
                }
                                                                                                                                                                                                                                                                                                        
                Achieve t = new  Achieve ();
                String templateStr;
                t.setId(id);
                //更新可更新字段  //非法判断
                t.setId (tb_id .getText().trim());//字符串
                t.setCourseid (getByIndexCourseid (tb_courseid .getSelectedIndex()));//外键
                t.setStudentid (getByIndexStudentid (tb_studentid .getSelectedIndex()));//外键
                String score = tb_score .getText().trim();//double
                if(!StringUtils.isNullOrEmpty(score) && Utils.strToDouble(score)==null){
                    JOptionPane.showMessageDialog(null, Utils.DIGIT_ERROR);return;
                }
                t.setScore (Utils.strToDouble(score));
                                                                                            String createtime = tb_createtime .getText().trim();//时间
                if(!StringUtils.isNullOrEmpty(createtime) && Utils.strToDate(createtime)==null){
                    JOptionPane.showMessageDialog(null, Utils.TIME_ERROR);return;
                }
                t.setCreatetime (Utils.strToDate(createtime));
                int res = dao.updateById(t);
                if(res == 0){
                    JOptionPane.showMessageDialog(null, "更新失败！");
                }else {
                    JOptionPane.showMessageDialog(null, "更新成功！");
                    query(null);
                }
            }
            //导出数据到excel
            else if(e.getSource().equals(jButtonExport)){
                List<Achieve> data = dao.list();
                //循环遍历list数据，统计、获取外键数据
                for(Achieve stAv : data){
                    //获取外键数据
                stAv.setCourseidFrn ( CourseDao.me().getBy("id",stAv .getCourseid () ) );
                stAv.setCourseid(stAv.getCourseidFrn().getName() );
                //获取外键数据
                stAv.setStudentidFrn ( StudentDao.me().getBy("id",stAv .getStudentid () ) );
                stAv.setStudentid(stAv.getStudentidFrn().getName() );
                }
                HashMap<String,String> headMap = new LinkedHashMap<>();
                                                                                headMap.put("courseid","课程");
                                                            headMap.put("studentid","学生");
                                                            headMap.put("score","成绩");
                                                            headMap.put("createtime","评定时间");
                                try {
                    File f=new File("成绩数据.xls");
                    OutputStream out = new FileOutputStream(f);
                    ExcelUtil.exportExcel(headMap, data, out);
                    out.close();
                    JOptionPane.showMessageDialog(null, "导出成功："+f.getAbsolutePath());
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
                tb_id .setText(jTable.getValueAt(index,0).toString());
                o = jTable.getValueAt(index,1);
    tb_courseid .setSelectedItem(o == null?"":o.toString());
    o = jTable.getValueAt(index,2);
    tb_studentid .setSelectedItem(o == null?"":o.toString());
                                                tb_score .setText(jTable.getValueAt(index,3).toString());
                                                tb_createtime .setText(jTable.getValueAt(index,4).toString());
                        }
}
}

    //查询数据到界面
    private void query(Map<String,String> wrapper){
        if(wrapper == null){
            wrapper = new HashMap<>();
        }
    if(Login.type.equals("student")){
        wrapper.put("studentid",Login.student .getId());//只能查看和自己相关的内容
        con_studentid .setEnabled(false);
        tb_studentid .setEnabled(false);
    }
        listData = dao.list(wrapper);
        //设置外键、统计、平均
                                                                                                //定义需要统计的字段
                                double sumscore = 0;
                                                for (Achieve t : listData){
                                                                                                            //获取外键数据
                    t .setCourseidFrn( CourseDao.me().getBy ("id", t .getCourseid () ) );
                                                                                //获取外键数据
                    t .setStudentidFrn( StudentDao.me().getBy ("id", t .getStudentid () ) );
                                                                  if(t .getScore ()!=null)sumscore += t .getScore ();
                                                                                                }
        //统计和平均：
        int count = listData.size();
        jLabelCounts.setText("记录数:" + count + "");
        String labelStaAvg = "";
                                                                                                                                                if( count> 0){
                    labelStaAvg += "成绩"+"平均："+sumscore / count + "; ";
                }
                                                            jLabelTips.setText(labelStaAvg);
        model.setRowCount(count);// 设置行数
        String d;
        for(int i=0;i<count;i++){
            int j = 0;
            model.setValueAt(listData.get(i).getId (), i, j);
        j++;
                if(listData.get(i).getCourseidFrn()!=null)
            model.setValueAt(listData.get(i).getCourseidFrn ().getName (), i, j);
        else model.setValueAt("", i, j);
        j++;
                if(listData.get(i).getStudentidFrn()!=null)
            model.setValueAt(listData.get(i).getStudentidFrn ().getName (), i, j);
        else model.setValueAt("", i, j);
        j++;
                model.setValueAt(listData.get(i).getScore (), i, j);
        j++;
                d = Utils.dateToStr(listData.get(i).getCreatetime ());
        model.setValueAt(d, i, j);
        j++;
            }

        jTable.setModel(model);
        jTable.setAutoCreateRowSorter(true);//为JTable设置排序器
        hideColumn(jTable,0);//隐藏id列
    }

    //隐藏表格列，设置不可见。
    void hideColumn(JTable table,int column) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setMaxWidth(0);
        tc.setPreferredWidth(0);
        tc.setWidth(0);
        tc.setMinWidth(0);
        table.getTableHeader().getColumnModel().getColumn(column).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(column).setMinWidth(0);
    }
                //存储外键数据 tb_courseid
        private List<Course> listCourseid;
        private void setCourseid (JComboBox comboBox){
            CourseDao dao = CourseDao.me();
            listCourseid = dao.list(null);
            comboBox.addItem("");
            for (Course i : listCourseid){
                                comboBox.addItem(i.getName ());//对应外键字段
            }
        }
        //获取id
        private String getByIndexCourseid (int index){
            index = index-1;
            if(index<0 || index>=listCourseid .size()){
                return "";
            }
            return listCourseid .get(index).getId();
        }
                //存储外键数据 tb_studentid
        private List<Student> listStudentid;
        private void setStudentid (JComboBox comboBox){
            StudentDao dao = StudentDao.me();
            listStudentid = dao.list(null);
            comboBox.addItem("");
            for (Student i : listStudentid){
                                comboBox.addItem(i.getName ());//对应外键字段
            }
        }
        //获取id
        private String getByIndexStudentid (int index){
            index = index-1;
            if(index<0 || index>=listStudentid .size()){
                return "";
            }
            return listStudentid .get(index).getId();
        }
                private void initialize() {
        int x,y;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setSize(1200, 650);
        this.setTitle("成绩");
        //检索区域
        jPanel.setLayout(null);
        jPanel.setBounds(new Rectangle(1, 1, 1150, 80));
        jPanel.setBorder(BorderFactory.createTitledBorder("查找数据"));
        jPanel.add(jButtonQuery, null);//查询按钮
        jButtonQuery.setBounds(new Rectangle(1000, 50, 100, 24));
                                           jPanel.add( labelC_courseid, null);
        labelC_courseid .setBounds(new Rectangle(10, 20, 60, 24));
                                    jPanel.add(con_courseid, null);
            con_courseid .setBounds(new Rectangle(80, 20, 100, 24));
                                                jPanel.add( labelC_studentid, null);
        labelC_studentid .setBounds(new Rectangle(185, 20, 60, 24));
                                    jPanel.add(con_studentid, null);
            con_studentid .setBounds(new Rectangle(255, 20, 100, 24));
                                                jPanel.add( labelC_score, null);
        labelC_score .setBounds(new Rectangle(360, 20, 60, 24));
                                     jPanel.add(con_scoreL, null);
            con_scoreL .setBounds(new Rectangle(430, 20, 100, 24));
                        jPanel.add(labelA_score, null);
            labelA_score .setBounds(new Rectangle(530, 20, 10, 24));
                        jPanel.add(con_scoreR, null);
            con_scoreR .setBounds(new Rectangle(540, 20, 100, 24));
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
        tb_id.setVisible(false);
        x = 970; y = 110;
        //添加右边输入框和标签用来更新
            //添加右边输入框和标签用来更新
            jContentPane.add(label_courseid, null);
        label_courseid .setBounds(new Rectangle(x, y, 65, 24));
                    jContentPane.add(tb_courseid, null);
            tb_courseid .setBounds(new Rectangle(x+65, y, 130, 24));
            y+=35;
                            tb_courseid .setEnabled(false);
                    //添加右边输入框和标签用来更新
            jContentPane.add(label_studentid, null);
        label_studentid .setBounds(new Rectangle(x, y, 65, 24));
                    jContentPane.add(tb_studentid, null);
            tb_studentid .setBounds(new Rectangle(x+65, y, 130, 24));
            y+=35;
                            tb_studentid .setEnabled(false);
                    //添加右边输入框和标签用来更新
            jContentPane.add(label_score, null);
        label_score .setBounds(new Rectangle(x, y, 65, 24));
                    jContentPane.add(tb_score, null);
            tb_score .setBounds(new Rectangle(x+65, y, 130, 24));
            y+=35;
                            //添加右边输入框和标签用来更新
            jContentPane.add(label_createtime, null);
        label_createtime .setBounds(new Rectangle(x, y, 65, 24));
                    jContentPane.add(tb_createtime, null);
            tb_createtime .setBounds(new Rectangle(x+65, y, 130, 24));
            y+=35;
                            tb_createtime .setEnabled(false);
                    //按钮
        jContentPane.add(jButtonDel, null);
        jButtonDel.setVisible(false);
        jButtonDel.setBounds(new Rectangle(x, y, 80, 24));
        jContentPane.add(jButtonEdit, null);
        jButtonEdit.setVisible(false);
        jButtonEdit.setBounds(new Rectangle(x+90, y, 80, 24));
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
    private JButton jButtonDel =  new JButton("删除");
    private JButton jButtonQuery = new JButton("查询");
    private JTextField tb_id = new JTextField();

                                                      private JLabel label_courseid = new JLabel("课程");
                        private JComboBox tb_courseid = new JComboBox();
                                        private JLabel labelC_courseid = new JLabel("课程");
                        private JComboBox con_courseid = new JComboBox();
                                             private JLabel label_studentid = new JLabel("学生");
                        private JComboBox tb_studentid = new JComboBox();
                                        private JLabel labelC_studentid = new JLabel("学生");
                        private JComboBox con_studentid = new JComboBox();
                                             private JLabel label_score = new JLabel("成绩");
                        private JTextField tb_score = new JTextField();
                                        private JLabel labelC_score = new JLabel("成绩");
                         private JTextField con_scoreL = new JTextField();
            private JTextField con_scoreR = new JTextField();
            private JLabel labelA_score = new JLabel("~");
                                             private JLabel label_createtime = new JLabel("评定时间");
                        private JTextField tb_createtime = new JTextField();
                                    
}

