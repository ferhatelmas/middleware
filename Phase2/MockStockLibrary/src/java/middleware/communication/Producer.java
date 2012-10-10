package middleware.communication;


import com.sun.messaging.QueueConnectionFactory;
import javax.jms.*;
import middleware.exception.ProducerException;

/**
 *
 * @author felmas
 */
public class Producer {
    
    private QueueConnection            queueConnection;
    private QueueSession               queueSession;
    private Queue                      queue;
    private QueueSender                queueSender;
    private TextMessage                textMessage;
    private ObjectMessage              objectMessage;
    
    public Producer(String queueName) throws ProducerException {

        try {
            queueConnection = new QueueConnectionFactory().createQueueConnection();
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException ee) {}
            }
            throw new ProducerException("Connection problem: " + e.toString());
        } 
        
        try {
            queue = queueSession.createQueue(queueName);
            queueSender = queueSession.createSender(queue);
        } catch(JMSException e) {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException ee) {}
            }
            throw new ProducerException("Sender couldn't be created: " + e.toString());
        }
    }
    
    public void start() throws ProducerException {
        try {
            queueConnection.start();
        } catch(JMSException e) {
            throw new ProducerException("Connection couldn't be started: " + e.toString());
        }
        
        try {
            objectMessage = queueSession.createObjectMessage();
            textMessage   = queueSession.createTextMessage();
        } catch(JMSException e) {
            throw new ProducerException("Messages couldn't be created: " + e.toString());
        }
    }
    
    public void sendObjectMessage(Transaction t) throws ProducerException {
        try {
            objectMessage.setObject(t);
            queueSender.send(objectMessage);
        } catch(JMSException e) {
            throw new ProducerException("Message couldn't be sent: " + e.toString());
        }
    }
    
    public void sendTextMessage(String msg) throws ProducerException {
        try {
            textMessage.setText(msg);
            queueSender.send(textMessage);
        } catch(JMSException e) {
            throw new ProducerException("Message couldn't be sent: " + e.toString());
        }
    }
    
    public void close() {
        try {
            queueSender.close();
            queueConnection.close();
        } catch(JMSException e) {
            System.err.println("Queue couldn't be closed successfully but it only affects resources in the server.");
        }
    
    }
    
    
    
    
}
