import utility

import numpy as np
import matplotlib.pyplot as plt

# load data
x,y = utility.load('output_230420.mvdl')

# print all values
print(x)
print(y)

# plot specific frame
plt.imshow(x[464])
plt.show()

# plot export
utility.plot_export([x[89]], [y[89]])
