package com.codeying.entity;

import com.codeying.utils.excel.ExcelCell;

import java.io.Serializable;
public class Admin implements Serializable ,LoginUser{
    private String id;
    @ExcelCell(index = 2)
    private String username;

    @ExcelCell(index = 3)
    private String password;

    @ExcelCell(index = 4)
    private String name;

    @ExcelCell(index = 5)
    private String tele;

    @ExcelCell(index = 6)
    private String gender;
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

    public String getTele () {
        return tele;
    }

    public void setTele (String tele ) {
        this.tele = tele;
    }

    public String getGender () {
        return gender;
    }

    public void setGender (String gender ) {
        this.gender = gender;
    }

    //角色
    private String role = "admin";
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}

