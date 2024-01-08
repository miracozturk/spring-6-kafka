package mozt.dev.kafkaexampledispatch.handler;

import mozt.dev.kafkaexampledispatch.message.OrderCreatedMessage;
import mozt.dev.kafkaexampledispatch.services.DispatcherService;
import mozt.dev.kafkaexampledispatch.services.DispatcherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderCreatedHandlerTest {

    private OrderCreatedHandler orderCreatedHandler;
    private DispatcherService dispatcherService;

    @BeforeEach
    void setUp() {
        this.dispatcherService = mock(DispatcherServiceImpl.class);
        this.orderCreatedHandler = new OrderCreatedHandler(this.dispatcherService);
    }

    @Test
    void testListenSuccess() throws Exception {
        OrderCreatedMessage orderCreatedMessage = OrderCreatedMessage.builder()
                .orderId(UUID.randomUUID())
                .payload("some_payload").build();
        orderCreatedHandler.listen(orderCreatedMessage);
        verify(dispatcherService, times(1)).process(orderCreatedMessage);
    }
    @Test
    void testListenThrowsException() throws Exception{
        OrderCreatedMessage orderCreatedMessage = OrderCreatedMessage.builder()
                .orderId(UUID.randomUUID())
                .payload("some_payload").build();
        doThrow(new RuntimeException()).when(this.dispatcherService).process(any(OrderCreatedMessage.class));
        assertThrows(RuntimeException.class, ()-> orderCreatedHandler.listen(orderCreatedMessage));
        verify(dispatcherService, times(1)).process(orderCreatedMessage);
    }
}
