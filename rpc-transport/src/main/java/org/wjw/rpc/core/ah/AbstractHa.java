package org.wjw.rpc.core.ah;

import org.wjw.rpc.core.service.ServiceMeta;
import org.wjw.rpc.core.service.ServiceProcessor;

public abstract class AbstractHa extends ServiceProcessor {

    public AbstractHa(ServiceMeta serviceMeta, Object serviceInstance) {
        super(serviceMeta, serviceInstance);
    }
}
