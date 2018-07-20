package com.hexun.msg.client.remoting;

/**
 * Created by neo on 2018/7/20.
 */
public class NettyClientConfig {
    private String remoteHost;
    private int remotePort;

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }


}
