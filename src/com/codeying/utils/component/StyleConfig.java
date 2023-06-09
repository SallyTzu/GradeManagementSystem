package com.codeying.utils.component;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class StyleConfig {
    public static int loginBgStyle = 0;//登陆页样式
    public static int loginFrameHeight = 300;//登陆页高
    public static String mainBgImg;//主页背景
    public static String theme;//主题
    public static Font font;//字体

    static {
        loginFrameHeight = 400;//登陆页高
        mainBgImg = "/com/codeying/images/main.jpg";
        theme = "com.jtattoo.plaf.mint.MintLookAndFeel";//  椭圆按钮+黄色按钮背景
        font = new Font("黑体", Font.PLAIN, 14);
    }

    public static void initStyle() {
        //主题
        try {
            UIManager.setLookAndFeel(theme);
        } catch (Exception e) {
            System.out.println("[Warning]目前暂未设置主题,但不影响程序正常运行");
        }
        //字体
        if (font != null) {
            FontUIResource fontResource = new FontUIResource(font);
            for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource) {
                    UIManager.put(key, fontResource);
                }
            }
        }

    }

}
