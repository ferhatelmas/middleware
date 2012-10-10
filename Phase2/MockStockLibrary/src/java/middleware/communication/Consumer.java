package middleware.communication;

import com.sun.messaging.QueueConnectionFactory;
import javax.jms.*;
import middleware.exception.ConsumerException;

/**
 *
 * @author felmas
 */
public class Consumer {

    private Queue                   queue;
    private QueueConnection         queueConnection;
    private QueueSession            queueSession;
    private QueueReceiver           queueReceiver;

    public Consumer(String queueName, MessageListener messageListener) 
            throws ConsumerException {
        
        try {
            queueConnection = new QueueConnectionFactory().createQueueConnection();
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch(JMSException e) {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException ee) {}
            }
            throw new ConsumerException("Connection problem: " + e.toString());
        }
        
        try {
            queue = queueSession.createQueue(queueName);
        } catch(JMSException e) {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException ee) {}
            }
            throw new ConsumerException("Queue couldn't be created: " + e.toString());
        }
        
        try{
            queueReceiver = queueSession.createReceiver(queue);
            queueReceiver.setMessageListener(messageListener);
        } catch(JMSException e) {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException ee) {}
            }
            throw new ConsumerException("QueueReceiver couldn't be created: " + e.toString());
        }
    }
    
    public void start() throws ConsumerException {
        try {
            queueConnection.start();
        } catch(JMSException e) {
            throw new ConsumerException("Exception occurred and consumer couldn't be started: " + e.toString());
        }
    }
    
    public void close() {
        try {
            queueReceiver.close();
        } catch(JMSException e) {
            System.err.println("Exception occurred and consumer couldn't be closed: " + e.toString());
        }   
    }
    
    public void finish() {
        if (queueConnection != null) {
            try {
                queueSession.close();
                queueConnection.close();
            } catch (JMSException e) {
                System.err.println("Connection couldn't be closed successfully but it just affects resources on the server");
            }
        }
    }
    
    
}
