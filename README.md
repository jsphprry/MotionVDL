MotionVDL 2023 (active development)
-

Application development for CI536 Integrated group project.




Description
-

Motion Video Data Labeller is a tool for producing labelled
datasets from video data for use in supervised machine learning.




Potentional applications of the labelled data
-

The functionality of MotionVDL could be extended with 
a helper vision system that could help identify poorly 
labelled frame data.

Label sequences could be used to train LSTM models and 
labelled frames could be used to train conditional GANs.

LSTM-GANs could be used to model the label sequences and 
produce generated sequences that could then be used to 
produce new video data from a conditional GAN trained on 
the labelled frame data.




Controller system
-

All controllers inherit from the abstract superclass Controller. 
Each controller points to one linked controller, a display and 
a video. 

They are defined with point, process and complete 
methods, which define the application behaviour in the event of 
a frame click, process-button press or complete-button press 
action respectively. 

They also have frameUp and frameDown methods,
which are responsible for displaying either the next or previous 
frames on the display and depending on the display implementation, 
these actions are bound to either scrolling or to button-press.

Controllers also have access to the protected method pass which 
is used when passing control between the linked controllers.

The program is controlled by the main-controller, which stores 
instances of each of the subcontrollers in an array and passes 
control between them when its pass method is called. 

The main-controller's event methods call the event methods of 
the current subcontroller and each subcontroller defines the 
stage specific behavour of the superclass methods.




Model components
-

The model consists of classes for video-data and video-labels.

Video-labels are implemented by Label as an array of stacks of 
integer precision 2D points, with methods for poping and pushing 
to indexed stacks storing frame-wise point labels.

Video-data is represented as a 3D array of Color and wrapped by 
Video to implement the resolution and colorscale video manipulations 
needed for the video preprocessing.




Starter program
-

The application is started by the main method of the motionvdl.MotionVDL
class. This method creates the Display and MainController, then loads the 
video file into Video which is passes to the MainController start method;
responsible for passing initial control to the main-controller.

