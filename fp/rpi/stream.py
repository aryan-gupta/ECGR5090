#!/usr/bin/python3

# import required modules
from flask import Flask, render_template, Response 
from imutils.video import VideoStream
import numpy as np
import cv2
import time
import imutils

# This class detects motion using an rolling weighted average
# of the previous frames
class MotionDetector:
	def __init__(self, base, weight=0.25):
		# weight of the most recent frame received
		self.weight = weight
		# create a clone of the first frame and use it as a reference
		# for the next frame
		self.background = base.copy().astype("float")

	# updated the internal motion detection model
	def update(self, image):
		cv2.accumulateWeighted(image, self.background, self.weight)

	# gets the motion detected using the internal model (average)
	# and the current frame of the image
	def get_motion_bounding_box(self, image, threshold=25):
		# compute difference between new image and background
		delta = cv2.absdiff(self.background.astype("uint8"), image)
		thresh = cv2.threshold(delta, threshold, 255, cv2.THRESH_BINARY)[1]

		# remove thresholds that are too small (sensor noise)
		thresh = cv2.erode(thresh, None, iterations=2)
		thresh = cv2.dilate(thresh, None, iterations=2)

		# find the contors from the new image
		cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
		cnts = imutils.grab_contours(cnts)
		(minX, minY) = (np.inf, np.inf)
		(maxX, maxY) = (-np.inf, -np.inf)

		if len(cnts) == 0:
			return None

		for c in cnts:
			# compute the bounding box of the contour and use it to
			# update the minimum and maximum bounding box regions
			(x, y, w, h) = cv2.boundingRect(c)
			(minX, minY) = (min(minX, x), min(minY, y))
			(maxX, maxY) = (max(maxX, x + w), max(maxY, y + h))

		return (thresh, (minX, minY, maxX, maxY))

# Create Flask application
app = Flask(__name__) 

# start video camera and wait for it
# to initialize
vc = cv2.VideoCapture(0)
time.sleep(2.0)

if not vc.isOpened():
	print("Cannot open camera")
	exit()

ret, frame = vc.read()
if not ret:
	print("Can't receive frame (stream end?). Exiting ...")
	exit()

# read one frame and initialize the motion detector
# from this frame
frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
detector = MotionDetector(frame)

# this is some metrics that I was using to test if it was
# worth multithreading this app
frame_num = 0
gray_total = 0
bbox_total = 0
dete_total = 0
enco_total = 0

def gen(): 
	"""Video streaming generator function.""" 

	global vc
	global frame_num, gray_total, bbox_total, dete_total, enco_total

	while True: 
		frame_num += 1
		rval, frame = vc.read()
		
		# convert to grayscale
		start_time = time.time()
		gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
		gray_total += (time.time() - start_time)

		# @todo this should be moved before the previous set of 
		# statements
		if not rval:
			print("Can't receive frame (stream end?). Exiting ...")
			break

		# get bounding boxes (detect motion)
		start_time = time.time()
		bounding_box = detector.get_motion_bounding_box(gray)
		bbox_total += (time.time() - start_time)

		# update the detection model
		start_time = time.time()
		detector.update(gray)
		dete_total += (time.time() - start_time)

		# draw the bounding boxes
		if bounding_box is not None:
			(thresh, (minX, minY, maxX, maxY)) = bounding_box
			cv2.rectangle(frame, (minX, minY), (maxX, maxY), (0, 0, 255), 1)

		# encode the image for the webserver
		start_time = time.time()
		(flag, encodedImage) = cv2.imencode(".jpg", frame) 
		enco_total += (time.time() - start_time)

		if not flag:
			continue

		# yield the result of the generation
		yield (b'--frame\r\n' 
				b'Content-Type: image/jpeg\r\n\r\n' + bytearray(encodedImage) + b'\r\n') 


@app.route('/') 
def index(): 
	"""Video streaming .""" 
	return render_template('index.html') 

@app.route('/video_feed') 
def video_feed(): 
	"""Video streaming route. Put this in the src attribute of an img tag.""" 
	return Response(gen(), mimetype='multipart/x-mixed-replace; boundary=frame') 


app.run(host='0.0.0.0', port=8081, debug=False, threaded=True) 
vc.release()

# These are the timing values
# gray: 0.00015909435184857318
# bbox: 0.0017290442954492934
# dete: 0.0003856199084954104
# enco: 0.014365608758902125
# even if we pipeline it by taking the fastest 3 stages 
# and the 1 fastest stage, then we would only get an 16%
# improvement. Considering that this is tested on my 
# i7-4790K non-OC, it might be beneficial to thread it
# once we get it on a rpi
print()
print("gray: %s" % (gray_total / frame_num))
print("bbox: %s" % (bbox_total / frame_num))
print("dete: %s" % (dete_total / frame_num))
print("enco: %s" % (enco_total / frame_num))
