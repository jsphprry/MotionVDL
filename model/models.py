import numpy as np
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D
from tensorflow.keras.layers import ConvLSTM2D
from tensorflow.keras.layers import Dense
from tensorflow.keras.layers import LSTM
from tensorflow.keras.layers import MaxPooling2D
from tensorflow.keras.layers import Reshape



# cnn+lstm sequential model
class CNNLSTM(Sequential):
	
	# construct model
	def __init__(self):
		super().__init__()
		self.add(Conv2D(3, 3, strides=3, activation='sigmoid', input_shape=(50,50,1)))
		self.add(MaxPooling2D())
		self.add(Reshape((1,3*8*8)))
		self.add(LSTM(22))
		self.add(Reshape((11,2)))
	
	# fit to (samples, rows, cols, channels) reshape of 3d input
	def fit(self, x, y, **kwargs):
		return super().fit(np.reshape(x, (x.shape[0], x.shape[1], x.shape[2], 1)), y, **kwargs)
	
	# predict (samples, rows, cols, channels) reshape of 3d input
	def predict(self, x, **kwargs):
		return super().predict(np.reshape(x, (x.shape[0], x.shape[1], x.shape[2], 1)), **kwargs)



# convolutional lstm sequential model
class ConvLSTM(Sequential):
	
	# construct model
	def __init__(self):
		super().__init__()
		self.add(ConvLSTM2D(3, 3, strides=3,input_shape=(1,50,50,1)))
		self.add(MaxPooling2D())
		self.add(Reshape((1,1*3*8*8)))
		self.add(Dense(22, activation='sigmoid'))
		self.add(Reshape((11,2)))
		
	# fit to (samples, timesteps, rows, cols, channels) reshape of 3d input
	def fit(self, x, y, **kwargs):
		return super().fit(np.reshape(x, (x.shape[0], 1, x.shape[1], x.shape[2], 1)), y, **kwargs)
		
	# predict (samples, timesteps, rows, cols, channels) reshape of 3d input
	def predict(self, x, **kwargs):
		return super().predict(np.reshape(x, (x.shape[0], 1, x.shape[1], x.shape[2], 1)), **kwargs)
