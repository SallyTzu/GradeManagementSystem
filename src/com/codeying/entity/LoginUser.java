package com.codeying.entity;

/**
 * 用户接口
 * 采用面向接口编程方式，所有的登录用户实体类都实现此接口，标记为可登录用户
 */
public interface LoginUser {
    //用来标记登录用户的。
    String getId();

    String getUsername ();

    String getPassword ();

    String getRole();

}

