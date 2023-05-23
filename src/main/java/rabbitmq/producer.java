package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class producer {

    private final static String QUEUE_NAME = "demo_rabbitmq_queue";

    public static void main(String[] args) throws IOException, TimeoutException, Exception {
        System.out.println("Create a ConnectionFactory");
        ConnectionFactory factory = new ConnectionFactory();

        System.out.println("Create a Connection");
        System.out.println("Create a Channel");

        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {

            System.out.println("Create a queue " + QUEUE_NAME);
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("Start sending messages ... ");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));) {
                String message;
                do {
                    System.out.print("Enter message: ");
                    message = br.readLine().trim();
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.println("Sent: '" + message + "'");
                } while (!message.equalsIgnoreCase("close"));
            }
            channel.close();
            connection.close();
        } finally {
            System.out.println("Close connection and free resources");
        }
    }
}
