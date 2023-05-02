# note imports at eof
import numpy as np
from tensorflow.keras.models import Sequential
from tensorflow.keras import layers



# output layers
#
# Dense output   | LSTM output
# ----------------------------
# Dense			 | DenseLSTM
# ConvDense		 | ConvLSTM
# Conv_LSTMDense | Conv_LSTMLSTM

# input layers
#
# Conv input  	 | Dense input
# ----------------------------
# ConvLSTM		 | Dense
# ConvDense		 | DenseLSTM
# Conv_LSTMDense | 
# Conv_LSTMLSTM  | 

# memory layers
# 
# has memory	 | has no memory
# ------------------------------
# DenseLSTM		 | Dense
# ConvLSTM		 | ConvDense
# Conv_LSTMDense | 
# Conv_LSTMLSTM	 | 


# f(x) = dense(x)
class Dense(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Flatten(input_shape=(50,50)))
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.Reshape((11,2)))
		self._name = "Dense"



# f(x) = dense(conv(x))
class ConvDense(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((50,50,1), input_shape=(50,50)))
		self.add(layers.Conv2D(10, 3, strides=3, activation='tanh'))
		self.add(layers.Flatten())
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.Reshape((11,2)))
		self._name = "ConvDense"



# f(x) = dense(conv_lstm(x))
class Conv_LSTMDense(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((1,50,50,1), input_shape=(50,50)))
		self.add(layers.ConvLSTM2D(10, 3, strides=3, activation='tanh'))
		self.add(layers.Flatten())
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.Reshape((11,2)))
		self._name = "Conv_LSTMDense"



# f(x) = lstm(dense(x)) 
class DenseLSTM(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((1,50*50), input_shape=(50,50)))
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.LSTM(22))
		self.add(layers.Reshape((11,2)))
		self._name = "DenseLSTM"



# f(x) = lstm(dense(conv(x)))
class ConvDenseLSTM(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((50,50,1), input_shape=(50,50)))
		self.add(layers.Conv2D(10, 3, strides=3, activation='tanh', name='lstm_input'))
		self.add(layers.Reshape((1,np.prod(self.get_layer('lstm_input').output_shape[1:]))))
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.LSTM(22))
		self.add(layers.Reshape((11,2)))
		self._name = "ConvDenseLSTM"



# f(x) = lstm(dense(conv_lstm(x)))
class Conv_LSTMDenseLSTM(Sequential):
	def __init__(self):
		super().__init__()
		self.add(layers.Reshape((1,50,50,1), input_shape=(50,50)))
		self.add(layers.ConvLSTM2D(10, 3, strides=3, activation='tanh', name='lstm_input'))
		self.add(layers.Reshape((1,np.prod(self.get_layer('lstm_input').output_shape[1:]))))
		self.add(layers.Dense(22, activation='sigmoid'))
		self.add(layers.LSTM(22))
		self.add(layers.Reshape((11,2)))
		self._name = "Conv_LSTMDenseLSTM"



# export list of model names for video script
# https://stackoverflow.com/questions/1796180/how-can-i-get-a-list-of-all-classes-within-current-module-in-python
#import sys, inspect
#with open("to_video.list", "w") as f:
#	for name, obj in inspect.getmembers(sys.modules[__name__], inspect.isclass):
#		f.write(f"{name}\n")
