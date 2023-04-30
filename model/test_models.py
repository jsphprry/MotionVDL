import utility
import models

import numpy as np
import matplotlib.pyplot as plt



# print gtk warnings
plt.plot()

# load and partition data
x,y = utility.load('output_230420.mvdl')
train_x = x[:430]
train_y = y[:430]
test_x = x[430:800]
test_y = y[430:800]

# initialise models
_models = []
_models.append(models.Dense())
_models.append(models.Conv())
_models.append(models.LSTM())
_models.append(models.CnnLSTM())
_models.append(models.ConvLSTM())

# print summaries
for model in _models:
	model.summary()

# test models
for model in _models:
	
	# compile model
	model.compile(loss='mean_squared_error', optimizer='adam')
	
	# fit model and record history
	print()
	print(model._name)
	history = model.fit(train_x, train_y, epochs=10, batch_size=1, verbose=2)
	plt.plot(history.history['loss'], label=model._name)
	
	# output predictions
	train_predict = model.predict(train_x)
	test_predict = model.predict(test_x)
	utility.plot_export(train_x, train_predict, filename=model._name, figtitle=f"{model._name} train")
	utility.plot_export(test_x, test_predict, start=len(train_x), filename=model._name, figtitle=f"{model._name} test")

# plot histories
plt.xlabel('epoch')
plt.ylabel('loss')
plt.legend()
plt.show()
