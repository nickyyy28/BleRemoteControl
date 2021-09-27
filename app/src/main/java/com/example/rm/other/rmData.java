package com.example.rm.other;

import com.example.rm.views.RockerView;

import java.nio.ByteBuffer;

public class rmData {
    short channel1 = 0;
    short channel2 = 0;
    short channel3 = 0;
    short channel4 = 0;

    public short getChannel1() {
        return channel1;
    }

    public short getChannel2() {
        return channel2;
    }

    public short getChannel3() {
        return channel3;
    }

    public short getChannel4() {
        return channel4;
    }

    public rmData() {
    }

    public void updateChannels(float distance_left, float angle_left, float distance_right, float angle_right) {
        short max = RockerView.getMax();

        channel1 = (short) ((Math.cos(Math.toRadians(360 - angle_left)) * distance_left) / max * 100 + 100);
        channel2 = (short) ((Math.sin(Math.toRadians(360 - angle_left)) * distance_left) / max * 100 + 100);
        channel3 = (short) ((Math.cos(Math.toRadians(360 - angle_right)) * distance_right) /max * 100 + 100);
        channel4 = (short) ((Math.sin(Math.toRadians(360 - angle_right)) * distance_right) / max * 100 + 100);

        System.out.println("max = " + max);
        System.out.println("ch1 = " + channel1 + " ch2 = " + channel2);
        System.out.println("ch3 = " + channel3 + " ch4 = " + channel4);
    }

    private byte[] float2bytes(short f) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(f);
        return buffer.array();
    }

    public byte[] getDataPack() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putShort(channel1);
        buffer.putShort(channel2);
        buffer.putShort(channel3);
        buffer.putShort(channel4);
        return buffer.array();
    }

    public byte getCheckSum(){
        byte res = 0;
        byte[] data = getDataPack();
        for (byte a : data){
            res += a;
        }
        return res;
    }

}
