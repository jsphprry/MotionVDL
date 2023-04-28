#!/bin/bash

cd predictions/
ffmpeg -framerate 10 -i $1%d.png -c:v libx264 ../$1_predictions.mp4
