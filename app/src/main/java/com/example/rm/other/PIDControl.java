package com.example.rm.other;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PIDControl extends BaseData{

    private byte cmd_id;

    private short Kp;
    private short Ki;
    private short Kd;
    private short iLimit;
    private short iRange;

    public short getKp() {
        return Kp;
    }

    public void setKp(short kp) {
        Kp = kp;
    }

    public short getKi() {
        return Ki;
    }

    public void setKi(short ki) {
        Ki = ki;
    }

    public short getKd() {
        return Kd;
    }

    public void setKd(short kd) {
        Kd = kd;
    }

    public short getiLimit() {
        return iLimit;
    }

    public void setiLimit(short iLimit) {
        this.iLimit = iLimit;
    }

    public short getiRange() {
        return iRange;
    }

    public void setiRange(short iRange) {
        this.iRange = iRange;
    }

    public PIDControl(byte cmd_id) {
        this.cmd_id = cmd_id;
    }

    public byte getCmd_id() {
        return cmd_id;
    }

    public void setCmd_id(byte cmd_id) {
        this.cmd_id = cmd_id;
    }

    @Override
    public byte[] getCMD_ID() {
        return AgreeUtil.getBytes(cmd_id);
    }

    @Override
    public byte[] getDataSize() {
        return AgreeUtil.getBytes((byte) 10);
    }

    @Override
    public byte[] getData() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(Kp);
        buffer.putShort(Ki);
        buffer.putShort(Kd);
        buffer.putShort(iLimit);
        buffer.putShort(iRange);

        return buffer.array();
    }

    @Override
    public byte[] getCheckSum() {
        ByteBuffer buffer = ByteBuffer.allocate(23);
        buffer.put(getAgreeHead());
        buffer.put(getCMD_ID());
        buffer.put(getDataSize());
        buffer.put(getData());

        byte res = 0;
        for (byte b : buffer.array()){
            res += b;
        }

        return AgreeUtil.getBytes(res);
    }

    @Override
    public byte getTotalSize() {
        return 15;
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
