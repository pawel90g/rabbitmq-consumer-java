package eu.garbacik.rabbitmq.subscriber.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.garbacik.rabbitmq.subscriber.EventsBusSubscriber;
import eu.garbacik.rabbitmq.subscriber.RabbitConnection;

@Component
public class EventsBusSubscriberImpl implements EventsBusSubscriber {

    Logger logger = LoggerFactory.getLogger(EventsBusSubscriberImpl.class);

    private RabbitConnection rabbitConnection;

    public EventsBusSubscriberImpl(final RabbitConnection rabbitConnection) {
        this.rabbitConnection = rabbitConnection;
    }

    @Override
    public void subscribeQueue(final String queue, final Consumer<String> messageProcessor) throws IOException, TimeoutException {
        Channel channel = rabbitConnection.getChannel();
        channel.queueDeclare(queue, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
            String message = new String(delivery.getBody(), "UTF-8");
            messageProcessor.accept(message);
            } catch (Exception ex) {
				logger.error("Error occures during event processing", ex.getMessage());
			}
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
    }

    @Override
    public void subscribeExcchange(final String exchange, final String routingKey, final String exchangeType, final Consumer<String> messageProcessor)
            throws IOException, TimeoutException {
                Channel channel = rabbitConnection.getChannel();

                channel.exchangeDeclare(exchange, exchangeType);
                String queueName = channel.queueDeclare().getQueue();
        
                channel.queueBind(queueName, exchange, routingKey);
        
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    try {
                        String message = new String(delivery.getBody(), "UTF-8");
                        messageProcessor.accept(message);
                    } catch (Exception ex) {
                        logger.error("Error occures during event processing", ex.getMessage());
                    }
                };
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    @Override
    public <TEvent> void subscribeExcchange(final String exchange, final String routingKey, final String exchangeType, final Class<TEvent> classOf, final Consumer<TEvent> messageProcessor) throws IOException, TimeoutException {
        Channel channel = rabbitConnection.getChannel();

		channel.exchangeDeclare(exchange, exchangeType);
		String queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, exchange, routingKey);

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			try {
                TEvent event = this.deserializeEvent(delivery.getBody(), classOf);
                messageProcessor.accept(event);
			} catch (Exception ex) {
				logger.error("Error occures during event processing", ex.getMessage());
			}
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    private <TEvent> TEvent deserializeEvent(final byte[] bytes, final Class<TEvent> classOf)
            throws JsonSyntaxException, UnsupportedEncodingException {
        Gson g = new Gson();
        return g.fromJson(new String(bytes, "UTF-8"), classOf);
    }
}