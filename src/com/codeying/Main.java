package com.codeying;

import com.codeying.frame.Login;
import com.codeying.utils.component.StyleConfig;

/*
 * 主类，这个类是本程序的入口,运行这个类来启动程序！
 */

public class Main {
    public static void main(String[] args) {
        StyleConfig.initStyle();
        Login login = new Login();
        login.setVisible(true);
    }
}


