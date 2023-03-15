package org.james.transfer.file.message;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * File Token
 *
 * @author James Gosl
 * @since 2023/03/14 10:23
 */
@Message
public class FileToken implements Serializable {
    private static final long serialVersionUID = -599992515151615616L;
}
