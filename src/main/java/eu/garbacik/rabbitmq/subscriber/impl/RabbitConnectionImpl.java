package eu.garbacik.rabbitmq.subscriber.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.garbacik.rabbitmq.subscriber.RabbitConnection;

@Component
public class RabbitConnectionImpl implements RabbitConnection {

    @Value("${rabbitmq.hostname}")
	private String hostname;
	
	@Value("${rabbitmq.username}")
	private String username;
	
	@Value("${rabbitmq.password}")
	private String password;
	
	private Connection connection;
    private Channel channel;

    @Override
    public Connection getConnection() throws IOException, TimeoutException {

        if (connection != null)
            return connection;
        connection = getFactory().newConnection();

        return connection;
    }

    @Override
    public Channel getChannel() throws IOException, TimeoutException {
        return this.channel == null 
				? (this.channel = this.getConnection().createChannel()) 
				: this.channel;
    }

    @Override
    public void close() throws IOException {
        if(connection != null) {
            connection.close();
        }
    }
    
    private ConnectionFactory getFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(this.hostname);
		factory.setUsername(this.username);
		factory.setPassword(this.password);
		
		return factory;
	}
}