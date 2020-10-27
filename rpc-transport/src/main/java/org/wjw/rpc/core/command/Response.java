package org.wjw.rpc.core.command;

import java.io.Serializable;
import java.util.UUID;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-29 16:35
 **/
public class Response implements Serializable {
    private static final long serialVersionUID = 0L;

    private final String id;
    private final Object resp;

    public static final Response EMPTY = new Response(new Object(), UUID.randomUUID().toString());

    public Response(Object resp, String id) {
        this.resp = resp;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Object getResp() {
        return resp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"resp\":")
                .append(resp);
        sb.append('}');
        return sb.toString();
    }
}
