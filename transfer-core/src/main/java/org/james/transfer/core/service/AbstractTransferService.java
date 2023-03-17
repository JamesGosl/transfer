package org.james.transfer.core.service;

import org.james.transfer.conf.TransferConfiguration;
import org.james.transfer.core.transfer.Transfer;
import org.james.transfer.file.message.FileInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Abstract TransferService
 *
 * @author James Gosl
 * @since 2023/03/15 22:28
 */
public abstract class AbstractTransferService<T extends Transfer> implements TransferService {
    protected final TransferConfiguration configuration = TransferConfiguration.getInstance();
    protected final T transfer;
    protected final InetSocketAddress address;

    public AbstractTransferService(T transfer, InetSocketAddress address) {
        this.transfer = transfer;
        this.address = address;

        // 钩子方法
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    protected abstract void doStart(FileInformation information) throws IOException;
    protected abstract void doStop(FileInformation information) throws IOException;

    public void transfer(Socket socket, FileInformation information) throws IOException {
        // 发送详情
        transfer.transferMessage(socket, information);

        // 钩子方法
        // 1. doStart/doStop 应该每次transfer 都进行操作
        // 2. 能够让扩展模块在文件传输前后 都有操作 (例如 Dos 模块中的进度条)
        doStart(information);

        // 发送数据
        try {
            transfer.transferStream(socket, information);
        } finally {
            doStop(information);
        }
    }
}
