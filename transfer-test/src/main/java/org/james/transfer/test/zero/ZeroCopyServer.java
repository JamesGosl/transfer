//package org.james.transfer.test.zero;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketException;
//import java.nio.file.Files;
//import java.nio.file.StandardOpenOption;
//import java.util.UUID;
//
///**
// * TODO ZeroCopy Server
// *
// * @author James Gosl
// * @since 2023/03/08 09:16
// */
//public class ZeroCopyServer {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ZeroCopyServer.class);
//
//    private static final String TARGET_PATH = "C:\\Users\\Combat\\Desktop\\temp\\";
//
//    public static void main(String[] args) {
//        try {
//            ServerSocket serverSocket = new ServerSocket(1024);
//            while (true) {
//                Socket socket = serverSocket.accept();
//                files(socket);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void files(Socket socket) {
//        try (
//                InputStream inputStream = socket.getInputStream();
//        ){
//            File file = new File(TARGET_PATH, UUID.randomUUID().toString());
//            file.createNewFile();
//
//            // B KB MB GB
//            // B 字节 byte
//            byte[] bytes = new byte[Integer.MAX_VALUE / 2];
//            int len;
//            long count = 0;
//            try {
//                OutputStream outputStream = Files.newOutputStream(file.toPath(), StandardOpenOption.APPEND);
//                while((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
//                    // 65536B -> 64KB
//                    count += len;
//                    outputStream.write(bytes, 0, len);
//                }
//            } catch (SocketException e) {
//            }
//            // 3612586084
//            // 3612586084
//            LOGGER.debug("{}: {}", socket.getRemoteSocketAddress(), count);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private static void generic(Socket socket) {
//        try (
//                InputStream inputStream = socket.getInputStream();
//                FileOutputStream fileOutputStream = new FileOutputStream(TARGET_PATH + UUID.randomUUID());
//        ){
//            // B KB MB GB
//            // B 字节 byte
//            byte[] bytes = new byte[Integer.MAX_VALUE / 2];
//            int len;
//            long count = 0;
//            try {
//                while((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
//                    // 65536B -> 64KB
//                    count += len;
//                    fileOutputStream.write(bytes, 0, len);
//                }
//            } catch (SocketException e) {
//            }
//            // 3612586084
//            // 3612586084
//            LOGGER.debug("{}: {}", socket.getRemoteSocketAddress(), count);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}
