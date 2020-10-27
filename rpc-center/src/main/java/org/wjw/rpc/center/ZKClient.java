package org.wjw.rpc.center;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import sun.security.util.ArrayUtil;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @description: 注册服务的相关操作
 * @author: wang.jianwen
 * @create: 2020-10-14 10:54
 **/
public class ZKClient {
    static final String CONN_ADDR = System.getProperty("zk-addr", "127.0.0.1:2181");
    static final int SESSION_TIMEOUT = 5000;
    private static final CuratorFramework cf;
    private static final ZKClient instance = new ZKClient();
    static {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        cf = CuratorFrameworkFactory.builder()
                .connectString(CONN_ADDR)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(retryPolicy)
                .build();
        cf.start();
    }

    public static ZKClient getInstance() {
        return instance;
    }

    public void createNode(String path, String data) throws Exception {
        createNode(path, data, CreateMode.PERSISTENT);
    }

    public void createNode(String path, String data, CreateMode mode) throws Exception {
        Stat stat = cf.checkExists().forPath(path);
        if (Objects.isNull(stat)) {
            cf.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data.getBytes());
        }
        path = path + "/ch/" + UUID.randomUUID().toString().substring(5, 15);
        cf.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data.getBytes());
    }

    public void delNode(String path) throws Exception {
        Stat stat = cf.checkExists().forPath(path);
        if (Objects.isNull(stat)) {
            return;
        }
        cf.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
    }

    public String getData(String path) throws Exception {
        Stat stat = cf.checkExists().forPath(path);
        if (Objects.isNull(stat)) {
            return null;
        }
        return new String(cf.getData().forPath(path));
    }

    public List<String> getChild(String path) throws Exception {
        Stat stat = cf.checkExists().forPath(path);
        if (Objects.isNull(stat)) {
            return null;
        }
        List<String> paths = Lists.newArrayList();
        List<String> chPaths = cf.getChildren().forPath(path);
        if (CollectionUtils.isNotEmpty(chPaths)) {
            paths.addAll(chPaths);
        }
        chPaths.add(path);
        List<String> datas = Lists.newArrayList();
        for (String p : paths) {
            byte[] data = cf.getData().forPath(p);
            if (ArrayUtils.isEmpty(data)) {
                continue;
            }
            datas.add(new String(data));
        }
        return datas;
    }
}
