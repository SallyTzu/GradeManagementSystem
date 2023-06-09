package com.codeying.entity;

import com.codeying.utils.excel.ExcelCell;
import java.util.Date;
import java.io.Serializable;

//成绩实体类
public class Achieve implements Serializable {
    //主键
    private String id;

    //课程
    @ExcelCell(index = 2)
    private String courseid;
    private Course courseidFrn;

    //学生
    @ExcelCell(index = 3)
    private String studentid;
    private Student studentidFrn;

    //成绩
    @ExcelCell(index = 4)
    private Double score;

    //评定时间
    @ExcelCell(index = 5)
    private Date createtime;

    public String getId () {
        return id;
    }

    public void setId (String id ) {
        this.id = id;
    }

        public String getCourseid () {
        return courseid;
    }

    public void setCourseid (String courseid ) {
        this.courseid = courseid;
    }

        public Course getCourseidFrn(){
        return  courseidFrn;
    }

    public void setCourseidFrn (Course courseidFrn){
        this.courseidFrn = courseidFrn;
    }

    public String getStudentid () {
        return studentid;
    }

    public void setStudentid (String studentid ) {
        this.studentid = studentid;
    }

    public Student getStudentidFrn(){
        return  studentidFrn;
    }

    public void setStudentidFrn (Student studentidFrn){
        this.studentidFrn = studentidFrn;
    }

    public Double getScore () {
        return score;
    }

    public void setScore (Double score ) {
        this.score = score;
    }

    public Date getCreatetime () {
        return createtime;
    }

    public void setCreatetime (Date createtime ) {
        this.createtime = createtime;
    }

}

