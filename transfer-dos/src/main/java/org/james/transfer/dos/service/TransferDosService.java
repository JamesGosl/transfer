package org.james.transfer.dos.service;

import org.james.transfer.dos.utils.FileProgressBar;
import org.james.transfer.file.TransferFileConfiguration;
import org.james.transfer.file.message.FileInformation;

/**
 * Transfer Dos Service
 *
 * @author James Gosl
 * @since 2023/03/15 23:42
 */
public interface TransferDosService {

    // 初始化进度条
    default void initFileProgressBar(FileInformation information) {
        FileProgressBar fileProgressBar =
                new FileProgressBar(information.getFileLength(), information.getFileName());

        // 启动进度条
        startFileProgressBar(fileProgressBar);

        // 线程本地存储
        FileProgressBar.setLocal(fileProgressBar);
    }

    default void startFileProgressBar(FileProgressBar fileProgressBar) {
        TransferFileConfiguration.SERVICE.execute(fileProgressBar);
    }
}
