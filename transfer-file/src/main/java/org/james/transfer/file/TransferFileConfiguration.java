package org.james.transfer.file;

import org.james.transfer.file.message.FileToken;
import org.james.transfer.file.progress.FileProgressBar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TransferFile Configuration
 *
 * @author James Gosl
 * @since 2023/03/15 12:46
 */
public interface TransferFileConfiguration {

    ThreadLocal<FileProgressBar> PROGRESS_BARS = new ThreadLocal<>();

    FileToken TOKEN = new FileToken();

    ExecutorService SERVICE =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

}
