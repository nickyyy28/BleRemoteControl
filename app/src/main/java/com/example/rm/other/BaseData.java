package com.example.rm.other;

public abstract class BaseData implements Agreement{
    protected static final byte head = (byte) 0x5A;
    protected static final byte tail = (byte) 0xA5;

    public byte[] getAgreeHead() {
        return AgreeUtil.getBytes(head);
    }

    public byte[] getAgreeTail() {
        return AgreeUtil.getBytes(tail);
    }
}
