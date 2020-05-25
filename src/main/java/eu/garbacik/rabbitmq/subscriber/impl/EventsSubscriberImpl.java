package eu.garbacik.rabbitmq.subscriber.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.garbacik.rabbitmq.subscriber.EventsSubscriber;
import eu.garbacik.rabbitmq.subscriber.RabbitConnection;

public class EventsSubscriberImpl implements EventsSubscriber {

    Logger logger = LoggerFactory.getLogger(EventsSubscriberImpl.class);
    
    private final String exchangeType = "direct";

    private RabbitConnection rabbitConnection;

    public EventsSubscriberImpl(final RabbitConnection rabbitConnection) {
        this.rabbitConnection = rabbitConnection;
    }

    @Override
    public void subscribe(String queue, String routingKey) throws IOException, TimeoutException {
        Channel channel = rabbitConnection.getChannel();
        channel.queueDeclare(queue, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
            String message = new String(delivery.getBody(), "UTF-8");
            } catch (Exception ex) {
				logger.error("Error occures during event processing", ex.getMessage());
			}
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
    }

    @Override
    public <TEvent> void subscribe(final Class<TEvent> classOf,final String exchange, final String routingKey) throws IOException, TimeoutException {
        Channel channel = rabbitConnection.getChannel();

		channel.exchangeDeclare(exchange, exchangeType);
		String queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, exchange, routingKey);

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			try {
                TEvent event = this.deserializeEvent(delivery.getBody(), classOf);
			} catch (Exception ex) {
				logger.error("Error occures during event processing", ex.getMessage());
			}
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    private <TEvent> TEvent deserializeEvent(byte[] bytes, Class<TEvent> classOf)
            throws JsonSyntaxException, UnsupportedEncodingException {
        Gson g = new Gson();
        return g.fromJson(new String(bytes, "UTF-8"), classOf);
    }
}