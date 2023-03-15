package org.james.transfer.core.service;

import org.james.transfer.conf.TransferConfiguration;
import org.james.transfer.core.service.impl.TransferClientService;
import org.james.transfer.core.service.impl.TransferServerService;
import org.james.transfer.core.transfer.impl.TransferClient;
import org.james.transfer.core.transfer.impl.TransferServer;
import org.james.transfer.file.message.FileInformation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * TransferService Tests
 *
 * @author James Gosl
 * @since 2023/03/15 23:00
 */
public class TransferServiceTests {
    private final TransferConfiguration configuration = TransferConfiguration.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferServiceTests.class);

    @Test
    public void serverTransferServiceTest() throws IOException {
        TransferServer transferServer = new TransferServer() {
            @Override
            protected void transfer(long len, FileInformation information) {
                LOGGER.debug("{}", len);
            }
        };
        InetSocketAddress address = new InetSocketAddress(configuration.getTransferPort());

        TransferService transferService =
                new TransferServerService(transferServer, "ideaIU-2021.3.3.exe", address) {
            @Override
            protected void doStart(FileInformation information) throws IOException {

            }

            @Override
            protected void doStop() throws IOException {

            }
        };

        transferService.start();
    }

    @Test
    public void clientTransferServiceTest() throws IOException {
        TransferClient transferClient = new TransferClient() {
            @Override
            protected void transfer(long len, FileInformation information) {
                LOGGER.debug("{}", len);
            }
        };
        InetSocketAddress address = new InetSocketAddress(configuration.getTransferPort());

        TransferService transferService = new TransferClientService(transferClient, address) {
            @Override
            protected void doStart(FileInformation information) throws IOException {

            }

            @Override
            protected void doStop() throws IOException {

            }
        };

        transferService.start();
    }
}
