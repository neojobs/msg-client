package com.hexun.msg.client.remoting.codec;

import com.hexun.msg.client.remoting.proto.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by neo on 2018/7/20.
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder {
    private final static int MAX_FRAME_LENGTH = 65536;
    public NettyDecoder() {
        super(MAX_FRAME_LENGTH, 0,4,0,4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("time to decode");
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        return RemotingCommand.decode(frame);
    }
}
