package com.github.kirivasile.technotrack.message;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kirill on 24.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Тестирование протокола сериализации
 */
public class SerializationProtocolTest {
    private static Message message;
    private static SerializationProtocol<Message> protocol;

    @BeforeClass
    public static void beforeClass() {
        message = new Message();
        message.setId(1);
        message.setMessage("Hi, I am #1");
        message.setAuthorId(2);
        protocol = new SerializationProtocol<>();
    }

    @Test
    public void testEncodeAndDecode() throws Exception {
        byte[] buf = protocol.encode(message);
        Message result = protocol.decode(buf);
        assertEquals(result.getId(), 1);
        assertEquals(result.getAuthorId(), 2);
        assertEquals(result.getMessage(), "Hi, I am #1");
    }
}