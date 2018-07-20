package com.hexun.msg.client;

import com.hexun.msg.client.remoting.NettyClientConfig;
import com.hexun.msg.client.remoting.NettyRemoteClient;
import com.hexun.msg.client.remoting.proto.CommandCustomHeader;
import com.hexun.msg.client.remoting.proto.GetRouteInfoRequestHeader;
import com.hexun.msg.client.remoting.proto.RemotingCommand;
import com.hexun.msg.client.remoting.proto.RequestCode;
import org.testng.annotations.Test;

/**
 * Created by neo on 2018/7/20.
 */
public class NettyClientTest {

    @Test
    public void test() {
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        nettyClientConfig.setRemoteHost("localhost");
        nettyClientConfig.setRemotePort(9876);

        NettyRemoteClient client = new NettyRemoteClient(nettyClientConfig);
        client.start();

        GetRouteInfoRequestHeader header = new GetRouteInfoRequestHeader();
        header.setTopic("HELLO");
        RemotingCommand command = RemotingCommand.createRequest(RequestCode.GET_ROUTEINTO_BY_TOPIC, header);
        RemotingCommand response = client.invokeSync(command, 4000);
        System.out.println(response);
        try {
            Thread.sleep(1000*10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
