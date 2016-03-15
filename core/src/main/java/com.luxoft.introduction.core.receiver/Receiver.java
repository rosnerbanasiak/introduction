package com.luxoft.introduction.core.receiver;


import org.apache.activemq.ActiveMQConnectionFactory;


import javax.jms.*;
import javax.naming.*;
import javax.naming.Context;

/**
 * Created by PBanasiak on 3/15/2016.
 */
public class Receiver implements ExceptionListener {

    private static TextMessage messageFromJMS = null;
    private static QueueReceiver queueReceiver = null;
    private String quoueName;
    private static Context context = null;
    private static QueueConnectionFactory queueConnectionFactory = null;
    private static QueueSession queueSession = null;
    private static QueueConnection queueConnection = null;
    private static Queue queue = null;

    public Receiver(String quoueName) throws JMSException {
        this.quoueName = quoueName;


        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.31.218.30:61616");

        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();

        connection.setExceptionListener(this);

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue(quoueName);

        // Create a MessageConsumer from the Session to the Topic or Queue
        MessageConsumer consumer = session.createConsumer(destination);

        // Wait for a message
        Message message = consumer.receive(1000);

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println("Received: " + text);
        } else {
            System.out.println("Received: " + message);
        }

        consumer.close();
        session.close();
        connection.close();

    }








    public static void main(String []args) throws JMSException {

        Receiver firstReceiver = new Receiver("myQueue");



    }

    public void onException(JMSException e) {
        e.printStackTrace();

    }

}
