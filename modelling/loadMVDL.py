import numpy as np

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
