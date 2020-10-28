package org.wjw.rpc.center.utils;

import com.google.common.base.Joiner;

import java.util.List;

public class CenterStringUtils {

    public static String objs2Str(List<Object> args) {
        return objs2Str("\t", args);
    }

    public static String objs2Str(String delimiter,List<Object> args) {
        return Joiner.on(delimiter).join(args);
    }
}
