package org.wjw.rpc.center;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.CreateMode;
import org.wjw.rpc.center.loadbalance.LoadBalanceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 注册中心
 */
public class DefaultRegister implements Register{

    static final LoadBalanceStrategy loadBalance;
    static final ZKClient zkClient;
    static {
        zkClient = ZKClient.getInstance();
        loadBalance = LoadBalanceFactory.rand();
    }

    /**
     * 注册服务时默认采用 zk临时结点+netty 长连接实现
     * @param serviceUri
     * @param hostAndPort
     */
    @Override
    public void register(String serviceUri, String hostAndPort) throws Exception {
        zkClient.createNode(serviceUri, hostAndPort, CreateMode.EPHEMERAL);
    }

    @Override
    public void unRegister(String serviceUri) throws Exception {
        zkClient.delNode(serviceUri);
    }

    @Override
    public List<String> lookup(String serviceUri) throws Exception {
        String addr = zkClient.getData(serviceUri);
        List<String> addrs = Lists.newArrayList();
        if (Objects.nonNull(addr)) {
            addrs.add(addr);
            List<String> childAddr = zkClient.getChild(serviceUri);
            if (CollectionUtils.isNotEmpty(childAddr)) {
                addrs.addAll(childAddr);
            }
        }
        return addrs;
    }

}
