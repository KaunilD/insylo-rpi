package com.kaunildhruv.insylo;

import org.openni.*;

import java.util.List;

public class Main {

    public static void main(String args[]){

        OpenNI.initialize();
        List<DeviceInfo> deviceInfoList = OpenNI.enumerateDevices();
        Device device = Device.open(deviceInfoList.get(0).getUri());
        System.out.println("Opening device : " + deviceInfoList.get(0).getUri());
        VideoStream videoStream = VideoStream.create(device, SensorType.DEPTH);
        VideoMode videoMode = new VideoMode(320, 240, 30, 100);
        videoStream.setVideoMode(videoMode);

        DataCapture dataCapture = new DataCapture(videoStream);
        dataCapture.captureFrame();

    }
}
