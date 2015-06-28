package com.mac.bluebox;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by anyer on 6/27/15.
 */
public class ArrayHelper {
    public static byte[] convertListToArrayOfBytes(List<String> list) {
        // write to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : list) {
            try {
                out.writeUTF(element);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return baos.toByteArray();
    }
}
