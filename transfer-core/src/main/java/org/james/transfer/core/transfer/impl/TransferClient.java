package org.james.transfer.core.transfer.impl;

import org.james.transfer.core.transfer.AbstractTransfer;
import org.james.transfer.core.transfer.Transfer;
import org.james.transfer.file.TransferFileConfiguration;
import org.james.transfer.file.message.FileInformation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Path;

/**
 * Transfer Client
 *
 * @author James Gosl
 * @since 2023/03/15 14:30
 */
public class TransferClient extends AbstractTransfer implements Transfer {

    @Override
    protected void doTransferMessage(Socket socket, FileInformation information) throws IOException {
        try (
                ObjectInputStream inputStream =
                        new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream =
                        new ObjectOutputStream(socket.getOutputStream());
        ) {
            information.warp((FileInformation) inputStream.readObject());

            // 发送令牌
            outputStream.writeObject(TransferFileConfiguration.TOKEN);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void doTransferStream(Socket socket, FileInformation information) throws IOException {
        // 效验文件是否存在
        validatePath(information);

        Path path = toPath(information);
    }
}
