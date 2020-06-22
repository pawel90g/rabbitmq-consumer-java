package eu.garbacik.rabbitmq.subscriber.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.garbacik.rabbitmq.subscriber.ConsumerConfigProvider;
import eu.garbacik.rabbitmq.subscriber.EventsBusSubscriber;

public class ConsumerWorkerImpl implements Runnable {

    private ConsumerConfigProvider configProvider;
    private EventsBusSubscriber eventsBusSubscriber;

    Logger logger = LoggerFactory.getLogger(ConsumerWorkerImpl.class);

    public ConsumerWorkerImpl(ConsumerConfigProvider configProvider, EventsBusSubscriber eventsBusSubscriber) {
        this.configProvider = configProvider;
        this.eventsBusSubscriber = eventsBusSubscriber;
    }

    @Override
    public void run() {
        if (configProvider.publishToExchange()) {
            System.out.println("Start subscribing exchange: " + configProvider.getExchangeName() + ", routingKey: "
                    + configProvider.getRoutingKey() + ", exchange type: " + configProvider.getExchangeType());

            try {
                eventsBusSubscriber.subscribeExcchange(configProvider.getExchangeName(), configProvider.getRoutingKey(),
                        configProvider.getExchangeType(), (message) -> {
                            System.out.println("[Java Consumer] Message received: " + message);
                        });
            } catch (IOException e) {
                System.out.println("[Error] " + e.getMessage());
                e.printStackTrace();
            } catch (TimeoutException e) {
                System.out.println("[Error] " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Start subscribing queue: " + configProvider.getQueueName());

            try {
                eventsBusSubscriber.subscribeQueue(configProvider.getQueueName(), (message) -> {
                    System.out.println("[Java Consumer] Message received: " + message);
                });
            } catch (IOException e) {
                System.out.println("[Error] " + e.getMessage());
                e.printStackTrace();
            } catch (TimeoutException e) {
                System.out.println("[Error] " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}