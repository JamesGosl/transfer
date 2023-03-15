package org.james.transfer.dos.service;

import org.james.transfer.core.service.impl.TransferClientService;
import org.james.transfer.core.transfer.impl.TransferClient;
import org.james.transfer.file.message.FileInformation;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Transfer Dos ClientService
 *
 * @author James Gosl
 * @since 2023/03/15 23:19
 */
public class TransferDosClientService extends TransferClientService implements TransferDosService {

    public TransferDosClientService(TransferClient transfer, InetSocketAddress address) {
        super(transfer, address);
    }

    @Override
    protected void doStart(FileInformation information) throws IOException {
        // 初始化进度条
        initFileProgressBar(information);
    }

    @Override
    protected void doStop() throws IOException {

    }
}
