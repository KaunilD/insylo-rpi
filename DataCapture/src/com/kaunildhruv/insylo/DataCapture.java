package com.kaunildhruv.insylo;

import com.google.gson.Gson;
import org.openni.*;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class DataCapture {
    int[] imagePixels;
    float[] histogram;
    VideoStream videoStream;
    public DataCapture(VideoStream videoStream) {

        this.videoStream = videoStream;
    }

    public void captureFrame(){
        boolean run = true;
        int i = 0;
        videoStream.start();
        List<VideoStream> videoStreamList = new ArrayList<>();

        videoStreamList.add(videoStream);

        while(run){
            VideoFrameRef videoFrameRef = null;
            try {
                OpenNI.waitForAnyStream(videoStreamList, 1000);
                videoFrameRef = videoStream.readFrame();
                ByteBuffer frameData = videoFrameRef.getData().order(ByteOrder.LITTLE_ENDIAN);
                calculateHist(frameData);
                frameData.rewind();
                buffer2pixels(videoFrameRef);
                videoFrameRef.release();
                run = !run;
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        videoStream.stop();
        OpenNI.shutdown();

    }

    public void buffer2pixels(VideoFrameRef videoFrameRef) {
        imagePixels = new int[videoFrameRef.getWidth() * videoFrameRef.getHeight()];
        String timeStamp = new SimpleDateFormat("yyyMMddHHmmss").format(new Date());
        String depthCSVFileName = "depth.csv";

        ByteBuffer frameData = videoFrameRef.getData();
        calculateHist(frameData);
        frameData.rewind();
        int pos = 0;
        while(frameData.remaining() > 0){
            int depth = (int) frameData.getShort() & 0xFFFF;
            short pixel = (short) histogram[depth];
            imagePixels[pos] = (pixel << 16) | (pixel << 8);
            pos++;

        }
        DataPacket dataPacket = new DataPacket(imagePixels, "001.TH.MH.IND");

        Gson gson = new Gson();
        String dataPacketJSON = gson.toJson(dataPacket);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("data.json");
            fileWriter.write(dataPacketJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileWriter!=null){
                try{
                    fileWriter.close();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }

    }


    /*

    public void buffer2pixels(VideoFrameRef videoFrameRef) {
        imagePixels = new int[videoFrameRef.getWidth() * videoFrameRef.getHeight()];
        String timeStamp = new SimpleDateFormat("yyyMMddHHmmss").format(new Date());
        String depthCSVFileName = "depth.csv";

        ByteBuffer frameData = videoFrameRef.getData();
        calculateHist(frameData);
        frameData.rewind();
        int pos = 0;
        List<String> depthValues = new ArrayList<>();
        while(frameData.remaining() > 0){
            int depth = (int) frameData.getShort() & 0xFFFF;
            short pixel = (short) histogram[depth];
            imagePixels[pos] = (pixel << 16) | (pixel << 8);
            pos++;

        }

        for (int i = 0; i < imagePixels.length; i++){
            depthValues.add(Integer.toString(imagePixels[i]));
        }

        try{
            FileWriter csvWriter = new FileWriter(depthCSVFileName);
            CSVUtils.writeLine(csvWriter, depthValues);
            csvWriter.flush();
            csvWriter.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    */
    public void calculateHist(ByteBuffer byteBuffer) {
        histogram = new float[videoStream.getMaxPixelValue()];
        for (int i = 0; i < histogram.length; ++i)
            histogram[i] = 0;

        int points = 0;

        while(byteBuffer.remaining() > 0){
            int depth = byteBuffer.getShort() & 0xFFFF;
            if(depth!=0){
                histogram[depth]++;
                points++;
            }
        }

        for (int i = 1; i < histogram.length; i++){
            histogram[i] += histogram[i-1];
        }

        if(points > 0) {
            for (int i = 1; i < histogram.length; i++){
                histogram[i] = (int) 256*(1.0f - (histogram[i] / (float) points));
            }
        }
    }
}
