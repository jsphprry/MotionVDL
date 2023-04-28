#!/bin/bash

cd predictions/
ffmpeg -framerate 10 -i frame%d.png -c:v libx264 ../predictions.mp4
