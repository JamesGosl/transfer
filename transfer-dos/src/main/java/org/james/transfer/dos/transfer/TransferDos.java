package org.james.transfer.dos.transfer;

import org.james.transfer.dos.utils.FileProgressBar;
import org.james.transfer.file.message.FileInformation;

/**
 * Transfer Dos
 *
 * @author James Gosl
 * @since 2023/03/15 23:52
 */
public interface TransferDos {

    default void doTransfer(long len, FileInformation information) {
        FileProgressBar.getLocal().incr(len);
    }
}
