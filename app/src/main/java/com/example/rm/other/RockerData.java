package com.example.rm.other;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class RockerData extends BaseData implements Agreement {

    private static final byte cmd_id = (byte) 0x01;

    public RockerData(rmData data) {
        this.data = data;
    }

    public void setData(rmData data) {
        this.data = data;
    }

    private rmData data;

    @Override
    public byte[] getAgreeHead() {
        return AgreeUtil.getBytes(head);
    }

    @Override
    public byte[] getCMD_ID() {
        return AgreeUtil.getBytes(cmd_id);
    }

    @Override
    public byte[] getDataSize() {
        return AgreeUtil.getBytes(AgreeUtil.getDataSizeByCMD_ID((byte) 0x01));
    }

    @Override
    public byte[] getPack() {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalSize());
        buffer.put(getAgreeHead());
        buffer.put(getCMD_ID());
        buffer.put(getDataSize());
        buffer.put(getData());
        buffer.put(getCheckSum());
        buffer.put(getAgreeTail());
        System.out.println("data pack " + Arrays.toString(buffer.array()));
        System.out.println("data pack size: " + buffer.array().length);
        return buffer.array();
    }

    @Override
    public byte[] getData() {
        return data.getDataPack();
    }

    @Override
    public byte[] getCheckSum() {
        return AgreeUtil.getBytes(data.getCheckSum());
    }

    @Override
    public byte getTotalSize() {
        //head + cmd_id + dataSize + dataPack + checkSum + tail
        return 1 + 1 + 1 + 8 + 1 + 1;
    }

    @Override
    public byte[] getAgreeTail() {
        return AgreeUtil.getBytes(tail);
    }
}
