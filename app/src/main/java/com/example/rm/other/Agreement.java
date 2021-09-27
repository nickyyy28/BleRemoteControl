package com.example.rm.other;

import java.nio.ByteBuffer;

public interface Agreement {
    public byte[] getAgreeHead();
    public byte[] getCMD_ID();
    public byte[] getDataSize();
    public byte[] getData();
    public byte[] getCheckSum();
    public byte[] getAgreeTail();
    public byte getTotalSize();

    public byte[] getPack();
}
