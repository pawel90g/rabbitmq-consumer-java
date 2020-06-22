package eu.garbacik.rabbitmq.subscriber.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.garbacik.rabbitmq.subscriber.ConsumerConfigProvider;

@Component
public class ConsumerConfigProviderImpl implements ConsumerConfigProvider {

    @Value("${rabbitmq.subscribeExchange}")
    private Boolean subscribeExchange;

    @Value("${rabbitmq.queueName}")
    private String queueName;
    
    @Value("${rabbitmq.excahngeName}")
    private String excahngeName;
    
    @Value("${rabbitmq.routingKey}")
    private String routingKey;
    
    @Value("${rabbitmq.exchangeType}")
    private String exchangeType;

    @Override
    public String getQueueName() {
        return this.queueName;
    }

    @Override
    public String getExchangeName() {
        return this.excahngeName;
    }

    @Override
    public String getRoutingKey() {
        return this.routingKey;
    }

    @Override
    public String getExchangeType() {
        return this.exchangeType;
    }

    @Override
    public Boolean publishToExchange() {
        return this.subscribeExchange;
    }

}