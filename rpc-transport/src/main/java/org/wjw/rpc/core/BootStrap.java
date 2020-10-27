package org.wjw.rpc.core;

import org.wjw.rpc.core.constant.RecConstants;
import org.wjw.rpc.core.remote.RpcServer;
import org.wjw.rpc.core.service.*;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-29 16:29
 **/
public class BootStrap {
    private Object instance;
    private ServiceMeta serviceMeta = new ServiceMeta();

    public BootStrap(String serviceUri, String interfaceName, Object instance) {
        setServiceUri(serviceUri);
        setInterface(interfaceName);
        setInstance(instance);
        init();
    }

    private void init() {
        String rpcPort = System.getProperty("rpcPort", "15000");
        System.out.println(rpcPort);
        RpcServer rpcServer = new RpcServer(Integer.valueOf(rpcPort), serviceMeta.getServiceUri());
        rpcServer.start();
        Processor serviceProcessor = new ServiceProcessor(serviceMeta, instance);
        RpcContext rpcContext = DefaultRpcContext.instance;
        rpcContext.put(RecConstants.RPC_SERVICE, serviceMeta.getServiceUri(), serviceProcessor);
        rpcContext.put(RecConstants.RPC_META, serviceMeta.getServiceUri(), serviceMeta);
        System.out.println("初始化完成");
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("宕机...");
//            try {
//                ZKClient.instance.delNode(serviceMeta.getServiceUri());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }));
    }

    public void setInterface(String interfaceName) {
        serviceMeta.setInterfaceName(interfaceName);
    }

    public void setServiceUri(String serviceUri) {
        serviceMeta.setServiceUri(serviceUri);
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
