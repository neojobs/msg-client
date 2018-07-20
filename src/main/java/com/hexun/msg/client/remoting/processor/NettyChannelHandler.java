package com.hexun.msg.client.remoting.processor;

import com.hexun.msg.client.remoting.ResponseFuture;
import com.hexun.msg.client.remoting.proto.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by neo on 2018/7/20.
 */
public class NettyChannelHandler extends SimpleChannelInboundHandler<RemotingCommand> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RemotingCommand remotingCommand) throws Exception {
        ResponseFuture future = ResponseFuture.getResponseFuture(remotingCommand.getOpaque());
        if(future != null) {
            future.setResponse(remotingCommand);
        }
    }
}
