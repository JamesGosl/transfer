package org.james.transfer.core.service.impl;

import org.james.transfer.core.service.AbstractTransferService;
import org.james.transfer.core.service.TransferService;
import org.james.transfer.core.transfer.impl.TransferServer;
import org.james.transfer.file.TransferFileConfiguration;
import org.james.transfer.file.message.FileInformation;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Transfer ServerService
 *
 * @author James Gosl
 * @since 2023/03/15 22:28
 */
public abstract class TransferServerService extends AbstractTransferService<TransferServer> implements TransferService {
    private final File source;
    private ServerSocket serverSocket;

    public TransferServerService(TransferServer transfer, String source, InetSocketAddress address) {
        super(transfer, address);

        File file = new File(configuration.getTransferHome(), source);
        if (file.exists()) {
            this.source = file;
        } else {
            throw new RuntimeException(source + " is no exists !!!");
        }
    }

    @Override
    public void start() throws IOException {
        this.serverSocket = new ServerSocket();
        serverSocket.bind(address);

        Socket socket;
        while ((socket = serverSocket.accept()) != null) {
            TransferFileConfiguration.SERVICE.execute(new ServerTask(source, socket, transfer, this));
        }
    }


    @Override
    public void stop() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }

        TransferFileConfiguration.SERVICE.shutdown();
        doStop();
    }


    static class ServerTask implements Runnable {
        final File file;
        final Socket socket;
        final TransferServer transfer;
        final AbstractTransferService<TransferServer> transferService;

        public ServerTask(File file, Socket socket,
                          TransferServer transfer, AbstractTransferService<TransferServer> transferService) {
            this.file = file;
            this.socket = socket;
            this.transfer = transfer;
            this.transferService = transferService;
        }

        @Override
        public void run() {
            try {
                // 封装数据
                FileInformation information = new FileInformation();
                information.setFileLength(file.length());
                information.setFileName(file.getName());

                // 传输数据
                transferService.transfer(socket, information);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
