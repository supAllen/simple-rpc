package org.wjw.rpc.core.ah;

import org.wjw.rpc.center.ZKClient;
import org.wjw.rpc.core.command.RequestCommand;
import org.wjw.rpc.core.command.Response;
import org.wjw.rpc.core.remote.RpcExecutor;
import org.wjw.rpc.core.service.ServiceMeta;

import java.lang.reflect.InvocationTargetException;

/**
 * 当前节点如果遇到错误，它会尝试调用另外一个节点
 */
public class FailOver extends AbstractHa{

    static final int count;
    static final ZKClient zkClient;
    static {
        zkClient = ZKClient.getInstance();
        count = Integer.parseInt(System.setProperty("count", String.valueOf(3)));
    }

    public FailOver(ServiceMeta serviceMeta, Object serviceInstance) {
        super(serviceMeta, serviceInstance);
    }

    @Override
    public Response handler(RequestCommand command) throws InvocationTargetException, IllegalAccessException {
        try {
            return super.handler(command);
        } catch (Exception e) {
            retryOtherAndReturnRes();
        }
    }

    private void retryOtherAndReturnRes() {
        int c = count;
        while (c-- != 0) {
            // rand node request
            // exist success record current Exception
            // from zk rm cur node
            // return res
        }
    }
}
