package org.wjw.rpc.core.service;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-30 15:25
 **/
public interface RpcContext {
    /**
     * 读取数据。
     * @param dataSetKey
     * @param key
     * @return
     */
    <T> T get(String dataSetKey, String key);

    /**
     * 存储数据。
     * @param dataSetKey
     * @param key
     * @param value
     */
    void put(String dataSetKey, String key, Object value);
}
