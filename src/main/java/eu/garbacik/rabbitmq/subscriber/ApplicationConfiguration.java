package eu.garbacik.rabbitmq.subscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.garbacik.rabbitmq.subscriber.impl.ConsumerWorkerImpl;

@Configuration
public class ApplicationConfiguration {

    @Autowired
    private EventsBusSubscriber defaultEventsBusSubscriber;

    @Autowired
    private ConsumerConfigProvider defaultConsumerConfigProvider;

    @Bean
    public ConsumerWorkerImpl getConsumerWorker() {
        ConsumerWorkerImpl worker = new ConsumerWorkerImpl(this.defaultConsumerConfigProvider, this.defaultEventsBusSubscriber);
        worker.run();

        return worker;
    }
}