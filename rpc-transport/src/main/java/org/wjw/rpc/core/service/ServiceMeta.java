package org.wjw.rpc.core.service;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-29 16:58
 **/
public class ServiceMeta {
    private String serviceUri;
    private String interfaceName;

    public ServiceMeta() {
    }

    public void setServiceUri(String serviceUri) {
        this.serviceUri = serviceUri;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getServiceUri() {
        return serviceUri;
    }

    public String getInterfaceName() {
        return interfaceName;
    }
}
