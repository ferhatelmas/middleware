package  middleware.communication;

import com.sun.messaging.TopicConnectionFactory;
import javax.jms.*;
import middleware.common.StockProduct;
import middleware.exception.PublisherException;

/**
 *
 * @author felmas
 */
public class Publisher {
    
    private TopicConnection            topicConnection;
    private TopicSession               topicSession;
    private Topic                      topic;
    private TopicPublisher             topicPublisher;
    private TextMessage                textMessage;
    private ObjectMessage              objectMessage;
    
    public Publisher(String topicName) 
            throws PublisherException {
        
        try {
            topicConnection = new TopicConnectionFactory().createTopicConnection();
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException ee) {}
            }
            throw new PublisherException("Connection problem: " + e.toString());
        }
        
        try {
            topic = topicSession.createTopic(topicName);
        } catch(JMSException e) {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException ee) {}
            }
            throw new PublisherException("Topic couldn't be created: " + e.toString());
        }
        
        try {
            topicPublisher = topicSession.createPublisher(topic);
        } catch(JMSException e) {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException ee) {}
            }
            throw new PublisherException("Publisher couldn't be created: " + e.toString());
        }
         
    }
    
    public void start() throws PublisherException {
        try {
            topicConnection.start();
        } catch(JMSException e) {
            throw new PublisherException("Connection couldn't be started: " + e.toString());
        }
        
        try {
            textMessage = topicSession.createTextMessage();
            objectMessage = topicSession.createObjectMessage();
        } catch(JMSException e) {
            throw new PublisherException("Messages couldn't be created: " + e.toString());
        }
    }
    
    public void publishTextMessage(String message) throws PublisherException {
        try {
            textMessage.setText(message);
            topicPublisher.publish(textMessage);
        } catch(JMSException e) {
            throw new PublisherException("Text message couldn't be published: " + e.toString());
        }
    }
    
    public void publishStockProduct(StockProduct sp) throws PublisherException{
        try {
            objectMessage.setObject(sp);
            topicPublisher.publish(objectMessage);
        } catch(JMSException e) {
            throw new PublisherException("Object message couldn't be published: " + e.toString());
        }
    }
    
    public void close() {
        try {
            topicPublisher.close();
            topicConnection.close();
        } catch(JMSException e) {
            System.err.println("Publisher couldn't be closed successfully: " + e.toString());
        }
    }
    
}