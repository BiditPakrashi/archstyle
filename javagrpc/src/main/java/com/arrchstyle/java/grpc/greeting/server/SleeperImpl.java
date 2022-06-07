package com.arrchstyle.java.grpc.greeting.server;

import com.arrchstyle.java.grpc.utils.Sleeper;

public class SleeperImpl implements Sleeper {

    @Override
    public void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}
