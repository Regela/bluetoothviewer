package net.hitdetecter;

/**
 * Common interface to connect to a device
 */
public interface DeviceConnector {

    void connect();

    void disconnect();

    void sendAsciiMessage(CharSequence chars);
}
