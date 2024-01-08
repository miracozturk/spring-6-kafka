package mozt.dev.kafkaexampledispatch.services;

import mozt.dev.kafkaexampledispatch.message.OrderCreatedMessage;
import mozt.dev.kafkaexampledispatch.message.OrderDispatchedMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DispatcherServiceImplTest {

    private  DispatcherService dServ;
    private KafkaTemplate<String,Object> kafkaTemplateMock;


    @BeforeEach
    void setUp() {
        kafkaTemplateMock = mock(KafkaTemplate.class);
        this.dServ = new DispatcherServiceImpl(kafkaTemplateMock);
    }

    @Test
    void testProcessSuccess() throws Exception {
        OrderCreatedMessage orderCreatedMessage = OrderCreatedMessage.builder()
                .orderId(UUID.randomUUID())
                .payload("some_payload").build();
        when(kafkaTemplateMock.send(any(String.class), any(OrderDispatchedMessage.class))).thenReturn(mock(CompletableFuture.class));
        this.dServ.process(orderCreatedMessage);
        verify(kafkaTemplateMock, times(1)).send(eq(DispatcherService.TOPIC_ORDER_DISPATCHED), any(OrderDispatchedMessage.class));
    }

    @Test
    void testProcessThrowException(){
        OrderCreatedMessage orderCreatedMessage = OrderCreatedMessage.builder()
                .orderId(UUID.randomUUID())
                .payload("some_payload").build();
        doThrow(new RuntimeException("Exception Thrown.")).when(kafkaTemplateMock).send(anyString(), any(OrderDispatchedMessage.class));
        Exception exception = assertThrows(RuntimeException.class, () -> this.dServ.process(orderCreatedMessage));
        verify(kafkaTemplateMock, times(1)).send(eq(DispatcherService.TOPIC_ORDER_DISPATCHED), any(OrderDispatchedMessage.class));
    }
}
