package eu.garbacik.rabbitmq.subscriber;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public interface EventsBusSubscriber {
    void subscribeQueue(final String queue, final Consumer<String> messageProcessor) throws IOException, TimeoutException;

    void subscribeExcchange(final String exchange, final String routingKey, final String exchangeType,
            final Consumer<String> messageProcessor) throws IOException, TimeoutException;

    <TEvent> void subscribeExcchange(final String exchange, final String routingKey, final String exchangeType,
            final Class<TEvent> classOf, final Consumer<TEvent> messageProcessor) throws IOException, TimeoutException;
}