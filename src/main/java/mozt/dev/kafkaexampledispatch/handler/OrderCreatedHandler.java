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
    private final DispatcherService dispatcherService;

    @KafkaListener(
            id = "consumerClient",
            topics="order.created", //. or _ between words is best practise
            groupId = "kafka.dispatch.order.created.consumer"
    )
    public void listen(OrderCreatedMessage orderCreatedMessage){
        log.info("Received Message is: payload: " + orderCreatedMessage.toString());
        dispatcherService.process(orderCreatedMessage);
    }
}
