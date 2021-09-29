package com.example.rm.other;

import java.nio.ByteBuffer;

public class ImuControl extends BaseData{

    private static final byte cmd_id = 0x03;

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
        buffer.put(getAgreeHead());
        buffer.put(getCMD_ID());
        buffer.put(getDataSize());

        byte res = 0;

        for (byte b : buffer.array()){
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
        System.out.println("buffer : " + getTotalSize() + " byte");
        ByteBuffer buffer = ByteBuffer.allocate(getTotalSize());
        buffer.put(getAgreeHead());
        buffer.put(getCMD_ID());
        buffer.put(getDataSize());
        buffer.put(getCheckSum());
        buffer.put(getAgreeTail());
        return buffer.array();
    }
}
