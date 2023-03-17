package org.james.transfer.dos.utils;

import org.james.transfer.conf.TransferConfiguration;

import java.util.stream.Stream;

/**
 * File ProgressBar
 *
 *  65%[==================================================================>                                 ]xxx
 * 100%[====================================================================================================]xxx
 *
 * 这个类应该出现在DOS 里面 Web PC 用不到
 *
 * @author James Gosl
 * @since 2023/03/14 18:25
 */
public class FileProgressBar implements Runnable {
    private static final ThreadLocal<FileProgressBar> PROGRESS_BAR = new ThreadLocal<>();

    private final TransferConfiguration configuration = TransferConfiguration.getInstance();

    private final Thread thread;

    public static void setLocal(FileProgressBar progressBar) {
        PROGRESS_BAR.set(progressBar);
    }

    public static FileProgressBar getLocal() {
        return PROGRESS_BAR.get();
    }

    /**
     * total 数据量
     * limit 传输量
     * count 进度量
     */
    private final long total;
    private long limit = 0L;
    private long count = 1L;

    private final String fileName;

    public FileProgressBar(long total, String fileName) {
        this.total = total;
        this.fileName = fileName;
        this.thread = new Thread(this);
    }

    public void start() {
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    public void stop() {
        if(!thread.isInterrupted()) {
            thread.interrupt();
        }
    }

    /**
     * 增量
     *
     * @param len 传输量
     */
    public synchronized void incr(long len) {
        if(count + len < total) {
            count += len;
        } else {
            count = total;
        }
    }

    @Override
    public void run() {
        // 线程池调用的
        while (print() && !thread.isInterrupted()) {
            try {
                Thread.sleep(configuration.getTransferProgress());
            } catch (InterruptedException e) {
                // Thread.currentThread().interrupt(); 会打断Thread.sleep(...)
//                e.printStackTrace();

                // 重新打标识
                thread.interrupt();
            }
        }
        System.out.println();
    }

    public synchronized boolean print() {
        // 进度条 100
        // 数据量 5000
        // 传输量 300
        // 进度量 1

        // 进度量 = 传输量/数据量 * 100

        for (int i = (int) (((float)limit / (float)total) * 100),
             l = (int) (((float)count / (float)total) * 100); i < l; i += 1) {

            ProgressBarFormat.SB.replace(i, i + 1, "=");
            if(i + 1 < 100)
                ProgressBarFormat.SB.replace(i + 1, i + 2, ">");
            String format = String.format("\r%3d%%[%s]%s", i + 1, ProgressBarFormat.SB, fileName);
            System.err.print(format);
        }

        limit = count;
        return limit != total;
    }

    static class ProgressBarFormat {
        static final StringBuilder SB = new StringBuilder();

        static {
            Stream.generate(() -> ' ').limit(100).forEach(SB::append);
        }
    }
}
