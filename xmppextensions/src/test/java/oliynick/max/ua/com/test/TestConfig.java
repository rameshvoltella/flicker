package oliynick.max.ua.com.test;

import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

/**
 * <p>
 * Contains connection configuration for testing purposes
 * </p>
 * @author Max Oliynick
 * */
public final class TestConfig {

    // Default server host
    public static final String defaultHost = "localhost";
    // Default server port
    public static final int defaultPort = 5222;
    // Default server service name
    public static final String defaultServiceName = "maxlaptop";
    // Default packet responce timeout
    public static final int defaultTimeout = 3000;

    /**
     * Builds default connection configuration
     * */
    public static XMPPTCPConnectionConfiguration buildConfig() {

        // Building server connection configuration
        final XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setHost(defaultHost).setServiceName(defaultServiceName)
                .setPort(defaultPort).setCompressionEnabled(false)
                .setSendPresence(true).setConnectTimeout(defaultTimeout)
                .setDebuggerEnabled(true);

        return builder.build();
    }
}
