package org.james.transfer.core.transfer;

import org.james.transfer.conf.TransferConfiguration;
import org.james.transfer.core.transfer.impl.TransferClient;
import org.james.transfer.core.transfer.impl.TransferServer;
import org.james.transfer.file.message.FileInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    TransferConfiguration configuration = TransferConfiguration.getInstance();

    @Test
    public void serverTransferMessageTest() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(configuration.getTransferPort()));
        Socket socket = serverSocket.accept();

        FileInformation fileInformation = new FileInformation();
        fileInformation.setFileLength(26262L);
        fileInformation.setFileName("combat.x");

        Transfer transfer = new TransferServer();
        transfer.transferMessage(socket, fileInformation);
    }

    @Test
    public void clientTransferMessageTest() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(
                "127.0.0.1", configuration.getTransferPort()));

        FileInformation fileInformation = new FileInformation();

        Transfer transfer = new TransferClient();
        transfer.transferMessage(socket, fileInformation);
    }
}
