package org.james.transfer.dos.transfer;

import org.james.transfer.core.transfer.impl.TransferServer;
import org.james.transfer.file.message.FileInformation;

/**
 * Transfer Dos Server
 *
 * @author James Gosl
 * @since 2023/03/15 23:21
 */
public class TransferDosServer extends TransferServer implements TransferDos {

    @Override
    protected void transfer(long len, FileInformation information) {
        doTransfer(len, information);
    }
}
