package com.example.rm.other;

import java.nio.ByteBuffer;

public class PowerControl extends BaseData {
    private static final byte cmd_id = 0x04;

    @Override
    public byte[] getCMD_ID() {
        return AgreeUtil.getBytes(cmd_id);
    }

    @Override
    public byte[] getDataSize() {
        return AgreeUtil.getBytes((byte) 0);
    }

    @Override
    public byte[] getData() {
        return null;
    }

    @Override
    public byte[] getCheckSum() {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        byte res = 0;
        buffer.put(getAgreeHead());
        buffer.put(getCMD_ID());
        buffer.put(getDataSize());

        for (byte b : buffer.array()) {
            res += b;
        }
        return AgreeUtil.getBytes(res);
    }

    @Override
    public byte getTotalSize() {
        return 5;
    }

    @Override
    public byte[] getPack() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put(getAgreeHead());
        buffer.put(getCMD_ID());
        buffer.put(getDataSize());
        buffer.put(getCheckSum());
        buffer.put(getAgreeTail());

        return buffer.array();
    }

    public PowerControl() {
    }
}
