package middleware.communication;

import com.sun.messaging.TopicConnectionFactory;
import javax.jms.*;
import middleware.exception.SubscriberException;

/**
 *
 * @author felmas
 */
public class Subscriber {
   
    private TopicConnection            topicConnection;
    private TopicSession               topicSession;
    private Topic                      topic;
    private TopicSubscriber            topicSubscriber;

    public Subscriber(String topicName, MessageListener topicListener) 
            throws SubscriberException {
        
        try {
            topicConnection = new TopicConnectionFactory().createTopicConnection();
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException ee) {}
            }
            throw new SubscriberException("Connection problem: " + e.toString());
        }
        
        try {
            topic = topicSession.createTopic(topicName);
        } catch(JMSException e) {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException ee) {}
            }
            throw new SubscriberException("Topic couldn't be created: " + e.toString());
        }
        
        try {
            topicSubscriber = topicSession.createSubscriber(topic);
        } catch(JMSException e) {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException ee) {}
            }
            throw new SubscriberException("Subscriber couldn't be created: " + e.toString());
        }
        
        try {
            topicSubscriber.setMessageListener(topicListener);
        } catch(JMSException e) {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException ee) {}
            }
            throw new SubscriberException("Message Listener couldn't be set for subscriber: " + e.toString());
        }
    }

    public void start() throws SubscriberException {
        try {
            topicConnection.start();
        } catch (JMSException e) {
            throw new SubscriberException("Exception occurred and subscriber couldn't be started: " + e.toString());
        }
    }
        
    public void close() {
        try {
            topicSubscriber.close();
            topicConnection.close();
        } catch (JMSException e) {
            System.err.println("Exception occurred and subscriber couldn't be closed: " + e.toString());
        }
    }
        
    public void finish() {
        if (topicConnection != null) {
            try {
                topicSession.unsubscribe(null); 
                topicConnection.close();
            } catch (JMSException e) {
                System.err.println("Connection couldn't be closed successfully but it just affects resources on the server");
            }
        }
    }
   
}