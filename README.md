# insylo-rpi

## 1. Workflow
1. RPi boots
2. A conrtab job (J) starts as soon as the bootsequence is complete
3. J executes a script - [`capturedata.sh`][2].
4. [`capturedata.sh`][2] executes the compiled [`DataCapture`][1] app and sends the `JSON`
   formatted output to the server for further processing.

## 2. External libraries used
1. [GSON][7] for serializing java object - [`DataPacket`][3] into `JSON`
2. [OpenNI][6] - Linux for interfacing [`DataCapture App`][1] with Astra Pro.

## 3. Instructions for setup

1. Compiling the java CLI app.
2. Setup the cron job.

#### [3.1] Compiling the java CLI app.

1. Copy [`DataCapture`][1] directory under `/home/pi/Desktop/` on RPi.
2. CD into the [`src`][4] directory `cd /home/pi/Desktop/DataCapture/src`
3. Compile the source with the following command:

`javac -cp /home/pi/Desktop/DataCapture/libs/org.openni.jar:/home/pi/Desktop/DataCapture/libs/gson-2.6.2.jar:/home/pi/Desktop/DataCapture/src/com/kaunildhruv/insylo com/kaunildhruv/insylo/*.java`

4. You should now be able to see all the respective bytecodes `(.class)` files under [`/home/pi/Desktop/DataCapture/src/com/kaunildhruv/insylo/`][5]

#### [3.2] Setup the cron job.

Cron tab allows us to run a shell script as soon as the operating system's boot sequence completes.
In this case we will instruct the cron job to execute the [`capturedata.sh`][2] script on reboot.  


1. First, copy the file [`capturedata.sh`][2] to `/home/pi/Desktop/capturedata.sh`.
2. Give the file su permissions by entering the following command in the terminal  
`chmod +x 755`.

##### NOTE: Before we proceed we will check if everything is working correctly by executing the bash script [`capturedata.sh`][2].

3. Later run the crontab command.  
`crontab -e`
4. Add the following line at the end of file.  
`@reboot /home/pi/Desktop/capturedata.sh`
5. Save and close the file.
6. Start crond automatically at boot.  
`update-rc.d cron defaults`  


## The setup is complete!

[1]: https://github.com/KaunilD/insylo-rpi/tree/master/DataCapture
[2]: https://github.com/KaunilD/insylo-rpi/blob/master/capturedata.sh
[3]: https://github.com/KaunilD/insylo-rpi/blob/master/DataCapture/src/com/kaunildhruv/insylo/DataPacket.java
[4]: https://github.com/KaunilD/insylo-rpi/tree/master/DataCapture/src
[5]: https://github.com/KaunilD/insylo-rpi/tree/master/DataCapture/src/com/kaunildhruv/insylo
[6]: https://s3.amazonaws.com/com.occipital.openni/OpenNI-Linux-Arm-2.2.0.33.tar.bz2
[7]: http://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.5/gson-2.8.5.jar
