package org.wjw.rpc.core.service;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-30 15:25
 **/
public class DefaultRpcContext implements RpcContext {

    private static final Map<String, Map<String, Object>> context = Maps.newConcurrentMap();
    public static final DefaultRpcContext instance = new DefaultRpcContext();

    private DefaultRpcContext() {
    }

    @Override
    public <T> T get(String dataSetKey, String key) {
        return (T) context.get(dataSetKey).get(key);
    }

    @Override
    public void put(String dataSetKey, String key, Object value) {
        context.putIfAbsent(dataSetKey, new HashMap<String, Object>(){{putIfAbsent(key, value);}});
    }
}
