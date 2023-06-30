import numpy as np
import matplotlib.pyplot as plt



# load byte encoding into tuple of numpy arrays
@profile
def load(location):
	
	# open filesystem location
	f = open(location, 'rb')
	
	# read video metadata
	b0 = int.from_bytes(f.read(1), 'big')
	b1 = int.from_bytes(f.read(1), 'big')
	b2 = int.from_bytes(f.read(1), 'big')
	b3 = int.from_bytes(f.read(1), 'big')
	b4 = int.from_bytes(f.read(1), 'big')
	b5 = int.from_bytes(f.read(1), 'big')
	
	# decode video metadata
	vl = b0*256+b1 # video length
	vh = b2*256+b3 # video height
	vw = b4*256+b5 # video width
	
	# read and decode video buffer
	video = np.zeros((vl,vh,vw))
	for i in range(vl):
		for j in range(vh):
			for k in range(vw):
				video[i][j][k] = int.from_bytes(f.read(1), 'big') / 255.0
	
	# read label metadata
	b6 = int.from_bytes(f.read(1), 'big')
	b7 = int.from_bytes(f.read(1), 'big')
	b8 = int.from_bytes(f.read(1), 'big')
	
	# read and decode label metadata
	ll = b6*256+b7 # label length
	lc = b8        # label width (capacity)
	sizes = np.zeros(ll, dtype=int)
	for i in range(ll):
		sizes[i] = int.from_bytes(f.read(1), 'big')
	
	# read and decode label buffer
	label = np.zeros((ll,lc,2))
	for i in range(ll):
		for j in range(sizes[i]):
			label[i][j][0] = int.from_bytes(f.read(1), 'big') / 255.0
			label[i][j][1] = int.from_bytes(f.read(1), 'big') / 255.0
	
	# return as tuple
	return (video, label)



# export plotted prediction figures
@profile
def plot_export(x, y, start=0, filename='frame', figtitle=None, vmin=0.0, vmax=0.5, res=50):
	
	# empty line for tidy track
	print()
	
	# enumerate x,y pairs
	for index, (image, label) in enumerate(zip(x,y)):
		
		# reformat values
		index = index + start
		image = image.reshape(res,res)
		lx,ly = np.split(label*res, [-1], axis=1)
		
		# create figure as figure 1
		f,a = plt.subplots(num=1, clear=True)
		
		# plot image
		a.imshow(image, vmin=vmin, vmax=vmax)
		
		# plot connectors
		for i,c in enumerate([0,0,1,2,1,4,1,6,7,6,9]):
			line_x = (lx[i], lx[c])
			line_y = (ly[i], ly[c])
			a.plot(line_x, line_y, color='red')
		
		# plot nodes
		a.scatter(lx, ly, color='black', marker='+')
		
		# plot figure title
		a.text(res-1, 1.5, figtitle, color='black', fontfamily='sans-serif', fontsize='small', horizontalalignment='right')
		
		# output figure
		f.savefig(f"predictions/{filename}{index}.png")
		
		# track progress
		print('\033[A                             \033[A') # tidy track, clear last line of output
		print(f"saved predictions/{filename}{index}.png {index-start+1}/{min(len(x),len(y))}")



# plot optimization figure
def plot_results(result_list, labels, key, fig_num):
	
	# create figure as figure 'fig_num'
	f,a = plt.subplots(num=fig_num, clear=True)
	
	# plot labelled loss histories
	for r,n in zip(result_list,labels):
		plt.plot(r.history[key], label=n)
	
	# output figure
	a.set_xlabel('epoch')
	a.set_ylabel(key)
	a.legend()
	f.savefig(f"optimization_{key}.png")
