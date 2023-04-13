#https://stackoverflow.com/questions/1035340/reading-binary-file-and-looping-over-each-byte
#https://www.geeksforgeeks.org/how-to-convert-bytes-to-int-in-python/
import numpy as np
import matplotlib.pyplot as plt


# Load label data into numpy array
# loc: Location of label file
def loadLabel(loc):
	
	# open binary file
	bf = open(loc, "rb")
	
	# read metadata
	y = int.from_bytes(bf.read(1), 'big')
	x = int.from_bytes(bf.read(1), 'big')
	
	# setup array
	label = np.empty((y,x,2))
	
	# populate array
	for i in range(y):
		for j in range(x):
			label[i][j][0] = int.from_bytes(bf.read(1), 'big') / 255
			label[i][j][1] = int.from_bytes(bf.read(1), 'big') / 255
	
	return label


# Load video data into numpy array
# loc: Location of video file
def loadVideo(loc):
	
	# open binary file
	bf = open(loc, "rb")
	
	# read metadata
	z = int.from_bytes(bf.read(1), 'big')
	y = int.from_bytes(bf.read(1), 'big')
	x = int.from_bytes(bf.read(1), 'big')
	
	# setup array
	video = np.empty((z,y,x))
	
	# populate array
	for i in range(z):
		for j in range(y):
			for k in range(z):
					video[i][j][k] = int.from_bytes(bf.read(1), 'big') / 255
	
	return video


label = loadLabel('230325_source0009_label.mvdl')
video = loadVideo('230325_source0009_video50_50_50.mvdl')

print(np.round(label, decimals=2))
print(np.round(video, decimals=2))

for i in range(len(video)):
	print('frame',i)
	plt.imshow(video[i])
	plt.scatter(*label[i].T)
	plt.show()

