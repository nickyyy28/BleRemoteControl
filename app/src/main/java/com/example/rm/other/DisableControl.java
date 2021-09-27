package com.example.rm.other;

public class DisableControl implements Agreement{
    @Override
    public byte[] getAgreeHead() {
        return new byte[0];
    }

    @Override
    public byte[] getCMD_ID() {
        return new byte[0];
    }

    @Override
    public byte[] getDataSize() {
        return new byte[0];
    }

    @Override
    public byte[] getData() {
        return new byte[0];
    }

    @Override
    public byte[] getCheckSum() {
        return new byte[0];
    }

    @Override
    public byte[] getAgreeTail() {
        return new byte[0];
    }

    @Override
    public byte getTotalSize() {
        return 0;
    }

    @Override
    public byte[] getPack() {
        return new byte[0];
    }

    public DisableControl() {
    }
}
