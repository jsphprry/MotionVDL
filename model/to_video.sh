#!/bin/bash

#cd predictions/
#ffmpeg -framerate 10 -i $1%d.png -c:v libx264 ../$1_predictions.mp4

if [ -z "$1" ]
then
	# convert all videos
	cd predictions/
	for s in $(cat ../to_video.list); do
		ffmpeg -framerate 10 -i $s%d.png -c:v libx264 ../"$s"_predictions.mp4;
	done
else
	# convert specified video
	cd predictions/
	ffmpeg -framerate 10 -i $1%d.png -c:v libx264 ../$1_predictions.mp4
fi
