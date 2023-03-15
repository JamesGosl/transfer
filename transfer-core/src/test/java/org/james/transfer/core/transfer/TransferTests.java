package org.james.transfer.core.transfer;

import org.james.transfer.conf.TransferConfiguration;
import org.james.transfer.core.transfer.impl.TransferClient;
import org.james.transfer.core.transfer.impl.TransferServer;
import org.james.transfer.file.message.FileInformation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Transfer Tests
 *
 * @author James Gosl
 * @since 2023/03/15 14:35
 */
public class TransferTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferTests.class);
    TransferConfiguration configuration = TransferConfiguration.getInstance();

    @Test
    public void serverTransferTest() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(configuration.getTransferPort()));
        Socket socket = serverSocket.accept();

        FileInformation fileInformation = new FileInformation();
        fileInformation.setFileName("CLion-2021.3.3.exe");
        fileInformation.setFileLength(612634016L);

        Transfer transfer = new TransferServer() {
            @Override
            protected void transfer(long len, FileInformation information) {
                LOGGER.debug("{}", len);
            }
        };
        transfer.transferMessage(socket, fileInformation);
        transfer.transferStream(socket, fileInformation);
    }

    @Test
    public void clientTransferTest() throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(configuration.getTransferTimeout());
        socket.connect(new InetSocketAddress(
                "127.0.0.1", configuration.getTransferPort()));

        FileInformation fileInformation = new FileInformation();

        Transfer transfer = new TransferClient() {
            @Override
            protected void transfer(long len, FileInformation information) {
                LOGGER.debug("{}", len);
            }
        };
        transfer.transferMessage(socket, fileInformation);
        transfer.transferStream(socket, fileInformation);
    }
}
