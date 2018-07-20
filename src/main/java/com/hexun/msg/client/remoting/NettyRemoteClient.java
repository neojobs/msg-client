package com.hexun.msg.client.remoting;

import com.hexun.msg.client.remoting.codec.NettyDecoder;
import com.hexun.msg.client.remoting.codec.NettyEncoder;
import com.hexun.msg.client.remoting.processor.NettyChannelHandler;
import com.hexun.msg.client.remoting.proto.RemotingCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by neo on 2018/7/20.
 */
public class NettyRemoteClient {

    private NettyClientConfig nettyClientConfig;
    private EventLoopGroup eventLoopGroup;
    private EventLoopGroup workerEventGroup;
    private Bootstrap bootstrap;
    private ChannelFuture channelFuture;

    public NettyRemoteClient(NettyClientConfig nettyClientConfig) {
        this.nettyClientConfig = nettyClientConfig;
        this.eventLoopGroup = new NioEventLoopGroup(1);
        this.workerEventGroup = new NioEventLoopGroup(4);
    }

    public void start() {
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(
                        workerEventGroup,
                        new NettyEncoder(),
                        new NettyDecoder(),
                        new NettyChannelHandler());
            }
        });
        bootstrap.channel(NioSocketChannel.class);
        channelFuture = bootstrap.connect(nettyClientConfig.getRemoteHost(), nettyClientConfig.getRemotePort());
    }

    public void shutdown() {
        this.eventLoopGroup.shutdownGracefully();
        this.workerEventGroup.shutdownGracefully();
    }

    public RemotingCommand invokeSync(RemotingCommand request, long timeoutMills) {
        ResponseFuture future = new ResponseFuture(request, timeoutMills);
        Channel channel = channelFuture.channel();
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture f) throws Exception {
                System.out.println();
                System.out.println(f.isSuccess());
            }
        });
        return future.get();
    }
}
