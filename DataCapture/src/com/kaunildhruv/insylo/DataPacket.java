package com.kaunildhruv.insylo;

import java.lang.reflect.Array;

public class DataPacket {
    int[] depth;
    String deviceID;

    public DataPacket() {
    }

    public DataPacket(int[] depth) {
        this.depth = depth;
    }

    public int[] getDepth() {
        return depth;
    }

    public void setDepth(int[] depth) {
        this.depth = depth;
    }

    public DataPacket(int[] depth, String deviceID) {
        this.depth = depth;
        this.deviceID = deviceID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
