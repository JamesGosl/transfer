package org.james.transfer.dos.service;

import org.james.transfer.core.service.impl.TransferServerService;
import org.james.transfer.core.transfer.impl.TransferServer;
import org.james.transfer.file.message.FileInformation;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Transfer Dos ServerService
 *
 * @author James Gosl
 * @since 2023/03/15 23:20
 */
public class TransferDosServerService extends TransferServerService implements TransferDosService {

    public TransferDosServerService(TransferServer transfer, String source, InetSocketAddress address) {
        super(transfer, source, address);
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
