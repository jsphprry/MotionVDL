import numpy as np
from tensorflow.keras.models import Sequential
from tensorflow.keras import layers



# dense 
class Dense(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((1,50*50), input_shape=(50,50)))
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.Reshape((11,2)))
		self._name = "Dense"



# convolutional
class Conv(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((50,50,1), input_shape=(50,50)))	# 1,50,50
		self.add(layers.Conv2D(3, 9, activation='sigmoid'))			# 3,42,42
		self.add(layers.MaxPooling2D())								# 3,21,21
		self.add(layers.Conv2D(3, 5, activation='sigmoid'))			# 3,17,17
		self.add(layers.MaxPooling2D())								# 3,8,8
		self.add(layers.Conv2D(3, 5, activation='sigmoid'))			# 3,13,13
		self.add(layers.MaxPooling2D())								# 3,6,6
		self.add(layers.Reshape((1,12)))
		self.add(layers.Dense(22))
		self.add(layers.Reshape((11,2)))
		self._name = "Conv"



# dense 
class LSTM(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((1,50*50), input_shape=(50,50)))
		self.add(layers.LSTM(22, activation='sigmoid'))
		self.add(layers.Reshape((11,2)))
		self._name = "LSTM"



# convolutional+lstm
class CnnLSTM(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((50,50,1), input_shape=(50,50)))
		self.add(layers.Conv2D(3, 3, strides=3, activation='sigmoid'))
		self.add(layers.MaxPooling2D())
		self.add(layers.Reshape((1,3*8*8)))
		self.add(layers.LSTM(22))
		self.add(layers.Reshape((11,2)))
		self._name = "CnnLSTM"



# convolutional lstm
class ConvLSTM(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((1,50,50,1), input_shape=(50,50)))
		self.add(layers.ConvLSTM2D(5, 9))
		self.add(layers.MaxPooling2D())
		self.add(layers.Reshape((1,1*5*21*21)))
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.Reshape((11,2)))
		self._name = "ConvLSTM"
