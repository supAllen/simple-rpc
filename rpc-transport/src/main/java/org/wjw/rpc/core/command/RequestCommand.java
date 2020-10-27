package org.wjw.rpc.core.command;

import java.util.Map;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-29 17:21
 **/
public class RequestCommand {
    private final String id;
    private final String method;
    private final Map<String, Object> params;

    public RequestCommand(Request request) {
        Command command = request.getCommand();
        this.id = request.getId();
        this.method = command.getMethod();
        this.params = command.getParams();
    }

    public String getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
