import numpy as np
from tensorflow.keras.models import Sequential
from tensorflow.keras import layers



# cnn+lstm sequential model
class CNNLSTM(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((50,50,1), input_shape=(50,50)))
		self.add(layers.Conv2D(3, 3, strides=3, activation='sigmoid'))
		self.add(layers.MaxPooling2D())
		self.add(layers.Reshape((1,3*8*8)))
		self.add(layers.LSTM(22))
		self.add(layers.Reshape((11,2)))



# convolutional lstm sequential model
class ConvLSTM(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((1,50,50,1), input_shape=(50,50)))
		self.add(layers.ConvLSTM2D(3, 3, strides=3))
		self.add(layers.MaxPooling2D())
		self.add(layers.Reshape((1,1*3*8*8)))
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.Reshape((11,2)))



# cnn sequential model
class CNN(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((50,50,1), input_shape=(50,50)))
		self.add(layers.Conv2D(3, 3, strides=3, activation='sigmoid'))
		self.add(layers.MaxPooling2D())
		self.add(layers.Reshape((1,3*8*8)))
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.Reshape((11,2)))



# dense sequential model
class Dense(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((1,50*50), input_shape=(50,50)))
		self.add(layers.Dense(25, activation='sigmoid'))
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.Reshape((11,2)))
