package mozt.dev.kafkaexampledispatch.message;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedMessage {
    UUID orderId;
    String payload;
}
