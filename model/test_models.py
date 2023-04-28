import utility
import models

import numpy as np
import matplotlib.pyplot as plt



# get gtk warnings out the way
plt.plot()

# load and partition data
x,y = utility.load('output_230420.mvdl')
train_x = x[:430]
train_y = y[:430]
test_x = x[430:800]
test_y = y[430:800]
#train_x = x[:25]
#train_y = y[:25]
#test_x = x[25:50]
#test_y = y[25:50]

# initialise models
_models = [] # list of tuples (model, model-name)
_models.append((models.CNNLSTM(),'CNNLSTM'))
_models.append((models.ConvLSTM(),'ConvLSTM'))

# test models
for model,name in _models:
	
	# compile model
	model.compile(loss='mean_squared_error', optimizer='adam')
	model.summary()
	
	# fit model and record history
	history = model.fit(train_x, train_y, epochs=100, batch_size=1, verbose=2)
	plt.plot(history.history['loss'], label=name)
	
	# output predictions
	train_predict = model.predict(train_x)
	test_predict = model.predict(test_x)
	utility.plot_export(train_x, train_predict, filename=name, figtitle=f"{name} train")
	utility.plot_export(test_x, test_predict, start=len(train_x), filename=name, figtitle=f"{name} test")

# plot histories
plt.xlabel('epoch')
plt.ylabel('loss')
plt.legend()
plt.show()
