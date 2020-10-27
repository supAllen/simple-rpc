package org.wjw.rpc.core.ah;

import org.wjw.rpc.core.command.RequestCommand;
import org.wjw.rpc.core.command.Response;
import org.wjw.rpc.core.service.ServiceMeta;

import java.lang.reflect.InvocationTargetException;

/**
 * 快速失败模式
 */
public class FailFast extends AbstractHa {

    public FailFast(ServiceMeta serviceMeta, Object serviceInstance) {
        super(serviceMeta, serviceInstance);
    }

    @Override
    public Response handler(RequestCommand command) throws InvocationTargetException, IllegalAccessException {
        return super.handler(command);
    }
}
