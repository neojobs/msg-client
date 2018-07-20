package com.hexun.msg.client.remoting.proto;

import com.hexun.msg.client.remoting.exception.RemotingCommandException;

/**
 * Created by neo on 2018/7/20.
 */
public interface CommandCustomHeader {
    void checkFields() throws RemotingCommandException;
}
