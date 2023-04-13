#!/bin/sh
JAVA_PATH=/usr/lib64/openjdk-17/bin/

x-terminal-emulator -- "${JAVA_PATH}"java -Xmx4g -classpath ./bin motionvdl.MotionVDL output.mvdl
