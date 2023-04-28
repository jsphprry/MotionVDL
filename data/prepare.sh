#!/bin/bash

# The frames directory should contain only the processed video frames as pngs, 
# any non-image files in this directory will cause an exception in the file loader

# prepare directory
mkdir -pv frames/
rm -rfv frames/*

# sample images at 10fps from downscaled input video '$1'
ffmpeg -i $1 -vf scale=722:-1 -an downscale.mp4
ffmpeg -i downscale.mp4 -vf fps=10 "frames/%d.png"

# remove file extensions (unused)
#cd frames/
#for file in *; do
#	filename=$(basename "$file" .png)
#	mv "$file" "${filename}.png"
#done
