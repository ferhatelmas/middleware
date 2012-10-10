package mockstock.bonjour;

import com.apple.dnssd.DNSSD;
import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDRegistration;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.RegisterListener;

/**
 *
 * @author felmas
 */
public class ServiceAnnouncer implements RegisterListener {
    static final String serviceName = "MockStock";
    static final String serviceType = "trading";
    static final String serviceProtocol = "tcp";
    static final String registrationType = "_" + serviceType + "._" + serviceProtocol;
    private DNSSDRegistration serviceRecord;
    private int listeningPort;

    public void registerService(int port) {
        try {
            listeningPort = port;
            serviceRecord = DNSSD.register(serviceName, registrationType, listeningPort, this);
        } catch (DNSSDException e) {
            System.err.println("Unable to register the service: " + e.getMessage());
        }
    }

    public void unregisterService() {
        serviceRecord.stop();
    }


    @Override
    public void serviceRegistered(DNSSDRegistration registration, int flags,
                                  String serviceName, String regType, String domain) {
            System.out.println("-> Service " + serviceName + " registered in domain " + domain);
    }

    @Override
    public void operationFailed(DNSSDService registration, int error) {
        System.err.println("-> Service registration failed");
    }

}