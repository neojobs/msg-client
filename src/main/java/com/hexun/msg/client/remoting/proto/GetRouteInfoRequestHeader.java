package com.hexun.msg.client.remoting.proto;

import com.hexun.msg.client.remoting.exception.RemotingCommandException;

/**
 * Created by neo on 2018/7/20.
 */
public class GetRouteInfoRequestHeader implements CommandCustomHeader {
    private String topic;
    public void checkFields() throws RemotingCommandException {

    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
