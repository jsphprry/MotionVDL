Monday 13th March 2023 
-

Main issues

* Crop stage clicks not following intended behaviour
* Scaling stage needs input field (implemented, just need to remove placeholder)
* Image frame has unexplained vertical bands, even after greyscaling

Precision testing

* Frame clicks must be accurate
* Click coords must be normalised [0..1]
(where the image frame size is stored)

Revisions to comments

* MotionVDL.java contructor params
* Label.java contructor params

Optimizations

* Display.java drawDiagonal (functional solution for coords)
* CSS styling
* Merge process and complete button functions
