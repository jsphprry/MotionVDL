import numpy as np
import matplotlib.pyplot as plt

from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
from tensorflow.keras.layers import LSTM




## create data




# map x values to polynomial curve
def makePolynomial(params, x):
	params = list(reversed(params))
	return sum([params[i]*x**i for i in range(len(params))])


# pose time series data as supervised learning problem
def makeTimeSeries(data):
	
	# x,y lists
	x = []
	y = []
	
	# label each x value at index i with the x value at index i+1
	for i in range(len(data)-1):
		x.append(data[i:i+1])
		y.append(data[i+1])
	
	# return tuple of numpy arrays
	return np.array(x), np.array(y)


# define values
increments = 100
params = [1,0,0,0]

# convert polynomial curve y values to time series x,y pairs
curve_x = np.linspace(-1.0, 1.0, num=increments)
curve_y = makePolynomial(params, curve_x)
target_x, target_y = makeTimeSeries(curve_y)

# plot time series
plt.plot(range(len(target_x)), target_x, label='target x')
plt.plot(range(len(target_y)), target_y, label='target y')
plt.title(f"Order {len(params)-1} polynomial time series")
plt.xlabel('time')
plt.ylabel('value')
plt.legend()
plt.show()




## train model




# split curve data into test and train time series sets
partition = int(0.67*len(curve_y))
train_x, train_y = makeTimeSeries(curve_y[:partition])
test_x, test_y = makeTimeSeries(curve_y[partition:])

# reshape inputs to (batch, timesteps, features)
train_x = np.reshape(train_x, (train_x.shape[0], 1, train_x.shape[1]))
test_x = np.reshape(test_x, (test_x.shape[0], 1, test_x.shape[1]))

# fit model to train set
model = Sequential()
model.add(LSTM(4, input_shape=(1,1)))
model.add(Dense(1))
model.compile(loss='mean_squared_error', optimizer='adam')
model.fit(target_x, target_y, epochs=100, batch_size=1, verbose=2)

# predict test set
train_predict = model.predict(train_x)
test_predict = model.predict(test_x)

# plot predictions
plt.plot(range(len(curve_y)), curve_y, label='target')
plt.plot(range(len(train_predict)), train_predict, label='trained predictions')
plt.plot(range(len(train_predict),len(train_predict)+len(test_predict)), test_predict, label='unseen predictions')
plt.title(f"Time series predictions")
plt.xlabel('time')
plt.ylabel('value')
plt.legend()
plt.show()
