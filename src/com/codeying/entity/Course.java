package com.codeying.entity;

import com.codeying.utils.excel.ExcelCell;
import java.util.Date;

import java.io.Serializable;
public class Course implements Serializable {
    private String id;
    @ExcelCell(index = 2)
    private String teacherid;
    private Teacher teacheridFrn;
    @ExcelCell(index = 3)
    private String name;
    @ExcelCell(index = 4)
    private Double course;
    @ExcelCell(index = 5)
    private Integer stutime;
    public String getId () {
        return id;
    }
    public void setId (String id ) {
        this.id = id;
    }
        public String getTeacherid () {
        return teacherid;
    }
    public void setTeacherid (String teacherid ) {
        this.teacherid = teacherid;
    }
        public Teacher getTeacheridFrn(){
        return  teacheridFrn;
    }
    public void setTeacheridFrn (Teacher teacheridFrn){
        this.teacheridFrn = teacheridFrn;
    }
    public String getName () {
        return name;
    }
    public void setName (String name ) {
        this.name = name;
    }
        public Double getCourse () {
        return course;
    }
    public void setCourse (Double course ) {
        this.course = course;
    }
        public Integer getStutime () {
        return stutime;
    }
    public void setStutime (Integer stutime ) {
        this.stutime = stutime;
    }
}

