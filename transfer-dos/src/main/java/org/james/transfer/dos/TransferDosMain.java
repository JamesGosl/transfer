package org.james.transfer.dos;

import org.james.transfer.conf.TransferConfiguration;
import org.james.transfer.core.service.TransferService;
import org.james.transfer.core.transfer.impl.TransferClient;
import org.james.transfer.core.transfer.impl.TransferServer;
import org.james.transfer.dos.service.TransferDosClientService;
import org.james.transfer.dos.service.TransferDosServerService;
import org.james.transfer.dos.transfer.TransferDosClient;
import org.james.transfer.dos.transfer.TransferDosServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * TransferDosMain
 *
 * @author James Gosl
 * @since 2023/03/15 23:18
 */
public class TransferDosMain {
    private static final TransferConfiguration CONFIGURATION = TransferConfiguration.getInstance();

    // 命令行输入
    public static void main(String[] args) throws IOException {
        // ["server", "xxx.xx"]
        // ["client", "127.0.0.1"]

        // server ideaIU-2021.3.3.exe
        // client 127.0.0.1

        if (args.length != 2) {
            throw new RuntimeException("Program arguments is error !!!");
        }

        switch (args[0]) {
            case "server":
                startServer(args[1]);
                break;
            case "client":
                startClient(args[1]);
                break;
            default:
                throw new RuntimeException("Program arguments is error !!!");
        }
    }

    public static void startServer(String source) throws IOException {
        InetSocketAddress address = new InetSocketAddress(CONFIGURATION.getTransferPort());

        TransferServer transferServer = new TransferDosServer();

        TransferService transferServerService =
                new TransferDosServerService(transferServer, source, address);

        transferServerService.start();
    }

    public static void startClient(String remote) throws IOException {
        InetSocketAddress address = new InetSocketAddress(remote, CONFIGURATION.getTransferPort());

        TransferClient transferClient = new TransferDosClient();
        TransferService transferClientService = new TransferDosClientService(transferClient, address);

        transferClientService.start();
    }
}
