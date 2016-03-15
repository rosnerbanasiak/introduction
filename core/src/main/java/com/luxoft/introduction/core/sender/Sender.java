package com.luxoft.introduction.core.sender;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by Prosner on 3/15/2016.
 *
 */
public class Sender {

    private String queueName;
    private Context jndiContext;
    private QueueConnectionFactory queueConnectionFactory;
    private QueueConnection queueConnection;
    private QueueSession queueSession;
    private Queue queue;
    private QueueSender queueSender;
    private TextMessage textMessage;

    public Sender(String queueName) throws JMSException {

        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("http://localhost");

        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue(queueName);

        // Create a MessageProducer from the Session to the Topic or Queue
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // Create a messages
        String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
        TextMessage message = session.createTextMessage(text);

        // Tell the producer to send the message
        System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
        producer.send(message);

        // Clean up
        session.close();
        connection.close();


//        MQConnectionFactory cf = new MQConnectionFactory();
//        cf.setHostName(HOSTNAME);
//        cf.setPort(PORT);
//        cf.setChannel(CHANNEL);
//        cf.setQueueManager(QMNAME);
//        cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);

        /*this.queueName = queueName;
        try {
            jndiContext = new InitialContext();
            queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
            queue = (Queue) jndiContext.lookup(this.queueName);
            queueConnection = queueConnectionFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            queueSender = queueSession.createSender(queue);
            textMessage = queueSession.createTextMessage();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }*/
    }

    public void sendMessage(String message) {
        try {
            textMessage.setText(message);
            System.out.println("Sending msg: " + message);
            queueSender.send(textMessage);
            queueSender.send(queueSession.createMessage());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws JMSException {
        Sender sender = new Sender("myQueue");
        for(int i = 0; i < 5; i++)
            sender.sendMessage("test message #" + i);
    }
}
