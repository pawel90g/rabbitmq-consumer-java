package eu.garbacik.rabbitmq.subscriber;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public interface RabbitConnection {
	Connection getConnection() throws IOException, TimeoutException;
    Channel getChannel() throws IOException, TimeoutException;
    void close() throws IOException;
}