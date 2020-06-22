package eu.garbacik.rabbitmq.subscriber;

public interface ConsumerConfigProvider {

    Boolean publishToExchange();

    String getQueueName();

    String getExchangeName();

    String getRoutingKey();

    String getExchangeType();
}