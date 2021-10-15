package com.example.rm.other;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FineTuningControl extends BaseData {
    private static final byte cmd_id = 0x02;

    public FineTuningControl(rmData data) {
        this.data = data;
    }

    public void setData(rmData data) {
        this.data = data;
    }

    private rmData data;

    @Override
    public byte[] getCMD_ID() {
        return AgreeUtil.getBytes(cmd_id);
    }

    @Override
    public byte[] getDataSize() {
        return AgreeUtil.getBytes((byte) 6);
    }

    @Override
    public byte[] getData() {
        ByteBuffer buffer = ByteBuffer.allocate(6);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(data.getChannel1());
        buffer.putShort(data.getChannel3());
        buffer.putShort(data.getChannel4());
        return buffer.array();
    }

    @Override
    public byte[] getCheckSum() {
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.put(getAgreeHead());
        buffer.put(getCMD_ID());
        buffer.put(getDataSize());
        buffer.put(getData());
        byte res = 0;

        for (byte b : buffer.array()) {
            res += b;
        }

        return AgreeUtil.getBytes(res);
    }

    @Override
    public byte getTotalSize() {
        return 11;
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
        return buffer.array();
    }
}