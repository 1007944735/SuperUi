package com.sgevf.ui.utils;

import java.util.ArrayList;
import java.util.List;

public class ParseUtil {
    public static List<String> arrayToList(String[] strings) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            list.add(strings[i]);
        }
        return list;
    }

    public String formatTime(String format) {
        return null;
    }

    public static List<String> copyList(List<String> source, List<String> target) {
        List<String> l = target;
        for (String string : source) {
            if (l == null) {
                l = new ArrayList<>();
            }
            l.add(string);
        }
        return l;
    }
}
