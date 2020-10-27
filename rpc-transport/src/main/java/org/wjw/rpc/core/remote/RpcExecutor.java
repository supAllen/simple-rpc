package org.wjw.rpc.core.remote;

import org.wjw.rpc.center.ZKClient;
import org.wjw.rpc.core.command.Command;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-30 17:03
 **/
public class RpcExecutor {

    private RpcClient rpcClient;

    public RpcExecutor(String serverUri) {
        String data = null;
        try {
            data = ZKClient.getInstance().getData(serverUri);
            System.out.println("host and port: "+data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isOk = false;
        if (!Objects.isNull(data)) {
            String[] hostAndPort = data.split(":");
            if (hostAndPort.length == 2) {
                String host = hostAndPort[0];
                String port = hostAndPort[1];
                if (StringUtils.isNumeric(port)) {
                    int portInt = Integer.parseInt(port);
                    this.rpcClient = new RpcClient(host, portInt);
                    isOk = true;
                }
            }
        }
        if (!isOk) {
            throw new RuntimeException("server uri is illegal");
        }
    }

    public RpcExecutor(String host, int port) {
        this.rpcClient = new RpcClient(host, port);
    }

    public Object exectue(Command command) {
        return rpcClient.send(command);
    }
}
