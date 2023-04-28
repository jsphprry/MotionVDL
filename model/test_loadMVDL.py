import numpy as np
import matplotlib.pyplot as plt
import loadMVDL as mvdl

# make gtk errors early
plt.plot()

# load data
x,y = mvdl.load('output_230416.mvdl')

# print all values
print(x)
print(y)

# plot frame
im = plt.imshow(x[464])
plt.show()

# prepare frame and label
frame = x[89]
label = y[89]
lx,ly = np.split(label*50, [-1], axis=1)

# plot image
plt.imshow(frame)

# plot connectors
for i,c in enumerate([0,0,1,2,1,4,1,6,7,6,9]):
	line_x = (lx[i], lx[c])
	line_y = (ly[i], ly[c])
	plt.plot(line_x, line_y, color='red')

# plot nodes
plt.scatter(lx, ly, color='black', marker='+')
plt.show()
