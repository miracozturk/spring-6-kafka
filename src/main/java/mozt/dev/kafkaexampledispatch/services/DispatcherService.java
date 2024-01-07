package mozt.dev.kafkaexampledispatch.services;

import mozt.dev.kafkaexampledispatch.message.OrderCreatedMessage;

public interface DispatcherService {
    void process(OrderCreatedMessage payload);
}
