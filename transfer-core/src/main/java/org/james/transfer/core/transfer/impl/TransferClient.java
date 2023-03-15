package org.james.transfer.core.transfer.impl;

import org.james.transfer.core.transfer.AbstractTransfer;
import org.james.transfer.core.transfer.Transfer;
import org.james.transfer.file.TransferFileConfiguration;
import org.james.transfer.file.message.FileInformation;
import org.james.transfer.file.message.FileToken;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Transfer Client
 *
 * @author James Gosl
 * @since 2023/03/15 14:30
 */
public abstract class TransferClient extends AbstractTransfer implements Transfer {

    @Override
    protected void doTransferMessage(Socket socket, FileInformation information) throws IOException {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            FileInformation fileInformation = (FileInformation) inputStream.readObject();

            // 效验文件是否存在
            validatePath(fileInformation);

            // 包装
            information.warp(fileInformation);

            // 发送令牌
            fileToken(outputStream);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doTransferStream(Socket socket, FileInformation information) throws IOException {
        Path path = toPath(information);

        // 禁止私自关闭
        InputStream inputStream = socket.getInputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

        try (
                OutputStream out = Files.newOutputStream(path);
        ) {
            // 接受数据
            doStream(inputStream, out, information);

            // 回执令牌
            fileToken(outputStream);
        }
    }

    protected void fileToken(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeObject(TransferFileConfiguration.TOKEN);
    }
}
