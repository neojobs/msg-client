package com.hexun.msg.client.remoting.codec;

import com.hexun.msg.client.remoting.proto.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by neo on 2018/7/20.
 */
public class NettyEncoder extends MessageToByteEncoder <RemotingCommand> {
    protected void encode(ChannelHandlerContext channelHandlerContext, RemotingCommand command, ByteBuf byteBuf) throws Exception {
        System.out.println("here");
        try {
            byteBuf.writeBytes(command.encode());
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}
