package com.mac.bluebox.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anyer on 6/27/15.
 */
public class ArrayHelper {
    public static String joinStringByComma(List<String> list){
        String join = "";

        for (String item: list) {
            join += item + ",";
        }

        join = join.substring(0, join.length() - 1);
        return join;
    }
}
