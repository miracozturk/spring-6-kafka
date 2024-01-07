package mozt.dev.kafkaexampledispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "mozt.dev.kafkaexampledispatch.configuration")
@SpringBootApplication
public class KafkaExampleDispatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaExampleDispatchApplication.class, args);
    }

}
