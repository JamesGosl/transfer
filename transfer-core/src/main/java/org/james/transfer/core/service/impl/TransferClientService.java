package org.james.transfer.core.service.impl;

import org.james.transfer.core.service.AbstractTransferService;
import org.james.transfer.core.service.TransferService;
import org.james.transfer.core.transfer.impl.TransferClient;
import org.james.transfer.file.message.FileInformation;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Transfer ClientService
 *
 * @author James Gosl
 * @since 2023/03/15 22:28
 */
public abstract class TransferClientService extends AbstractTransferService<TransferClient> implements TransferService {
    private Socket socket;

    public TransferClientService(TransferClient transfer, InetSocketAddress address) {
        super(transfer, address);
    }

    @Override
    public void start() throws IOException {
        socket = new Socket();
        socket.connect(address);
        // 应不应该加超时？？？
        // 1. 大数据过不来 就死了
        // 2. 小数据完事了 堵塞死
        socket.setSoTimeout(configuration.getTransferTimeout());


        // 封装数据
        FileInformation information = new FileInformation();

        // 传输数据
        transfer(socket, information);
    }

    @Override
    public void stop() throws IOException {
        if (socket.isClosed()) {
            socket.close();
        }
        doStop();
    }
}
