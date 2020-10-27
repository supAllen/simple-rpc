package org.wjw.rpc.center;

import java.util.List;

/**
 * 服务发现于注册
 */
public interface Register {
    void register(String serviceUri, String hostAndPort) throws Exception;
    void unRegister(String serviceUri) throws Exception;
    List<String> lookup(String serviceUri) throws Exception;
}
