package org.wjw.rpc.center;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.wjw.rpc.center.utils.CenterStringUtils.objs2Str;

/**
 * @description: 注册服务的相关操作
 * @author: wang.jianwen
 * @create: 2020-10-14 10:54
 **/
public class ZKClient {
    static final String CONN_ADDR = System.getProperty("zk.addr", "127.0.0.1:2181");
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
            addListener(path);
        } else {
            path = path + "/ch/" + UUID.randomUUID().toString().substring(5, 15);
        }
        cf.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data.getBytes());
    }

    /**
     * 监听根节点，就意味着监听 一个服务中所有的结点情况，当结点异常时可以做上报操作
     * @param path 根节点path
     * @throws Exception
     */
    private void addListener(String path) throws Exception {
        TreeCache treeCache = new TreeCache(cf, path);
        treeCache.start();
        treeCache.getUnhandledErrorListenable().addListener((message, e) -> {
            String msg = objs2Str(Lists.asList(message, e.getSuppressed()));
            System.out.println("结点错误原因 ==》"+msg);
        });

        treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
            switch (treeCacheEvent.getType()) {
                case NODE_ADDED:
                    System.out.println("add instance");
                case NODE_REMOVED:
                    String nodePath = treeCacheEvent.getData().getPath();
                    System.out.println(nodePath);
                    // todo 结点因为不明原因挂掉 notice();
                    // todo 如果是主动断开连接+心跳，还要删除path
            }
        });
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
