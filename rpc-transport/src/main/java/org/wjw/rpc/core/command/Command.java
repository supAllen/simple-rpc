package org.wjw.rpc.core.command;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-29 16:33
 **/
public class Command implements Serializable {
    private static final long serialVersionUID = 0L;

    private String action = "";
    private String method = "";
    private Map<String, Object> params = new HashMap<String, Object>();

    public Command(String action, String method, Map<String, Object> params) {
        this.action = action;
        this.method = method;
        this.params = params;
    }

    public String getAction() {
        return action;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"action\":\"")
                .append(action).append('\"');
        sb.append(",\"method\":\"")
                .append(method).append('\"');
        sb.append(",\"params\":")
                .append(params);
        sb.append('}');
        return sb.toString();
    }
}
