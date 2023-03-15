package org.james.transfer.core.transfer;

import org.james.transfer.file.message.FileInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * Transfer
 *
 * @author James Gosl
 * @since 2023/03/15 12:51
 */
public interface Transfer {


    // TODO 传输 文件信息
    void transferMessage(Socket socket, FileInformation information) throws IOException;

    // TODO 传输 文件数据
    void transferStream(Socket socket, FileInformation information) throws IOException;
}
