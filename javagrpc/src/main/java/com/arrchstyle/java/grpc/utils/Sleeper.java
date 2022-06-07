package com.arrchstyle.java.grpc.utils;

public interface Sleeper {
    void sleep(long millis) throws InterruptedException;
}
