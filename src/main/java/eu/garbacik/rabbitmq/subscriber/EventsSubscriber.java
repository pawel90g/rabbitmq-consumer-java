package eu.garbacik.rabbitmq.subscriber;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface EventsSubscriber {
    void subscribe(String queue, String routingKey) throws IOException, TimeoutException;

    <TEvent> void subscribe(final Class<TEvent> classOf, final String exchange, final String routingKey)
            throws IOException, TimeoutException;
}