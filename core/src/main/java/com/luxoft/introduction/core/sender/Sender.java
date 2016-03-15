package com.luxoft.introduction.core.sender;

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

    public Sender(String queueName) {
        this.queueName = queueName;
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
        }
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



    public static void main(String[] args) {
        Sender sender = new Sender("myQueue");
        for(int i = 0; i < 5; i++)
            sender.sendMessage("test message #" + i);
    }
}
