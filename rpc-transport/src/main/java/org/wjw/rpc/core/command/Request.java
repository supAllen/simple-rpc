package org.wjw.rpc.core.command;

import java.io.Serializable;
import java.util.UUID;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-29 16:35
 **/
public class Request implements Serializable {
    private static final long serialVersionUID = 0L;

    private final String id;
    private final Command command;

    public Request(Command command) {
        this.command = command;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"command\":")
                .append(command);
        sb.append('}');
        return sb.toString();
    }
}
