package com.example.rm.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ReceiveUtil {
    private static final ArrayList<Byte> receiveList = new ArrayList<>();

    public static void appendData(byte[] data){
        for (byte b : data){
            receiveList.add(b);
        }
    }

    public static ArrayList<Byte> getReceiveData(){
        return receiveList;
    }
}
