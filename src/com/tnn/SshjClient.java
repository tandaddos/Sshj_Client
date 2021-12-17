package com.tnn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "SshjClient", version = "1.0", mixinStandardHelpOptions = true)
public class SshjClient implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(SshjClientImpl.class);
    @CommandLine.Option(names = {"-n", "--name"}, description = "hostname or ip")
    String host = "localhost";

    @CommandLine.Option(names = {"-u", "--user"}, description = "User account")
    String user = "ifetest";

    @CommandLine.Option(names = {"-p", "--password"}, description = "Password", interactive = true)
    String password = "ifetest";

    @Override
    public void run() {
        LOG.info("Creating sshj client instance ...");
        SshjClientImpl client = new SshjClientImpl();
        try {
            client.connect(user, password.toCharArray(), host);
        } catch (IOException ioe) {
            LOG.info ("caught exception");
        }
    }

    public static void main(String[] args) {
        System.out.println("Simple SSHJ client");
        CommandLine.run(new SshjClient(), args);
    }
}
