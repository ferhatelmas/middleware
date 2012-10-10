package mockstock.ejb.backend;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import middleware.common.StockProduct;

/**
 *
 * @author felmas
 */

/* Default durable subscriber, it doesn't hurt so I keep as it is */
@MessageDriven(mappedName = "msUpdate", name="UpdateListener",activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "UpdateListenerBean"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "UpdateListenerBean")
})
public class UpdateListenerBean implements MessageListener {
    
    @EJB(beanName="Singleton")
    private SingletonBeanLocal singleton;
    
    public UpdateListenerBean() {
    }
    
    /* Gets price updates from market by jms(topic)
     * Puts new prices into singleton that will be accessed by session beans
     */
    
    @Override
    public void onMessage(Message message) {
        
        System.out.println("Message has arrived!");
        try {
            if(message instanceof ObjectMessage) {
                singleton.updateStock((StockProduct)((ObjectMessage)message).getObject());
            }
        } catch(JMSException j) {
            System.err.print("SEVERE: StockPrice couldn't be updated");
        }
        
    }
}
