#!/bin/bash
jar_dir="/home/pi/Desktop/DataCapture/src/"
depth_csv=$jar_dir"data.json"
cd $jar_dir
echo "Capturing frame."
java -cp ":/home/pi/Desktop/DataCapture/libs/org.openni.jar:/home/pi/Desktop/DataCapture/libs/gson-2.6.2.jar" com.kaunildhruv.insylo.Main
csv_data=$(cat $depth_csv)
echo "Uploading to db"
curl -H "Content-Type: application/json" -X POST -d @$depth_csv 'https://insyloapi.herokuapp.com/insert'

