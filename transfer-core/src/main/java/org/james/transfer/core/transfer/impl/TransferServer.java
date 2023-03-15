package org.james.transfer.core.transfer.impl;

import org.james.transfer.core.transfer.AbstractTransfer;
import org.james.transfer.core.transfer.Transfer;
import org.james.transfer.file.message.FileInformation;
import org.james.transfer.file.message.FileToken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Transfer Server
 *
 * @author James Gosl
 * @since 2023/03/15 14:30
 */
public class TransferServer extends AbstractTransfer implements Transfer {

    @Override
    protected void doTransferMessage(Socket socket, FileInformation information) throws IOException {
        try (
                ObjectOutputStream outputStream =
                        new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream =
                        new ObjectInputStream(socket.getInputStream());
        ) {
            outputStream.writeObject(information);

            // 等待堵塞
            while (inputStream.readObject().getClass() != FileToken.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void doTransferStream(Socket socket, FileInformation information) throws IOException {
        // 获取Path

        // Files 读取

        // socket 读出

        // 进度条
    }
}
