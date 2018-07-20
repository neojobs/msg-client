package com.hexun.msg.client.remoting;

import com.hexun.msg.client.remoting.proto.RemotingCommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by neo on 2018/7/20.
 */
public class ResponseFuture {

    private final static Map<Integer, ResponseFuture> FUTURES = new ConcurrentHashMap<Integer, ResponseFuture>();
    private boolean isDone = false;
    private RemotingCommand request;
    private RemotingCommand response;
    private long timeoutMills;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public ResponseFuture(RemotingCommand request, long timeoutMills) {
        this.request = request;
        this.timeoutMills = timeoutMills;
        FUTURES.put(request.getOpaque(), this);
    }

    public RemotingCommand get() {
        if(!isDone) {

            try {
                lock.lock();
                condition.await(timeoutMills, TimeUnit.MILLISECONDS);
            }catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                lock.unlock();
            }
        }
        return response;
    }

    public void setResponse(RemotingCommand response) {
        this.response = response;
    }

    public static ResponseFuture getResponseFuture(int opaque) {
        return FUTURES.get(opaque);
    }
}
