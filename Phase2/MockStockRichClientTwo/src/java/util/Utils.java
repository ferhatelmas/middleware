package util;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JOptionPane;
import middleware.communication.Producer;
import middleware.exception.ProducerException;

/**
 *
 * @author felmas
 */
public class Utils {
   
    public enum BEAN {TRADER_FACADE, TRADER_TRANSACTION, ADMIN, ANALYTICS}
    
    private static final String queueName = "msControl";
    private static final Pattern emailPattern = Pattern
            .compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");

    
    public static void setLocation(Frame frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;

        frame.setLocation(x, y);
    }
    
    public static Object getBean(BEAN bean){
        Object traderBean = null;
        try{
            Properties p = new Properties();
            p.setProperty(Context.PROVIDER_URL, "corbaname:iiop:localhost:3700");
            Context context = new InitialContext(p);
            
            switch(bean) {
                case TRADER_FACADE: 
                    traderBean = context.lookup("TraderFacade#mockstock.ejb.front.TraderFacadeRemote");
                    break;
                case TRADER_TRANSACTION:
                    traderBean = context.lookup("TraderTransaction#mockstock.ejb.front.TraderTransactionRemote");
                    break;
                case ADMIN:
                    traderBean = context.lookup("Admin#mockstock.ejb.front.AdminRemote");
                    break;
                case ANALYTICS:
                    traderBean = context.lookup("Analytics#mockstock.ejb.front.AnalyticsBeanRemote");
                    break;
            }
        } catch(Exception e) {
            System.err.println("JNDI lookup for bean is failed!");
            System.exit(0);
        }
        
        return traderBean;
    }
    
    /* Producer communication is done by bean */
    public static Producer getProducer() {
        Producer producer = null;
        try {
            producer = new Producer(queueName);
            producer.start();
        } catch(ProducerException p) {
            JOptionPane.showMessageDialog(null, "Required communication channel couldn't be setup\nMockStock is closing!");
            System.exit(1);
        }
        return producer;
    }
    
    public static boolean isEmail(String email) {
        return emailPattern.matcher(email).matches();
    }
    	
    public static String encryptPassword(String password) {
	
        String hash = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes(), 0, password.length());
            hash = new BigInteger(1, m.digest()).toString(16);
            
            //if the returned md5 value has a 0 in front it will be trunked
            while (hash.length() < 32) {
                hash = "0" + hash;
            }
        } catch (NoSuchAlgorithmException ex) {
            System.err.print("MD5 isn't implemented!");
        }
        return hash;
    }
}
