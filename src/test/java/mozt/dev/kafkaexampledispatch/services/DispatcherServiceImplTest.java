package mozt.dev.kafkaexampledispatch.services;

import lombok.extern.slf4j.Slf4j;
import mozt.dev.kafkaexampledispatch.message.DispatchPreparingMessage;
import mozt.dev.kafkaexampledispatch.message.OrderCreatedMessage;
import mozt.dev.kafkaexampledispatch.message.OrderDispatchedMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
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
        when(kafkaTemplateMock.send(eq(DispatcherService.TOPIC_ORDER_DISPATCHED), any(OrderDispatchedMessage.class))).thenReturn(mock(CompletableFuture.class));
        when(kafkaTemplateMock.send(eq(DispatcherService.TOPIC_TRACING_STATUS), any(DispatchPreparingMessage.class))).thenReturn(mock(CompletableFuture.class));

        this.dServ.process(orderCreatedMessage);
        verify(kafkaTemplateMock, times(1)).send(eq(DispatcherService.TOPIC_ORDER_DISPATCHED), any(OrderDispatchedMessage.class));
        verify(kafkaTemplateMock, times(1)).send(eq(DispatcherService.TOPIC_TRACING_STATUS), any(DispatchPreparingMessage.class));
    }

    @Test
    void testProcessOrderDispatchedThrowException(){
        OrderCreatedMessage orderCreatedMessage = OrderCreatedMessage.builder()
                .orderId(UUID.randomUUID())
                .payload("some_payload").build();
        doThrow(new RuntimeException("Exception Thrown from " + DispatcherService.TOPIC_ORDER_DISPATCHED)).when(kafkaTemplateMock).send(eq(DispatcherServiceImpl.TOPIC_ORDER_DISPATCHED), any(OrderDispatchedMessage.class));
        Exception exception = assertThrows(RuntimeException.class, () -> this.dServ.process(orderCreatedMessage));
        verify(kafkaTemplateMock, times(1)).send(eq(DispatcherService.TOPIC_ORDER_DISPATCHED), any(OrderDispatchedMessage.class));
        verifyNoMoreInteractions(kafkaTemplateMock);
        assertThat(exception.getMessage(), equalTo("Exception Thrown from " + DispatcherService.TOPIC_ORDER_DISPATCHED));
    }

    @Test
    void testProcessDispatchPreparingThrowException(){
        OrderCreatedMessage orderCreatedMessage = OrderCreatedMessage.builder()
                .orderId(UUID.randomUUID())
                .payload("some_payload").build();
        when(kafkaTemplateMock.send(eq(DispatcherServiceImpl.TOPIC_ORDER_DISPATCHED), any(OrderDispatchedMessage.class))).thenReturn(mock(CompletableFuture.class));
        doThrow(new RuntimeException("Exception Thrown " + DispatcherServiceImpl.TOPIC_TRACING_STATUS)).when(kafkaTemplateMock).send(eq(DispatcherServiceImpl.TOPIC_TRACING_STATUS), any(DispatchPreparingMessage.class));

        Exception exception = assertThrows(RuntimeException.class, () -> this.dServ.process(orderCreatedMessage));
//        exception.printStackTrace();

        verify(kafkaTemplateMock, times(1)).send(eq(DispatcherService.TOPIC_ORDER_DISPATCHED), any(OrderDispatchedMessage.class));
        verify(kafkaTemplateMock, times(1)).send(any(String.class), any(DispatchPreparingMessage.class));
        assertThat(exception.getMessage(), equalTo("Exception Thrown " + DispatcherServiceImpl.TOPIC_TRACING_STATUS));
    }
}
