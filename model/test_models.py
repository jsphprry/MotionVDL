import models
import utility

import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf


# train a model and export predictions
@profile
def evaluate_model(model, train_x, train_y, test_x, test_y, export_predictions=True):
	
	# compile model
	print()
	print(model._name)
	model.compile(loss='mean_squared_error', optimizer='adam')
	
	# train model and record results
	results = model.fit(train_x, train_y, validation_split=0.2, batch_size=int(0.8*len(train_x)), shuffle=False, epochs=1_000, verbose=2)
	
	# output predictions
	if (export_predictions):
		train_predict = model.predict(train_x)
		test_predict = model.predict(test_x)
		utility.plot_export(train_x, train_predict, filename=model._name, figtitle=f"{model._name} train")
		utility.plot_export(test_x, test_predict, start=len(train_x), filename=model._name, figtitle=f"{model._name} test")
	
	# clear model
	tf.keras.backend.clear_session()
	
	# return results
	return results


# get gtk warnings out the way
_,_ = plt.subplots(num=1, clear=True) # fig 1 overwritten by plotting functions

# load and partition data
x,y = utility.load('output_230420.mvdl')
train_start = 0
train_stop = 430
test_start = train_stop
test_stop = 800
train_x = x[train_start:train_stop]
train_y = y[train_start:train_stop]
test_x = x[test_start:test_stop]
test_y = y[test_start:test_stop]

# setup models
model_list = [ # list of (model,bool) tuples
	(models.Dense2(), True),
	(models.ConvDense(), True),
	(models.Conv_LSTMDense(), True)]

# print model_list information
for model,flag in model_list:
	print()
	model.summary()
	print(f"Print predictions: {flag}")

# train each model, export predictions and collect training history
results_list = []
for model,flag in model_list:
	results = evaluate_model(model, train_x, train_y, test_x, test_y)
	results_list.append(results)

# plot training history
result_labels = [m._name for m,_ in model_list]
utility.plot_results(results_list, result_labels, 'loss', 1)
utility.plot_results(results_list, result_labels, 'val_loss', 2)

# show figures
plt.show()
