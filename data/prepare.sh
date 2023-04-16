#!/bin/bash
rm frames/* -v
ffmpeg -i $1 -vf scale=722:-1 -an downscale.mp4
ffmpeg -i downscale.mp4 -vf fps=10 "$(pwd)/frames/%d.png"

#cd frames/
#for file in *; do
#	filename=$(basename "$file" .png)
#	mv "$file" "${filename}.png"
#done
