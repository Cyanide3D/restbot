package ru.server.util;

import org.apache.commons.lang3.StringUtils;

public class UrlUtil {

    public static String cutSlash(String path) {
        String result = path;
        if (path.length()==1) return path;
        if (path.startsWith("/")) result = path.substring(1);
        if (path.endsWith("/")) result = result.substring(0, result.length()-1);

        return result;
    }

}