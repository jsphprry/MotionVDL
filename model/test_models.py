import utility
import models

import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf



# print gtk warnings
f,_ = plt.subplots(clear=True)

# load and partition data
x,y = utility.load('output_230420.mvdl')
train_x = x[:430]
train_y = y[:430]
test_x = x[430:800]
test_y = y[430:800]

# initialise models
_models = []
_models.append(models.Dense())
_models.append(models.ConvDense())
_models.append(models.Conv_LSTMDense())
_models.append(models.DenseLSTM())
_models.append(models.ConvDenseLSTM())
_models.append(models.Conv_LSTMDenseLSTM())

# print summaries
for model in _models:
	print()
	model.summary()

# create history figure
f,a = plt.subplots()

# test models
for model in _models:
	
	# compile model
	model.compile(loss='mean_squared_error', optimizer='adam')
	
	# fit model and record history
	print()
	print(model._name)
	history = model.fit(train_x, train_y, epochs=25, batch_size=1, verbose=2)
	a.plot(history.history['loss'], label=model._name)
	
	# output predictions
	train_predict = model.predict(train_x)
	test_predict = model.predict(test_x)
	#utility.plot_export(train_x, train_predict, filename=model._name, figtitle=f"{model._name} train")
	#utility.plot_export(test_x, test_predict, start=len(train_x), filename=model._name, figtitle=f"{model._name} test")
	
	# clear model
	tf.keras.backend.clear_session()

# plot histories
a.set_xlabel('epoch')
a.set_ylabel('loss')
a.legend()
plt.show()
