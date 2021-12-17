package com.tnn;

import java.io.IOException;

public interface ISshClient {
    public final int DEFAULT_PORT = 22;

    /**
     * Ssh login (connect) to a user account on a remote host
     * @param host      host to connect to
     * @param port      port on host to connect to (defaults 22)
     * @param username  account on host to connect to
     * @param password  password for account to connect to
     */
    void connect(String username, char[] password, String host, int port) throws IOException;

    /**
     * Ssh login (connect) to a user account on a remote host
     * @param host      host to connect to
     * @param username  account on host to connect to
     * @param password  password for account to connect to
     */
    void connect(String username, char[] password, String host) throws IOException;

    /**
     * Checks that the ssh connection exist
     * @return true if ssh connection exists, false otherwise
     */
    boolean isConnected() throws IOException;

    void close() throws IOException;
}
