package org.james.transfer.core.transfer;

import org.james.transfer.conf.TransferConfiguration;
import org.james.transfer.file.message.FileInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Abstract Transfer
 *
 * @author James Gosl
 * @since 2023/03/15 14:27
 */
public abstract class AbstractTransfer implements Transfer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Transfer.class);
    protected TransferConfiguration configuration = TransferConfiguration.getInstance();

    // TODO 传输 文件信息
    public void transferMessage(Socket socket, FileInformation information) throws IOException {
        try {
            doTransferMessage(socket, information);
        } finally {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("message {}", information);
            }
        }
    }

    // TODO 传输 文件数据
    public void transferStream(Socket socket, FileInformation information) throws IOException {
        try {
            doTransferStream(socket, information);
        } finally {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("stream {}", information);
            }
        }
    }

    protected void validatePath(FileInformation information) throws IOException {
        File file =
                new File(configuration.getTransferHome(), information.getFileName());

        // 如果存在则更换文件 不存在则创建
        if (!file.createNewFile()) {
            file = new File(
                    configuration.getTransferHome(), System.currentTimeMillis() + "-" + information.getFileName());
            information.setFileName(file.getName());
        }
    }

    protected Path toPath(FileInformation information) {
        File file =
                new File(configuration.getTransferHome(), information.getFileName());
        return file.toPath();
    }

    protected abstract void doTransferMessage(Socket socket, FileInformation information) throws IOException;
    protected abstract void doTransferStream(Socket socket, FileInformation information) throws IOException;
}
