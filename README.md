# MotionVDL 2023 (active development)

Application development for CI536 Integrated group project.\
Motion Video Data Labeller is a tool for producing labelled
datasets from video data for use in supervised machine learning.


## Introduction

For this group project, we have set out to implement a video labelling tool for use in supervised machine learning called Motion Video Data Labeller, abbreviated to MVDL or MotionVDL. We have worked collaboratively using the Agile Scrumban management style, meeting twice a week to communicate about our progress, review the development stage and update our Kanban board.

MotionVDL is a scientific tool providing a graphical interface for the manual labelling of video frame data. The labelling process involves placing 11 ordered nodes marking key points on the human body on each video frame, with the aim of producing a dataset of sequence-dependent labelled video frames. The tool also provides a preprocessing stage, responsible for square cropping, downscaling and grey-scaling the video data. Using supervised machine learning techniques, the labelled data could be used as an optimization target for computer vision systems aiming to form a similar model of human motion to the one defined by the dataset.

In addition to the implementation of the labelling tool, the second objective of this project has been to collect the raw video data that the tool should eventually process. With the help of the university’s television studio equipment and staff and the collaboration of the group members, we have collected around 41 minutes of video data at 25fps equivalent to around 62,500 frames of video data.


## Technical Details
This project has been developed using __openjdk version 17.0.5__ and __javafx version 17.0.6__

## How To Run

### Eclipse
Setup JavaFX JRE:  
1. Download javafx-sdk to __/local/path/to/javafx-sdk__  
2. Under __Window > Preferences > Java > Installed JREs__ duplicate default jre  
3. Rename duplicated jre __javafx-jre-17__  
4. Add __--module-path /local/path/to/javafx-sdk/lib/ --add-modules javafx.controls,javafx.fxml,javafx.swing__ to the jre *Default VM arguments*  
5. Click *Add External JARs* and select __/local/path/to/javafx-sdk/lib/*__  
6. Select *Finish* then *Apply and close*

Add JavaFX JRE to project:  
1. Under __Run > Run Configurations > Java Application > MotionVDL__ select the *JRE* tab  
2. Select the *Alternate JRE* radio button, then select __javafx-jre-17__ from the corresponding dropdown menu  
3. Click *Apply*

Start application by running motionvdl.MotionVDL.main()

### IntelliJ

No additional actions required - root specifications and external libraries included in supplied .idea directory.\
Start application by running main/JavaFXLauncher.


## How To Use

This repository contains 20 example frames which can be opened using File -> Open Directory -> frames.\
Using the sliders, the frame must be cropped so the subject is always in frame in all frames.\
Specify the target resolution using the provided TextField.\
Use the process button to process the frames to begin labelling; labelling can be achieved by clicking the frame.\
Save your progress using either File -> Save As or File -> Save\
This file can be reopened using File -> Open File -> 'Your .mvdl File'\
See the video below for a visual demonstration:

https://github.com/H-jj-R/MotionVDL/assets/63592626/434749e7-816f-4177-a0f3-cc56f246e760

