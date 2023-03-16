//package org.james.transfer.test.zero;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import sun.nio.ch.ChannelInputStream;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.nio.MappedByteBuffer;
//import java.nio.channels.Channels;
//import java.nio.channels.FileChannel;
//import java.nio.channels.SeekableByteChannel;
//import java.nio.channels.SocketChannel;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
///**
// * TODO Zero Copy
// * 1. 零拷贝技术是指计算机执行操作时，CPU 不需要先将数据从某处内存复制到另一个特定区域
// * 2. 这种技术通常用于通过网络传输文件时节省CPU 周期和内存宽带 (本地磁盘I/O 使用直接I/O)
// * 3. 零拷贝技术可以减少数据拷贝和共享总线操作的次数，消除传输数据在存储器之间不必要的中间拷贝次数，从而有效的提高数据传输效率
// * 4. 零拷贝技术减少了用户进程地址空间和内存地址空间之间因为上下文切换而带来的开销
// *
// * https://blog.csdn.net/lonelymanontheway/article/details/105888792
// *
// * @author James Gosl
// * @since 2023/03/07 23:25
// */
//public class ZeroCopyClients {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ZeroCopyClients.class);
//
//    // 3.36 GB (3,612,586,084 字节)
//    private final static File SOURCE_FILE = new File("C:\\Users\\Combat\\Desktop\\尚硅谷宋红康Java核心基础_好评如潮（30天入门）.zip");
//
//    private static final String HOST = "172.20.10.2";
//    private static final Integer PORT = 1024;
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        long start = System.currentTimeMillis();
//
//        // 1849/1823/1960 612634016
//        // 7493/8252/7379 3612586083
//        // 76376          31121121147
////        common();
//
//        // 1442/1750/1820 612634016
//        // 6157/6187/5304 3612586083
////        mmap();
//
//        // 1169/1143/1436     612634016
//        // 3661/3625/3609     3612586083
//        // 63561/62156/62851  31121121147
////        sendFile();
//
//        // 60520/72654        31121121147
//        files();
//
//        println(System.currentTimeMillis() - start);
//    }
//
//    private static void common() throws IOException, InterruptedException {
//        // TODO 传统数据发送
//        // 1. 这里发生了四次copy 的过程
//        //      1. DMA 拷贝 文件读取缓冲区
//        //      2. CPU 拷贝 应用进程缓冲区
//        //      3. CPU 拷贝 套接字发缓冲区
//        //      4. DMA 拷贝 网络设备缓冲区
//        // 2. 从宏观上来看就只有两次copy 过程
//        //      1. 文件读取copy
//        //      2. 文件发送copy
//        // 3. 从微观实现上来说就是四次copy 过程 同时也发生了四次上下文切换
//        //      1. 用户态 read 内核态
//        //      2. 内核态 read 用户态
//        //      3. 用户态 send 内核态
//        //      4. 内核态 send 用户态
//        // 4. 不难看出直接让文件从文件读取缓冲区copy 到套接字发缓冲区就可以了 这样减少了二次copy 过程
//        Socket socket = new Socket(HOST, PORT);
//        OutputStream outputStream = socket.getOutputStream();
//
//        // RandomAccessFile 常用于大文件的下载上传 随机流 (r w rw)
//        // FileInputStream  各有利弊 I/O流
//        FileInputStream fileInputStream = new FileInputStream(SOURCE_FILE);
//        byte[] bytes = new byte[Integer.MAX_VALUE / 2];
//        int len = 0;
//        while ((len = fileInputStream.read(bytes)) != -1) {
//            outputStream.write(bytes, 0, len);
//        }
//    }
//
//    private static void mmap() throws IOException {
//        // TODO Liunx 中MMAP 内存映射技术 MappedByteBuffer
//        // 1. 采用Liunx MMAP 技术可以将传统的发送占用资源缩短为 三次拷贝过程和四次上下文切换
//        //      1. DMA 拷贝 MMAP 内存映射 到应用进程缓冲区
//        //      2. CPU 拷贝 应用进程缓冲区 到套接字发缓冲区
//        //      3. DMA 拷贝 套接字发缓冲区 到网络设备缓冲区
//        // 2. 三次copy 过程 四次上下文切换
//        Socket socket = new Socket(HOST, PORT);
//        OutputStream outputStream = socket.getOutputStream();
//        FileChannel fileChannel = new FileInputStream(SOURCE_FILE).getChannel();
//        long len = SOURCE_FILE.length();
//        byte[] bytes = new byte[Integer.MAX_VALUE / 2];
//
//        // 这里的设计 估计还是和sendFile 的transferTo 概念一致 为了能够显示进度 (MMAP 显示的更为细致)
//        // 最大映射 Integer.MAX_VALUE 2147483647B -> 2097151KB -> 2047MB -> 2GB
//        if(len > Integer.MAX_VALUE)
//        {
//            long l = 0;
//            while (l < len) {
//                MappedByteBuffer map;
//
//                // 针对MMAP 最大2GB 分批操作
//                if((l + Integer.MAX_VALUE) >= len) {
//                    long count = len - l;
//                    map = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, count);
//                } else {
//                    map = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, Integer.MAX_VALUE);
//                }
//                l += map.capacity();
//
//                // 处理分批任务
//                long l1 = 0, l2 = map.capacity();
//                while (l1 < l2) {
//                    // 确保数组下标不会越界
//                    if((l1 + bytes.length) > l2) {
//                        int count = (int)(l2 - bytes.length);
//                        map.get(bytes, 0, count);
//                        outputStream.write(bytes, 0, count + 1);
//                    }
//                    // 每次取的量达不到 bytes.length
//                    else {
//                        map.get(bytes);
//                        outputStream.write(bytes, 0, bytes.length);
//                    }
//                    l1 += map.position();
//                }
//            }
//        }
//        else
//        {
//            MappedByteBuffer map =
//                    fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, len);
//            map.get(bytes, 0, (int) len);
//            outputStream.write(bytes, 0, (int) len);
//        }
//    }
//
//    private static void sendFile() throws IOException, InterruptedException {
//        // TODO Liunx 中sendfile 技术 需要硬件支持 FileChannel
//        // 1. 从传统的数据发送中 让文件从文件读取缓冲区直接copy 到套接字发送缓冲区 这就是sendfile
//        //      1. DMA 拷贝 到文件读取缓冲区
//        //      2. CPU 拷贝 到套接字发缓冲区 (sendfile 描述符 数据地址等)
//        //      3. DMA 拷贝 到网络设备缓冲区
//        // 2. 三次copy 过程 两次上下文切换
//        SocketChannel socketChannel = SocketChannel.open();
//        socketChannel.connect(new InetSocketAddress(HOST, PORT));
//        socketChannel.configureBlocking(true);
//
//        FileChannel channel = new FileInputStream(SOURCE_FILE).getChannel();
//        // 8388608B -> 8192KB -> 8MB
//        long len = channel.size();
//        long l = 0;
//        while (l < len) {
//            // transferTo 每次只能发送2GB (可能是为了能够显示进度)
//            // transferFrom 一次发送全部数据
//            l += channel.transferTo(l, len - l, socketChannel);
//        }
//    }
//
//    private static void slice() {
//        // TODO Liunx 中slice 技术 管道共享技术
//        // 1. Liunx 中的sendfile 技术需要硬件的支持 于是又提出了不需要硬件支持的slice 利用管道来完成的
//        //      1. DMA 拷贝 到文件读取缓冲区
//        //      2. PIPE    到套接字发缓冲区
//        //      3. DMA 拷贝 到网络设备缓冲区
//        // 2. 两次拷贝 两次上下文切换
//    }
//
//    private static void files() throws IOException {
//        Socket socket = new Socket(HOST, PORT);
//        OutputStream outputStream = socket.getOutputStream();
//
//        SeekableByteChannel sbc
//                = Files.newByteChannel(Paths.get(SOURCE_FILE.getAbsolutePath()));
//
//        ChannelInputStream inputStream = (ChannelInputStream) Channels.newInputStream(sbc);
//
//        int len = 0;
//        byte[] bytes = new byte[Integer.MAX_VALUE / 2];
//        while ((len = inputStream.read(bytes)) != -1) {
//            outputStream.write(bytes, 0, len);
//
//        }
//    }
//
//    private static <T> void println(T message) {
//        LOGGER.debug("{}", message);
//    }
//}
