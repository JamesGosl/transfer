package org.james.transfer.test.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;

/**
 * Files Tests
 *
 * @author James Gosl
 * @since 2023/03/15 12:31
 */
public class FilesTests {

    public static void main(String[] args) throws IOException {
//        Files.copy();
//        Files.write();

        File file = new File("C:\\Users\\Combat\\Desktop\\temp\\CLion-2021.3.3.exe");


        for (int i = 0; i < 10; i++) {
            byte[] buf = new byte[Integer.MAX_VALUE - 8];

            try (SeekableByteChannel sbc = Files.newByteChannel(file.toPath());
                 InputStream in = Channels.newInputStream(sbc)) {
                int len;

                while ((len = in.read(buf)) != -1) {
                }
            }

            System.out.println(i);
        }
    }
}
