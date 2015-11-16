package com.github.kirivasile.technotrack.message;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class SerializationProtocol<T> implements Protocol<T> {
    @Override
    public byte[] encode(T msg) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(msg);
            byte[] objData = bos.toByteArray();
            int size = objData.length;

            ByteBuffer buf = ByteBuffer.allocate(size + 4);
            buf.putInt(size);
            buf.put(objData);
            return buf.array();
        } catch (IOException e) {
            System.err.println("Failed to encode message " + e.toString());
        }
        return new byte[0];
    }

    @Override
    public T decode(byte[] data) throws Exception {
        ByteBuffer buf = ByteBuffer.wrap(data);
        int size = buf.getInt();
        if (size > data.length - 4) {
            System.err.println("Failed to decode mesage");
        }
        byte[] objData = new byte[size];
        buf.get(objData);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(objData);
             ObjectInput in = new ObjectInputStream(bis)) {
            return (T) in.readObject();
        } catch (IOException e) {
            System.err.println("Failed to decode messsage " + e.toString());
        } catch (ClassNotFoundException e) {
            System.err.println("Decode: no class found " + e.toString());
        }
        return null;
    }
}
