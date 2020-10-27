package org.wjw.rpc.core.remote;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.wjw.rpc.center.ZKClient;

import java.net.InetAddress;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-29 16:44
 **/
public class RpcServer implements IServer {

    private final Integer port;
    private final String serviceUri;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public RpcServer(Integer port, String serviceUri) {
        this.port = port;
        this.serviceUri = serviceUri;
    }

    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(
                                new ObjectEncoder(),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                new DefaultServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture channelFuture = bootstrap.bind(port);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("server init success...");
                System.out.println(String.format("server ip: %s, port: %d", InetAddress.getLoopbackAddress(), port));
                ZKClient.getInstance().createNode(serviceUri, String.format("%s:%d",
                        InetAddress.getLoopbackAddress().getHostAddress(), port));
            }
        });
        channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("停机...");
                ZKClient.getInstance().delNode(serviceUri);
            }
        });
    }

    @Override
    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
