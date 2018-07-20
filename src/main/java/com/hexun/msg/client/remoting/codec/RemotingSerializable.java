package com.hexun.msg.client.remoting.codec;

import com.alibaba.fastjson.JSON;
import com.hexun.msg.client.remoting.proto.RemotingCommand;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by neo on 2018/7/20.
 */
public class RemotingSerializable {
    private final static Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    public static byte[] serialize(RemotingCommand command) {
        final String json = JSON.toJSONString(command, false);
        try {
            return json.getBytes(CHARSET_UTF8.name());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static RemotingCommand unserialize(byte []data) {
        try {
            String str = new String(data, CHARSET_UTF8.name());
            System.out.println(str);
            return JSON.parseObject(str, RemotingCommand.class);
        } catch (UnsupportedEncodingException e) {

        }
        return  null;
    }
}
