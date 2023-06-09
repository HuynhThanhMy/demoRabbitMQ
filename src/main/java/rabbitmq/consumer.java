package rabbitmq;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class consumer {

    private final static String QUEUE_NAME = "demo_rabbitmq_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        // factory.setVirtualHost("/");
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        System.out.println("Create a Connection");
        System.out.println("Create a Channel");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        System.out.println("Create a queue " + QUEUE_NAME);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" Waiting for messages!. To exit press CTRL + C");
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {

                String message = new String(body, "UTF-8");
                System.out.println("---Consumed: '" + message + "'");
                System.out.println("consumerTag: " + consumerTag);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
