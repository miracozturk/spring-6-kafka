package mozt.dev.kafkaexampledispatch.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mozt.dev.kafkaexampledispatch.message.OrderCreatedMessage;
import mozt.dev.kafkaexampledispatch.services.DispatcherService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedHandler {
    public static final String TOPIC_ORDER_CREATED = "order.created";
    private final DispatcherService dispatcherService;

    @KafkaListener(
            id = "consumerClient",
            topics= TOPIC_ORDER_CREATED, //. or _ between words is best practise
            groupId = "kafka.dispatch.order.created.consumer"
    )
    public void listen(OrderCreatedMessage orderCreatedMessage) throws Exception{
        log.info("Received Message is: payload: " + orderCreatedMessage.toString());
        dispatcherService.process(orderCreatedMessage);
    }
}
