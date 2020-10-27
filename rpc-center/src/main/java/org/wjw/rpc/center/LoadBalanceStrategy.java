package org.wjw.rpc.center;

/**
 * 负载均衡策略
 */
public interface LoadBalanceStrategy {
    String balance(String path) throws Exception;
}
