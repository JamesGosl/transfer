package org.james.transfer.core.transfer;

import org.james.transfer.conf.TransferConfiguration;
import org.james.transfer.file.message.FileInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
    protected final Integer MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;


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

    protected void doStream(InputStream inputStream,
                            OutputStream outputStream, FileInformation information) throws IOException {
        int len;
        byte[] buf = new byte[MAX_BUFFER_SIZE];

        try {
            // Files 读取
            while ((len = inputStream.read(buf)) != -1) { //这里会有read 堵塞死 设置超时SocketOptions.SO_TIMEOUT
                // 进度条
                transfer(len, information);

                // socket 读出
                outputStream.write(buf, 0, len);
            }
        } catch (SocketTimeoutException e) {
//            e.printStackTrace();
        }
    }

    protected abstract void doTransferMessage(Socket socket, FileInformation information) throws IOException;
    protected abstract void doTransferStream(Socket socket, FileInformation information) throws IOException;
    protected abstract void transfer(long len, final FileInformation information);
}
