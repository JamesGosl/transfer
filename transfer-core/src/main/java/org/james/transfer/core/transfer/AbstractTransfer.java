package org.james.transfer.core.transfer;

import org.james.transfer.conf.TransferConfiguration;
import org.james.transfer.file.message.FileInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;

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



    // 传输 文件信息
    public void transferMessage(Socket socket, FileInformation information) throws IOException {
        try {
            doTransferMessage(socket, information);
        } finally {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("message {}", information);
            }
        }
    }

    // 传输 文件数据
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
        byte[] bytes = new byte[MAX_BUFFER_SIZE];

        // 解决 ChannelInputStream 直接内存泄露
        if (inputStream instanceof sun.nio.ch.ChannelInputStream) {
            sun.nio.ch.ChannelInputStream channelInputStream = (sun.nio.ch.ChannelInputStream) inputStream;
            try {
                // 1MB
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024);
                Field ch = sun.nio.ch.ChannelInputStream.class.getDeclaredField("ch");
                ch.setAccessible(true);
                SeekableByteChannel byteChannel =
                        (SeekableByteChannel) ch.get(channelInputStream);

                while((len = byteChannel.read(byteBuffer)) != -1) {
                    byteBuffer.flip();
//                    byte[] array = byteBuffer.array(); //只适用于堆内缓冲区
                    byteBuffer.get(bytes, 0, len); //堆外缓冲区
                    doWrite(len, bytes, outputStream, information);
                }

                // 直接内存泄露
                if(byteBuffer instanceof DirectBuffer) {
                    DirectBuffer directBuffer = (DirectBuffer) byteBuffer;
                    // 清除直接内存
                    directBuffer.cleaner().clean();
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                // Files 读取
                while ((len = inputStream.read(bytes)) != -1) { //这里会有read 堵塞死 设置超时SocketOptions.SO_TIMEOUT
                    doWrite(len, bytes, outputStream, information);
                }
            } catch (SocketTimeoutException e) {
//            e.printStackTrace();
            }
        }
    }

    protected void doWrite(int len, byte[] bytes,
                           OutputStream outputStream, FileInformation information) throws IOException {
        // 进度条
        transfer(len, information);

        // socket 读出
        outputStream.write(bytes, 0, len);
    }

    protected abstract void doTransferMessage(Socket socket, FileInformation information) throws IOException;
    protected abstract void doTransferStream(Socket socket, FileInformation information) throws IOException;
    protected abstract void transfer(long len, final FileInformation information);
}
