# MotionVDL 2023 (active development)

Application development for CI536 Integrated group project.\
Motion Video Data Labeller is a tool for producing labelled
datasets from video data for use in supervised machine learning.


## Introduction

For this group project, we have set out to implement a video labelling tool for use in supervised machine learning called Motion Video Data Labeller, abbreviated to MVDL or MotionVDL. We have worked collaboratively using the Agile Scrumban management style, meeting twice a week to communicate about our progress, review the development stage and update our Kanban board.

MotionVDL is a scientific tool providing a graphical interface for the manual labelling of video frame data. The labelling process involves placing 11 ordered nodes marking key points on the human body on each video frame, with the aim of producing a dataset of sequence-dependent labelled video frames. The tool also provides a preprocessing stage, responsible for square cropping, downscaling and grey-scaling the video data. Using supervised machine learning techniques, the labelled data could be used as an optimization target for computer vision systems aiming to form a similar model of human motion to the one defined by the dataset.

In addition to the implementation of the labelling tool, the second objective of this project has been to collect the raw video data that the tool should eventually process. With the help of the university’s television studio equipment and staff and the collaboration of the group members, we have collected around 41 minutes of video data at 25fps equivalent to around 62,500 frames of video data.


## Technical Details

> Information such as Java SDK version (JDK 17), JavaFX version (17), etc.


## How To Run

> From a fresh download, what steps need to be reproduced to run the application.

### Eclipse


### IntelliJ

No additional actions required - root specifications and external libraries included in supplied .idea directory.\
Start application by running main/JavaFXLauncher.


## How To Use

In this repository are 20 example frames which can be opened using File -> Open Directory -> frames.\
Using the sliders, the frame must be cropped so the subject is always in frame.\
Specify the target resolution using the provided TextField.\
Process the frames to begin labelling, which can be achieved by clicking the frame.\
Save your progress using either File -> Save As or File -> Save\
This file can be reopened using File -> Open File -> 'Your .mvdl File'\
See the video below for a visual demonstration:

https://github.com/H-jj-R/MotionVDL/assets/63592626/434749e7-816f-4177-a0f3-cc56f246e760

