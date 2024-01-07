package mozt.dev.kafkaexampledispatch.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import mozt.dev.kafkaexampledispatch.message.OrderCreatedMessage;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class DispatcherServiceImpl implements DispatcherService {
    @Override
    public void process(OrderCreatedMessage payload) {
    }
}
