import numpy as np
import matplotlib.pyplot as plt

from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D
from tensorflow.keras.layers import Dense
from tensorflow.keras.layers import LSTM
from tensorflow.keras.layers import Reshape
from tensorflow.keras.optimizers import Adam

import loadMVDL as mvdl


# load data
x,y = mvdl.load('output_230420.mvdl')

# format data
x = np.reshape(x, (x.shape[0], x.shape[1], x.shape[2], 1))

# partition data
train_x = x[:430]
train_y = y[:430]
test_x  = x[430:800]
test_y  = y[430:800]

# define model
model = Sequential()
model.add(Conv2D(3, 3, strides=3, activation='sigmoid', input_shape=(50,50,1))) # s=s-k+1
model.add(Reshape((1,3*16*16)))
#model.add(Dense(22, activation='sigmoid'))
model.add(LSTM(22))
model.add(Reshape((11,2)))
model.compile(loss='mean_squared_error', optimizer='adam')
model.summary()

# fit model and make predictions
history       = model.fit(train_x, train_y, epochs=100, batch_size=1, verbose=2)
train_predict = model.predict(train_x)
test_predict  = model.predict(test_x)

# plot predictions
predictions = np.concatenate((train_predict,test_predict))
for index,(image,label) in enumerate(zip(x, predictions)):
	
	# prepare image and label
	image = image.reshape(50,50)
	lx,ly = np.split(label*50, [-1], axis=1)
	
	# plot image
	plt.imshow(image)
	
	# plot connectors
	for i,c in enumerate([0,0,1,2,1,4,1,6,7,6,9]):
		line_x = (lx[i], lx[c])
		line_y = (ly[i], ly[c])
		plt.plot(line_x, line_y, color='red')
	
	# plot nodes
	plt.scatter(lx, ly, color='black', marker='+')
	
	# output figure
	plt.savefig(f"predictions/frame{index}.png")
	plt.clf()
	print(f"saved frame {index}/{len(predictions)-1}")
