package org.james.transfer.core.transfer.impl;

import org.james.transfer.core.transfer.AbstractTransfer;
import org.james.transfer.core.transfer.Transfer;
import org.james.transfer.file.message.FileInformation;
import org.james.transfer.file.message.FileToken;

import java.io.*;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Transfer Server
 *
 * @author James Gosl
 * @since 2023/03/15 14:30
 */
public abstract class TransferServer extends AbstractTransfer implements Transfer {

    @Override
    protected void doTransferMessage(Socket socket, FileInformation information) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        // 写数据
        outputStream.writeObject(information);

        // 等待堵塞
        fileToken(inputStream);
    }

    @Override
    protected void doTransferStream(Socket socket, FileInformation information) throws IOException {

        // 获取Path
        Path path = toPath(information);

        // 禁止私自关闭
        OutputStream outputStream = socket.getOutputStream();
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        try (
                SeekableByteChannel sbc = Files.newByteChannel(path);
                InputStream in = Channels.newInputStream(sbc);
        ) {
            // 发送数据
            doStream(in, outputStream, information);

            // 等待回执
            fileToken(inputStream);
        }
    }

    protected void fileToken(ObjectInputStream inputStream) throws IOException {
        while (true) {
            try {
                if (inputStream.readObject().getClass() == FileToken.class) break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
