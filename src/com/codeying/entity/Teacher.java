package com.codeying.entity;

import com.codeying.utils.excel.ExcelCell;
import java.util.Date;

import java.io.Serializable;

public class Teacher implements Serializable ,LoginUser{

    /**
     * 主键
    */
    private String id;
    
        /**
     * 用户名
    */
    @ExcelCell(index = 2)
    private String username;
    
        /**
     * 密码
    */
    @ExcelCell(index = 3)
    private String password;
    
        /**
     * 姓名
    */
    @ExcelCell(index = 4)
    private String name;
    
        /**
     * 性别
    */
    @ExcelCell(index = 5)
    private String gender;
    
        /**
     * 编号
    */
    @ExcelCell(index = 6)
    private String numb;
    
        /**
     * 年龄
    */
    @ExcelCell(index = 7)
    private Integer age;
    
    

    public String getId () {
        return id;
    }
    public void setId (String id ) {
        this.id = id;
    }
        public String getUsername () {
        return username;
    }
    public void setUsername (String username ) {
        this.username = username;
    }
        public String getPassword () {
        return password;
    }
    public void setPassword (String password ) {
        this.password = password;
    }
        public String getName () {
        return name;
    }
    public void setName (String name ) {
        this.name = name;
    }
        public String getGender () {
        return gender;
    }
    public void setGender (String gender ) {
        this.gender = gender;
    }
        public String getNumb () {
        return numb;
    }
    public void setNumb (String numb ) {
        this.numb = numb;
    }
        public Integer getAge () {
        return age;
    }
    public void setAge (Integer age ) {
        this.age = age;
    }
        /**
     * 角色
     */
    private String role = "teacher";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}

