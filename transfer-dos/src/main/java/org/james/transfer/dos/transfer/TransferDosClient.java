package org.james.transfer.dos.transfer;

import org.james.transfer.core.transfer.impl.TransferClient;
import org.james.transfer.dos.utils.FileProgressBar;
import org.james.transfer.file.message.FileInformation;

/**
 * Transfer Dos Client
 *
 * @author James Gosl
 * @since 2023/03/15 23:20
 */
public class TransferDosClient extends TransferClient implements TransferDos {

    @Override
    protected void transfer(long len, FileInformation information) {
        doTransfer(len, information);
    }
}
