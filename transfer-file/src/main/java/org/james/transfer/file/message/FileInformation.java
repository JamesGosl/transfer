package org.james.transfer.file.message;

import org.james.transfer.utils.ConvertUtils;
import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.util.UUID;

/**
 * File Information
 *
 * 文件信息描述符
 *
 * 网络传输
 *
 * @author James Gosl
 * @since 2023/03/13 20:07
 */
@Message
public class FileInformation implements Serializable {
    private static final long serialVersionUID = -59999921812999498L;

    private String fileName;
    private long fileLength;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public void warp(FileInformation information) {
        if (information != null) {
            this.fileName = ConvertUtils.convertGeneric(information.fileName,
                    () -> UUID.randomUUID().toString().replaceAll("-", ""));
            this.fileLength = ConvertUtils.convertGeneric(information.fileLength, () -> {
                throw new RuntimeException("file length is not null !!!");
            });
        }
    }

    @Override
    public String toString() {
        return "{" +
                "fileName='" + fileName + '\'' +
                ", fileLength=" + fileLength +
                '}';
    }
}
