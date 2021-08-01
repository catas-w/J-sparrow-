package com.catas.glimmer.util;

import java.util.List;

public enum StringUtil {
    ;

    //获取List参数值
    public static String getListString(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String s : list) {
            result.append(s).append(" ");
        }
        return result.toString();
    }

}
