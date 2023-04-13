import numpy as np
import matplotlib.pyplot as plt
import loadMVDL as mvdl

# make gtk errors early
plt.plot()

# load data
x,y = mvdl.load('../labellers/MotionVDL personal/output.mvdl')

# print all values
print(x)
print(y)

# plot frame
im = plt.imshow(x[464])
plt.show()

# plot label on frame
frame = x[89]
label = y[89]
lx,ly = np.split(label, [-1], axis=1)
fw,fh = np.split(frame.shape, [-1], axis=0)
plt.imshow(frame)
plt.scatter(lx*fw, ly*fh, color='red', marker='+')
plt.show()
