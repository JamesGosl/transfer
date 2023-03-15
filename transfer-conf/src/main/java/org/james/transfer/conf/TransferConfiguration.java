package org.james.transfer.conf;

import org.james.transfer.utils.ConvertUtils;
import org.james.transfer.utils.ResourcesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Properties;
import static org.james.transfer.conf.TransferConfigurationConst.*;

/**
 * Transfer Configuration (settings.properties)
 *
 * 采用单例模式
 *
 * @author James Gosl
 * @since 2023/03/12 18:39
 */
public class TransferConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(TransferConfiguration.class);

    private static final String SETTING_PROPERTIES = "settings.properties";

    private final File transferHome;
    private final int transferThread;
    private final int transferPort;
    private final long transferProgress;
    private final int transferTimeout;

    private TransferConfiguration() {
        Properties properties = ResourcesUtils.getProperties(SETTING_PROPERTIES);
        // 工作目录
        this.transferHome =
                ConvertUtils.convertFile(properties.getProperty(TRANSFER_HOME),
                        () -> FileSystemView.getFileSystemView().getHomeDirectory().getPath());

        // 核心线程数
        this.transferThread =
                ConvertUtils.convertInteger(properties.getProperty(TRANSFER_THREAD),
                        () -> Runtime.getRuntime().availableProcessors() + 1);

        // 连接端口
        this.transferPort =
                ConvertUtils.convertInteger(properties.getProperty(TRANSFER_PORT),
                        () -> 1024);

        // 进度条延迟
        this.transferProgress =
                ConvertUtils.convertLong(properties.getProperty(TRANSFER_PROGRESS),
                        () -> 300L);

        // 读写超时
        this.transferTimeout =
                ConvertUtils.convertInteger(properties.getProperty(TRANSFER_TIMEOUT),
                        () -> 1000);

        if (logger.isInfoEnabled()) {
            logger.info(toString());
        }
    }

    // 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
    private static class SingletonHolder {
        // 静态初始化器，由JVM来保证线程安全
        private static final TransferConfiguration instance = new TransferConfiguration();
    }

    public static TransferConfiguration getInstance(){
        return SingletonHolder.instance;
    }

    public File getTransferHome() {
        return transferHome;
    }

    public int getTransferThread() {
        return transferThread;
    }

    public int getTransferPort() {
        return transferPort;
    }

    public long getTransferProgress() {
        return transferProgress;
    }

    public int getTransferTimeout() {
        return transferTimeout;
    }

    @Override
    public String toString() {
        return "{" +
                "transferHome=" + transferHome +
                ", transferThread=" + transferThread +
                ", transferPort=" + transferPort +
                ", transferProgress=" + transferProgress +
                ", transferTimeout=" + transferTimeout +
                '}';
    }
}
