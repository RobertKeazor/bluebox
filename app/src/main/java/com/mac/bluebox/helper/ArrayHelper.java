package com.mac.bluebox.helper;

import com.google.common.primitives.Ints;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anyer on 6/27/15.
 */
public final class ArrayHelper {
    public static String joinStringByComma(List<String> list){
        String join = "";

        for (String item: list) {
            join += item + ",";
        }

        join = join.substring(0, join.length() - 1);
        return join;
    }


    public static byte[] encodePacket(byte command, byte[] data, int dataSize){
        byte[] dataSizeBuffer = ArrayHelper.intToBytes(dataSize);

        // 1 byte of command, 4 bytes of dataSize
        byte[] bytes = new byte[1 + 4 + dataSize];

        bytes[0] = command;
        for (int i = 0; i < 4; i++) {
            bytes[i + 1] = dataSizeBuffer[i];
        }

        for (int i = 0; i < dataSize; i++) {
            bytes[i + 5] = data[i];
        }

        return bytes;
    }

    public static byte decodeCommandPacket(byte[] data) {
        return data[0];
    }

    public static byte[] decodeDataPacket(byte[] data) {
        byte[] bytes = new byte[data.length - 5];

        for (int i = 0; i < data.length - 5; i++) {
            bytes[i] = data[i + 5];
        }

        return bytes;
    }

    public static int decodeDataPacketSize(byte[] data){
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = data[i + 1];
        }

        return bytesToInt(bytes);
    }

    public static byte[] intToBytes( final int i ) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }

    public static int bytesToInt(byte[] bytes){
        return Ints.fromByteArray(bytes);
    }
}
