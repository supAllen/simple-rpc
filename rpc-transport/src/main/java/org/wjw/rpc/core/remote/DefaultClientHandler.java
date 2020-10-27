package org.wjw.rpc.core.remote;

import org.wjw.rpc.core.command.Response;
import org.wjw.rpc.core.future.ResponseFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-30 17:23
 **/
public class DefaultClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("建立连接成功...");
        ctx.writeAndFlush("ping");
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(msg);
        System.out.println(msg.getClass().getSimpleName());
        if (msg instanceof Response) {
            System.out.println("设置响应结果\t" + System.currentTimeMillis() + "\t" + msg + "\t" + ((Response) msg).getId());
            ResponseFuture.responseReceived((Response) msg);
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
