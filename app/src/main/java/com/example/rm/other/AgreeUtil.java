package com.example.rm.other;

import java.nio.ByteBuffer;

public class AgreeUtil {
    public static byte[] getBytes(byte a){
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put(a);
        return buffer.array();
    }

    public static byte[] getBytes(short a){
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(a);
        return buffer.array();
    }

    public static byte[] getBytes(int a){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(a);
        return buffer.array();
    }

    public static byte[] getBytes(long a){
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(a);
        return buffer.array();
    }

    public static byte getDataSizeByCMD_ID(byte cmd_id){
        switch (cmd_id) {
            case 0x01:
                return (byte) 8;
            case 0x02:
                return (byte) 6;
            case (byte) 0xAA:
            case (byte) 0xFF:
            default:
                return (byte) 0;
        }

    }



}
