package mozt.dev.kafkaexampledispatch.services;

import mozt.dev.kafkaexampledispatch.message.OrderCreatedMessage;

import java.util.concurrent.ExecutionException;

public interface DispatcherService {
    String TOPIC_ORDER_DISPATCHED = "topic.order.dispatched";
    void process(OrderCreatedMessage payload) throws ExecutionException, InterruptedException;
}
