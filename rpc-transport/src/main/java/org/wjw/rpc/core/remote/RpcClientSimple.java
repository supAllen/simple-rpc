package org.wjw.rpc.core.remote;

import org.wjw.rpc.core.command.Command;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-30 16:45
 **/
public class RpcClientSimple {
    private RpcExecutor rpcExecutor;

    private RpcClientSimple() {
    }

    private RpcClientSimple(String serverUri) {
        this.rpcExecutor = new RpcExecutor(serverUri);
    }

    public static RpcClientSimple getInstance(String serverUri) {
        return new RpcClientSimple(serverUri);
    }

    public Object execute(Command command) {
        return rpcExecutor.exectue(command);
    }
}
