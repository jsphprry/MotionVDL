import numpy as np
import matplotlib.pyplot as plt



# load byte encoding into tuple of numpy arrays
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



# plot a sequence of images and labels
def plot_export(x, y, start=0, filename='frame', figtitle=None, vmin=0.0, vmax=0.5):
	
	# empty line for tidy track
	print()
	
	# enumerate x,y pairs
	for index, (image, label) in enumerate(zip(x,y)):
		
		# prepare index, image and label
		index = index + start
		image = image.reshape(50,50)
		lx,ly = np.split(label*50, [-1], axis=1)
		
		# create figure
		fig, ax = plt.subplots()
		
		# plot image
		ax.imshow(image, vmin=vmin, vmax=vmax)
		
		# plot connectors
		for i,c in enumerate([0,0,1,2,1,4,1,6,7,6,9]):
			line_x = (lx[i], lx[c])
			line_y = (ly[i], ly[c])
			ax.plot(line_x, line_y, color='red')
		
		# plot nodes
		ax.scatter(lx, ly, color='black', marker='+')
		
		# draw figure title
		ax.text(49, 1.5, figtitle, color='black', fontfamily='sans-serif', fontsize='small', horizontalalignment='right')
		
		# output figure
		fig.savefig(f"predictions/{filename}{index}.png")
		plt.close(fig=fig)
		
		# track progress
		print('\033[A                             \033[A') # tidy track, clear last line of output
		print(f"saved predictions/{filename}{index}.png {index-start+1}/{min(len(x),len(y))}")
