package com.arrchstyle.java.grpc.greeting.server;
import com.proto.greeting.GreetingRequest;
import com.proto.greeting.GreetingResponse;
import com.proto.greeting.GreetingServiceGrpc;
import io.grpc.stub.StreamObserver;
import com.arrchstyle.java.grpc.utils.Sleeper;
public final class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    final Sleeper sleeper;

    GreetingServiceImpl(Sleeper sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public void greet(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        responseObserver.onNext(GreetingResponse.newBuilder().setResult("Hello " + request.getFirstName()).build());
        responseObserver.onCompleted();
    }


}
