package com.example.rm.utils;

import java.util.ArrayList;

public class DataTransformUtil {
    public static int getInt(byte[] bytes)
    {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    public static int getInt(byte[] bytes, int start)
    {
        return (0xff & bytes[start]) | (0xff00 & (bytes[start + 1] << 8)) | (0xff0000 & (bytes[start + 2] << 16)) | (0xff000000 & (bytes[start + 3] << 24));
    }

    public static float getFloat(byte[] bytes)
    {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static float getFloat(byte[] bytes, int start)
    {
        return Float.intBitsToFloat(getInt(bytes, start));
    }

    public static float getFloat(ArrayList<Byte> list, int start)
    {
        byte[] temp = new byte[list.size()];
        for (int i = 0 ; i < list.size() ; i++){
            temp[i] = list.get(i);
        }

        return getFloat(temp, start);
    }
}
