package org.wjw.rpc.core.remote;

import org.wjw.rpc.core.command.Command;
import org.wjw.rpc.core.command.Request;
import org.wjw.rpc.core.future.ResponseFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.wjw.rpc.core.command.Response.EMPTY;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-30 15:56
 **/
public class RpcClient {
    static final boolean SSL = System.getProperty("ssl") != null;
    private Channel channel;
    private EventLoopGroup group;

    public RpcClient(String host, int port) {
        try {
            init(host, port);
        } catch (SSLException e) {
            e.printStackTrace();
        }
    }

    private void init(String host, int port) throws SSLException {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .remoteAddress(host, port)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                            }
                            p.addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new DefaultClientHandler());
                        }
                    });

            // Start the connection attempt.
            ChannelFuture connectFuture = b.connect();
            connectFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("connect success");
                    System.out.println("connect-" + System.currentTimeMillis());
                }
            });

            long start = System.currentTimeMillis();
            System.out.println("channel get pre-" + start);
            channel = connectFuture.channel();
            System.out.println("channel get time-" + (System.currentTimeMillis() - start));
            System.out.println(channel);

            channel.closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("channel is close");
                    System.out.println("close-" + System.currentTimeMillis());
                }
            });
        } finally {
            System.out.println("客户端创建成功...");
        }
    }

    public Object send(Command command) {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(channel);
        System.out.println("send-" + System.currentTimeMillis());
        Request request = new Request(command);
        ResponseFuture responseFuture = new ResponseFuture(request);
        ChannelFuture channelFuture = channel.writeAndFlush(request);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("send success...");
                }
            }
        });
        try {
            Object res = responseFuture.getRes(2, TimeUnit.SECONDS);
            System.out.println("get res: " + res);
            return res;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return EMPTY;
        }
    }
}
