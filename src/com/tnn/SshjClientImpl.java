package com.tnn;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static net.schmizz.sshj.common.IOUtils.readFully;


public class SshjClientImpl implements ISshClient {
    private static final Logger LOG = LoggerFactory.getLogger(SshjClientImpl.class);
    private static final Console con = System.console();
    private SSHClient client;

    public SshjClientImpl() {
        this.client = new SSHClient();
        LOG.info("A new SshjClient instance created !");
    }

    SshjClientImpl(SSHClient client) {
        this.client = client;
    }

    @Override
    public void connect(String username, char[] password, String host, int port) throws IOException {
        LOG.info("connecting to {}:{} user={}", new Object[]{host, port, username});
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect(host, port);
        try {
            client.authPassword(username, password);
            try (Session session = client.startSession()) {
                LOG.info("is connected: {}", client.isConnected());
                LOG.info("is authenticated: {}", client.isAuthenticated());

                // run a ls command and hang around for a little bit
                if (con != null) {
                    try (Session.Command cmd = session.exec("hostname; id; ls -l /tmp")) {
                        con.writer().print(readFully(cmd.getInputStream()).toString());
                        cmd.join(3, TimeUnit.SECONDS);
                    } finally {
                        try {
                            if (session != null) {
                                session.close();
                            }
                        } catch (IOException ioe) {
                            /* nothing to do */
                        }
                    }
                } else {
                    LOG.info("could not obtain console so not running remote command");
                }
            }
        } catch (UserAuthException authException) {
            LOG.info("Could not authenticate user {}", username);
        } catch (TransportException tpException) {
            LOG.info("Encountered transport exception");
        } finally {
            client.disconnect();
        }
    }

    @Override
    public void connect(String username, char[] password, String host) throws IOException {
        connect(username, password, host, DEFAULT_PORT);
    }

    @Override
    public boolean isConnected() throws IOException {
        return client.isConnected();
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.disconnect();
            client.close();
            client = null;
        }
    }
}
