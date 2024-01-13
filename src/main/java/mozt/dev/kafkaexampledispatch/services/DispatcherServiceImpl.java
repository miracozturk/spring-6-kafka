package mozt.dev.kafkaexampledispatch.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mozt.dev.kafkaexampledispatch.message.DispatchPreparingMessage;
import mozt.dev.kafkaexampledispatch.message.OrderCreatedMessage;
import mozt.dev.kafkaexampledispatch.message.OrderDispatchedMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatcherServiceImpl implements DispatcherService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Override
    public void process(OrderCreatedMessage payload) throws ExecutionException, InterruptedException {
        //create a OrderDispatchedMessage and sent it with syncronous.
        OrderDispatchedMessage orderDispatchedMessage = OrderDispatchedMessage.builder()
                .orderId(payload.getOrderId())
                .build();
        kafkaTemplate.send(TOPIC_ORDER_DISPATCHED, orderDispatchedMessage).get();

        DispatchPreparingMessage dpm = DispatchPreparingMessage.builder()
                .orderId(UUID.randomUUID())
                .build();
        kafkaTemplate.send(TOPIC_TRACING_STATUS, dpm).get();
    }
}
