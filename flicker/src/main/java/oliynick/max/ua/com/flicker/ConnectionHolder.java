package oliynick.max.ua.com.flicker;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by Максим on 31.12.2015.
 */
public class ConnectionHolder {

    private static AbstractXMPPConnection connection = null;

    public static AbstractXMPPConnection getInstance() {

        if(connection == null) {
            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();

            config.setHost("192.168.0.100");
            config.setPort(5222);
            config.setServiceName("maxlaptop");

            connection = new XMPPTCPConnection(config.build());
            try {
                connection.connect();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            }

        }

        return connection;
    }

    private ConnectionHolder() {}
}
