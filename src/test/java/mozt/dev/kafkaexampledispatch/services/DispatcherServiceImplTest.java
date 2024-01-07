package mozt.dev.kafkaexampledispatch.services;

import mozt.dev.kafkaexampledispatch.message.OrderCreatedMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DispatcherServiceImplTest {

    private  DispatcherService dServ;

    @BeforeEach
    void setUp() {
        this.dServ = new DispatcherServiceImpl();
    }

    @Test
    void process() {
        OrderCreatedMessage orderCreatedMessage = OrderCreatedMessage.builder()
                .orderId(UUID.randomUUID())
                .payload("some_payload").build();

        this.dServ.process(orderCreatedMessage);
    }
}
