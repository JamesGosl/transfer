package org.james.transfer.core.service;

import java.io.IOException;

/**
 * Transfer Service
 *
 * @author James Gosl
 * @since 2023/03/15 13:04
 */
public interface TransferService {

    // 开始
    void start() throws IOException;

    // 结束
    void stop() throws IOException;
}
