MotionVDL 2023 (active development)
-

Application development for CI536 Integrated group project.




Introduction
-

For this group project, we have set out to implement a video labelling tool for use in supervised machine learning called Motion Video Data Labeller, abbreviated to MVDL or MotionVDL. We have worked collaboratively using the Agile Scrumban management style, meeting twice a week to communicate about our progress, review the development stage and update our Kanban board.

MotionVDL is a scientific tool providing a graphical interface for the manual labelling of video frame data. The labelling process involves placing 11 ordered nodes marking key points on the human body on each video frame, with the aim of producing a dataset of sequence-dependent labelled video frames. The tool also provides a preprocessing stage, responsible for square cropping, downscaling and grey-scaling the video data. Using supervised machine learning techniques, the labelled data could be used as an optimization target for computer vision systems aiming to form a similar model of human motion to the one defined by the dataset.

In addition to the implementation of the labelling tool, the second objective of this project has been to collect the raw video data that the tool should eventually process. With the help of the university’s television studio equipment and staff and the collaboration of the group members, we have collected around 41 minutes of video data at 25fps equivalent to around 62,500 frames of video data.
