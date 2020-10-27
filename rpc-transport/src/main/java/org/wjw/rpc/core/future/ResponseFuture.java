package org.wjw.rpc.core.future;

import com.google.common.collect.Maps;
import org.wjw.rpc.core.command.Request;
import org.wjw.rpc.core.command.Response;
import io.netty.util.concurrent.DefaultPromise;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-10-12 19:06
 **/
public class ResponseFuture extends DefaultPromise {
    private static final Map<String, ResponseFuture> futures = Maps.newConcurrentMap();

    public ResponseFuture(Request request) {
        futures.putIfAbsent(request.getId(), this);
    }

    private void setResp(Object res) {
        super.setSuccess(res);
    }

    public static void responseReceived(Response response) {
        ResponseFuture future = futures.remove(response.getId());
        if (future != null) {
            future.setResp(response.getResp());
        }
    }

    public Object getRes(long expire, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return super.get(expire, unit);
    }
}
